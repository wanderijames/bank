package com.example.bank.domain.logic;

import java.util.Optional;

public interface DataCaching<T> {
    boolean put(T cacheObject, String cacheKey, int expiresInSeconds);
    boolean delete(String cacheKey);
    T get(String cacheKey);
}
