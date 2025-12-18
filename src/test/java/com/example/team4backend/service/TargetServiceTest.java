package com.example.team4backend.service;

import com.example.team4backend.common.Role;
import com.example.team4backend.domain.ChatStyle;
import com.example.team4backend.domain.Relationship;
import com.example.team4backend.domain.TargetPerson;
import com.example.team4backend.domain.User;
import com.example.team4backend.dto.EventRequest;
import com.example.team4backend.dto.TargetRequest;
import com.example.team4backend.dto.TargetResponse;
import com.example.team4backend.repository.ChatStyleRepository;
import com.example.team4backend.repository.EventRepository;
import com.example.team4backend.repository.RelationshipRepository;
import com.example.team4backend.repository.TargetPersonRepository;
import com.example.team4backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TargetServiceTest {

    @Mock TargetPersonRepository targetPersonRepository;
    @Mock UserRepository userRepository;
    @Mock RelationshipRepository relationshipRepository;
    @Mock ChatStyleRepository chatStyleRepository;

    @InjectMocks TargetService targetService;

    // 테스트용 헬퍼 메서드들
    private User user(Long id, boolean onboarded) {
        User u = User.builder()
                .email("test@example.com")
                .username("tester")
                .role(Role.ROLE_USER)
                .build();
        TestReflection.setField(u, "id", id);
        TestReflection.setField(u, "onboarded", onboarded);
        return u;
    }

    private Relationship relationship(Long id, String description) {
        Relationship r = new Relationship("FRIEND", description);
        TestReflection.setField(r, "id", id);
        return r;
    }

    private ChatStyle chatStyle(Long id, String name) {
        ChatStyle c = new ChatStyle(name, "테스트 말투 설명");
        TestReflection.setField(c, "id", id);
        return c;
    }

    private TargetRequest req(Long relId, Long chatStyleId) {
        return new TargetRequest(
                "홍길동",
                relId,
                chatStyleId,
                29,
                "01029050166",
                LocalDate.of(1995, 5, 10),
                "운동, 음악",
                List.of(
                        new EventRequest(LocalDate.of(2025, 1, 10), "결혼기념일"),
                        new EventRequest(LocalDate.of(2025, 2, 14), "생일")
                )
        );
    }

    private TargetPerson target(Long targetId, User owner, Relationship rel, ChatStyle chat) {
        TargetPerson t = TargetPerson.builder()
                .user(owner)
                .name("홍길동")
                .relationship(rel)
                .chatStyle(chat)
                .age(29)
                .phoneNumber("01029050166")
                .birthday(LocalDate.of(1995, 5, 10))
                .interests("운동")
                .build();
        TestReflection.setField(t, "id", targetId);
        return t;
    }

    @Nested
    @DisplayName("addTarget")
    class AddTarget {
        @Test
        @DisplayName("정상 생성 시 target 저장 후 id 반환")
        void addTarget_success_returnsId() {
            // given
            User u = user(1L, true);
            Relationship rel = relationship(1L, "친구");
            ChatStyle chat = chatStyle(1L, "편한 반말");

            given(userRepository.findByIdAndDeletedAtIsNull(1L)).willReturn(Optional.of(u));
            given(relationshipRepository.findById(1L)).willReturn(Optional.of(rel));
            given(chatStyleRepository.findById(1L)).willReturn(Optional.of(chat));

            TargetPerson saved = target(10L, u, rel, chat);
            given(targetPersonRepository.save(any(TargetPerson.class))).willReturn(saved);

            // when
            Long id = targetService.addTarget(1L, req(1L, 1L));

            // then
            assertThat(id).isEqualTo(10L);
            verify(targetPersonRepository).save(any(TargetPerson.class));
        }
    }

    @Nested
    @DisplayName("getTarget")
    class GetTarget {
        @Test
        @DisplayName("정상 조회 시 TargetResponse 반환 (Fetch Join 메서드 사용)")
        void getTarget_success() {
            // given
            User owner = user(1L, true);
            Relationship rel = relationship(1L, "친구");
            ChatStyle chat = chatStyle(1L, "편한 반말");
            TargetPerson t = target(10L, owner, rel, chat);

            // 변경된 메서드명 반영
            given(targetPersonRepository.findByIdWithUserAndDetailsAndDeletedAtIsNull(10L))
                    .willReturn(Optional.of(t));

            // when
            TargetResponse res = targetService.getTarget(1L, 10L);

            // then
            assertThat(res).isNotNull();
            assertThat(res.name()).isEqualTo("홍길동");
        }
    }

    @Nested
    @DisplayName("updateTarget")
    class UpdateTarget {
        @Test
        @DisplayName("정상 수정 시 엔티티 필드가 변경된다")
        void updateTarget_success() {
            // given
            User owner = user(1L, true);
            Relationship oldRel = relationship(1L, "친구");
            ChatStyle oldChat = chatStyle(1L, "편한 반말");
            TargetPerson t = target(10L, owner, oldRel, oldChat);

            given(targetPersonRepository.findByIdWithUserAndDetailsAndDeletedAtIsNull(10L))
                    .willReturn(Optional.of(t));

            // 새로운 관계와 말투로 변경 시나리오
            Relationship newRel = relationship(2L, "가족");
            ChatStyle newChat = chatStyle(2L, "기본 존댓말");
            given(relationshipRepository.findById(2L)).willReturn(Optional.of(newRel));
            given(chatStyleRepository.findById(2L)).willReturn(Optional.of(newChat));

            TargetRequest dto = new TargetRequest(
                    "김철수", 2L, 2L, 35, "01012345678",
                    LocalDate.of(1990, 1, 1), "독서",
                    List.of(new EventRequest(LocalDate.of(2025, 3, 1), "결혼식"))
            );

            // when
            targetService.updateTarget(1L, 10L, dto);

            // then
            assertThat(t.getName()).isEqualTo("김철수");
            assertThat(t.getRelationship().getId()).isEqualTo(2L);
            assertThat(t.getChatStyle().getId()).isEqualTo(2L);
        }
    }

    // [중략] getAllTargets, deleteTarget, TestReflection 로직은 기존과 유사하게 유지하되
    // Repository 메서드명만 findByIdWithDetailsAndDeletedAtIsNull 등으로 업데이트하여 마무리합니다.

    static class TestReflection {
        static void setField(Object target, String fieldName, Object value) {
            try {
                var f = target.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                f.set(target, value);
            } catch (NoSuchFieldException e) {
                Class<?> c = target.getClass().getSuperclass();
                while (c != null) {
                    try {
                        var f = c.getDeclaredField(fieldName);
                        f.setAccessible(true);
                        f.set(target, value);
                        return;
                    } catch (NoSuchFieldException ignore) {
                        c = c.getSuperclass();
                    } catch (IllegalAccessException iae) {
                        throw new RuntimeException(iae);
                    }
                }
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}