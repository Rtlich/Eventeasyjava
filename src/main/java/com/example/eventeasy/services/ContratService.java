package com.example.eventeasy.services;

import com.example.eventeasy.entities.Contrat;
import com.example.eventeasy.entities.Partenaire;
import com.example.eventeasy.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratService {

    private static ContratService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public ContratService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static ContratService getInstance() {
        if (instance == null) {
            instance = new ContratService();
        }
        return instance;
    }

    public List<Contrat> getAll() {
        List<Contrat> listContrat = new ArrayList<>();
        try {

            String query = "SELECT * FROM `contrat` AS x "
                    + "RIGHT JOIN `partenaire` AS y1 ON x.partenaire_id = y1.id "
                    + "WHERE  x.partenaire_id = y1.id  " ;
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Contrat contrat = new Contrat();
                contrat.setId(resultSet.getInt("id"));
                contrat.setDatedebut(resultSet.getDate("datedebut") != null ? resultSet.getDate("datedebut").toLocalDate() : null);
                contrat.setDatefin(resultSet.getDate("datefin") != null ? resultSet.getDate("datefin").toLocalDate() : null);

                Partenaire partenaire = new Partenaire();
                partenaire.setId(resultSet.getInt("y1.id"));
                partenaire.setNom(resultSet.getString("y1.nom"));
                partenaire.setImage(resultSet.getString("y1.image"));
                partenaire.setTel(resultSet.getInt("y1.tel"));
                partenaire.setDon(resultSet.getFloat("y1.don"));

                contrat.setPartenaire(partenaire);

                listContrat.add(contrat);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) contrat : " + exception.getMessage());
        }
        return listContrat;
    }

    public List<Partenaire> getAllPartenaires() {
        List<Partenaire> listPartenaires = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `partenaire`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Partenaire partenaire = new Partenaire();
                partenaire.setId(resultSet.getInt("id"));
                partenaire.setNom(resultSet.getString("nom"));
                partenaire.setImage(resultSet.getString("image"));
                partenaire.setTel(resultSet.getInt("tel"));
                partenaire.setDon(resultSet.getFloat("don"));

                listPartenaires.add(partenaire);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) partenaires : " + exception.getMessage());
        }
        return listPartenaires;
    }


    public boolean add(Contrat contrat) {


        String request = "INSERT INTO `contrat`(`datedebut`, `datefin`, `partenaire_id`) VALUES(?, ?, ?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setDate(1, Date.valueOf(contrat.getDatedebut()));
            preparedStatement.setDate(2, Date.valueOf(contrat.getDatefin()));
            preparedStatement.setInt(3, contrat.getPartenaire().getId());

            preparedStatement.executeUpdate();
            System.out.println("Contrat added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) contrat : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Contrat contrat) {

        String request = "UPDATE `contrat` SET `datedebut` = ?, `datefin` = ?, `partenaire_id` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setDate(1, Date.valueOf(contrat.getDatedebut()));
            preparedStatement.setDate(2, Date.valueOf(contrat.getDatefin()));
            preparedStatement.setInt(3, contrat.getPartenaire().getId());
            preparedStatement.setInt(4, contrat.getId());

            preparedStatement.executeUpdate();
            System.out.println("Contrat edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) contrat : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `contrat` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Contrat deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) contrat : " + exception.getMessage());
        }
        return false;
    }
}
