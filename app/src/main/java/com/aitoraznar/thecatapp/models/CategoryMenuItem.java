package com.aitoraznar.thecatapp.models;

import com.aitoraznar.thecatapp.models.catapi.CategoryModel;

/**
 * Created by aitor on 28/09/15.
 */
public class CategoryMenuItem {
    private boolean showNotify;
    private CategoryModel category;

    public CategoryMenuItem() {

    }

    public CategoryMenuItem(boolean showNotify, CategoryModel category) {
        this.setShowNotify(showNotify);
        this.setCategory(category);
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }
}
