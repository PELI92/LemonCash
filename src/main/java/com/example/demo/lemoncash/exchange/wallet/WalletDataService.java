package com.example.demo.lemoncash.exchange.wallet;

import com.example.demo.lemoncash.exceptions.CreateEntityException;
import lombok.Builder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class WalletDataService {

    @Resource
    private final WalletDAO walletDAO;

    public Optional<Wallet> getById(Long walletId) {
        return walletDAO.getById(walletId).stream().findFirst();
    }

    public List<Wallet> getByIds(List<Long> walletIds) {
        return walletDAO.getByIds(walletIds);
    }

    public Long create(Wallet wallet) throws CreateEntityException {
        return walletDAO.save(wallet);
    }

    public void updateWalletBalance(Long walletId, Double balance) {
        walletDAO.updateWalletBalance(walletId, balance);
    }

}
