package wallet.ru.dto;

import java.util.UUID;

public record WalletResponseDto(
        UUID id,
        Long balance
) {
}
