package com.zinchenko.statistic;


import com.zinchenko.common.error.BasicErrorResponse;
import com.zinchenko.statistic.dto.GetStatisticRequest;
import com.zinchenko.statistic.dto.GetStatisticResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/statistic")
public class StatisticRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(StatisticRestControllerV1.class);
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicErrorResponse> handleException(Exception ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.internalServerError().body(
                new BasicErrorResponse("Internal server error")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BasicErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new BasicErrorResponse("Access Denied")
        );
    }
}
