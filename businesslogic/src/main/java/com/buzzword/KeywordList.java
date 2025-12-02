package com.buzzword;

import java.util.ArrayList;
import java.util.List;

public class KeywordList {
    private List<String> keywords;
    private final int MAX_KEYWORDS = 100;

    private final XssSanitizer resourceSanitizer;
    private final Logger logger = LoggerFactory.getEventLogger();

    public KeywordList() {
        this.keywords = new ArrayList<>();
        this.resourceSanitizer = new XssSanitizerImpl();
    }

    public KeywordList(String keywordStr) {
        this.keywords = validateKeywordStr(keywordStr);
        this.resourceSanitizer = new XssSanitizerImpl();
    }

    private List<String> validateKeywordStr(String keywordStr) {
        String[] keywordArray = keywordStr.split("\\s+", MAX_KEYWORDS);
        List<String> sanitizedKeywords = new ArrayList<>();
        for (String keyword : keywordArray) {
            sanitizedKeywords.add(resourceSanitizer.sanitizeInput(keyword.trim()));
        }
        return sanitizedKeywords;
    }

    private List<String> validateKeywords(List<String> keywords) {
        List<String> sanitizedKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            sanitizedKeywords.add(resourceSanitizer.sanitizeInput(keyword));
        }
        return sanitizedKeywords;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = validateKeywords(keywords);
    }

    public void setKeywordsByString(String keywordStr) {
        this.keywords = validateKeywordStr(keywordStr);
    }
}
