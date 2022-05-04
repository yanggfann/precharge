package com.example.precharge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.precharge.controller.dto.PrechargeRequestDTO;
import com.example.precharge.service.PrechargeService;
import com.example.precharge.service.exception.PrechargeFailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ExtendWith(SpringExtension.class)
class PrechargeControllerTest {

  @Autowired MockMvc mockMvc;

  @MockBean private PrechargeService prechargeService;

  // Story1 - AC1 - Example1 - 工序5
  @Test
  @SneakyThrows
  void shouldPrechargeSuccessWithStatusIs200() {
    doNothing().when(prechargeService).precharge(any());
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    PrechargeRequestDTO prechargeRequestDTO = PrechargeRequestDTO.builder().amount(200).build();
    String requestJson = ow.writeValueAsString(prechargeRequestDTO);

    mockMvc
        .perform(
            post("/precharge-contracts/{cid}/precharges", "4F267B87-ED83-4BC7-9629-33A9575F8F9F")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.responseMsg").value("本次充值成功"));
  }

  // Story1 - AC2 - Example1 - 工序5
  @Test
  @SneakyThrows
  void shouldPrechargeFailedWithStatusIs500() {
    doThrow(new PrechargeFailException("Precharge failed")).when(prechargeService).precharge(any());
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    PrechargeRequestDTO prechargeRequestDTO = PrechargeRequestDTO.builder().amount(200).build();
    String requestJson = ow.writeValueAsString(prechargeRequestDTO);

    mockMvc
        .perform(
            post("/precharge-contracts/{cid}/precharges", "4F267B87-ED83-4BC7-9629-33A9575F8F9F")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.responseMsg").value("本次充值失败，请稍后重试"));
  }
}
