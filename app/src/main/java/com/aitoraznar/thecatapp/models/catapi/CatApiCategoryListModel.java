package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.Element;

/**
 * Created by aitor on 28/9/15.
 */
public class CatApiCategoryListModel {

    @Element(name="data")
    private DataCategories data;

    public DataCategories getData() {
        return data;
    }

    public void setData(DataCategories data) {
        this.data = data;
    }

}
