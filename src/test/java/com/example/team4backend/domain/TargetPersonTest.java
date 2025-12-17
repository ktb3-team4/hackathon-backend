package com.example.team4backend.domain;

import com.example.team4backend.common.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TargetPersonTest {

    private User createUser() {
        return User.builder()
                .email("test@example.com")
                .username("tester")
                .role(Role.ROLE_USER)
                .build();
    }

    private Relationship createRelationship(String code, String description) {
        return new Relationship(code, description);
    }

    private ChatStyle createChatStyle(String styleName, String description) {
        return new ChatStyle(styleName, description);
    }

    @Nested
    @DisplayName("TargetPerson 생성")
    class CreateTargetPerson {

        @Test
        @DisplayName("Builder로 생성 시 모든 필드가 정상적으로 설정된다")
        void createTargetPerson_success() {
            // given
            User user = createUser();
            Relationship relationship = createRelationship("FRIEND", "친구");
            ChatStyle chatStyle = createChatStyle("편한 반말", "일상적이고 거리감 없는 말투");
            LocalDate birthday = LocalDate.of(1995, 5, 10);

            // when
            TargetPerson targetPerson = TargetPerson.builder()
                    .user(user)
                    .name("홍길동")
                    .relationship(relationship) // Enum 대신 Entity 사용
                    .chatStyle(chatStyle)       // 대화 형식 엔티티 추가
                    .age(29)
                    .phoneNumber("01029050166")
                    .birthday(birthday)
                    .job("개발자")
                    .interests("운동, 음악")
                    .events("생일")
                    .memo("고등학교 친구")
                    .build();

            // then
            assertThat(targetPerson.getId()).isNull();
            assertThat(targetPerson.getUser()).isEqualTo(user);
            assertThat(targetPerson.getName()).isEqualTo("홍길동");
            assertThat(targetPerson.getRelationship()).isEqualTo(relationship);
            assertThat(targetPerson.getChatStyle()).isEqualTo(chatStyle);
            assertThat(targetPerson.getAge()).isEqualTo(29);
            assertThat(targetPerson.getPhoneNumber()).isEqualTo("01029050166");
            assertThat(targetPerson.getBirthday()).isEqualTo(birthday);
            assertThat(targetPerson.getJob()).isEqualTo("개발자");
            assertThat(targetPerson.getInterests()).isEqualTo("운동, 음악");
            assertThat(targetPerson.getEvents()).isEqualTo("생일");
            assertThat(targetPerson.getMemo()).isEqualTo("고등학교 친구");
            assertThat(targetPerson.getDeletedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("TargetPerson 수정")
    class UpdateTargetPerson {

        @Test
        @DisplayName("update 호출 시 모든 필드가 변경된다")
        void updateTargetPerson_success() {
            // given
            User user = createUser();
            Relationship oldRel = createRelationship("FRIEND", "친구");
            ChatStyle oldStyle = createChatStyle("편한 반말", "반말 말투");

            TargetPerson targetPerson = TargetPerson.builder()
                    .user(user)
                    .name("홍길동")
                    .relationship(oldRel)
                    .chatStyle(oldStyle)
                    .age(29)
                    .phoneNumber("01029050166")
                    .birthday(LocalDate.of(1995, 5, 10))
                    .job("개발자")
                    .interests("운동")
                    .events("생일")
                    .memo("친구")
                    .build();

            Relationship newRel = createRelationship("FAMILY", "가족");
            ChatStyle newStyle = createChatStyle("기본 존댓말", "존댓말 말투");
            LocalDate newBirthday = LocalDate.of(1990, 1, 1);

            // when
            targetPerson.update(
                    "김철수",
                    newRel,
                    newStyle,
                    35,
                    "01012345678",
                    newBirthday,
                    "기획자",
                    "독서",
                    "결혼식",
                    "가족"
            );

            // then
            assertThat(targetPerson.getName()).isEqualTo("김철수");
            assertThat(targetPerson.getRelationship()).isEqualTo(newRel);
            assertThat(targetPerson.getChatStyle()).isEqualTo(newStyle);
            assertThat(targetPerson.getAge()).isEqualTo(35);
            assertThat(targetPerson.getPhoneNumber()).isEqualTo("01012345678");
            assertThat(targetPerson.getBirthday()).isEqualTo(newBirthday);
            assertThat(targetPerson.getJob()).isEqualTo("기획자");
            assertThat(targetPerson.getInterests()).isEqualTo("독서");
            assertThat(targetPerson.getEvents()).isEqualTo("결혼식");
            assertThat(targetPerson.getMemo()).isEqualTo("가족");
        }
    }
}