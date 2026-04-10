package dao;

import database.DatabaseConnection;
import model.Marchand;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarchandDAO {


    public List<Marchand> listerTous() {
        List<Marchand> marchands = new ArrayList<>();
        String sql = "SELECT * FROM MARCHAND";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                marchands.add(mapResultSetToMarchand(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la liste des marchands : " + e.getMessage());
        }
        return marchands;
    }


    public Marchand trouverParNom(String nom) {
        String sql = "SELECT * FROM MARCHAND WHERE nom_enseigne = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMarchand(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du marchand par nom : " + e.getMessage());
        }
        return null;
    }

    
    public Marchand trouverParCode(String code) {
        String sql = "SELECT * FROM MARCHAND WHERE code_marchand = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMarchand(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du marchand par code : " + e.getMessage());
        }
        return null;
    }


    private Marchand mapResultSetToMarchand(ResultSet rs) throws SQLException {
        return new Marchand(
            rs.getInt("id"),
            rs.getString("nom_enseigne"),
            rs.getString("code_marchand")
        );
    }
}