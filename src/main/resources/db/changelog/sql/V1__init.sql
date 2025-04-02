CREATE TABLE wallet (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    balance BIGINT NOT NULL
);
CREATE TABLE wallet_operation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id UUID NOT NULL,
    operation_type VARCHAR(20) NOT NULL,
    amount BIGINT NOT NULL,
    balance BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);

INSERT INTO wallet (balance) VALUES (400000);
