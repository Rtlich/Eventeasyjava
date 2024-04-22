package com.example.eventeasymaven.entities;


public class Event {

    private int id;
    private String title;

    public Event(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Event(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}