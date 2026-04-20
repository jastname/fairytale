package io.github.jastname.fairytale.rag.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FairyTaleSearchQueryDto {

    // 검색용 사용자 질문
    private String searchPhrase;
    // 핵심 검색 키워드 (제목, 등장인물, 주제 등)
    private List<String> keywords;
    // 보조 검색
    private List<String> semanticTags;
    private String expandedQuery;

    public FairyTaleSearchQueryDto() { }

    public FairyTaleSearchQueryDto(String searchPhrase, List<String> keywords, List<String> semanticTags, String expandedQuery) {
        this.searchPhrase = searchPhrase;
        this.keywords = keywords;
        this.semanticTags = semanticTags;
        this.expandedQuery = expandedQuery;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getSemanticTags() {
        return semanticTags;
    }

    public void setSemanticTags(List<String> semanticTags) {
        this.semanticTags = semanticTags;
    }

    public String getExpandedQuery() {
        return expandedQuery;
    }

    public void setExpandedQuery(String expandedQuery) {
        this.expandedQuery = expandedQuery;
    }
}
