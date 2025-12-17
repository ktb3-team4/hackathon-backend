package com.example.team4backend.service;

import com.example.team4backend.common.Role;
import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.domain.Relationship;
import com.example.team4backend.domain.TargetPerson;
import com.example.team4backend.domain.User;
import com.example.team4backend.dto.TargetListResponse;
import com.example.team4backend.dto.TargetRequest;
import com.example.team4backend.dto.TargetResponse;
import com.example.team4backend.exception.BusinessException;
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

    @InjectMocks TargetService targetService;

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

    private TargetRequest req() {
        return new TargetRequest(
                "홍길동",
                Relationship.FRIEND,
                29,
                "01029050166",
                LocalDate.of(1995, 5, 10),
                "개발자",
                "운동, 음악",
                "생일",
                "메모"
        );
    }

    private TargetPerson target(Long targetId, User owner) {
        TargetPerson t = TargetPerson.builder()
                .user(owner)
                .name("홍길동")
                .relation(Relationship.FRIEND)
                .age(29)
                .birthday(LocalDate.of(1995, 5, 10))
                .job("개발자")
                .interests("운동")
                .events("생일")
                .memo("친구")
                .build();

        TestReflection.setField(t, "id", targetId);
        return t;
    }

    @Nested
    @DisplayName("addTarget")
    class AddTarget {

        @Test
        @DisplayName("유저가 없으면 USER_NOT_FOUND 예외")
        void addTarget_userNotFound() {
            // given
            given(userRepository.findByIdAndDeletedAtIsNull(1L)).willReturn(Optional.empty());

            // when + then
            BusinessException ex = catchThrowableOfType(
                    () -> targetService.addTarget(1L, req()),
                    BusinessException.class
            );
            assertThat(ex).isNotNull();
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);

            verify(targetPersonRepository, never()).save(any());
        }

        @Test
        @DisplayName("정상 생성 시 target 저장 후 id 반환")
        void addTarget_success_returnsId() {
            // given
            User u = user(1L, true);
            given(userRepository.findByIdAndDeletedAtIsNull(1L)).willReturn(Optional.of(u));

            // save 결과에 id가 있어야 함
            TargetPerson saved = target(10L, u);
            given(targetPersonRepository.save(any(TargetPerson.class))).willReturn(saved);

            // when
            Long id = targetService.addTarget(1L, req());

            // then
            assertThat(id).isEqualTo(10L);
            verify(targetPersonRepository).save(any(TargetPerson.class));
        }

        @Test
        @DisplayName("온보딩이 false이면 completeOnboarding이 호출된다")
        void addTarget_onboardingFalse_thenCompleteOnboarding() {
            // given
            User u = user(1L, false);
            given(userRepository.findByIdAndDeletedAtIsNull(1L)).willReturn(Optional.of(u));

            TargetPerson saved = target(10L, u);
            given(targetPersonRepository.save(any(TargetPerson.class))).willReturn(saved);

            // when
            targetService.addTarget(1L, req());

            // then
            assertThat(u.isOnboarded()).isTrue();
        }

        @Test
        @DisplayName("온보딩이 true이면 completeOnboarding 호출로 상태가 변하지 않는다")
        void addTarget_onboardingTrue_noChange() {
            // given
            User u = user(1L, true);
            given(userRepository.findByIdAndDeletedAtIsNull(1L)).willReturn(Optional.of(u));

            TargetPerson saved = target(10L, u);
            given(targetPersonRepository.save(any(TargetPerson.class))).willReturn(saved);

            // when
            targetService.addTarget(1L, req());

            // then
            assertThat(u.isOnboarded()).isTrue();
        }
    }

    @Nested
    @DisplayName("getTarget")
    class GetTarget {

        @Test
        @DisplayName("target이 없으면 NOT_FOUND 예외")
        void getTarget_notFound() {
            // given
            given(targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(10L))
                    .willReturn(Optional.empty());

            // when + then
            BusinessException ex = catchThrowableOfType(
                    () -> targetService.getTarget(1L, 10L),
                    BusinessException.class
            );
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("소유자가 아니면 AUTH_FORBIDDEN 예외")
        void getTarget_forbidden() {
            // given
            User owner = user(999L, true);
            TargetPerson t = target(10L, owner);

            given(targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(10L))
                    .willReturn(Optional.of(t));

            // when + then
            BusinessException ex = catchThrowableOfType(
                    () -> targetService.getTarget(1L, 10L),
                    BusinessException.class
            );
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AUTH_FORBIDDEN);
        }

        @Test
        @DisplayName("정상 조회 시 TargetResponse 반환")
        void getTarget_success() {
            // given
            User owner = user(1L, true);
            TargetPerson t = target(10L, owner);

            given(targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(10L))
                    .willReturn(Optional.of(t));

            // when
            TargetResponse res = targetService.getTarget(1L, 10L);

            // then
            assertThat(res).isNotNull();
            // from() 구현에 따라 필요한 필드 더 검증 가능
        }
    }

    @Nested
    @DisplayName("getAllTargets")
    class GetAllTargets {

        @Test
        @DisplayName("유저의 target 목록을 반환한다")
        void getAllTargets_success() {
            // given
            User owner = user(1L, true);
            TargetPerson t1 = target(10L, owner);
            TargetPerson t2 = target(11L, owner);

            given(targetPersonRepository.findAllByUserIdWithUserAndDeletedAtIsNull(1L))
                    .willReturn(List.of(t1, t2));

            // when
            List<TargetListResponse> res = targetService.getAllTargets(1L);

            // then
            assertThat(res).hasSize(2);
        }

        @Test
        @DisplayName("목록이 없으면 빈 리스트를 반환한다")
        void getAllTargets_empty() {
            // given
            given(targetPersonRepository.findAllByUserIdWithUserAndDeletedAtIsNull(1L))
                    .willReturn(List.of());

            // when
            List<TargetListResponse> res = targetService.getAllTargets(1L);

            // then
            assertThat(res).isEmpty();
        }
    }

    @Nested
    @DisplayName("updateTarget")
    class UpdateTarget {

        @Test
        @DisplayName("target이 없으면 NOT_FOUND 예외")
        void updateTarget_notFound() {
            // given
            given(targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(10L))
                    .willReturn(Optional.empty());

            // when + then
            BusinessException ex = catchThrowableOfType(
                    () -> targetService.updateTarget(1L, 10L, req()),
                    BusinessException.class
            );
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("소유자가 아니면 AUTH_FORBIDDEN 예외")
        void updateTarget_forbidden() {
            // given
            User owner = user(999L, true);
            TargetPerson t = target(10L, owner);

            given(targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(10L))
                    .willReturn(Optional.of(t));

            // when + then
            BusinessException ex = catchThrowableOfType(
                    () -> targetService.updateTarget(1L, 10L, req()),
                    BusinessException.class
            );
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AUTH_FORBIDDEN);
        }

        @Test
        @DisplayName("정상 수정 시 엔티티 필드가 변경된다")
        void updateTarget_success() {
            // given
            User owner = user(1L, true);
            TargetPerson t = target(10L, owner);

            given(targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(10L))
                    .willReturn(Optional.of(t));

            TargetRequest dto = new TargetRequest(
                    "김철수",
                    Relationship.FAMILY,
                    35,
                    "01029050166",
                    LocalDate.of(1990, 1, 1),
                    "기획자",
                    "독서",
                    "결혼식",
                    "가족"
            );

            // when
            targetService.updateTarget(1L, 10L, dto);

            // then
            assertThat(t.getName()).isEqualTo("김철수");
            assertThat(t.getRelation()).isEqualTo(Relationship.FAMILY);
            assertThat(t.getAge()).isEqualTo(35);
            assertThat(t.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
            assertThat(t.getJob()).isEqualTo("기획자");
            assertThat(t.getInterests()).isEqualTo("독서");
            assertThat(t.getEvents()).isEqualTo("결혼식");
            assertThat(t.getMemo()).isEqualTo("가족");
        }
    }

    @Nested
    @DisplayName("deleteTarget")
    class DeleteTarget {

        @Test
        @DisplayName("target이 없으면 NOT_FOUND 예외")
        void deleteTarget_notFound() {
            // given
            given(targetPersonRepository.softDeleteByIdAndUserId(10L, 1L))
                    .willReturn(0);

            given(targetPersonRepository.findOwnerIdByIdAndDeletedAtIsNull(10L))
                    .willReturn(Optional.empty());

            // when + then
            BusinessException ex = catchThrowableOfType(
                    () -> targetService.deleteTarget(1L, 10L),
                    BusinessException.class
            );

            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        }

        @Test
        @DisplayName("소유자가 아니면 AUTH_FORBIDDEN 예외")
        void deleteTarget_forbidden() {
            // given
            given(targetPersonRepository.softDeleteByIdAndUserId(10L, 1L))
                    .willReturn(0);

            given(targetPersonRepository.findOwnerIdByIdAndDeletedAtIsNull(10L))
                    .willReturn(Optional.of(999L));

            // when + then
            BusinessException ex = catchThrowableOfType(
                    () -> targetService.deleteTarget(1L, 10L),
                    BusinessException.class
            );

            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.AUTH_FORBIDDEN);
        }

        @Test
        @DisplayName("정상 삭제 시 softDeleteByIdAndUserId가 호출된다")
        void deleteTarget_success() {
            // given
            given(targetPersonRepository.softDeleteByIdAndUserId(10L, 1L))
                    .willReturn(1);

            // when
            targetService.deleteTarget(1L, 10L);

            // then
            verify(targetPersonRepository).softDeleteByIdAndUserId(10L, 1L);
            verify(targetPersonRepository, never())
                    .findOwnerIdByIdAndDeletedAtIsNull(anyLong());
        }

    }

    /**
     * 테스트 편의를 위한 reflection 유틸 (엔티티 id/private 필드 주입용)
     * - 실무에서는 domain test에서만 제한적으로 사용.
     */
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
