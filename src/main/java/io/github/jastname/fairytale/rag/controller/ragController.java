package io.github.jastname.fairytale.rag.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.jastname.fairytale.rag.service.embeddingService;

@Controller
@RequestMapping("/rag/")
public class ragController {
	
	private static final Logger LOG = LogManager.getLogger(ragController.class);

	@Autowired
	@Qualifier("embeddingService")
	private embeddingService embeddingService;
	
	@ResponseBody
	@PostMapping("embedding")
	public String embedding() {
		LOG.info("embedding start");
		String result = embeddingService.chunkAndEmbedAllFairytales();
		LOG.info("embedding end");
		
		return result;
	}
}