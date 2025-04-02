package wallet.ru.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wallet.ru.dto.WalletOperationCreateDto;
import wallet.ru.dto.WalletOperationResponseDto;
import wallet.ru.entity.Wallet;
import wallet.ru.repository.WalletOperationRepository;
import wallet.ru.repository.WalletRepository;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wallet.ru.controller.URIConstants.*;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    WalletOperationRepository walletOperationRepository;
    Wallet wallet;

    @BeforeEach
    @Transactional
    void setUp() {
        wallet = Instancio.create(Wallet.class);
        wallet.setId(UUID.randomUUID());
        wallet = walletRepository.save(wallet);
    }

    @AfterEach
    void tearDown() {
        walletOperationRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    void getWalletByUUIDTest() throws Exception {
        mockMvc.perform(get(WALLETS_URI + ID_PARAM, wallet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(wallet.getBalance()));
    }

    @Test
    void createOperationDepositTest() throws Exception {
        WalletOperationCreateDto createDto = new WalletOperationCreateDto(
                wallet.getId(),
                "DEPOSIT",
                1000L
        );

        WalletOperationResponseDto responseDto = new WalletOperationResponseDto(
                wallet.getId(),
                "DEPOSIT",
                wallet.getBalance() + createDto.amount()
        );

        mockMvc.perform(post(WALLET_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(responseDto.walletId().toString()))
                .andExpect(jsonPath("$.operationType").value(responseDto.operationType()))
                .andExpect(jsonPath("$.balance").value(responseDto.balance()));
    }

    @Test
    void createOperationWithdrawTest() throws Exception {
        WalletOperationCreateDto createDto = new WalletOperationCreateDto(
                wallet.getId(),
                "WITHDRAW",
                1000L
        );

        WalletOperationResponseDto responseDto = new WalletOperationResponseDto(
                wallet.getId(),
                "WITHDRAW",
                wallet.getBalance() - createDto.amount()
        );

        mockMvc.perform(post(WALLET_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(responseDto.walletId().toString()))
                .andExpect(jsonPath("$.operationType").value(responseDto.operationType()))
                .andExpect(jsonPath("$.balance").value(responseDto.balance()));
    }

    @Test
    void createOperationWithdrawIllegalAmountTest() throws Exception {
        WalletOperationCreateDto createDto = new WalletOperationCreateDto(
                wallet.getId(),
                "WITHDRAW",
                wallet.getBalance() + 1
        );

        mockMvc.perform(post(WALLET_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOperationIllegalTypeTest() throws Exception {
        WalletOperationCreateDto createDto = new WalletOperationCreateDto(
                wallet.getId(),
                "WIT77RAW",
                1000L
        );

        mockMvc.perform(post(WALLET_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWalletsTest() throws Exception {
        mockMvc.perform(get(WALLETS_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}