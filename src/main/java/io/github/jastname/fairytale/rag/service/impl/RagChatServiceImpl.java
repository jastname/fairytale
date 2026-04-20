package io.github.jastname.fairytale.rag.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jastname.fairytale.rag.dto.FairyTaleSearchQueryDto;
import io.github.jastname.fairytale.rag.dto.RagChunk;
import io.github.jastname.fairytale.rag.dto.RagEmbed;
import io.github.jastname.fairytale.rag.dto.UserRagAsk;
import io.github.jastname.fairytale.rag.mapper.RagMapper;
import io.github.jastname.fairytale.rag.safeguard.RagSafeguard;
import io.github.jastname.fairytale.rag.service.RagChatService;
import io.github.jastname.fairytale.rag.service.KeywordExtractionService;
import io.github.jastname.fairytale.rag.safeguard.dto.SafeguardResult;
import lombok.RequiredArgsConstructor;



@Service("RagChatService")
@RequiredArgsConstructor
public class RagChatServiceImpl implements RagChatService {

    private static final Logger LOG = LoggerFactory.getLogger(RagChatServiceImpl.class);

    private final RagSafeguard ragSafeguard;

    private final KeywordExtractionService keywordExtractionService;

    
	@Override
	public String ask(UserRagAsk userRagAsk) {
		
		// 1. 사용자 질문 safeguard
		if(userRagAsk.getQuestion() == null) {
			LOG.warn("[Safeguard 차단] 빈 질문");
			return "[오류] 질문이 비어 있습니다.";
		}
		
		SafeguardResult safeGuard = ragSafeguard.validate(userRagAsk);
		if(!safeGuard.isSafe()) {
			LOG.info("[Safeguard 차단] code: {}, reason: {}", safeGuard.getCode(), safeGuard.getReason());
		}

		// 2. 사용자 질문 키워드 추출(제목, 등장인물, 주제 등)
		FairyTaleSearchQueryDto queryData = keywordExtractionService.keywordExtraction(userRagAsk);
		LOG.info("[키워드 추출] searchPhrase: {}, keywords: {}, semanticTags: {}, expandedQuery: {}",
				queryData.getSearchPhrase(), queryData.getKeywords(), queryData.getSemanticTags(), queryData.getExpandedQuery());
		
		// 3. 사용자 질문 원본, 추출 키워드 임베딩 후 유사도 검색
		
		// 4. 검색 결과 ranking 후 userId, conversationId로 이전 대화 조회 후 context 구성 
		
		// 5. context + 사용자 질문 LLM에 전달 후 답변 생성
		
		LOG.info("[검색] userId: {}", userRagAsk.getUserId());
		LOG.info("[검색] conversationId: {}", userRagAsk.getConversationId());
		
		
		
		return null;
	}


}