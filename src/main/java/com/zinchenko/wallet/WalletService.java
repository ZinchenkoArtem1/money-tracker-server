package com.zinchenko.wallet;

import com.zinchenko.admin.currency.Currency;
import com.zinchenko.admin.currency.CurrencyService;
import com.zinchenko.security.SecurityUserService;
import com.zinchenko.user.UserService;
import com.zinchenko.user.model.User;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletRepository;
import com.zinchenko.wallet.dto.WalletDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletConvertor walletConvertor;
    private final UserService userService;
    private final CurrencyService currencyService;
    private final SecurityUserService securityUserService;

    public WalletService(WalletRepository walletRepository, WalletConvertor walletConvertor,
                         UserService userService, CurrencyService currencyService,
                         SecurityUserService securityUserService) {
        this.walletRepository = walletRepository;
        this.walletConvertor = walletConvertor;
        this.userService = userService;
        this.currencyService = currencyService;
        this.securityUserService = securityUserService;
    }

    public List<WalletDto> findAllByUser() {
        return walletRepository.findByUserEmail(securityUserService.getActiveUser().getUsername()).stream()
                .map(walletConvertor::toDto)
                .toList();
    }

    public WalletDto getWalletDto(Integer id) {
        Wallet wallet = getWallet(id);
        return walletConvertor.toDto(wallet);
    }

    public Wallet getWallet(Integer id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Wallet with id [%s] not exist".formatted(id)));
    }

    public void create(WalletDto walletDto) {
        if (walletDto.getId() != null) {
            throw new IllegalStateException("Request body must not contain id for the create wallet operation");
        } else {
            Currency currency = currencyService.getCurrency(walletDto.getCurrencyId());
            User user = userService.getUserByEmail(securityUserService.getActiveUser().getUsername());
            walletRepository.save(walletConvertor.fromDto(walletDto, currency, user));
        }
    }

    public void update(WalletDto walletDto) {
        checkExist(walletDto.getId());
        Wallet wallet = getWallet(walletDto.getId());
        wallet.setName(walletDto.getName());
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
