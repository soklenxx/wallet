package wallet.ru.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import wallet.ru.dto.WalletOperationCreateDto;
import wallet.ru.entity.Wallet;
import wallet.ru.entity.WalletOperation;
import wallet.ru.mapper.WalletOperationMapper;
import wallet.ru.repository.WalletOperationRepository;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class WalletOperationService {
    private final WalletOperationRepository repository;

    private final WalletService walletService;
    private final WalletOperationMapper mapper;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public WalletOperation create(WalletOperationCreateDto createDto) {
        WalletOperation walletOperation = mapper.toEntity(createDto);
        walletOperation.setWallet(walletService.getById(createDto.walletId()));
        Wallet wallet = walletService.updateBalance(walletOperation);
        walletOperation.setCreatedAt(LocalDateTime.now());
        walletOperation.setBalance(wallet.getBalance());
        log.info("Create wallet operation");
        return repository.save(walletOperation);
    }
}
