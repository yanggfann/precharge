package com.example.precharge.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.precharge.constant.Constants;
import com.example.precharge.controller.dto.PrechargeRequestDTO;
import com.example.precharge.controller.dto.PrechargeResponseDTO;
import com.example.precharge.service.PrechargeService;
import com.example.precharge.service.model.PrechargeRequestModel;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/precharge-contracts", produces = APPLICATION_JSON_VALUE)
public class PrechargeController {

  public static final String PRECHARGE_SUCCESS = "本次充值成功";
  public static final String PRECHARGE_FAILED = "本次充值失败，请稍后重试";
  private PrechargeService prechargeService;

  @PostMapping("/{cid}/precharges")
  PrechargeResponseDTO precharge(
      @PathVariable(value = "cid") String cid, @RequestBody PrechargeRequestDTO prechargeRequest) {
    PrechargeRequestModel prechargeModel =
        PrechargeRequestModel.builder()
            .contractId(UUID.fromString(cid))
            .amount(prechargeRequest.getAmount())
            .subject(Constants.SIWO_RENTAL_PRECHARGE_SUBJECT)
            .build();
    try {
      prechargeService.precharge(prechargeModel);
      return PrechargeResponseDTO.builder()
          .responseCode(HttpStatus.OK.value())
          .responseMsg(PRECHARGE_SUCCESS)
          .build();
    } catch (Exception e) {
      return PrechargeResponseDTO.builder()
          .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .responseMsg(PRECHARGE_FAILED)
          .build();
    }
  }
}
