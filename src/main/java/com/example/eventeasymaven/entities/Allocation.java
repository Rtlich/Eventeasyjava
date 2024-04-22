package com.example.eventeasymaven.entities;

import java.time.LocalDate;

public class Allocation {
    private int id;
    private String nom;
    private float prix;
    private LocalDate date;
    private int quantity;

    // Relations
    private CategoryAllocation categoryAllocation;
    private Event event;

    private String image;

    public Allocation(int id, String nom, float prix, LocalDate date, int quantity, CategoryAllocation categoryAllocation, Event event, String image) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.date = date;
        this.quantity = quantity;
        this.categoryAllocation = categoryAllocation;
        this.event = event;
        this.image = image;
    }

    public Allocation(String nom, float prix, LocalDate date, int quantity, CategoryAllocation categoryAllocation, Event event, String image) {
        this.nom = nom;
        this.prix = prix;
        this.date = date;
        this.quantity = quantity;
        this.categoryAllocation = categoryAllocation;
        this.event = event;
        this.image = image;
    }

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

    @Override
    public String toString() {
        return "allocation{" +
                "id=" + id +
                ", category_id=" + categoryAllocation.toString() +
                ", event_id=" + event.toString() +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", date=" + date +
                ", quantity=" + quantity +
                ", image='" + image + '\'' +
                '}';
    }
}
