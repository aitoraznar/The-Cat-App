package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.Element;

/**
 * Created by aitor on 28/9/15.
 */
@Element(name="category")
public class CategoryModel {

    @Element(name = "id")
    private Integer id;

    @Element(name = "name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryModel() {
        // Empty
    }

    public CategoryModel(String name) {
        this.name = name;
    }
}
