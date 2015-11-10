package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by aitor on 28/09/15.
 */
public class ImageList {

    @ElementList
    List<CatModel> cats;

    public List<CatModel> getCats() {
        return cats;
    }

    public void setCats(List<CatModel> cats) {
        this.cats = cats;
    }

}
