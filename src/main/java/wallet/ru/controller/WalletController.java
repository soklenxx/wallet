package wallet.ru.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wallet.ru.dto.WalletOperationCreateDto;
import wallet.ru.dto.WalletOperationResponseDto;
import wallet.ru.dto.WalletResponseDto;
import wallet.ru.mapper.WalletMapper;
import wallet.ru.mapper.WalletOperationMapper;
import wallet.ru.service.WalletOperationService;
import wallet.ru.service.WalletService;

import java.util.List;
import java.util.UUID;

import static wallet.ru.controller.URIConstants.*;

@RestController
@RequiredArgsConstructor
public class WalletController {
    private final WalletMapper walletMapper;
    private final WalletOperationMapper operationMapper;
    private final WalletService walletServices;
    private final WalletOperationService operationService;
    @GetMapping(WALLETS_URI + ID_PARAM)
    public WalletResponseDto getWallet(@PathVariable UUID wallet_uuid) {
        return walletMapper.toResponse(walletServices.getById(wallet_uuid));
    }

    @PostMapping(WALLET_URI)
    public WalletOperationResponseDto createOperation(@RequestBody WalletOperationCreateDto createDto) {
        return operationMapper.toResponse(operationService.create(createDto));
    }

    @GetMapping(WALLETS_URI)
    public List<WalletResponseDto> getAllWallets() {
        return walletServices.findAllWallets().stream().map(walletMapper::toResponse).toList();
    }
}
