package wallet.ru.mapper;

import org.mapstruct.Mapper;
import wallet.ru.dto.WalletResponseDto;
import wallet.ru.entity.Wallet;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponseDto toResponse(Wallet wallet);
}
