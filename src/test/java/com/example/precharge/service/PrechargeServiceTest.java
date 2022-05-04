package com.example.precharge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.precharge.client.RestClient;
import com.example.precharge.client.model.ThirdPaymentRequest;
import com.example.precharge.client.model.ThirdPaymentResponse;
import com.example.precharge.constant.Constants;
import com.example.precharge.repository.PrechargeRepository;
import com.example.precharge.service.exception.PrechargeFailException;
import com.example.precharge.service.model.PrechargeRequestModel;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrechargeServiceTest {
  @Mock private PrechargeRepository prechargeRepository;
  @Mock private RestClient restClient;
  private PrechargeService prechargeService;

  @BeforeEach
  public void setup() {
    prechargeService = new PrechargeService(prechargeRepository, restClient);
  }

  // Story1 - AC1 - Example1 - 工序4
  @Test
  public void shouldPrechargeSuccessfully() {
    int amount = 100;
    PrechargeRequestModel prechargeModel =
        PrechargeRequestModel.builder()
            .contractId(UUID.randomUUID())
            .subject(Constants.SIWO_RENTAL_PRECHARGE_SUBJECT)
            .amount(amount)
            .build();
    ThirdPaymentRequest thirdPaymentRequest =
        ThirdPaymentRequest.builder()
            .subject(prechargeModel.getSubject())
            .amount(prechargeModel.getAmount())
            .build();
    ThirdPaymentResponse thirdPaymentResponse =
        ThirdPaymentResponse.builder().responseCode(200).build();
    when(restClient.post(
            eq("/precharge"), refEq(thirdPaymentRequest), eq(ThirdPaymentResponse.class)))
        .thenReturn(thirdPaymentResponse);

    prechargeService.precharge(prechargeModel);

    verify(prechargeRepository, times(1))
        .insert(argThat(argument -> argument.getAmount().intValue() == amount));
  }

  // Story1 - AC2 - Example1 - 工序4
  @Test
  public void shouldPrechargeFailedWhenInvokeThirdPaymentFailed() {
    PrechargeRequestModel prechargeRequestModel =
        PrechargeRequestModel.builder()
            .contractId(UUID.randomUUID())
            .subject(Constants.SIWO_RENTAL_PRECHARGE_SUBJECT)
            .amount(100)
            .build();
    ThirdPaymentRequest thirdPaymentRequest =
        ThirdPaymentRequest.builder()
            .subject(prechargeRequestModel.getSubject())
            .amount(prechargeRequestModel.getAmount())
            .build();
    ThirdPaymentResponse thirdPaymentResponse =
        ThirdPaymentResponse.builder().responseCode(500).responseMsg("Payment failed").build();
    when(restClient.post(
            eq("/precharge"), refEq(thirdPaymentRequest), eq(ThirdPaymentResponse.class)))
        .thenReturn(thirdPaymentResponse);

    PrechargeFailException prechargeFailException =
        assertThrows(
            PrechargeFailException.class, () -> prechargeService.precharge(prechargeRequestModel));
    assertEquals(
        "Precharge failed, amount: 100, error message: Payment failed",
        prechargeFailException.getMessage());
  }
}
