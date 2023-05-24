package com.zinchenko.wallet;


import com.zinchenko.admin.currency.domain.Currency;
import com.zinchenko.user.UserService;
import com.zinchenko.user.domain.User;
import com.zinchenko.wallet.domain.Wallet;
import com.zinchenko.wallet.domain.WalletRepository;
import com.zinchenko.wallet.domain.WalletType;
import com.zinchenko.wallet.dto.WalletDto;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletConvertor walletConvertor;
    @Mock
    private UserService userService;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        this.walletService = new WalletService(walletRepository, walletConvertor, userService);
    }

    @Test
    void getAllUserWalletsDtoTest() {
        String email = UUID.randomUUID().toString();
        Wallet wallet1 = mock(Wallet.class);
        Wallet wallet2 = mock(Wallet.class);
        WalletDto walletDto1 = mock(WalletDto.class);
        WalletDto walletDto2 = mock(WalletDto.class);
        User user = mock(User.class);

        when(user.getEmail()).thenReturn(email);
        when(userService.getActiveUser()).thenReturn(user);
        when(walletRepository.findByUserEmail(email)).thenReturn(List.of(wallet1, wallet2));
        when(walletConvertor.toWalletDto(wallet1)).thenReturn(walletDto1);
        when(walletConvertor.toWalletDto(wallet2)).thenReturn(walletDto2);

        List<WalletDto> wallets = walletService.getAllUserWalletsDto();

        assertIterableEquals(List.of(walletDto1, walletDto2), wallets);
    }

    @Test
    void getWalletDtoExistTest() {
        Integer id = RandomUtils.nextInt();
        Wallet wallet = mock(Wallet.class);
        WalletDto walletDto = mock(WalletDto.class);

        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));
        when(walletConvertor.toWalletDto(wallet)).thenReturn(walletDto);

        assertEquals(walletDto, walletService.getWalletDto(id));
    }

    @Test
    void getWalletDtoNotExistTest() {
        Integer id = RandomUtils.nextInt();

        when(walletRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> walletService.getWalletDto(id));
        assertEquals("Wallet with id [%s] not exist".formatted(id), exc.getMessage());
    }

    @Test
    void saveWithBalanceInCentsTest() {
        String name = UUID.randomUUID().toString();
        Currency currency = mock(Currency.class);
        User user = mock(User.class);
        Long actualBalanceInCents = RandomUtils.nextLong();
        Wallet wallet = mock(Wallet.class);

        when(userService.getActiveUser()).thenReturn(user);
        when(walletConvertor.toWallet(name, currency, user, actualBalanceInCents, WalletType.MANUAL)).thenReturn(wallet);

        walletService.save(name, currency, actualBalanceInCents, WalletType.MANUAL);

        verify(walletRepository).save(wallet);
    }

    @Test
    void saveWithBalanceInUnitsTest() {
        String name = UUID.randomUUID().toString();
        Currency currency = mock(Currency.class);
        User user = mock(User.class);
        Double actualBalanceInUnits = RandomUtils.nextDouble();
        Wallet wallet = mock(Wallet.class);

        when(userService.getActiveUser()).thenReturn(user);
        when(walletConvertor.toWallet(name, currency, user, actualBalanceInUnits, WalletType.MANUAL)).thenReturn(wallet);

        walletService.save(name, currency, actualBalanceInUnits, WalletType.MANUAL);

        verify(walletRepository).save(wallet);
    }

    @Test
    void deleteByIdSuccessTest() {
        Integer id = RandomUtils.nextInt();

        when(walletRepository.existsById(id)).thenReturn(true);

        walletService.deleteById(id);

        verify(walletRepository).deleteById(id);
    }

    @Test
    void deleteByIdFailedTest() {
        Integer id = RandomUtils.nextInt();

        when(walletRepository.existsById(id)).thenReturn(false);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> walletService.deleteById(id));
        assertEquals("Wallet with id [%s] not found".formatted(id), exc.getMessage());
        verifyNoMoreInteractions(walletRepository);
    }

    @Test
    void updateBalance() {
        Integer id = RandomUtils.nextInt();
        Long newBalance = RandomUtils.nextLong();
        Wallet wallet = mock(Wallet.class);

        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));

        walletService.updateBalance(id, newBalance);

        verify(wallet).setActualBalanceInCents(newBalance);
        verify(walletRepository).save(wallet);
    }

    @Test
    void getWalletExistTest() {
        Integer id = RandomUtils.nextInt();
        Wallet wallet = mock(Wallet.class);

        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));

        assertEquals(wallet, walletService.getWallet(id));
    }

    @Test
    void getWalletNotExistTest() {
        Integer id = RandomUtils.nextInt();

        when(walletRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> walletService.getWallet(id));
        assertEquals("Wallet with id [%s] not exist".formatted(id), exc.getMessage());
    }
}
