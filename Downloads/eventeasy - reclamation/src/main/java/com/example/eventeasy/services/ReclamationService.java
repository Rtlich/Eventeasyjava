package com.example.eventeasy.services;

import com.example.eventeasy.entities.Reclamation;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {

    private static ReclamationService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public ReclamationService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static ReclamationService getInstance() {
        if (instance == null) {
            instance = new ReclamationService();
        }
        return instance;
    }

    public List<Reclamation> getAll() {
        List<Reclamation> listReclamation = new ArrayList<>();
        try {

            String query = "SELECT * FROM `reclamation` AS x "
                    + "RIGHT JOIN `user` AS y1 ON x.user_id = y1.id "
                    + "WHERE  x.user_id = y1.id  " ;
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setDescription(resultSet.getString("description"));
                reclamation.setDate(resultSet.getDate("date") != null ? resultSet.getDate("date").toLocalDate() : null);

                User user = new User();
                user.setId(resultSet.getInt("y1.id"));
                user.setFname(resultSet.getString("y1.fname"));
                user.setEmail(resultSet.getString("y1.email"));
                reclamation.setUser(user);

                listReclamation.add(reclamation);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) reclamation : " + exception.getMessage());
        }
        return listReclamation;
    }

    public List<User> getAllUsers() {
        List<User> listUsers = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `user`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFname(resultSet.getString("fname"));
                user.setEmail(resultSet.getString("email"));
                listUsers.add(user);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) users : " + exception.getMessage());
        }
        return listUsers;
    }


    public boolean add(Reclamation reclamation) {


        String request = "INSERT INTO `reclamation`(`description`, `date`, `user_id`) VALUES(?, ?, ?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, reclamation.getDescription());
            preparedStatement.setDate(2, Date.valueOf(reclamation.getDate()));

            preparedStatement.setInt(3, reclamation.getUser().getId());


            preparedStatement.executeUpdate();
            System.out.println("Reclamation added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) reclamation : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Reclamation reclamation) {

        String request = "UPDATE `reclamation` SET `description` = ?, `date` = ?, `user_id` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, reclamation.getDescription());
            preparedStatement.setDate(2, Date.valueOf(reclamation.getDate()));

            preparedStatement.setInt(3, reclamation.getUser().getId());

            preparedStatement.setInt(4, reclamation.getId());

            preparedStatement.executeUpdate();
            System.out.println("Reclamation edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) reclamation : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `reclamation` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Reclamation deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) reclamation : " + exception.getMessage());
        }
        return false;
    }
}
