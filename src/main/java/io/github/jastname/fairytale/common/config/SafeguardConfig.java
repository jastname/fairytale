package io.github.jastname.fairytale.common.config;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Safeguard 전용 ChatModel 빈 설정
 *
 * 메인 RAG 모델(qwen3:8b)과 분리하여 safeguard 전용 경량 모델을 사용합니다.
 * application.yaml 의 app.safeguard.model 값으로 모델을 지정할 수 있습니다.
 */
@Configuration
public class SafeguardConfig {

    @Value("${spring.ai.ollama.base-url}")
    private String ollamaBaseUrl;

    @Value("${app.safeguard.model:qwen3:1.7b}")
    private String safeguardModel;

    @Bean(name = "safeguardChatModel")
    public OllamaChatModel safeguardChatModel() {
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl(ollamaBaseUrl)
                .build();

        OllamaChatOptions options = OllamaChatOptions.builder()
                .model(safeguardModel)
                .temperature(0.0)   // 판단 일관성을 위해 temperature 0
                .build();

        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }
}