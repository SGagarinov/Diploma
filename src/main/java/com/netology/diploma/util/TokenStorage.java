package com.netology.diploma.util;

import com.netology.diploma.entity.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TokenStorage {

    private final ConcurrentHashMap<String, User> tokenList = new ConcurrentHashMap<>();

    public TokenStorage() {

    }

    public Map<String, User> getTokenList() {
        return tokenList;
    }
}
