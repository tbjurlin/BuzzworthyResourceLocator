package com.buzzword;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class KeywordList {
    @JsonIgnore
    private List<String> keywordList;
    private final int MAX_KEYWORDS = 100;

    private final XssSanitizer resourceSanitizer;
    private final Logger logger = LoggerFactory.getEventLogger();

    public KeywordList() {
        this.resourceSanitizer = new XssSanitizerImpl();
        this.keywordList = new ArrayList<>();
    }

    public KeywordList(String keywords) {
        this();
        setKeywords(keywords);
    }

    public List<String> getKeywords() {
        return keywordList;
    }

    public void setKeywords(String keywords) {
        String[] splitKeywords = keywords.split(" ", MAX_KEYWORDS);
        for (String keyword : splitKeywords) {
            this.keywordList.add(resourceSanitizer.sanitizeInput(keyword.trim().toLowerCase()));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywordList) {
            sb.append(keyword).append(" ");
        }
        return sb.toString().trim();
    }
}
