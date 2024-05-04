package com.example.eventeasy.controllers.front.lieu;

import com.calendarfx.view.CalendarView;
import com.example.eventeasy.entities.BookingL;
import com.example.eventeasy.services.BookingLService;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import com.calendarfx.model.Entry;
import com.calendarfx.model.CalendarSource;


import java.util.List;

public class Calendar {

    @FXML
    private Pane root;

    @FXML
    private void initialize() {
        CalendarView calendarView = new CalendarView();
        root.getChildren().add(calendarView);

        // Créer une source de calendrier
        CalendarSource calendarSource = new CalendarSource("Réservations");

        // Récupérer toutes les réservations
        List<BookingL> reservations = BookingLService.getInstance().getAll();

        // Créer un calendrier
        com.calendarfx.model.Calendar calendar = new com.calendarfx.model.Calendar("Réservations");

        // Ajouter le calendrier à la source de calendrier
        calendarSource.getCalendars().add(calendar);

        // Ajouter la source de calendrier à la vue du calendrier
        calendarView.getCalendarSources().add(calendarSource);

        // Ajouter les réservations en tant qu'entrées au calendrier
        for (BookingL reservation : reservations) {
            Entry<BookingL> entry = new Entry<>(reservation.getNomLieu());
            entry.changeStartDate(reservation.getDateD());
            entry.changeEndDate(reservation.getDateF().plusDays(1));
            entry.setLocation(reservation.getNomLieu());
            calendar.addEntry(entry);
        }
    }
}
