package com.example.bank.infrarepo;

import com.example.bank.domain.logic.AccountTransactionCaching;
import com.example.bank.domain.model.AccountTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;



@Component
public class AccountTransactionCachingSimple implements AccountTransactionCaching {

    @Autowired
    private CacheManager cacheManager;

    private final static String CACHE_NAME = "myBankCache";

    @Override
    public boolean put(AccountTransaction cacheObject, String cacheKey, int expiresInSeconds) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        assert cache != null;
        cache.put(cacheKey, cacheObject);
        return true;
    }

    @Override
    public boolean delete(String cacheKey) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        assert cache != null;
        cache.evict(cacheKey);
        return true;
    }

    @Override
    public AccountTransaction get(String cacheKey) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        assert cache != null;
        Cache.ValueWrapper valueWrapper = cache.get(CACHE_NAME);
        if (valueWrapper != null) {
            Object object = valueWrapper.get();
            if (object instanceof AccountTransaction) {
                return (AccountTransaction) object;
            }
        }
        return null;
    }
}
