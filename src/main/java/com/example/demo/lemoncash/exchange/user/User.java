package com.example.demo.lemoncash.exchange.user;

import com.example.demo.lemoncash.exchange.wallet.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private String surname;
    private String alias;
    private String email;
    private List<Wallet> wallets;
}
