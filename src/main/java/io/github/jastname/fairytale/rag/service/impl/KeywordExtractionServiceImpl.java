package io.github.jastname.fairytale.rag.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.ThinkOption;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jastname.fairytale.rag.dto.UserRagAsk;
import io.github.jastname.fairytale.rag.dto.FairyTaleSearchQueryDto;
import io.github.jastname.fairytale.rag.service.KeywordExtractionService;
import lombok.RequiredArgsConstructor;



@Service("keywordExtractionService")
@RequiredArgsConstructor
public class KeywordExtractionServiceImpl implements KeywordExtractionService {

    private static final Logger LOG = LoggerFactory.getLogger(KeywordExtractionServiceImpl.class);
    
    private final ChatModel chatModel;
    
    private static final String KEYWORD_EXTRACTION_JSON_SCHEMA = """
    {
      "type": "object",
      "properties": {
        "searchPhrase": {
          "type": "string"
        },
        "keywords": {
          "type": "array",
          "items": {
            "type": "string"
          },
          "minItems": 1,
          "maxItems": 5
        },
        "semanticTags": {
          "type": "array",
          "items": {
            "type": "string"
          },
          "minItems": 1,
          "maxItems": 4
        },
        "expandedQuery": {
          "type": "string"
        }
      },
      "required": [
        "searchPhrase",
        "keywords",
        "semanticTags",
        "expandedQuery"
      ],
      "additionalProperties": false
    }
    """;

    private static final String KEYWORD_EXTRACTION_SYSTEM_PROMPT = """
    당신은 어린이 동화 검색 RAG 시스템의 질의 정규화 및 키워드 추출기이다.

    입력된 사용자 질문을 바탕으로 어린이 동화 검색에 적합한 검색 질의를 생성해야 한다.

    규칙:
    - 출력은 반드시 JSON 객체 하나만 반환한다.
    - JSON 외의 설명은 절대 포함하지 않는다.
    - 모든 값은 한국어로 작성한다.
    - searchPhrase는 검색용 핵심 문장이다.
    - keywords는 1개 이상 5개 이하의 핵심 키워드다.
    - semanticTags는 1개 이상 4개 이하의 의미 태그다.
    - expandedQuery는 확장 검색 문장이다.
    - 너무 일반적인 단어는 제외한다.
    - 각 항목들은(searchPhrase, keywords, semanticTags, expandedQuery) 빈 값이 될 수 없다.
    
    반드시 아래 JSON 스키마를 만족하라:
    %s
    """;
    
    private static final Map<String, Object> KEYWORD_EXTRACTION_SCHEMA_MAP;
    static {
        try {
            KEYWORD_EXTRACTION_SCHEMA_MAP = new ObjectMapper().readValue(
                KEYWORD_EXTRACTION_JSON_SCHEMA, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new ExceptionInInitializerError("[KeywordExtraction] JSON schema parse failed: " + e.getMessage());
        }
    }
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
	@Override
	public FairyTaleSearchQueryDto keywordExtraction(UserRagAsk userRagAsk) {
        
		String userPrompt = """
        사용자 질문:
        %s

        위 질문을 어린이 동화 검색용 질의로 변환하라.
        JSON 스키마에 맞는 결과만 반환하라.
        """.formatted(userRagAsk.getQuestion());
		
		LOG.info("[키워드 추출 시작]");
        String llmResponse = null;
        try {
            Prompt prompt = new Prompt(
                List.of(
                    new SystemMessage(KEYWORD_EXTRACTION_SYSTEM_PROMPT.formatted(KEYWORD_EXTRACTION_JSON_SCHEMA)),
                    new UserMessage(userPrompt)
                ),
                OllamaChatOptions.builder()
                    .format(KEYWORD_EXTRACTION_SCHEMA_MAP)
                    .thinkOption(ThinkOption.ThinkBoolean.DISABLED)
                    .build()
            );

            llmResponse = chatModel.call(prompt)
                    .getResult()
                    .getOutput()
                    .getText()
                    .trim();

        } catch (Exception e) {
            LOG.error("[키워드 추출] LLM 호출 실패 - 통과 처리", e);
        }

        LOG.info("[키워드 추출] LLM 원본 응답: {}", llmResponse);
        
        if (llmResponse == null || llmResponse.isBlank()) {
            return fallbackDto(userRagAsk);
        }

        try {
            FairyTaleSearchQueryDto dto = objectMapper.readValue(llmResponse, FairyTaleSearchQueryDto.class);
            return dto;
        } catch (Exception e) {
            LOG.error("[키워드 추출] LLM 응답 파싱 실패 - 통과 처리", e);
            return fallbackDto(userRagAsk);
        }
	}
	
    private FairyTaleSearchQueryDto fallbackDto(UserRagAsk ask) {
        FairyTaleSearchQueryDto fallback = new FairyTaleSearchQueryDto();
        fallback.setSearchPhrase(ask.getQuestion());
        fallback.setKeywords(List.of());
        fallback.setSemanticTags(List.of());
        fallback.setExpandedQuery(ask.getQuestion());
        return fallback;
    }
	


}