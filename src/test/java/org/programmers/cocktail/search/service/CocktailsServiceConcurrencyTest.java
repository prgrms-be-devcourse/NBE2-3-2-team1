package org.programmers.cocktail.search.service;

import org.junit.jupiter.api.Test;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.programmers.cocktail.search.service.CocktailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CocktailsServiceConcurrencyTest {

    @Autowired
    private CocktailsRepository cocktailsRepository;

    @Autowired
    private CocktailsService cocktailsService;

    @Test
    void testPessimisticLockWithConcurrentUpdates() throws InterruptedException {
        // Given: 초기 데이터 설정
        Cocktails cocktail = new Cocktails();
        cocktail.setName("testcode");
        cocktail.setIngredients("testcode");
        cocktail.setRecipes("testcode");
        cocktail.setHits(0L);
        cocktail.setLikes(0L);
        cocktailsRepository.save(cocktail);

        Long cocktailId = cocktail.getId();
        System.out.println("cocktailID: " +cocktailId);
        int numberOfThreads = 100; // 동시 실행할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // When: 여러 스레드가 동시에 업데이트 요청
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    // CocktailsTO 객체 생성 및 setter로 ID 설정
                    CocktailsTO cocktailsTO = new CocktailsTO();
                    cocktailsTO.setId(cocktailId);

                    // Service 메서드 호출
                    cocktailsService.updateCocktailHits(cocktailsTO); // 동시성 작업
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드 작업 완료 대기
        executorService.shutdown();

        // Then: 최종 Hits 값이 정확한지 확인
        Cocktails updatedCocktail = cocktailsRepository.findById(cocktailId).orElseThrow();
        assertThat(updatedCocktail.getHits()).isEqualTo((long) numberOfThreads);
    }
}
