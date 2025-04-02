package wallet.ru.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import wallet.ru.dto.WalletOperationCreateDto;
import wallet.ru.dto.WalletOperationResponseDto;
import wallet.ru.entity.WalletOperation;
import wallet.ru.entity.WalletOperationType;
import wallet.ru.exception.IllegalOperationTypeException;

@Mapper(componentModel = "spring")
public interface WalletOperationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "operationType", source = "operationType", qualifiedByName = "operationType")
    WalletOperation toEntity(WalletOperationCreateDto walletCreateDto);

    @Mapping(target = "walletId", source = "wallet.id")
    WalletOperationResponseDto toResponse(WalletOperation walletOperation);

    @Named("operationType")
    default WalletOperationType setOperationType(String operationType) {
        try {
            return WalletOperationType.valueOf(operationType.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalOperationTypeException("Illegal operation type");
        }
    }
}
