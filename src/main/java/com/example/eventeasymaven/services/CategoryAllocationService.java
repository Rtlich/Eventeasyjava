package com.example.eventeasymaven.services;

import com.example.eventeasymaven.entities.CategoryAllocation;
import com.example.eventeasymaven.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryAllocationService implements IService<CategoryAllocation> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    private static CategoryAllocationService instance;

    public CategoryAllocationService() {
    }

    public static CategoryAllocationService getInstance() {
        if (instance == null) {
            instance = new CategoryAllocationService();
        }
        return instance;
    }

    public boolean ajouter(CategoryAllocation c) {
        String req = "INSERT INTO `category_a`(`nom`) VALUES (?);";

        try {
            PreparedStatement catsv = connection.prepareStatement(req);

            catsv.setString(1, c.getNom());
            catsv.executeUpdate();
            System.out.println("category ajouté avec succès !");

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean supprimer(CategoryAllocation c) {
        String req = "DELETE FROM `category_a` WHERE `category_a`.`id` = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, c.getId());
            pst.executeUpdate();
            System.out.println("category supprimer avec succes !");

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean modifier(CategoryAllocation c) {
        String req = "UPDATE `category_a` SET `nom` = ? WHERE `category_a`.`id` = ?;";

        try {
            PreparedStatement catsv = connection.prepareStatement(req);

            catsv.setString(1, c.getNom());

            catsv.setInt(2, c.getId());
            catsv.executeUpdate();
            System.out.println("category modifie avec succès !");

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public List<CategoryAllocation> afficher() {

        List<CategoryAllocation> cats = new ArrayList<>();
        String req = "SELECT * FROM `category_a`";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                cats.add(
                        new CategoryAllocation(
                                rs.getInt("id"),
                                rs.getString("nom")
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cats;
    }

}

