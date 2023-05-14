package com.zinchenko.wallet;

import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.monobank.MonobankService;
import com.zinchenko.monobank.integration.MonobankClient;
import com.zinchenko.monobank.integration.dto.StatementResponse;
import com.zinchenko.security.SecurityUserService;
import com.zinchenko.user.UserService;
import com.zinchenko.user.model.User;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletRepository;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.CreateWalletRequest;
import com.zinchenko.wallet.dto.UpdateWalletRequest;
import com.zinchenko.wallet.dto.WalletDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class WalletService {

    private final WalletRepository walletRepository;
    private final MonobankService monobankService;

    private final WalletConvertor walletConvertor;

    private final UserService userService;
    private final CurrencyService currencyService;
    private final SecurityUserService securityUserService;

    private final MonobankClient monobankClient;


    public WalletService(WalletRepository walletRepository, MonobankService monobankService,
                         WalletConvertor walletConvertor,
                         UserService userService, CurrencyService currencyService,
                         SecurityUserService securityUserService,
                         MonobankClient monobankClient) {
        this.walletRepository = walletRepository;
        this.monobankService = monobankService;
        this.walletConvertor = walletConvertor;
        this.userService = userService;
        this.currencyService = currencyService;
        this.securityUserService = securityUserService;
        this.monobankClient = monobankClient;
    }

    public List<WalletDto> getAllUserWallets() {
        String email = securityUserService.getActiveUser().getUsername();
        return walletRepository.findByUserEmail(email).stream()
                .map(walletConvertor::toWalletDto)
                .toList();
    }

    public WalletDto getWalletDto(Integer id) {
        Wallet wallet = getWallet(id);
        return walletConvertor.toWalletDto(wallet);
    }

    private Wallet getWallet(Integer id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Wallet with id [%s] not exist".formatted(id)));
    }

    public void create(CreateWalletRequest request) {
        if (request.getWalletType() == WalletType.MANUAL) {
            createManualWallet(request);
        } else if (request.getWalletType() == WalletType.MONOBANK) {
            createMonobankWallet(request);
        } else {
            throw new IllegalStateException("Unexpected wallet type [%s]".formatted(request.getWalletType()));
        }
    }

    private void createManualWallet(CreateWalletRequest request) {
        Currency currency = currencyService.getCurrency(request.getCurrencyId());
        User user = userService.getUserByEmail(securityUserService.getActiveUser().getUsername());

        walletRepository.save(
                walletConvertor.toWallet(request.getName(), currency, user, request.getActualBalanceInUnits())
        );
    }

    private void createMonobankWallet(CreateWalletRequest request) {
        Instant to = Instant.now();
        Instant from = to.minus(request.getSyncPeriod().getCount(), request.getSyncPeriod().getUnit());

        List<StatementResponse> statements = monobankClient.getStatements(
                request.getToken(),
                request.getAccountId(),
                from.getEpochSecond(),
                to.getEpochSecond()
        );
        User user = userService.getUserByEmail(securityUserService.getActiveUser().getUsername());
        Currency currency = currencyService.getCurrencyByCode(statements.get(0).getCurrencyCode());

        Wallet wallet = walletRepository.save(
                walletConvertor.toWallet(request.getName(), currency, user, statements.get(0).getBalance())
        );

        monobankService.create(wallet, to, request.getToken(), request.getAccountId(), statements);
    }

    public void update(UpdateWalletRequest request) {
        checkExist(request.getId());
        Wallet wallet = getWallet(request.getId())
                .setName(request.getName());
        walletRepository.save(wallet);
    }

    public void deleteById(Integer id) {
        checkExist(id);
        walletRepository.deleteById(id);
    }

    private void checkExist(Integer id) {
        if (!walletRepository.existsById(id)) {
            throw new IllegalStateException("Wallet with id [%s] not found".formatted(id));
        }
    }
}
