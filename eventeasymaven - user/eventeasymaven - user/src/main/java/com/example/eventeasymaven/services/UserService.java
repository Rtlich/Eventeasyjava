package com.example.eventeasymaven.services;

import com.example.eventeasymaven.entities.User;
import com.example.eventeasymaven.entities.UserRole;
import com.example.eventeasymaven.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static UserService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public UserService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User checkUser(String email, String password) {

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE `email` LIKE ?");

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getInt("phonenumber"),
                        resultSet.getString("roles").equalsIgnoreCase("[\"ROLE_ADMIN\"]") ? UserRole.Admin : UserRole.User,
                        resultSet.getBoolean("enabled")

                );

                if (checkPassword(password, user.getPassword())) {
                    return user;
                }

            }
        } catch (SQLException e) {
            System.out.println("Aucun email : " + e.getMessage());
        }

        return null;
    }

    private Boolean checkPassword(String inputPassword, String hashedPasswordFromDatabase) {
        try {
            return BCrypt.checkpw(inputPassword, hashedPasswordFromDatabase);
        } catch (Exception e) {
            System.out.println("Error (checkPassword) : " + e.getMessage());
        }
        return false;
    }

    private String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(13));
    }

    public List<User> getAll() {
        List<User> listUser = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `user`");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                listUser.add(
                        new User(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("fname"),
                                resultSet.getString("lname"),
                                resultSet.getInt("phonenumber"),
                                resultSet.getString("roles").equalsIgnoreCase("[\"ROLE_ADMIN\"]") ? UserRole.Admin : UserRole.User,
                                resultSet.getBoolean("enabled")
                        )
                );
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) user : " + exception.getMessage());
        }
        return listUser;
    }


    public boolean checkExist(String email) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `user` WHERE `email` = ?");

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException exception) {
            System.out.println("Error (checkExist) : " + exception.getMessage());
        }
        return false;
    }

    public boolean add(User user) {
        if (user == null) {
            return false;
        }

        if (checkExist(user.getEmail())) {
            return false;
        }


        String request = "INSERT INTO `user`(`email`, `password`, `fname`, `lname`, `phonenumber`, `roles`, `enabled`) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, encodePassword(user.getPassword()));
            preparedStatement.setString(3, user.getFname());
            preparedStatement.setString(4, user.getLname());
            preparedStatement.setInt(5, user.getPhonenumber());
            preparedStatement.setString(6, user.getRoleString());
            preparedStatement.setBoolean(7, user.getEnabled());

            preparedStatement.executeUpdate();
            System.out.println("User added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) user : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(User user) {

        String request = "UPDATE `user` SET `email` = ?, `fname` = ?, `lname` = ?, `phonenumber` = ?, `roles` = ?, `enabled` = ? WHERE `id`=" + user.getId();
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFname());
            preparedStatement.setString(3, user.getLname());
            preparedStatement.setInt(4, user.getPhonenumber());
            preparedStatement.setString(5, user.getRoleString());
            preparedStatement.setBoolean(6, user.getEnabled());

            preparedStatement.executeUpdate();
            System.out.println("User edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) user : " + exception.getMessage());
        }
        return false;
    }


    public boolean setEnabled(int userId, boolean enabled) {

        String request = "UPDATE `user` SET `enabled` = ? WHERE `id`=" + userId;
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setBoolean(1, enabled);

            preparedStatement.executeUpdate();
            System.out.println("User edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (setEnabled) user : " + exception.getMessage());
        }
        return false;
    }

    public boolean updatePassword(String email, String newPassword) {

        String request = "UPDATE `user` SET `password` = ? WHERE `email` LIKE '" + email + "'";
        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, encodePassword(newPassword));

            preparedStatement.executeUpdate();
            System.out.println("User edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) user : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `user` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("User deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) user : " + exception.getMessage());
        }
        return false;
    }
}
