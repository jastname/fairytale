package io.github.jastname.fairytale.common.config;

import javax.sql.DataSource;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ChatMemoryConfig {

    /**
     * JdbcChatMemoryRepository : DataSource 를 받아 JDBC 기반으로 대화 내역을 저장한다.
     */
    @Bean
    public JdbcChatMemoryRepository jdbcChatMemoryRepository(DataSource dataSource) {
        return JdbcChatMemoryRepository.builder()
                .dataSource(dataSource)
                .build();
    }

    /**
     * ChatMemory : 최근 20개 메시지를 윈도우로 유지한다.
     */
    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(20)
                .build();
    }
}
