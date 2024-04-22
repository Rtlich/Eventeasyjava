package com.example.eventeasy.services;

import com.example.eventeasy.entities.CategoryE;
import com.example.eventeasy.entities.Event;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {

    private static EventService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public EventService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static EventService getInstance() {
        if (instance == null) {
            instance = new EventService();
        }
        return instance;
    }

    public List<Event> getAll() {
        List<Event> listEvent = new ArrayList<>();
        try {

            String query = "SELECT * FROM `event` AS x "
                    + "RIGHT JOIN `category_e` AS y1 ON x.categoryid_id = y1.id "
                    + "WHERE  x.categoryid_id = y1.id   " ;
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Event event = new Event();
                event.setId(resultSet.getInt("id"));
                event.setTitle(resultSet.getString("title"));
                event.setEmail(resultSet.getString("email"));
                event.setPhone(resultSet.getInt("phone"));
                event.setDate(resultSet.getDate("date") != null ? resultSet.getDate("date").toLocalDate() : null);

                CategoryE category = new CategoryE();
                category.setId(resultSet.getInt("y1.id"));
                category.setType(resultSet.getString("y1.type"));
                event.setCategory(category);

                listEvent.add(event);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) event : " + exception.getMessage());
        }
        return listEvent;
    }

    public List<CategoryE> getAllCategorys() {
        List<CategoryE> listCategorys = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `category_e`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryE category = new CategoryE();
                category.setId(resultSet.getInt("id"));
                category.setType(resultSet.getString("type"));
                listCategorys.add(category);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) categorys : " + exception.getMessage());
        }
        return listCategorys;
    }

    public boolean add(Event event) {


        String request = "INSERT INTO `event`(`title`, `email`, `phone`, `date`, `categoryid_id`) VALUES(?, ?, ?, ?, ?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, event.getTitle());
            preparedStatement.setString(2, event.getEmail());
            preparedStatement.setInt(3, event.getPhone());
            preparedStatement.setDate(4, Date.valueOf(event.getDate()));

            preparedStatement.setInt(5, event.getCategory().getId());


            preparedStatement.executeUpdate();
            System.out.println("Event added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) event : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Event event) {

        String request = "UPDATE `event` SET `title` = ?, `email` = ?, `phone` = ?, `date` = ?, `categoryid_id` = ? WHERE `id` = ? " ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, event.getTitle());
            preparedStatement.setString(2, event.getEmail());
            preparedStatement.setInt(3, event.getPhone());
            preparedStatement.setDate(4, Date.valueOf(event.getDate()));

            preparedStatement.setInt(5, event.getCategory().getId());
//            preparedStatement.setInt(6, 0);
//            preparedStatement.setInt(7, 0);

            preparedStatement.setInt(6, event.getId());

            preparedStatement.executeUpdate();
            System.out.println("Event edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) event : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `event` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Event deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) event : " + exception.getMessage());
        }
        return false;
    }
}
