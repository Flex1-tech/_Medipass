import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DossierMedical {
    private static int compteur = 0;
    private int idDossier;
    private int nbConsultations = 0;
    private List<Consultation> consultations = new ArrayList<>();
    private List<String> antecedants = new ArrayList<>();

    // Constructeur
    public DossierMedical() {
        this.idDossier = ++compteur;
    }

    public void mettreAJourNbconsultations(){
        this.nbConsultations = this.consultations.size();
        return;
    }

    // Méthodes
    public void ajouterAntecedant(String antecedant) {
        this.antecedants.add(antecedant);
    }

    // Getters
    public int getIdDossier() {
        return idDossier;
    }

    public int getNbConsultations() {
        return nbConsultations;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public List<String> getAntecedants() {
        return antecedants;
    }

    public void ajouterConsultation(Consultation consultation) {
        this.consultations.add(consultation);
        this.nbConsultations++;
        
        // On ajoute le diagnostic dans les antecedants
        this.antecedants.add(consultation.getDiagnostic());
    }

    public void save(Connection conn, int idUser) {
        if(conn == null){
            try {
                conn = DriverManager.getConnection(User.url);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {

            // Vérifier si un dossier existe déjà
            String sqlCheck = "SELECT idDossier FROM DossierMedical WHERE idPatient = ?";
            boolean existe = false;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlCheck)) {
                pstmt.setInt(1, idUser);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        this.idDossier = rs.getInt("idDossier");
                        existe = true;
                    }
                }
            }

            // INSERT si aucun dossier n’existe
            if (!existe) {
                String sqlInsert = "INSERT INTO DossierMedical (idPatient) VALUES (?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, idUser);
                    pstmt.executeUpdate();

                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.idDossier = rs.getInt(1);
                        }
                    }
                }
            }

            //  Sauvegarde des antécédents
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "DELETE FROM Antecedents WHERE idDossier=?")) {
                pstmt.setInt(1, this.idDossier);
                pstmt.executeUpdate();
            }

            String sqlInsertAnte = "INSERT INTO Antecedents (idDossier, texte) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertAnte)) {
                if (antecedants != null) {
                    for (String antecedant : antecedants) {
                        pstmt.setInt(1, this.idDossier);
                        pstmt.setString(2, antecedant);
                        pstmt.executeUpdate();
                    }
                }
            }

            // 4) Sauvegarde des consultations
            for (Consultation c : consultations) {
                c.save(conn, idUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n===== DOSSIER MÉDICAL N°").append(idDossier).append(" =====\n");

        // ANTÉCÉDENTS
        sb.append("\n--- ANTÉCÉDENTS ---\n");
        if (antecedants.isEmpty()) {
            sb.append("Aucun antécédent enregistré.\n");
        } else {
            for (int i = 0; i < antecedants.size(); i++) {
                sb.append((i + 1)).append(") ").append(antecedants.get(i)).append("\n");
            }
        }

        // CONSULTATIONS
        sb.append("\n--- CONSULTATIONS (").append(nbConsultations).append(") ---\n");
        if (consultations.isEmpty()) {
            sb.append("Aucune consultation.\n");
        } else {
            for (Consultation c : consultations) {
                sb.append(c.toString()).append("\n");
            }
        }

        sb.append("===============================\n");

        return sb.toString();
    }

}

