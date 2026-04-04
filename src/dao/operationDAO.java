package dao;

import database.DatabaseConnection;
import model.Operation;
import model.Operation.TypeOperation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OperationDAO {

    public int enregistrer(Operation op) {
        String sql = "INSERT INTO OPERATION (type_operation, montant, date_operation, compte_source, compte_destination, marchand) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, op.getTypeOperation().name());
            ps.setDouble(2, op.getMontant());
            ps.setTimestamp(3, Timestamp.valueOf(op.getDateOperation()));
            ps.setString(4, op.getCompteSource());

            if (op.getCompteDestination() != null)
                ps.setString(5, op.getCompteDestination());
            else
                ps.setNull(5, Types.VARCHAR);

            if (op.getMarchand() != null)
                ps.setString(6, op.getMarchand());
            else
                ps.setNull(6, Types.VARCHAR);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }

        } catch (SQLException e) {
            afficherErreur("enregistrement opération", e);
        }

        return -1;
    }

    public List<Operation> listerToutes() {
        List<Operation> ops = new ArrayList<>();
        String sql = "SELECT * FROM OPERATION ORDER BY date_operation DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ops.add(mapper(rs));
            }

        } catch (SQLException e) {
            afficherErreur("liste opérations", e);
        }

        return ops;
    }

    public List<Operation> listerParCompte(String numero) {
        List<Operation> ops = new ArrayList<>();
        String sql = "SELECT * FROM OPERATION WHERE compte_source = ? OR compte_destination = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numero);
            ps.setString(2, numero);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ops.add(mapper(rs));
                }
            }

        } catch (SQLException e) {
            afficherErreur("opérations par compte", e);
        }

        return ops;
    }

    private Operation mapper(ResultSet rs) throws SQLException {
        return new Operation(
                rs.getInt("id"),
                TypeOperation.valueOf(rs.getString("type_operation")),
                rs.getDouble("montant"),
                rs.getTimestamp("date_operation").toLocalDateTime(),
                rs.getString("compte_source"),
                rs.getString("compte_destination"),
                rs.getString("marchand")
        );
    }

    private void afficherErreur(String action, SQLException e) {
        System.out.println("Erreur lors de " + action);
        System.out.println("Message: " + e.getMessage());
    }
}