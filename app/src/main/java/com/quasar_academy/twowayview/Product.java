package com.quasar_academy.twowayview;

import java.io.Serializable;

/**
 * Created by Prashant on 11/22/2014.
 */
public class Product implements Serializable {
    String featured_src;
    String title;
    String description;
    int id;

    public Product() {
    }

    public Product(String featured_src, String title, String description, int id) {
        this.featured_src = featured_src;
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public String getFeatured_src() {
        return featured_src;
    }

    public void setFeatured_src(String featured_src) {
        this.featured_src = featured_src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
