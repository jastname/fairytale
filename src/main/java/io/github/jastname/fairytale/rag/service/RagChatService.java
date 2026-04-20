package io.github.jastname.fairytale.rag.service;

import io.github.jastname.fairytale.rag.dto.UserRagAsk;

public interface RagChatService {
	
	public String ask(UserRagAsk userRagAsk);

}