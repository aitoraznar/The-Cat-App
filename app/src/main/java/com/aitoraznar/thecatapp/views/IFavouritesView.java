package com.aitoraznar.thecatapp.views;

import com.aitoraznar.thecatapp.models.FavouriteModel;
import com.aitoraznar.thecatapp.models.catapi.CatModel;

import java.util.List;


/**
 * @author aitor aznar
 * @since 29/09/15
 */
public interface IFavouritesView extends IMainView {

    void setFavourites(List<FavouriteModel> favourites);

    void addFavourite(CatModel cat);

    void removeFavourite(CatModel cat);

}