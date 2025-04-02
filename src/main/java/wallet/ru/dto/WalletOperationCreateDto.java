package wallet.ru.dto;

import java.util.UUID;

public record WalletOperationCreateDto(
        UUID walletId,
        String operationType,
        Long amount
) {
}
