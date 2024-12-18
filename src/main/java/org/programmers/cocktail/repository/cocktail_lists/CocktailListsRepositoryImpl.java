package org.programmers.cocktail.repository.cocktail_lists;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.programmers.cocktail.entity.CocktailLists;
import org.programmers.cocktail.search.dto.CocktailListsTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class CocktailListsRepositoryImpl {

    private ModelMapper modelMapper;

    @Autowired
    public CocktailListsRepositoryImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        modelMapper.addMappings(new PropertyMap<CocktailLists, CocktailListsTO>() {
            @Override
            protected void configure() {
                map(source.getUsers().getId(), destination.getUserId());
                map(source.getCocktails().getId(), destination.getCocktailId());
            }
        });
    }

    public CocktailListsTO convertToCocktailsListsTO(CocktailLists cocktailLists) {
        return modelMapper.map(cocktailLists, CocktailListsTO.class);
    }

}

