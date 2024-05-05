package com.example.eventeasy.entities;


import com.example.eventeasy.services.LieuService;

import java.time.LocalDate;

public class BookingL {

    private int id;
    private float prix;
    private LocalDate dateD;
    private LocalDate dateF;
    private int lieub_id;


    public int getLieub_id() {
        return lieub_id;
    }

    public void setLieub_id(int lieub_id) {
        this.lieub_id = lieub_id;
    }


    public BookingL() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
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

    public BookingL(int id, float prix, LocalDate dateD, LocalDate dateF, int lieub_id) {
        this.id = id;
        this.prix = prix;
        this.dateD = dateD;
        this.dateF = dateF;
        this.lieub_id = lieub_id;
    }

    public BookingL(float prix, LocalDate dateD, LocalDate dateF, int lieub_id) {
        this.prix = prix;
        this.dateD = dateD;
        this.dateF = dateF;
        this.lieub_id = lieub_id;
    }

    @Override
    public String toString() {
        return "BookingL{" +
                "id=" + id +
                ", prix=" + prix +
                ", dateD=" + dateD +
                ", dateF=" + dateF +
                ", lieub_id=" + lieub_id +
                '}';
    }

    public BookingL(int id) {
        this.id = id;
    }
    public String getNomLieu() {
        LieuService lieuService = new LieuService(); // Instancier le service pour accéder aux méthodes
        Lieu lieu = lieuService.getLieuById(lieub_id); // Récupérer le lieu par ID
        if (lieu != null) {
            return lieu.getNom(); // Retourner le nom du lieu
        } else {
            return "Lieu introuvable"; // Gérer le cas où le lieu n'est pas trouvé
        }
    }



}
