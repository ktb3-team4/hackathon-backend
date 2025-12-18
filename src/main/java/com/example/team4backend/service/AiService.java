package com.example.team4backend.service;

import java.util.List;

public interface AiService {
    String generateContent(String prompt);
    Object getOptions();
    String generateContentFromImages(List<ImageInput> images);
}
