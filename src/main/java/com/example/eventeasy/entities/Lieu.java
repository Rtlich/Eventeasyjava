package com.example.eventeasy.entities;


import java.time.LocalDate;

public class Lieu {

    private int id;
    private String nom;
    private float prix;
    private String image;
    private LocalDate dateD;
    private LocalDate dateF;
    private int capacity;
    private String region;

    private double latitude;
    private double longitude;
    private int reservationCount;


    private CategoryL category;


    public Lieu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getDateD() {
        return dateD;
    }

    public void setDateD(LocalDate dateD) {
        this.dateD = dateD;
    }

    public LocalDate getDateF() {
        return dateF;
    }

    public void setDateF(LocalDate dateF) {
        this.dateF = dateF;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public CategoryL getCategory() {
        return category;
    }

    public void setCategory(CategoryL category) {
        this.category = category;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }


    public String toString() {
        return nom;
    }


}