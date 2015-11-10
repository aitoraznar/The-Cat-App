package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.Element;

/**
 * Created by aitor on 29/09/15.
 */
public class FavouriteListResponseModel {

    @Element(name="data")
    private DataFavourites data;

    public DataFavourites getData() {
        return data;
    }

    public void setData(DataFavourites data) {
        this.data = data;
    }

}
