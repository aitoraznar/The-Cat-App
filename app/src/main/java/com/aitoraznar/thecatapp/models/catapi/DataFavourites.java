package com.aitoraznar.thecatapp.models.catapi;

import com.aitoraznar.thecatapp.models.FavouriteModel;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by aitor on 29/09/15.
 */
public class DataFavourites {

    @ElementList(name="images")
    private List<FavouriteModel> favourites;

    public List<FavouriteModel> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<FavouriteModel> favourites) {
        this.favourites = favourites;
    }


}
