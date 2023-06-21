package com.netology.diploma.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TokenStorage {

    private final ConcurrentHashMap<String, String> tokenList = new ConcurrentHashMap<>();

    public TokenStorage() {

    }

    public Map<String, String> getTokenList() {
        return tokenList;
    }
}
