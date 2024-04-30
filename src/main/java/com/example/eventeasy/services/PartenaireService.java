package com.example.eventeasy.services;

import com.example.eventeasy.entities.Partenaire;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartenaireService {

    private static PartenaireService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public PartenaireService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static PartenaireService getInstance() {
        if (instance == null) {
            instance = new PartenaireService();
        }
        return instance;
    }

    public List<Partenaire> getAll() {
        List<Partenaire> listPartenaire = new ArrayList<>();
        try {

            preparedStatement = connection.prepareStatement("SELECT * FROM `partenaire`");


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Partenaire partenaire = new Partenaire();
                partenaire.setId(resultSet.getInt("id"));
                partenaire.setNom(resultSet.getString("nom"));
                partenaire.setTel(resultSet.getInt("tel"));
                partenaire.setDon(resultSet.getFloat("don"));
                partenaire.setImage(resultSet.getString("image"));


                listPartenaire.add(partenaire);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) partenaire : " + exception.getMessage());
        }
        return listPartenaire;
    }


    public boolean add(Partenaire partenaire) {


        String request = "INSERT INTO `partenaire`(`nom`, `tel`, `don`, `image`) VALUES(?, ?, ?, ?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, partenaire.getNom());
            preparedStatement.setInt(2, partenaire.getTel());
            preparedStatement.setFloat(3, partenaire.getDon());
            preparedStatement.setString(4, partenaire.getImage());


            preparedStatement.executeUpdate();
            System.out.println("Partenaire added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) partenaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Partenaire partenaire) {

        String request = "UPDATE `partenaire` SET `nom` = ?, `tel` = ?, `don` = ?, `image` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, partenaire.getNom());
            preparedStatement.setInt(2, partenaire.getTel());
            preparedStatement.setFloat(3, partenaire.getDon());
            preparedStatement.setString(4, partenaire.getImage());


            preparedStatement.setInt(5, partenaire.getId());

            preparedStatement.executeUpdate();
            System.out.println("Partenaire edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) partenaire : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `partenaire` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Partenaire deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) partenaire : " + exception.getMessage());
        }
        return false;
    }
}
