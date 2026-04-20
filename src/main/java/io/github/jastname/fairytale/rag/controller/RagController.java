package io.github.jastname.fairytale.rag.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.jastname.fairytale.rag.service.EmbeddingService;
import io.github.jastname.fairytale.rag.service.RagChatService;

import io.github.jastname.fairytale.rag.dto.UserRagAsk;

@Controller
@RequestMapping("/rag/")
public class RagController {
	
	private static final Logger LOG = LogManager.getLogger(RagController.class);

	@Autowired
	@Qualifier("embeddingService")
	private EmbeddingService embeddingService;
	
	@Autowired
	private RagChatService ragChatService;
	
	@ResponseBody
	@PostMapping("embedding")
	public String embedding() {
		LOG.info("[청킹및 임베딩 시작]");
		String result = embeddingService.chunkAndEmbedAllFairytales();
		LOG.info("[청킹및 임베딩 종료] 결과: {}", result);
		
		return result;
	}
	
	@ResponseBody
	@PostMapping("ask")
	public String ask(UserRagAsk userRagAsk) {
		
		LOG.info("[검색 시작] 검색어: {}", userRagAsk.getQuestion());
		

		String answer = ragChatService.ask(userRagAsk);
		LOG.info("[검색 완료] 답변: {}", answer);
		return answer;
	}
}