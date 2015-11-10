package com.aitoraznar.thecatapp.views;

import com.aitoraznar.thecatapp.models.catapi.CatModel;


/**
 * @author aitor aznar
 * @since 28/09/15
 */
public interface IHomeView extends IMainView {

    void setCat(CatModel cat);

    void addFavourite(CatModel cat);

    void removeFavourite(CatModel cat);

}