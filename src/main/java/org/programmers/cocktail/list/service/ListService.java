package org.programmers.cocktail.list.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.list.dto.ListCocktail;
import org.programmers.cocktail.repository.cocktails.CocktailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListService {
    private final CocktailsRepository cocktailsRepository;

    @Autowired
    public ListService(CocktailsRepository cocktailsRepository) {
        this.cocktailsRepository = cocktailsRepository;
    }

    public List<ListCocktail> showList() {
        List<ListCocktail> result = new ArrayList<>();
        List<Cocktails> cocktails = cocktailsRepository.findAllByOrderByLikesDesc();

        for (Cocktails cocktail : cocktails) {
            ListCocktail listCocktail = new ListCocktail();

            listCocktail.setId(cocktail.getId());
            listCocktail.setName(cocktail.getName());
            listCocktail.setImage_url(cocktail.getImage_url());
            listCocktail.setLikes(cocktail.getLikes());

            result.add(listCocktail);
        }

        return result;

    }

    // ID로 칵테일 가져오기
    public ListCocktail getCocktailById(Long id) {
        Optional<Cocktails> optionalCocktail = cocktailsRepository.findById(id);

        if (optionalCocktail.isPresent()) {
            Cocktails cocktail = optionalCocktail.get();
            ListCocktail listCocktail = new ListCocktail();

            listCocktail.setName(cocktail.getName());
            listCocktail.setImage_url(cocktail.getImage_url());
            listCocktail.setLikes(cocktail.getLikes());

            // 추가적인 칵테일의 상세 정보가 필요하다면 여기서 추가 설정할 수 있습니다.
            // 예: listCocktail.setDescription(cocktail.getDescription());

            return listCocktail;
        } else {
            // 칵테일이 없으면 예외를 던질 수 있습니다.
            throw new RuntimeException("칵테일을 찾을 수 없습니다. ID: " + id);
        }
    }
}
