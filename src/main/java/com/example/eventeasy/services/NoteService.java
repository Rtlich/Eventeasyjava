package com.example.eventeasy.services;

import com.example.eventeasy.entities.Note;
import com.example.eventeasy.entities.User;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteService {

    private static NoteService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public NoteService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static NoteService getInstance() {
        if (instance == null) {
            instance = new NoteService();
        }
        return instance;
    }

    public List<Note> getAll() {
        List<Note> listNote = new ArrayList<>();
        try {

            String query = "SELECT * FROM `note` AS x "
                    + "RIGHT JOIN `user` AS y1 ON x.user_id = y1.id "

                    + "WHERE  x.user_id = y1.id  " ;
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Note note = new Note();
                note.setId(resultSet.getInt("id"));
                note.setDescription(resultSet.getString("description"));
                note.setDate(resultSet.getDate("date") != null ? resultSet.getDate("date").toLocalDate() : null);

                User user = new User();
                user.setId(resultSet.getInt("y1.id"));
                user.setFname(resultSet.getString("y1.fname"));
                note.setUser(user);

                listNote.add(note);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) note : " + exception.getMessage());
        }
        return listNote;
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
                listUsers.add(user);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) users : " + exception.getMessage());
        }
        return listUsers;
    }


    public boolean add(Note note) {


        String request = "INSERT INTO `note`(`description`, `date`, `user_id`) VALUES(?, ?, ?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, note.getDescription());
            preparedStatement.setDate(2, Date.valueOf(note.getDate()));

            preparedStatement.setInt(3, note.getUser().getId());


            preparedStatement.executeUpdate();
            System.out.println("Note added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) note : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Note note) {

        String request = "UPDATE `note` SET `description` = ?, `date` = ?, `user_id` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, note.getDescription());
            preparedStatement.setDate(2, Date.valueOf(note.getDate()));

            preparedStatement.setInt(3, note.getUser().getId());

            preparedStatement.setInt(4, note.getId());

            preparedStatement.executeUpdate();
            System.out.println("Note edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) note : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `note` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Note deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) note : " + exception.getMessage());
        }
        return false;
    }
}
