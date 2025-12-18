package com.example.team4backend.common.runner;

import com.example.team4backend.domain.ChatStyle;
import com.example.team4backend.repository.ChatStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatStyleDataRunner implements CommandLineRunner {

    private final ChatStyleRepository chatStyleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (chatStyleRepository.count() > 0) {
            return;
        }

        List<ChatStyle> styles = Arrays.asList(
                new ChatStyle("편한 반말", "일상적이고 거리감 없는 말투"),
                new ChatStyle("기본 존댓말", "무난하고 예의 중심의 말투"),
                new ChatStyle("애교 섞인 말투", "감정 표현이 많고 친근함을 강조"),
                new ChatStyle("걱정·배려 중심 말투", "상대 상태를 먼저 살피는 말투"),
                new ChatStyle("농담 섞인 편안한 말투", "가벼운 웃음 포인트가 있는 스타일")
//                new ChatStyle("부드러운 존댓말", "말끝을 완화해 정중함을 강조한 말투"),
//                new ChatStyle("차분한 설명형 말투", "또박또박 설명하듯 말하는 스타일"),
//                new ChatStyle("보고·전달형 말투", "사실 위주로 간단히 전달하는 스타일"),
//                new ChatStyle("감사·존중 강조 말투", "고마움과 존중 표현이 자주 들어가는 말투"),
//                new ChatStyle("조심스러운 요청형 말투", "부탁이나 제안을 할 때 사용하는 말투")
        );

        chatStyleRepository.saveAll(styles);
        System.out.println("ChatStyle 초기 데이터 " + styles.size() + "개 삽입 완료");
    }
}
