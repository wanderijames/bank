package com.example.bank;

import com.example.bank.web.BankAccountController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountInterfaceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountController bankAccountController;

    @Test
    void contextLoads() throws Exception {
        assertThat(bankAccountController).isNotNull();
    }

    @Test
    void shouldBeAbleToOpenBankAccount() throws Exception {
        this.mockMvc.perform(
                        post("/bank/openAccount?openingBalance=1000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance", is(1000)));
    }

    @Test
    void shouldBeAbleToWithdraw() throws Exception {
        // An account with 1000 balance should allow withdrawal of 10
        MvcResult result = this.mockMvc.perform(
                        post("/bank/openAccount?openingBalance=1000")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance", is(1000)))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Map<String, Object> resultMap = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        this.mockMvc.perform(
                        post(String.format("/bank/withdraw?amount=%d&accountId=%d", 10, resultMap.get("accountNumber")))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Withdrawal successful")));

    }

    @Test
    void shouldNotBeAbleToWithdraw() throws Exception {
        // An account with 1000 balance should not allow withdrawal of 10000
        MvcResult result = this.mockMvc.perform(
                        post("/bank/openAccount?openingBalance=1000")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountBalance", is(1000)))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        Map<String, Object> resultMap = new ObjectMapper().readValue(content, new TypeReference<>() {
        });
        this.mockMvc.perform(
                        post(String.format("/bank/withdraw?amount=%d&accountId=%d", 10000, resultMap.get("accountNumber")))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Insufficient funds for withdrawal")));

    }

}
