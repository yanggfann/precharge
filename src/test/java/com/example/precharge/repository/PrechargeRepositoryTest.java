package com.example.precharge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.precharge.TestBase;
import com.example.precharge.repository.entity.PreCharge;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PrechargeRepositoryTest extends TestBase {

  @Autowired private PrechargeRepository prechargeRepository;

  @Test
  public void shouldReturnEmptyPreChargeRecord() {
    UUID contractId = UUID.randomUUID();

    List<PreCharge> preCharges = prechargeRepository.findByContractId(contractId);

    assertThat(preCharges.size()).isEqualTo(0);
  }

  @Test
  public void shouldReturnOnePreChargeRecord() {
    UUID contractId = UUID.randomUUID();
    OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
    PreCharge preCharge =
        PreCharge.builder()
            .id(UUID.randomUUID())
            .contractId(contractId)
            .amount(BigDecimal.valueOf(100))
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build();
    prechargeRepository.insert(preCharge);

    List<PreCharge> preCharges = prechargeRepository.findByContractId(contractId);

    assertThat(preCharges.size()).isEqualTo(1);
    assertThat(preCharges.get(0)).usingRecursiveComparison().isEqualTo(preCharge);
  }

  @Test
  public void shouldReturnAllPreChargeRecord() {
    UUID contractId = UUID.randomUUID();
    OffsetDateTime currentTime1 = OffsetDateTime.now(ZoneOffset.UTC);
    PreCharge preCharge1 =
        PreCharge.builder()
            .id(UUID.randomUUID())
            .contractId(contractId)
            .amount(BigDecimal.valueOf(200))
            .createdAt(currentTime1)
            .updatedAt(currentTime1)
            .build();
    prechargeRepository.insert(preCharge1);

    OffsetDateTime currentTime2 = OffsetDateTime.now(ZoneOffset.UTC);
    PreCharge preCharge2 =
        PreCharge.builder()
            .id(UUID.randomUUID())
            .contractId(contractId)
            .amount(BigDecimal.valueOf(300))
            .createdAt(currentTime2)
            .updatedAt(currentTime2)
            .build();
    prechargeRepository.insert(preCharge2);

    List<PreCharge> preCharges = prechargeRepository.findByContractId(contractId);

    assertThat(preCharges.size()).isEqualTo(2);
    assertThat(preCharges.get(0)).usingRecursiveComparison().isEqualTo(preCharge1);
    assertThat(preCharges.get(1)).usingRecursiveComparison().isEqualTo(preCharge2);
  }
}
