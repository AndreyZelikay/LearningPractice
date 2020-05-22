package org.bsu.twiter.dao;

import java.util.Optional;

public interface CRUD<T> {
    Optional<T> findById(Long id);
    long save(T object);
    boolean update(T object);
    boolean delete(Long id);
}
