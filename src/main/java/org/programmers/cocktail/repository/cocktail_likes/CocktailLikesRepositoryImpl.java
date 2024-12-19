package org.programmers.cocktail.repository.cocktail_likes;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.programmers.cocktail.entity.CocktailLikes;
import org.programmers.cocktail.search.dto.CocktailLikesTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CocktailLikesRepositoryImpl {

    private ModelMapper modelMapper;

    @Autowired
    public CocktailLikesRepositoryImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // CocktailLikes -> CocktailLikesTO 변환 매핑
        modelMapper.addMappings(new PropertyMap<CocktailLikes, CocktailLikesTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUsers().getId());
                map().setCocktailId(source.getCocktails().getId());
            }
        });

        // CocktailLikesTO -> CocktailLikes 변환 매핑
        modelMapper.addMappings(new PropertyMap<CocktailLikesTO, CocktailLikes>() {
            @Override
            protected void configure() {
                map().getUsers().setId(source.getUserId());
                map().getCocktails().setId(source.getCocktailId());
            }
        });
    }

    public CocktailLikesTO convertToCocktailsLikesTO(CocktailLikes cocktailLikes) {
        return modelMapper.map(cocktailLikes, CocktailLikesTO.class);
    }

    public CocktailLikes convertToCocktailLikes(CocktailLikesTO cocktailLikesTO){
        return modelMapper.map(cocktailLikesTO, CocktailLikes.class);
    }

}
