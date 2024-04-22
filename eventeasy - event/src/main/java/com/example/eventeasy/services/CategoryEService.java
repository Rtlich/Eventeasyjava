package com.example.eventeasy.services;

import com.example.eventeasy.entities.CategoryE;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryEService {

    private static CategoryEService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public CategoryEService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static CategoryEService getInstance() {
        if (instance == null) {
            instance = new CategoryEService();
        }
        return instance;
    }

    public List<CategoryE> getAll() {
        List<CategoryE> listCategoryE = new ArrayList<>();
        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM `category_e`");


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryE categoryE = new CategoryE();
                categoryE.setId(resultSet.getInt("id"));
                categoryE.setType(resultSet.getString("type"));


                listCategoryE.add(categoryE);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) categoryE : " + exception.getMessage());
        }
        return listCategoryE;
    }


    public boolean add(CategoryE categoryE) {


        String request = "INSERT INTO `category_e`(`type`) VALUES(?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, categoryE.getType());


            preparedStatement.executeUpdate();
            System.out.println("Category event added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) categoryE : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(CategoryE categoryE) {

        String request = "UPDATE `category_e` SET `type` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, categoryE.getType());


            preparedStatement.setInt(2, categoryE.getId());

            preparedStatement.executeUpdate();
            System.out.println("Category event edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) categoryE : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `category_e` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Category event deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) categoryE : " + exception.getMessage());
        }
        return false;
    }
}
