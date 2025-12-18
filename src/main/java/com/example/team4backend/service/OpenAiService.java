package com.example.team4backend.service;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.content.Media;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeType;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

@Primary
@Service
public class OpenAiService implements AiService {

    private final OpenAiChatModel chatModel;

    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;

    public OpenAiService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String generateContent(String prompt) {
        var options = (OpenAiChatOptions) getOptions();
        return chatModel.call(new Prompt(prompt, options))
                .getResult().getOutput().getText();
    }


    @Override
    public String generateContentFromImages(List<ImageInput> images) {

        String fixedPrompt = """
    You are an assistant that extracts text from images.

    If the image is a chat or messaging conversation:
    - Extract ONLY the messages sent by the current user.
    - The current user's messages are on the RIGHT side.
    - NEVER extract any LEFT-side messages.
    - Remove timestamps, names, profile info, system notices, and reactions.
    - Output ONLY the extracted message text in Korean.
    """;

        var options = (OpenAiChatOptions) getOptions();

        List<Media> mediaList = images.stream()
                .map(image -> {
                    CroppedImage cropped = cropRightOnly(image.data(), image.mimeType(), 0.70);

                    return new Media(
                            MimeType.valueOf(cropped.mimeType()),
                            new ByteArrayResource(cropped.bytes())
                    );
                })
                .toList();

        UserMessage userMessage = UserMessage.builder()
                .text(fixedPrompt)
                .media(mediaList)
                .build();

        return chatModel
                .call(new Prompt(List.of(userMessage), options))
                .getResult()
                .getOutput()
                .getText();
    }

    private CroppedImage cropRightOnly(byte[] originalBytes, String originalMimeType, double keepRatio) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(originalBytes));
            if (img == null) return new CroppedImage(originalBytes, originalMimeType);

            int w = img.getWidth();
            int h = img.getHeight();

            int keepW = Math.max(1, (int) Math.round(w * keepRatio));
            int x = Math.max(0, w - keepW);

            BufferedImage rightOnly = img.getSubimage(x, 0, keepW, h);

            String format = mimeToFormat(originalMimeType);
            String outMime = format.equals("png") ? "image/png" : "image/jpeg";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(rightOnly, format, baos);

            return new CroppedImage(baos.toByteArray(), outMime);
        } catch (Exception e) {
            return new CroppedImage(originalBytes, originalMimeType);
        }
    }

    private String mimeToFormat(String mime) {
        if (mime == null) return "png";
        String m = mime.toLowerCase();
        if (m.contains("png")) return "png";
        if (m.contains("jpeg") || m.contains("jpg")) return "jpg";
        return "png";
    }

    private record CroppedImage(byte[] bytes, String mimeType) {}



    public Object getOptions() {
        return OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(0.5)
                .maxTokens(150)
                .build();
    }
}
