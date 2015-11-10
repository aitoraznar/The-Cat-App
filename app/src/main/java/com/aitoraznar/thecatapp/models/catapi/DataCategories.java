package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by aitor on 28/09/15.
 */
public class DataCategories {

    @ElementList(name="categories")
    private List<CategoryModel> categories;

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }
}
