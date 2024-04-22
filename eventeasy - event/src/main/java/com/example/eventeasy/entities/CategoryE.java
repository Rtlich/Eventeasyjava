package com.example.eventeasy.entities;


public class CategoryE {

    private int id;
    private String type;


    public CategoryE() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String toString() {
        return type;
    }
}