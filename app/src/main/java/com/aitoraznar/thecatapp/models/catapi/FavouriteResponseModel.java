package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.Element;

/**
 * Created by aitor on 29/09/15.
 */
public class FavouriteResponseModel {

    @Element(name="data", required = false)
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Element(name="apierror", required = false)
    private CatApiError error;

    public CatApiError getError() {
        return error;
    }

    public void setError(CatApiError error) {
        this.error = error;
    }
}
