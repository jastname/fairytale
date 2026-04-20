package io.github.jastname.fairytale.rag.service;

import io.github.jastname.fairytale.rag.dto.FairyTaleSearchQueryDto;
import io.github.jastname.fairytale.rag.dto.UserRagAsk;

public interface KeywordExtractionService {
	
	public FairyTaleSearchQueryDto keywordExtraction(UserRagAsk userRagAsk);

}