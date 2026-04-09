package io.github.jastname.fairytale.collect.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jastname.fairytale.collect.client.fairytaleCollectApiClient;
import io.github.jastname.fairytale.collect.dto.KcisaItem;
import io.github.jastname.fairytale.collect.dto.KcisaResponse;
import io.github.jastname.fairytale.collect.mapper.fairytaleCollectMapper;
import io.github.jastname.fairytale.collect.model.CollectHistory;
import io.github.jastname.fairytale.collect.service.fairytaleCollectService;
import lombok.RequiredArgsConstructor;

import io.github.jastname.fairytale.utill.CommonUtil;

@Service("fairytaleCollectService")
@RequiredArgsConstructor
public class fairytaleCollectServiceImpl implements fairytaleCollectService {

    private static final Logger log = LoggerFactory.getLogger(fairytaleCollectServiceImpl.class);
    private static final long PAGE_DELAY_MS = 500L;

    private final fairytaleCollectApiClient apiClient;
    private final fairytaleCollectMapper mapper;

    @Override
    public String collectFairytales() {
        int page = 1;
        int totalCollected = 0;
        int totalCount = 0;

        try {
        	//이전 적재 내용 제거
        	mapper.deleteFairytaleAll();
            while (true) {
                // 1. API 호출
                KcisaResponse response = fetchResponse(page);

                // 2. 데이터 추출
                List<KcisaItem> items = getItems(response, page);

                totalCollected += items.size();
                totalCount = getTotalCount(response, page);

                log.info("[수집 진행] collected={}", totalCollected);

                // 3. 수집 이력 저장
                String collectId = saveHistory(page, response, items);

                // 4. API 호출 딜레이
                sleep(page);

                // 5. 데이터 저장
                saveItems(items, collectId, page);

                log.info("[페이지 완료] page={}, progress={}/{}", page, totalCollected, totalCount);

                // 6. 종료 조건 체크
                if (isCollectionComplete(totalCollected, totalCount)) {
                    log.info("[완료] 총 수집된 동화 수: {}", totalCollected);
                    break;
                }
                page++;               

            }

            return "success";

        } catch (Exception e) {
            log.error("[ERROR] 동화 수집 실패 page={}, collected={}", page, totalCollected, e);
            return "fail";
        }
    }

    //api 호출 및 응답 검증
    private KcisaResponse fetchResponse(int page) {
        try {
            KcisaResponse response = apiClient.getFairytales(page);

            if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                throw new IllegalStateException("API 응답 구조가 올바르지 않습니다.");
            }

            return response;

        } catch (Exception e) {
            log.error("[API ERROR] page={}", page, e);
            throw e;
        }
    }
    
    private List<KcisaItem> getItems(KcisaResponse response, int page) {
        List<KcisaItem> items = response.getBody().getItems().getItem();

        if (items == null) {
            throw new IllegalStateException("item 리스트가 null 입니다. page=" + page);
        }

        return items;
    }
    
    // totalCount 파싱
    private int getTotalCount(KcisaResponse response, int page) {
        try {
            return Integer.parseInt(response.getBody().getTotalCount());
        } catch (NumberFormatException e) {
            log.error("[PARSE ERROR] page={}, totalCount={}",
                    page, response.getBody().getTotalCount(), e);
            throw e;
        }
    }

    private boolean isCollectionComplete(int collected, int total) {
        return collected >= total;
    }

    
    //수집 내역 저장
    @Transactional
    private String saveHistory(int page, KcisaResponse response, List<KcisaItem> items) {
        String collectId = CommonUtil.idMake("");

        CollectHistory history = new CollectHistory();
        history.setCollectId(collectId);
        history.setPageNo(page);
        history.setRequestUrl(apiClient.buildUrl(page));
        history.setNumOfRows(apiClient.getNumOfRows());
        history.setResultCode(response.getHeader().getResultCode());
        history.setResultMsg(response.getHeader().getResultMsg());
        history.setSuccessYn("Y");
        history.setCollectedCount(items.size());

        try {
            mapper.insertCollectHistory(history);
            return collectId;

        } catch (Exception e) {
            log.error("[DB ERROR] history 저장 실패 collectId={}, page={}", collectId, page, e);
            throw e;
        }
    }
    
    @Transactional
    private void saveItems(List<KcisaItem> items, String collectId, int page) {
        try {
            for (KcisaItem item : items) {
                item.setFairytaleId(CommonUtil.idMake(""));
                item.setCollectId(collectId);
                mapper.upsertStory(item);
            }
        } catch (Exception e) {
            log.error("[DB ERROR] 동화 저장 실패 collectId={}, page={}", collectId, page, e);
            throw e;
        }
    }


    private void sleep(int page) {
        try {
            TimeUnit.MILLISECONDS.sleep(PAGE_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[INTERRUPT] sleep 중 인터럽트 page={}", page, e);
            throw new RuntimeException(e);
        }
    }
}