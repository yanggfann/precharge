package com.example.precharge.repository;

import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.transaction.annotation.Transactional;

public class CustomizedOperationsImpl<T> implements CustomizedOperations<T> {

  private final JdbcAggregateOperations jdbcOperations;

  public CustomizedOperationsImpl(JdbcAggregateOperations jdbcOperations) {
    this.jdbcOperations = jdbcOperations;
  }

  @Transactional
  @Override
  public <S extends T> S insert(S entity) {
    return jdbcOperations.insert(entity);
  }
}
