package com.example.eventeasymaven.services;

import com.example.eventeasymaven.entities.Allocation;
import com.example.eventeasymaven.entities.CategoryAllocation;
import com.example.eventeasymaven.entities.Event;
import com.example.eventeasymaven.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AllocationService implements IService<Allocation> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    private static AllocationService instance;

    public AllocationService() {
    }

    public static AllocationService getInstance() {
        if (instance == null) {
            instance = new AllocationService();
        }
        return instance;
    }

    public boolean ajouter(Allocation a) {
        String req = "INSERT INTO `allocation`(`category_id`,`event_id`, `nom`,`prix`,`date`, `quantity`,`image`) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement rentalsv = connection.prepareStatement(req);
            rentalsv.setInt(1, a.getCategoryAllocation().getId());
            rentalsv.setInt(2, a.getEvent().getId());
            rentalsv.setString(3, a.getNom());
            rentalsv.setFloat(4, a.getPrix());
            rentalsv.setDate(5, Date.valueOf(a.getDate()));
            rentalsv.setInt(6, a.getQuantity());
            rentalsv.setString(7, a.getImage());
            rentalsv.executeUpdate();
            System.out.println("allocation ajouté avec succès !");

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean supprimer(Allocation a) {
        String req = "DELETE FROM `allocation` WHERE `allocation`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, a.getId());
            pst.executeUpdate();
            System.out.println("allocation supprimer avec succes !");

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean modifier(Allocation a) {
        String req = "UPDATE `allocation` SET `category_id` = ?, `event_id` = ?, `nom` = ?, `prix` = ?, `date` = ?, `quantity` = ?, `image` = ? WHERE allocation.`id` = ?;";

        try {
            PreparedStatement rentalsv = connection.prepareStatement(req);

            rentalsv.setInt(1, a.getCategoryAllocation().getId());
            rentalsv.setInt(2, a.getEvent().getId());
            rentalsv.setString(3, a.getNom());
            rentalsv.setFloat(4, a.getPrix());
            rentalsv.setDate(5, Date.valueOf(a.getDate()));
            rentalsv.setInt(6, a.getQuantity());
            rentalsv.setString(7, a.getImage());
            rentalsv.setInt(8, a.getId());
            rentalsv.executeUpdate();
            System.out.println("allocation modifie avec succès !");

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public List<Allocation> afficher() {

        List<Allocation> allocations = new ArrayList<>();
        String req = "SELECT * FROM `allocation` AS a " +
                "RIGHT JOIN `category_a` AS c ON a.category_id = c.id " +
                "RIGHT JOIN `event` AS e ON a.event_id = e.id " +
                "WHERE a.category_id = c.id " +
                "AND a.event_id = e.id";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                allocations.add(new Allocation(
                        rs.getInt("a.id"),
                        rs.getString("a.nom"),
                        rs.getFloat("a.prix"),
                        LocalDate.parse(String.valueOf(rs.getDate("a.date"))),
                        rs.getInt("a.quantity"),
                        new CategoryAllocation(rs.getInt("c.id"), rs.getString("c.nom")),
                        new Event(rs.getInt("e.id"), rs.getString("e.title")),
                        rs.getString("a.image")

                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return allocations;
    }

    public List<Event> getAllEvent() {
        List<Event> listEvent = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `event`");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listEvent.add(
                        new Event(
                                resultSet.getInt("id"),
                                resultSet.getString("title")
                        )
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) event : " + exception.getMessage());
        }
        return listEvent;
    }

}