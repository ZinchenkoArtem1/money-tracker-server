package com.zinchenko.wallet;

import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.user.UserService;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletRepository;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.UpdateWalletRequest;
import com.zinchenko.wallet.dto.WalletDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletConvertor walletConvertor;
    private final UserService userService;

    public WalletService(WalletRepository walletRepository, WalletConvertor walletConvertor, UserService userService) {
        this.walletRepository = walletRepository;
        this.walletConvertor = walletConvertor;
        this.userService = userService;
    }

    public List<WalletDto> getAllUserWalletsDto() {
        return getAllUserWallets().stream()
                .map(walletConvertor::toWalletDto)
                .toList();
    }

    public List<Wallet> getAllUserWallets() {
        String email = userService.getActiveUser().getEmail();

        return walletRepository.findByUserEmail(email).stream()
                .toList();
    }


    public WalletDto getWalletDto(Integer id) {
        Wallet wallet = getWallet(id);
        return walletConvertor.toWalletDto(wallet);
    }

    public Wallet save(String name, Currency currency, Double actualBalanceInUnits, WalletType walletType) {
        return walletRepository.save(walletConvertor.toWallet(
                name, currency, userService.getActiveUser(), actualBalanceInUnits, walletType
        ));
    }

    public void updateName(UpdateWalletRequest request) {
        Wallet wallet = getWallet(request.getId());
        wallet.setName(request.getName());
        walletRepository.save(wallet);
    }

    public void updateBalance(Integer walletId, Long actualBalanceInCents) {
        Wallet wallet = getWallet(walletId);
        wallet.setActualBalanceInCents(actualBalanceInCents);
        walletRepository.save(wallet);
    }

    public void deleteById(Integer id) {
        checkExist(id);
        walletRepository.deleteById(id);
    }

    public Wallet getWallet(Integer id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Wallet with id [%s] not exist".formatted(id)));
    }

    private void checkExist(Integer id) {
        if (!walletRepository.existsById(id)) {
            throw new IllegalStateException("Wallet with id [%s] not found".formatted(id));
        }
    }
}
