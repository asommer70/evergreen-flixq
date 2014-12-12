package com.thehoick.evergreenflixq;

/**
 * Created by adam on 12/12/14.
 * Stores status of DVD at each Library that is returned from Evergreen ILS.
 */
public class Library {
    private String name;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
