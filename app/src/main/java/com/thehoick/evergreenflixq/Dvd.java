package com.thehoick.evergreenflixq;

import android.util.Log;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 12/8/14.
 * Useful to get attributes at different times.
 */
public class Dvd {
    private static String TAG = Dvd.class.getSimpleName();

    private String title;
    private String description;
    private String status;
    private String link;
    private String imgUrl;
    private String evergreenLink;
    private List<Library> libaries;
    private Boolean libraryGotten = false;

    public Boolean getLibraryGotten() {
        return libraryGotten;
    }

    public void setLibraryGotten(Boolean libraryGotten) {
        this.libraryGotten = libraryGotten;
    }

    public String getEvergreenLink() {
        return evergreenLink;
    }

    public void setEvergreenLink(String evergreenLink) {
        this.evergreenLink = evergreenLink;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Library> getLibaries() {
        return libaries;
    }

    public void setLibaries(List<Library> libaries) {
        this.libaries = libaries;
    }
}
