package wallet.ru.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wallet.ru.entity.Wallet;
import wallet.ru.entity.WalletOperation;
import wallet.ru.entity.WalletOperationType;
import wallet.ru.exception.NotFoundException;
import wallet.ru.exception.InsufficientFundsException;
import wallet.ru.repository.WalletRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository repository;
    @Transactional(propagation = Propagation.MANDATORY)
    public Wallet updateBalance(WalletOperation walletOperation) {
        log.info("Updating balance for wallet {}", walletOperation.getWallet().getId());
        UUID walletId = walletOperation.getWallet().getId();
        Wallet wallet = repository.findByIdWithLock(walletId).orElseThrow(() ->
                new NotFoundException("Wallet with id: %s not found".formatted(walletId)));

        Long amount = walletOperation.getAmount();

        if (walletOperation.getOperationType() == WalletOperationType.DEPOSIT) {
            wallet.setBalance(wallet.getBalance() + amount);
        } else if (walletOperation.getOperationType() == WalletOperationType.WITHDRAW) {
            if (wallet.getBalance() < amount) {
                throw new InsufficientFundsException("There are not enough funds in the wallet");
            }
            wallet.setBalance(wallet.getBalance() - amount);
        }
        return repository.save(wallet);
    }


    public List<Wallet> findAllWallets() {
        return repository.findAll();
    }

    public Wallet getById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException("Wallet with id: %s not found".formatted(id)));
    }
}
