package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by aitor on 29/09/15.
 */
@Root(name = "response")
public abstract class CatApiBaseResponseModel<TYPE> {

    @Element(name="data")
    private TYPE data;

    public TYPE getData() {
        return data;
    }

    public void setData(TYPE data) {
        this.data = data;
    }

}
