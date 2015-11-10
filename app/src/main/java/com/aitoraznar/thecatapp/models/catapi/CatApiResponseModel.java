package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.Element;

/**
 * @author aitor aznar
 * @since 27/09/15
 */
public class CatApiResponseModel {

    @Element(name="data")
    private DataImages data;

    public DataImages getData() {
        return data;
    }

    public void setData(DataImages data) {
        this.data = data;
    }

}