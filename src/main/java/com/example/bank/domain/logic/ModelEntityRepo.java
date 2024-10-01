package com.example.bank.domain.logic;

import java.util.List;
import java.util.Optional;

public interface ModelEntityRepo<T,U> {

    public Optional<T> save(T record);

    public T update(T record);

    public T findById(U id);

    public List<T> findAll();
}