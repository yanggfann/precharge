package com.example.precharge.repository;

public interface CustomizedOperations<T> {
  <S extends T> S insert(S entity);
}
