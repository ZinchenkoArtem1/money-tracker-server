package com.zinchenko.wallet;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

//    @Mock
//    private WalletRepository walletRepository;
//    @Mock
//    private MonobankService monobankService;
//    @Mock
//    private WalletConvertor walletConvertor;
//    @Mock
//    private UserService userService;
//    @Mock
//    private CurrencyService currencyService;
//    @Mock
//    private SecurityUserService securityUserService;
//
//    private WalletService walletService;

//    @BeforeEach
//    void setUp() {
//        this.walletService = new WalletService(walletRepository, monobankService, walletConvertor, userService, currencyService);
//    }
//
//    @Test
//    void getAllUserWalletsDtoTest() {
//        String email = UUID.randomUUID().toString();
//        Wallet wallet1 = mock(Wallet.class);
//        Wallet wallet2 = mock(Wallet.class);
//        WalletDto walletDto1 = mock(WalletDto.class);
//        WalletDto walletDto2 = mock(WalletDto.class);
//        UserDetails userDetails = mock(UserDetails.class);
//
//        when(userDetails.getUsername()).thenReturn(email);
//        when(securityUserService.getActiveUser()).thenReturn(userDetails);
//        when(walletRepository.findByUserEmail(email)).thenReturn(List.of(wallet1, wallet2));
//        when(walletConvertor.toWalletDto(wallet1)).thenReturn(walletDto1);
//        when(walletConvertor.toWalletDto(wallet2)).thenReturn(walletDto2);
//
//        List<WalletDto> wallets = walletService.getAllUserWalletsDto();
//
//        assertIterableEquals(List.of(walletDto1, walletDto2), wallets);
//    }
//
//    @Test
//    void getAllUserWalletsTest() {
//        String email = UUID.randomUUID().toString();
//        Wallet wallet1 = mock(Wallet.class);
//        Wallet wallet2 = mock(Wallet.class);
//        UserDetails userDetails = mock(UserDetails.class);
//
//        when(userDetails.getUsername()).thenReturn(email);
//        when(securityUserService.getActiveUser()).thenReturn(userDetails);
//        when(walletRepository.findByUserEmail(email)).thenReturn(List.of(wallet1, wallet2));
//
//        List<Wallet> wallets = walletService.getAllUserWallets();
//
//        assertIterableEquals(List.of(wallet1, wallet2), wallets);
//    }
//
//    @Test
//    void getWalletDtoExistTest() {
//        Integer id = RandomUtils.nextInt();
//        Wallet wallet = mock(Wallet.class);
//        WalletDto walletDto = mock(WalletDto.class);
//
//        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));
//        when(walletConvertor.toWalletDto(wallet)).thenReturn(walletDto);
//
//        assertEquals(walletDto, walletService.getWalletDto(id));
//    }
//
//    @Test
//    void getWalletDtoNotExistTest() {
//        Integer id = RandomUtils.nextInt();
//
//        when(walletRepository.findById(id)).thenReturn(Optional.empty());
//
//        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> walletService.getWalletDto(id));
//        assertEquals("Wallet with id [%s] not exist".formatted(id), exc.getMessage());
//    }
//
//    @Test
//    void updateExistWalletTest() {
//        Integer id = RandomUtils.nextInt();
//        String newName = UUID.randomUUID().toString();
//        Wallet wallet = mock(Wallet.class);
//
//        when(walletRepository.findById(id)).thenReturn(Optional.of(wallet));
//
//        walletService.update(new UpdateWalletRequest()
//                .setId(id)
//                .setName(newName));
//
//        verify(wallet).setName(newName);
//        verify(walletRepository).save(wallet);
//    }
//
//    @Test
//    void updateNotExistWalletTest() {
//        Integer id = RandomUtils.nextInt();
//        UpdateWalletRequest request = new UpdateWalletRequest().setId(id);
//
//        when(walletRepository.findById(id)).thenReturn(Optional.empty());
//        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> walletService.update(request));
//        assertEquals("Wallet with id [%s] not exist".formatted(id), exc.getMessage());
//    }
//
//    @Test
//    void deleteSuccessTest() {
//        Integer id = RandomUtils.nextInt();
//        when(walletRepository.existsById(id)).thenReturn(true);
//
//        walletService.deleteById(id);
//
//        verify(walletRepository).deleteById(id);
//    }
//
//    @Test
//    void deleteFailedTest() {
//        Integer id = RandomUtils.nextInt();
//        when(walletRepository.existsById(id)).thenReturn(false);
//
//        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> walletService.deleteById(id));
//        assertEquals("Wallet with id [%s] not found".formatted(id), exc.getMessage());
//        verifyNoMoreInteractions(walletRepository);
//    }
//
//    @Test
//    void createManualWalletTest() {
//        String name = UUID.randomUUID().toString();
//        Double balance = RandomUtils.nextDouble();
//        String email = UUID.randomUUID().toString();
//        Integer currencyId = RandomUtils.nextInt();
//        Currency currency = mock(Currency.class);
//        CreateWalletRequest request = new CreateWalletRequest()
//                .setCurrencyId(currencyId)
//                .setWalletType(WalletType.MANUAL)
//                .setActualBalanceInUnits(balance)
//                .setName(name);
//        User user = mock(User.class);
//        UserDetails userDetails = mock(UserDetails.class);
//        Wallet wallet = mock(Wallet.class);
//
//        when(userDetails.getUsername()).thenReturn(email);
//        when(securityUserService.getActiveUser()).thenReturn(userDetails);
//        when(userService.getUserByEmail(email)).thenReturn(user);
//        when(currencyService.getCurrency(currencyId)).thenReturn(currency);
//        when(walletConvertor.toManualWallet(name, currency, user, balance)).thenReturn(wallet);
//
//        walletService.create(request);
//
//        verify(walletRepository).save(wallet);
//    }
}
