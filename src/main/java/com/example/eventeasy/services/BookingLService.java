package com.example.eventeasy.services;

import com.example.eventeasy.entities.BookingL;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingLService {

    private static BookingLService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public BookingLService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static BookingLService getInstance() {
        if (instance == null) {
            instance = new BookingLService();
        }
        return instance;
    }

    public List<BookingL> getAll() {
        List<BookingL> listBookingL = new ArrayList<>();
        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM `booking_l`");


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BookingL bookingL = new BookingL();
                bookingL.setId(resultSet.getInt("id"));
                bookingL.setPrix(resultSet.getFloat("prix"));
                bookingL.setDateD(resultSet.getDate("date_d") != null ? resultSet.getDate("date_d").toLocalDate() : null);
                bookingL.setDateF(resultSet.getDate("date_f") != null ? resultSet.getDate("date_f").toLocalDate() : null);
                bookingL.setLieub_id(resultSet.getInt("lieub_id"));


                listBookingL.add(bookingL);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) bookingL : " + exception.getMessage());
        }
        return listBookingL;
    }


    public boolean add(BookingL bookingL) {


        String request = "INSERT INTO `booking_l`(`prix`, `lieub_id`, `date_d`, `date_f`) VALUES(?, ?, ?, ?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setFloat(1, bookingL.getPrix());
            preparedStatement.setInt(2, bookingL.getLieub_id());
            preparedStatement.setDate(3, Date.valueOf(bookingL.getDateD()));
            preparedStatement.setDate(4, Date.valueOf(bookingL.getDateF()));



            preparedStatement.executeUpdate();
            System.out.println("BookingL added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) bookingL : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(BookingL bookingL) {
        String request = "UPDATE `booking_l` SET `prix` = ?, `date_d` = ?, `date_f` = ?, `lieub_id` = ? WHERE `id` = ?";

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setFloat(1, bookingL.getPrix());
            preparedStatement.setDate(2, Date.valueOf(bookingL.getDateD()));
            preparedStatement.setDate(3, Date.valueOf(bookingL.getDateF()));
            preparedStatement.setInt(4, bookingL.getLieub_id()); // Setting lieub_id at index 4
            preparedStatement.setInt(5, bookingL.getId());

            preparedStatement.executeUpdate();
            System.out.println("BookingL edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) bookingL : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `booking_l` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("BookingL deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) bookingL : " + exception.getMessage());
        }
        return false;
    }
    public List<BookingL> getBookingsForLieu(int lieuId, LocalDate startDate, LocalDate endDate) {
        List<BookingL> bookings = new ArrayList<>();
        try {
            String request = "SELECT * FROM `booking_l` WHERE `lieub_id` = ? AND ((`date_d` BETWEEN ? AND ?) OR (`date_f` BETWEEN ? AND ?))";
            preparedStatement = connection.prepareStatement(request);
            preparedStatement.setInt(1, lieuId);
            preparedStatement.setDate(2, Date.valueOf(startDate));
            preparedStatement.setDate(3, Date.valueOf(endDate));
            preparedStatement.setDate(4, Date.valueOf(startDate));
            preparedStatement.setDate(5, Date.valueOf(endDate));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BookingL bookingL = new BookingL();
                bookingL.setId(resultSet.getInt("id"));
                bookingL.setPrix(resultSet.getFloat("prix"));
                bookingL.setDateD(resultSet.getDate("date_d").toLocalDate());
                bookingL.setDateF(resultSet.getDate("date_f").toLocalDate());
                bookingL.setLieub_id(resultSet.getInt("lieub_id"));
                bookings.add(bookingL);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getBookingsForLieu) bookingL : " + exception.getMessage());
        }
        return bookings;
    }
    public Map<String, Integer> getBookingsCountByLieu() {
        Map<String, Integer> bookingsCountByLieu = new HashMap<>();
        try {
            String request = "SELECT l.nom, COUNT(b.id) AS nb_reservations " +
                    "FROM `booking_l` b " +
                    "JOIN `lieu` l ON b.lieub_id = l.id " +
                    "GROUP BY l.nom";
            preparedStatement = connection.prepareStatement(request);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nomLieu = resultSet.getString("nom");
                int nbReservations = resultSet.getInt("nb_reservations");
                bookingsCountByLieu.put(nomLieu, nbReservations);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getBookingsCountByLieu) bookingL : " + exception.getMessage());
        }
        return bookingsCountByLieu;
    }







}
