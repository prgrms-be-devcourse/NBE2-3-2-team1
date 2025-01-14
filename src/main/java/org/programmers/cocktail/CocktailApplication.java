package org.programmers.cocktail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CocktailApplication {

    public static void main(String[] args) {
        SpringApplication.run(CocktailApplication.class, args);
    }

}
