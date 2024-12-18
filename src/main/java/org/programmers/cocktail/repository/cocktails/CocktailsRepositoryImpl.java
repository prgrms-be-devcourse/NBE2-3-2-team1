package org.programmers.cocktail.repository.cocktails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.programmers.cocktail.entity.Cocktails;
import org.programmers.cocktail.search.dto.CocktailsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CocktailsRepositoryImpl {

    @Autowired
    private ModelMapper modelMapper;

    public List<CocktailsTO> convertToCocktailsTOList(List<Cocktails> cocktailsList) {
        List<CocktailsTO> cocktailsTOList = cocktailsList.stream()
            .map(cocktails -> modelMapper.map(cocktails, CocktailsTO.class))
            .collect(Collectors.toList());
        return cocktailsTOList;
    }

    public Cocktails convertToCocktails(CocktailsTO cocktailsTO) {
        return modelMapper.map(cocktailsTO, Cocktails.class);
    }
}
