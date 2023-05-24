package com.zinchenko.statistic;


import com.zinchenko.statistic.dto.GetStatisticRequest;
import com.zinchenko.statistic.dto.GetStatisticResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistic")
public class StatisticRestControllerV1 {

    private final StatisticService statisticService;

    public StatisticRestControllerV1(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping("/wallet")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<GetStatisticResponse> findAllByWallet(@RequestBody GetStatisticRequest getStatisticRequest) {
        return ResponseEntity.ok(statisticService.getStatisticByWallet(getStatisticRequest));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<GetStatisticResponse> getAll(@RequestBody GetStatisticRequest getStatisticRequest) {
        return ResponseEntity.ok(statisticService.getStatisticForAllWallets(getStatisticRequest));
    }
}
