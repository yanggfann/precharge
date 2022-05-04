package com.example.precharge.service;

import com.example.precharge.client.RestClient;
import com.example.precharge.client.model.ThirdPaymentRequest;
import com.example.precharge.client.model.ThirdPaymentResponse;
import com.example.precharge.repository.PrechargeRepository;
import com.example.precharge.repository.entity.PreCharge;
import com.example.precharge.service.exception.PrechargeFailException;
import com.example.precharge.service.model.PrechargeRequestModel;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class PrechargeService {
  private PrechargeRepository prechargeRepository;
  private RestClient restClient;

  @SneakyThrows
  public void precharge(PrechargeRequestModel prechargeModel) {
    ThirdPaymentRequest thirdPaymentRequest =
        ThirdPaymentRequest.builder()
            .subject(prechargeModel.getSubject())
            .amount(prechargeModel.getAmount())
            .build();
    ThirdPaymentResponse paymentResponse =
        restClient.post("/precharge", thirdPaymentRequest, ThirdPaymentResponse.class);

    if (paymentResponse.getResponseCode() != 200) {
      throw new PrechargeFailException(
          String.format(
              "Precharge failed, amount: %s, error message: %s",
              prechargeModel.getAmount(), paymentResponse.getResponseMsg()));
    }
    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    PreCharge preCharge =
        PreCharge.builder()
            .id(UUID.randomUUID())
            .contractId(prechargeModel.getContractId())
            .amount(BigDecimal.valueOf(prechargeModel.getAmount()))
            .createdAt(now)
            .updatedAt(now)
            .build();
    prechargeRepository.insert(preCharge);
  }
}
