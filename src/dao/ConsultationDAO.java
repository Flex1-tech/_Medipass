package dao;

import model.Consultation;
import model.Patient;
import model.Professionnel_de_Sante;
import model.Disponibilite;
import database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class ConsultationDAO {

    public void insert(Consultation c) {
        String sql = """
                INSERT INTO consultation(
                    id_consultation, date_prevue, service, diagnostic, poids,
                    id_patient, id_professionnel, id_creneau
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getIdConsultation());
            stmt.setString(2, c.getDatePrevue().toString());
            stmt.setString(3, c.getService());
            stmt.setString(4, c.getDiagnostic());
            stmt.setObject(5, c.getPoids());
            stmt.setInt(6, c.getPatient().getIdUser());
            stmt.setInt(7, c.getProfessionnelDeSante().getIdUser());
            stmt.setInt(8, c.getCreneau().getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Consultation findById(int id) {
        String sql = "SELECT * FROM consultation WHERE id_consultation = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                LocalDate datePrevue = LocalDate.parse(rs.getString("date_prevue"));

                Patient p = new PatientDAO().findById(rs.getInt("id_patient"));
                Professionnel_de_Sante pro = new ProfessionnelDAO().findById(rs.getInt("id_professionnel"));
                Disponibilite creneau = new DisponibiliteDAO().findById(rs.getInt("id_creneau"));

                Consultation c = new Consultation(
                        p, pro,
                        rs.getString("service"),
                        datePrevue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        creneau);

                c.setDiagnostic(rs.getString("diagnostic"));
                return c;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void update(Consultation c) {
        String sql = "UPDATE consultation SET service = ?, diagnostic = ?, poids = ? WHERE id_consultation = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getService());
            stmt.setString(2, c.getDiagnostic());
            stmt.setObject(3, c.getPoids());
            stmt.setInt(4, c.getIdConsultation());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM consultation WHERE id_consultation = ?")) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
