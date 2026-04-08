package io.github.jastname.fairytale.collect.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.jastname.fairytale.common.config.KcisaProperties;
import io.github.jastname.fairytale.collect.dto.KcisaResponse;
import lombok.RequiredArgsConstructor;

//국립어린이청소년도서관_다국어동화구연-한국전래동화 API 클라이언트
@Component
@RequiredArgsConstructor
public class fairytaleCollectApiClient {

    private final RestClient restClient;
    private final KcisaProperties kcisaProperties;

    public KcisaResponse getFairytales(int pageNo) {
        String url = buildUrl(pageNo);

        return restClient.get()
                .uri(url)
                .accept(kcisaProperties.getResponseType())
                .retrieve()
                .body(KcisaResponse.class);
    }

    public String buildUrl(int pageNo) {
        return UriComponentsBuilder
                .fromHttpUrl(kcisaProperties.getBaseUrl())
                .path(kcisaProperties.getEndpoint())
                .queryParam("serviceKey", kcisaProperties.getServiceKey())
                .queryParam("numOfRows", kcisaProperties.getNumOfRows())
                .queryParam("pageNo", pageNo)
                .toUriString();
    }

    public int getNumOfRows() {
        return kcisaProperties.getNumOfRows();
    }
}