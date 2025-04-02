package wallet.ru.dto;

import java.util.UUID;

public record WalletOperationResponseDto(
        UUID walletId,
        String operationType,
        Long balance
) {
}
