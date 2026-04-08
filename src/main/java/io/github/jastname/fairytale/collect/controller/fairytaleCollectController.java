package io.github.jastname.fairytale.collect.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.jastname.fairytale.collect.service.fairytaleCollectService;

@Controller
@RequestMapping("/collect/")
public class fairytaleCollectController {
	
	private static final Logger LOG = LogManager.getLogger(fairytaleCollectController.class);

	@Autowired
	@Qualifier("fairytaleCollectService")
	private fairytaleCollectService fairytaleCollectService;
	
	
	@PostMapping("start")
	public String startCollecting() {
		LOG.info("동화 수집 시작");
				
		String result = fairytaleCollectService.collectFairytales();
		LOG.info("동화 수집 결과: {}", result);
		
		return result; 
	}
	
}