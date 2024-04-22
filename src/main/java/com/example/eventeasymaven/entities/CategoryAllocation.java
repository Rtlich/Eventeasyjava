package com.example.eventeasymaven.entities;

public class CategoryAllocation implements Comparable<CategoryAllocation> {
    private int id;
    private String nom;

    public CategoryAllocation(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public CategoryAllocation(int id) {
        this.id = id;
    }

    public CategoryAllocation(String nom) {
        this.nom = nom;
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

    @Override
    public int compareTo(CategoryAllocation categoryAllocation) {
        if (compareVar.equals("Nom")) {
            return categoryAllocation.getNom().compareTo(this.getNom());
        }
        return 0;
    }

    public static String compareVar = "";

    @Override
    public String toString() {
        return nom;
    }
}
