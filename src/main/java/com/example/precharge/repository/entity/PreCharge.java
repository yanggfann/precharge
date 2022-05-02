package com.example.precharge.repository.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("precharge")
public class PreCharge {
  @Id private UUID id;
  private UUID contractId;
  private BigDecimal amount;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}
