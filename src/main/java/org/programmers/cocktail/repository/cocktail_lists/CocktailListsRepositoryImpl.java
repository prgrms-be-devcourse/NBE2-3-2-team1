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

        // CocktailLists -> CocktailListsTO 변환 매핑
        modelMapper.addMappings(new PropertyMap<CocktailLists, CocktailListsTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUsers().getId());
                map().setCocktailId(source.getCocktails().getId());
            }
        });

        // CocktailListsTO -> CocktailLists 변환 매핑
        modelMapper.addMappings(new PropertyMap<CocktailListsTO, CocktailLists>() {
            @Override
            protected void configure() {
                map().getUsers().setId(source.getUserId());
                map().getCocktails().setId(source.getCocktailId());
            }
        });
    }

    public CocktailListsTO convertToCocktailsListsTO(CocktailLists cocktailLists) {
        return modelMapper.map(cocktailLists, CocktailListsTO.class);
    }

    public CocktailLists convertToCocktailLists(CocktailListsTO cocktailListsTO){
        return modelMapper.map(cocktailListsTO, CocktailLists.class);
    }

}

