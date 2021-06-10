package com.example.demo.lemoncash.exchange.user;

import com.example.demo.lemoncash.database.TransactionService;
import com.example.demo.lemoncash.exceptions.EntityNotFoundException;
import com.example.demo.lemoncash.exceptions.RetrieveEntityException;
import com.example.demo.lemoncash.exchange.coin.type.CoinTypeDataService;
import com.example.demo.lemoncash.exchange.userWallet.UserWallet;
import com.example.demo.lemoncash.exchange.userWallet.UserWalletDataService;
import com.example.demo.lemoncash.exchange.wallet.Wallet;
import com.example.demo.lemoncash.exchange.wallet.WalletDataService;
import lombok.Builder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

import static com.example.demo.lemoncash.exchange.wallet.DefaultWalletFactory.defaultEmptyWallet;
import static java.util.stream.Collectors.toList;

@Service
@Builder
public class UserService {

    @Resource
    private final UserDataService userDataService;

    @Resource
    private final WalletDataService walletDataService;

    @Resource
    private final UserWalletDataService userWalletDataService;

    @Resource
    private final CoinTypeDataService coinTypeDataService;

    @Resource
    private final TransactionService transactionService;


    public void create(UserRequest userRequest) throws SQLException {
        User user = userRequest.toUser();
        List<Wallet> wallets = defaultEmptyWallet(coinTypeDataService.getAllCoinTypes());
        transactionService.executeTransaction(() -> {
            Long newUserId = userDataService.create(user);
            for (Wallet wallet : wallets) {
                Long newWalletId = walletDataService.create(wallet);
                wallet.setId(newWalletId);
                UserWallet userWallet = UserWallet.builder()
                        .userId(newUserId)
                        .walletId(newWalletId)
                        .build();
                userWalletDataService.create(userWallet);
            }
        });

    }


    public User getById(Long id) {
        User user = userDataService.getById(id)
                .orElseThrow(() -> new RetrieveEntityException(String.format("404 - Not Found for entity user: %s", id)));
        List<UserWallet> userWallets = userWalletDataService.getByUserId(user.getId());

        return getUserWithWallets(user, userWallets);
    }

    public User getByAlias(String alias) {
        User user = userDataService.getByAlias(alias)
                .orElseThrow(() -> new RetrieveEntityException(String.format("404 - Not Found for entity user by alias: %s", alias)));
        List<UserWallet> userWallets = userWalletDataService.getByUserId(user.getId());

        return getUserWithWallets(user, userWallets);
    }

    public User getByEmail(String email) {
        User user = userDataService.getByEmail(email)
                .orElseThrow(() -> new RetrieveEntityException(String.format("404 - Not Found for entity user by email: %s", email)));
        List<UserWallet> userWallets = userWalletDataService.getByUserId(user.getId());

        return getUserWithWallets(user, userWallets);
    }


    private User getUserWithWallets(User user, List<UserWallet> userWallets) {
        List<Long> userWalletsIds = userWallets.stream().map(UserWallet::getWalletId).collect(toList());

        List<Wallet> wallet = walletDataService.getByIds(userWalletsIds);

        wallet.forEach(
                w -> coinTypeDataService.getById(w.getCoinTypeId()).ifPresentOrElse(
                        ct -> {
                            w.setCoinName(ct.getNameAbbr());
                            w.setFormatedBalance(getFormattedAmount(w.getBalance(), w.getCoinTypeId()));
                        },
                        () -> {
                            throw new EntityNotFoundException("Coin Type Not found");
                        }));


        user.getWallets().addAll(wallet);
        return user;
    }

    private String getFormattedAmount(Double amount, Integer coinTypeId) {
        return coinTypeDataService.getFormattedAmount(amount, coinTypeId);
    }

}
