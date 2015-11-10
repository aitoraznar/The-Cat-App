package com.aitoraznar.thecatapp.views;

import com.aitoraznar.thecatapp.models.catapi.CategoryModel;

import java.util.List;


/**
 * @author aitor aznar
 * @since 28/09/15
 */
public interface ICategoryMenuView extends IMainView {

    void setCategories(List<CategoryModel> categories);

}