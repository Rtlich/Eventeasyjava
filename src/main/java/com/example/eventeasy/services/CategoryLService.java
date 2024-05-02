package com.example.eventeasy.services;

import com.example.eventeasy.entities.CategoryL;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryLService {

    private static CategoryLService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public CategoryLService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static CategoryLService getInstance() {
        if (instance == null) {
            instance = new CategoryLService();
        }
        return instance;
    }

    public List<CategoryL> getAll() {
        List<CategoryL> listCategoryL = new ArrayList<>();
        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM `category_l`");


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryL categoryL = new CategoryL();
                categoryL.setId(resultSet.getInt("id"));
                categoryL.setNom(resultSet.getString("nom"));


                listCategoryL.add(categoryL);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) categoryL : " + exception.getMessage());
        }
        return listCategoryL;
    }
    // Méthode pour vérifier si un nom existe déjà dans la base de données
    public boolean existsNom(String nom) {
        try {
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM `category_l` WHERE `nom`=?");
            preparedStatement.setString(1, nom);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Si le compteur est supérieur à zéro, cela signifie que le nom existe déjà
            }
        } catch (SQLException exception) {
            System.out.println("Error (existsNom) categoryL : " + exception.getMessage());
        }
        return false;
    }



    public boolean add(CategoryL categoryL) {


        String request = "INSERT INTO `category_l`(`nom`) VALUES(?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, categoryL.getNom());


            preparedStatement.executeUpdate();
            System.out.println("CategoryL added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) categoryL : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(CategoryL categoryL) {

        String request = "UPDATE `category_l` SET `nom` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, categoryL.getNom());


            preparedStatement.setInt(2, categoryL.getId());

            preparedStatement.executeUpdate();
            System.out.println("CategoryL edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) categoryL : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `category_l` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("CategoryL deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) categoryL : " + exception.getMessage());
        }
        return false;
    }
}
