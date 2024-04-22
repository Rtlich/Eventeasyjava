package com.example.eventeasy.entities;


import java.time.LocalDate;

public class Event {

    private int id;
    private String title;
    private String email;
    private int phone;
    private LocalDate date;

    private CategoryE category;


    public Event() {
    }

    public Event(int id, String title) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CategoryE getCategory() {
        return category;
    }

    public void setCategory(CategoryE category) {
        this.category = category;
    }



    public String toString() {
        return title;
    }
}