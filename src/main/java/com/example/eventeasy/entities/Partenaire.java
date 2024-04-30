package com.example.eventeasy.entities;


import com.example.eventeasy.utils.Constants;

public class Partenaire implements Comparable<Partenaire> {

    private int id;
    private String nom;
    private int tel;
    private float don;
    private String image;


    public Partenaire() {
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

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public float getDon() {
        return don;
    }

    public void setDon(float don) {
        this.don = don;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String toString() {
        return nom;
    }

    @Override
    public int compareTo(Partenaire partenaire) {
        return switch (Constants.compareVar) {
            case "Nom" -> this.getNom().compareTo(partenaire.getNom());
            case "Don" -> Float.compare(this.getDon(), partenaire.getDon());
            default -> 0;
        };
    }
}