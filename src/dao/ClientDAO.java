package dao;

import database.DatabaseConnection;
import model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public int ajouter(Client client) {
        String sql = "INSERT INTO CLIENT (nom, prenom, telephone, adresse) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getAdresse());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }

        } catch (SQLException e) {
            afficherErreur("ajout Client", e);
        }
        return -1;
    }

    public List<Client> listerTous() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM CLIENT ORDER BY nom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("adresse")
                ));
            }

        } catch (SQLException e) {
            afficherErreur("liste Clients", e);
        }

        return clients;
    }

    public Client rechercherParTelephone(String telephone) {
        String sql = "SELECT * FROM CLIENT WHERE telephone = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, telephone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("telephone"),
                            rs.getString("adresse")
                    );
                }
            }

        } catch (SQLException e) {
            afficherErreur("recherche Client", e);
        }

        return null;
    }

    private void afficherErreur(String action, SQLException e) {
        System.out.println("Erreur lors de " + action);
        System.out.println("Message: " + e.getMessage());
        System.out.println("Code: " + e.getErrorCode());
    }
}