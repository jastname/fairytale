package io.github.jastname.fairytale.collect.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KcisaResponse {
    private Response response;

    @Getter
    @Setter
    public static class Response {
        private Header header;
        private Body body;
    }

    // 편의 메서드: 기존 코드 호환성 유지
    public Header getHeader() {
        return response != null ? response.getHeader() : null;
    }

    public Body getBody() {
        return response != null ? response.getBody() : null;
    }

    @Getter
    @Setter
    public static class Header {
    	
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class Body {
    	
        private Items items;
        private String numOfRows;
        private String pageNo;
        private String totalCount;
        
    }

    @Getter
    @Setter
    public static class Items {
    	
        private List<KcisaItem> item;
        
    }

    @Getter
    @Setter
    public static class Item {
        private String title;
        private String alternativeTitle;
        private String creator;
        private String regDate;
        private String collectionDb;
        private String subjectCategory;
        private String subjectKeyword;
        private String extent;
        private String description;
        private String spatialCoverage;
        private String temporal;
        private String person;
        private String language;
        private String sourceTitle;
        private String referenceIdentifier;
        private String rights;
        private String copyrightOthers;
        private String url;
        private String contributor;
    }
}