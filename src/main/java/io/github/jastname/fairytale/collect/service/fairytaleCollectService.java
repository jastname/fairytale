package io.github.jastname.fairytale.collect.service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.jastname.fairytale.collect.client.fairytaleCollectApiClient;
import io.github.jastname.fairytale.collect.dto.KcisaItem;
import io.github.jastname.fairytale.collect.dto.KcisaResponse;
import io.github.jastname.fairytale.collect.mapper.fairytaleCollectMapper;
import io.github.jastname.fairytale.collect.model.CollectHistory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class fairytaleCollectService {

    private static final Logger log = LoggerFactory.getLogger(fairytaleCollectService.class);
    private static final long PAGE_REQUEST_DELAY_MILLIS = 500L;

    private final fairytaleCollectApiClient fairytaleCollectApiClient;
    private final fairytaleCollectMapper fairytaleCollectMapper;

    
}