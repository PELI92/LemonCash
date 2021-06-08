package com.example.demo.lemoncash.exchange.userWallet;

import com.example.demo.lemoncash.exceptions.CreateEntityException;
import lombok.Builder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Builder
public class UserWalletDataService {

    @Resource
    private final UserWalletDAO userWalletDAO;

    public List<UserWallet> getByUserId(Long id) {
        return userWalletDAO.getByUserId(id);
    }

    public List<UserWallet> getByWalletId(Long id) {
        return userWalletDAO.getByWalletId(id);
    }

    public Long create(UserWallet userWallet) throws CreateEntityException {
        return userWalletDAO.save(userWallet);
    }

}
