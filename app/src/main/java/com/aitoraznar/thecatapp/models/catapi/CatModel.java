package com.aitoraznar.thecatapp.models.catapi;

import org.simpleframework.xml.Element;

/**
 * @author aitor aznar
 * @since 27/09/15
 */
@Element(name="image")
public class CatModel {

    @Element(name = "url")
    private String url;

    @Element(name = "id")
    private String id;

    @Element(name = "source_url")
    private String source_url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public CatModel() {
        // Empty
    }

    public CatModel(String id, String url) {
        this.setId(id);
        this.setUrl(url);
    }

}