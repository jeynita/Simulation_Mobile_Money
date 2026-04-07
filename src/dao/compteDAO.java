package dao;

import database.DatabaseConnection;
import model.Client;
import model.Compte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteDAO {

    public int creer(Compte compte) {
        String sql = "INSERT INTO COMPTE (numero_compte, solde, client_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, compte.getNumeroCompte());
            ps.setDouble(2, compte.getSolde());
            // Utilise l'objet client pour récupérer l'ID
            ps.setInt(3, compte.getClient().getId());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }

        } catch (SQLException e) {
            afficherErreur("création compte", e);
        }

        return -1;
    }

    public Compte consulterParNumero(String numero) {
        String sql = "SELECT * FROM COMPTE WHERE numero_compte = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numero);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Création d'un objet client avec l'ID de la DB
                    Client client = new Client();
                    client.setId(rs.getInt("client_id"));

                    Compte compte = new Compte(
                            rs.getString("numero_compte"),
                            rs.getDouble("solde"),
                            client
                    );
                    compte.setId(rs.getInt("id"));
                    return compte;
                }
            }

        } catch (SQLException e) {
            afficherErreur("consultation compte", e);
        }

        return null;
    }

    public double afficherSolde(String numero) {
        String sql = "SELECT solde FROM COMPTE WHERE numero_compte = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numero);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("solde");
            }

        } catch (SQLException e) {
            afficherErreur("affichage solde", e);
        }

        return -1;
    }

    public boolean mettreAJourSolde(String numero, double solde) {
        String sql = "UPDATE COMPTE SET solde = ? WHERE numero_compte = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, solde);
            ps.setString(2, numero);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            afficherErreur("mise à jour solde", e);
        }

        return false;
    }

    public List<Compte> listerTous() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM COMPTE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Création du client lié
                Client client = new Client();
                client.setId(rs.getInt("client_id"));

                Compte c = new Compte(
                        rs.getString("numero_compte"),
                        rs.getDouble("solde"),
                        client
                );
                c.setId(rs.getInt("id"));
                comptes.add(c);
            }

        } catch (SQLException e) {
            afficherErreur("liste comptes", e);
        }

        return comptes;
    }

    private void afficherErreur(String action, SQLException e) {
        System.out.println("Erreur lors de " + action);
        System.out.println("Message: " + e.getMessage());
        System.out.println("Code: " + e.getErrorCode());
    }
}