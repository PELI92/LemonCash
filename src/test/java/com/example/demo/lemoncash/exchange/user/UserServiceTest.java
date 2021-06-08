package com.example.demo.lemoncash.exchange.user;

import com.example.demo.lemoncash.database.TransactionService;
import com.example.demo.lemoncash.exchange.coin.type.CoinType;
import com.example.demo.lemoncash.exchange.coin.type.CoinTypeDataService;
import com.example.demo.lemoncash.exchange.userWallet.UserWallet;
import com.example.demo.lemoncash.exchange.userWallet.UserWalletDataService;
import com.example.demo.lemoncash.exchange.wallet.Wallet;
import com.example.demo.lemoncash.exchange.wallet.WalletDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.lemoncash.fixture.CoinTypeFixture.getAllCoinTypes;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserDataService userDataService;

    @MockBean
    private WalletDataService walletDataService;

    @MockBean
    private UserWalletDataService userWalletDataService;

    @MockBean
    private CoinTypeDataService coinTypeDataService;

    @Captor
    private ArgumentCaptor<Wallet> argumentCaptorWallet;

    @Captor
    private ArgumentCaptor<UserWallet> argumentCaptorUserWallet;

    @Captor
    private ArgumentCaptor<Integer> argumentCaptorCoinTypeId;

    @Before
    public void init() {
        userService = new UserService(
                userDataService,
                walletDataService,
                userWalletDataService,
                coinTypeDataService,
                new TransactionService(mock(PlatformTransactionManager.class)));
    }

    @Test
    public void create() throws Exception {

        UserRequest userRequest = UserRequest.builder()
                .name("Juan")
                .surname("Perez")
                .alias("Juan-Perez")
                .email("juan.perez@gmail.com")
                .build();

        User user = User.builder()
                .name("Juan")
                .surname("Perez")
                .alias("juan-perez")
                .email("juan.perez@gmail.com")
                .build();

        List<Wallet> expectedWallet = List.of(
                Wallet.builder()
                        .coinTypeId(1)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .coinTypeId(2)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .coinTypeId(3)
                        .balance(0.0)
                        .build());

        List<UserWallet> expectedUserWallet = List.of(
                UserWallet.builder()
                        .userId(1L)
                        .walletId(1L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(2L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(3L)
                        .build());


        when(coinTypeDataService.getAllCoinTypes()).thenReturn(getAllCoinTypes());

        when(userDataService.create(eq(user))).thenReturn(1L);

        when(walletDataService.create(eq(expectedWallet.get(0)))).thenReturn(1L);
        when(walletDataService.create(eq(expectedWallet.get(1)))).thenReturn(2L);
        when(walletDataService.create(eq(expectedWallet.get(2)))).thenReturn(3L);

        when(userWalletDataService.create(eq(expectedUserWallet.get(0)))).thenReturn(1L);
        when(userWalletDataService.create(eq(expectedUserWallet.get(1)))).thenReturn(2L);
        when(userWalletDataService.create(eq(expectedUserWallet.get(2)))).thenReturn(3L);

        userService.create(userRequest);

        verify(coinTypeDataService).getAllCoinTypes();

        verify(userDataService).create(eq(user));

        verify(walletDataService, times(3))
                .create(argumentCaptorWallet.capture());
        assertThat(argumentCaptorWallet.getAllValues()).isEqualTo(expectedWallet);


        verify(userWalletDataService, times(3))
                .create(argumentCaptorUserWallet.capture());
        assertThat(argumentCaptorUserWallet.getAllValues()).isEqualTo(expectedUserWallet);

    }

    @Test
    public void getById() {
        Long userId = 1L;

        Optional<User> user = Optional.of(User.builder()
                .id(1L)
                .name("Juan")
                .surname("Perez")
                .alias("juan-perez")
                .email("juan.perez@gmail.com")
                .wallets(new LinkedList<>())
                .build());


        List<UserWallet> userWallets = List.of(
                UserWallet.builder()
                        .userId(1L)
                        .walletId(1L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(2L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(3L)
                        .build());

        List<Wallet> wallets = List.of(
                Wallet.builder()
                        .id(1L)
                        .coinTypeId(1)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .id(2L)
                        .coinTypeId(2)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .id(3L)
                        .coinTypeId(3)
                        .balance(0.0)
                        .build());

        List<Long> walletIds = userWallets.stream().map(UserWallet::getWalletId).collect(toList());

        when(userDataService.getById(userId)).thenReturn(user);
        when(userWalletDataService.getByUserId(userId)).thenReturn(userWallets);
        when(walletDataService.getByIds(walletIds)).thenReturn(wallets);

        when(coinTypeDataService.getById(1)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(0)));
        when(coinTypeDataService.getById(2)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(1)));
        when(coinTypeDataService.getById(3)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(2)));

        User actualUser = userService.getById(userId);

        verify(userDataService).getById(userId);
        verify(userWalletDataService).getByUserId(userId);
        verify(walletDataService).getByIds(walletIds);

        verify(coinTypeDataService, times(3))
                .getById(argumentCaptorCoinTypeId.capture());
        assertThat(argumentCaptorCoinTypeId.getAllValues()).isEqualTo(getAllCoinTypes().stream().map(CoinType::getId).collect(toList()));

        User expectedUser = User.builder()
                .id(1L)
                .name("Juan")
                .surname("Perez")
                .alias("juan-perez")
                .email("juan.perez@gmail.com")
                .wallets(wallets)
                .build();

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    public void getByAlias() {
        String alias = "juan-perez";

        Optional<User> user = Optional.of(User.builder()
                .id(1L)
                .name("Juan")
                .surname("Perez")
                .alias("juan-perez")
                .email("juan.perez@gmail.com")
                .wallets(new LinkedList<>())
                .build());


        List<UserWallet> userWallets = List.of(
                UserWallet.builder()
                        .userId(1L)
                        .walletId(1L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(2L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(3L)
                        .build());

        List<Wallet> wallets = List.of(
                Wallet.builder()
                        .id(1L)
                        .coinTypeId(1)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .id(2L)
                        .coinTypeId(2)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .id(3L)
                        .coinTypeId(3)
                        .balance(0.0)
                        .build());

        List<Long> walletIds = userWallets.stream().map(UserWallet::getWalletId).collect(toList());

        when(userDataService.getByAlias(alias)).thenReturn(user);
        when(userWalletDataService.getByUserId(user.get().getId())).thenReturn(userWallets);
        when(walletDataService.getByIds(walletIds)).thenReturn(wallets);

        when(coinTypeDataService.getById(1)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(0)));
        when(coinTypeDataService.getById(2)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(1)));
        when(coinTypeDataService.getById(3)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(2)));

        User actualUser = userService.getByAlias(alias);

        verify(userDataService).getByAlias(alias);
        verify(userWalletDataService).getByUserId(user.get().getId());
        verify(walletDataService).getByIds(walletIds);

        verify(coinTypeDataService, times(3))
                .getById(argumentCaptorCoinTypeId.capture());
        assertThat(argumentCaptorCoinTypeId.getAllValues()).isEqualTo(getAllCoinTypes().stream().map(CoinType::getId).collect(toList()));

        User expectedUser = User.builder()
                .id(1L)
                .name("Juan")
                .surname("Perez")
                .alias("juan-perez")
                .email("juan.perez@gmail.com")
                .wallets(wallets)
                .build();

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    public void getByEmail() {
        String email = "juan.perez@gmail.com";

        Optional<User> user = Optional.of(User.builder()
                .id(1L)
                .name("Juan")
                .surname("Perez")
                .alias("juan-perez")
                .email("juan.perez@gmail.com")
                .wallets(new LinkedList<>())
                .build());


        List<UserWallet> userWallets = List.of(
                UserWallet.builder()
                        .userId(1L)
                        .walletId(1L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(2L)
                        .build(),
                UserWallet.builder()
                        .userId(1L)
                        .walletId(3L)
                        .build());

        List<Wallet> wallets = List.of(
                Wallet.builder()
                        .id(1L)
                        .coinTypeId(1)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .id(2L)
                        .coinTypeId(2)
                        .balance(0.0)
                        .build(),
                Wallet.builder()
                        .id(3L)
                        .coinTypeId(3)
                        .balance(0.0)
                        .build());

        List<Long> walletIds = userWallets.stream().map(UserWallet::getWalletId).collect(toList());

        when(userDataService.getByEmail(email)).thenReturn(user);
        when(userWalletDataService.getByUserId(user.get().getId())).thenReturn(userWallets);
        when(walletDataService.getByIds(walletIds)).thenReturn(wallets);

        when(coinTypeDataService.getById(1)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(0)));
        when(coinTypeDataService.getById(2)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(1)));
        when(coinTypeDataService.getById(3)).thenReturn(Optional.ofNullable(getAllCoinTypes().get(2)));

        User actualUser = userService.getByEmail(email);

        verify(userDataService).getByEmail(email);
        verify(userWalletDataService).getByUserId(user.get().getId());
        verify(walletDataService).getByIds(walletIds);

        verify(coinTypeDataService, times(3))
                .getById(argumentCaptorCoinTypeId.capture());
        assertThat(argumentCaptorCoinTypeId.getAllValues()).isEqualTo(getAllCoinTypes().stream().map(CoinType::getId).collect(toList()));

        User expectedUser = User.builder()
                .id(1L)
                .name("Juan")
                .surname("Perez")
                .alias("juan-perez")
                .email("juan.perez@gmail.com")
                .wallets(wallets)
                .build();

        assertThat(actualUser).isEqualTo(expectedUser);
    }
}