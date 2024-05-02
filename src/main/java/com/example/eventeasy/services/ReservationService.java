package com.example.eventeasy.services;

import com.example.eventeasy.entities.BookingL;

import java.time.LocalDate;

public class ReservationService {

    private static ReservationService instance;

    private ReservationService() {
        // Empêcher l'instanciation directe depuis l'extérieur de la classe
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public boolean reserverLieu(String lieuName, LocalDate dateDebut, LocalDate dateFin, float prix) {
        // Effectuer la logique de réservation ici
        // Par exemple, vous pouvez enregistrer la réservation dans une base de données ou dans un fichier

        // Pour l'exemple, nous allons simplement imprimer les informations de réservation
        System.out.println("Réservation effectuée pour le lieu : " + lieuName);
        System.out.println("Date de début : " + dateDebut);
        System.out.println("Date de fin : " + dateFin);
        System.out.println("Prix : " + prix);

        // Simuler une réservation réussie pour l'exemple
        return true;
    }

}
