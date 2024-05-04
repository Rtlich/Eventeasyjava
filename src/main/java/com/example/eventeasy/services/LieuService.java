package com.example.eventeasy.services;

import com.example.eventeasy.entities.CategoryL;
import com.example.eventeasy.entities.Lieu;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LieuService {

    private static LieuService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public LieuService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static LieuService getInstance() {
        if (instance == null) {
            instance = new LieuService();
        }
        return instance;
    }

    public List<Lieu> getAll() {
        List<Lieu> listLieu = new ArrayList<>();
        try {

            String query = "SELECT * FROM `lieu` AS x "
                    + "RIGHT JOIN `category_l` AS y1 ON x.category_id = y1.id "
                    + "WHERE  x.category_id = y1.id  " ;
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lieu lieu = new Lieu();
                lieu.setId(resultSet.getInt("id"));
                lieu.setNom(resultSet.getString("nom"));
                lieu.setPrix(resultSet.getFloat("prix"));
                lieu.setImage(resultSet.getString("image"));
                lieu.setDateD(resultSet.getDate("date_d") != null ? resultSet.getDate("date_d").toLocalDate() : null);
                lieu.setDateF(resultSet.getDate("date_f") != null ? resultSet.getDate("date_f").toLocalDate() : null);
                lieu.setCapacity(resultSet.getInt("capacity"));
                lieu.setRegion(resultSet.getString("region"));

                CategoryL category = new CategoryL();
                category.setId(resultSet.getInt("y1.id"));
                category.setNom(resultSet.getString("y1.nom"));
                lieu.setCategory(category);

                listLieu.add(lieu);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) lieu : " + exception.getMessage());
        }
        return listLieu;
    }

    public List<CategoryL> getAllCategorys() {
        List<CategoryL> listCategorys = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `category_l`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryL category = new CategoryL();
                category.setId(resultSet.getInt("id"));
                category.setNom(resultSet.getString("nom"));
                listCategorys.add(category);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) categorys : " + exception.getMessage());
        }
        return listCategorys;
    }


    public boolean add(Lieu lieu) {


        String request = "INSERT INTO `lieu`(`nom`, `prix`, `image`, `date_d`, `date_f`, `capacity`, `region`, `category_id`,`longitude`, `latitude`) VALUES(?, ?, ?, ?, ?, ?, ?, ?,?,?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, lieu.getNom());
            preparedStatement.setFloat(2, lieu.getPrix());
            preparedStatement.setString(3, lieu.getImage());
            preparedStatement.setDate(4, Date.valueOf(lieu.getDateD()));
            preparedStatement.setDate(5, Date.valueOf(lieu.getDateF()));
            preparedStatement.setInt(6, lieu.getCapacity());
            preparedStatement.setString(7, lieu.getRegion());

            preparedStatement.setInt(8, lieu.getCategory().getId());

            preparedStatement.setDouble(9, lieu.getLongitude());
            preparedStatement.setDouble(10, lieu.getLatitude());


            preparedStatement.executeUpdate();
            System.out.println("Lieu added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) lieu : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Lieu lieu) {

        String request = "UPDATE `lieu` SET `nom` = ?, `prix` = ?, `image` = ?, `date_d` = ?, `date_f` = ?, `capacity` = ?, `region` = ?, `category_id` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, lieu.getNom());
            preparedStatement.setFloat(2, lieu.getPrix());
            preparedStatement.setString(3, lieu.getImage());
            preparedStatement.setDate(4, Date.valueOf(lieu.getDateD()));
            preparedStatement.setDate(5, Date.valueOf(lieu.getDateF()));
            preparedStatement.setInt(6, lieu.getCapacity());
            preparedStatement.setString(7, lieu.getRegion());

            preparedStatement.setInt(8, lieu.getCategory().getId());

            preparedStatement.setInt(9, lieu.getId());

            preparedStatement.executeUpdate();
            System.out.println("Lieu edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) lieu : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `lieu` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Lieu deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) lieu : " + exception.getMessage());
        }
        return false;
    }
    public Lieu getLieuById(int lieuId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Lieu lieu = null;
        try {
            String query = "SELECT * FROM `lieu` WHERE `id` = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, lieuId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lieu = new Lieu();
                lieu.setId(resultSet.getInt("id"));
                lieu.setNom(resultSet.getString("nom"));
                lieu.setPrix(resultSet.getFloat("prix"));
                lieu.setImage(resultSet.getString("image"));
                lieu.setDateD(resultSet.getDate("date_d") != null ? resultSet.getDate("date_d").toLocalDate() : null);
                lieu.setDateF(resultSet.getDate("date_f") != null ? resultSet.getDate("date_f").toLocalDate() : null);
                lieu.setCapacity(resultSet.getInt("capacity"));
                lieu.setRegion(resultSet.getString("region"));

                CategoryL category = new CategoryL();
                category.setId(resultSet.getInt("category_id"));
                lieu.setCategory(category);
            }
        } catch (SQLException exception) {
            System.out.println("Error in getLieuById: " + exception.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lieu;
    }

    public List<Lieu> searchByNom(String nom) {
        List<Lieu> listLieu = new ArrayList<>();
        try {
            String query = "SELECT * FROM `lieu` AS x " +
                    "RIGHT JOIN `category_l` AS y1 ON x.category_id = y1.id " +
                    "WHERE LOWER(x.nom) LIKE ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + nom + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Lieu lieu = new Lieu();
                lieu.setId(resultSet.getInt("id"));
                lieu.setNom(resultSet.getString("nom"));
                lieu.setPrix(resultSet.getFloat("prix"));
                lieu.setImage(resultSet.getString("image"));
                lieu.setDateD(resultSet.getDate("date_d") != null ? resultSet.getDate("date_d").toLocalDate() : null);
                lieu.setDateF(resultSet.getDate("date_f") != null ? resultSet.getDate("date_f").toLocalDate() : null);
                lieu.setCapacity(resultSet.getInt("capacity"));
                lieu.setRegion(resultSet.getString("region"));

                CategoryL category = new CategoryL();
                category.setId(resultSet.getInt("y1.id"));
                category.setNom(resultSet.getString("y1.nom"));
                lieu.setCategory(category);

                listLieu.add(lieu);
            }
        } catch (SQLException exception) {
            System.out.println("Error (searchByNom) lieu : " + exception.getMessage());
        }
        return listLieu;
    }



}
