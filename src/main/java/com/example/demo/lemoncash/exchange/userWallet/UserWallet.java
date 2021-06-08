package com.example.demo.lemoncash.exchange.userWallet;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserWallet {
    private Long userId;
    private Long walletId;
    private LocalDateTime lastUpdated;
}
