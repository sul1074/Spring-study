package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService stockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                stockService.decrease(id, quantity);

                // 정상 수행되었다면 반복문 빠져나오기
                break;
            } catch (Exception e) {
                // 실패하면 잠시 후에 다시 시도
                Thread.sleep(50);
            }
        }
    }
}
