package com.aitoraznar.thecatapp.models;

import org.simpleframework.xml.Element;

/**
 * Created by aitor on 29/09/15.
 */
@Element(name="image")
public class FavouriteModel {

    @Element(name = "id")
    private String id;

    @Element(name = "url")
    private String url;

    @Element(name = "sub_id")
    private String userId;

    @Element(name = "created")
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
