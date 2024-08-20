package com.example.simple_crud.logout;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BlackList {
    private Set<String> blackListTokens = new HashSet<>();


    public void blackListToken(String token) {
        blackListTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blackListTokens.contains(token);
    }
}
