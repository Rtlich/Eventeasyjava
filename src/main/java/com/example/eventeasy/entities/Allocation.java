package com.example.eventeasy.entities;

import java.time.LocalDate;

public class Allocation {
    private int id;
    private String nom;
    private float prix;
    private LocalDate date;
    private int quantity;
    private Boolean upToRent;

    // Relations
    private CategoryAllocation categoryAllocation;
    private Event event;

    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoryAllocation getCategoryAllocation() {
        return categoryAllocation;
    }

    public void setCategoryAllocation(CategoryAllocation categoryAllocation) {
        this.categoryAllocation = categoryAllocation;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean isUpToRent() {
        return upToRent;
    }

    public void setUpToRent(Boolean upToRent) {
        this.upToRent = upToRent;
    }

    @Override
    public String toString() {
        return "Allocation : " +
                "\n nom : " + nom +
                "\n prix : " + prix +
                "\n date : " + date +
                "\n quantity : " + quantity +
                "\n categoryAllocation : " + categoryAllocation +
                "\n event : " + event;
    }
}
