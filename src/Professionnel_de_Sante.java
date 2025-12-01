import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Professionnel_de_Sante extends User {
    private String titre;
    
    private List<Disponibilite> disponibilites = new ArrayList<>();

    public String get_titre() {
        return this.titre;
    }

    public void set_titre(String titre) {
        this.titre = titre;
    }

    public Professionnel_de_Sante(String nom, String prenom, String telephone, String motDePasse, String adresse, String titre) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
        this.titre = titre;
    }

    public Professionnel_de_Sante(int idUser, String nom, String prenom, String telephone, String adresse,String titre2) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.titre = titre2;

    }

    public List<Disponibilite> get_Disponibilites() {
        return disponibilites;
    }

public boolean supprimer_Disponibilite(Connection conn, Disponibilite dispo) throws SQLException {

    boolean localTransaction = false;

    if (conn == null) {
        conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);
        localTransaction = true;   
    }

    try {
        dispo.delete(conn);            // suppression BDD
        boolean removed = disponibilites.remove(dispo); // suppression mémoire

        if (localTransaction) {
            conn.commit();  
        }

        return removed;

    } catch (SQLException e) {
        if (localTransaction) {
            conn.rollback(); 
        }
        throw e;
    } finally {
        if (localTransaction && conn != null) {
            conn.close(); 
        }
    }
}


    public void modifier_Disponibilite(Disponibilite ancienneDisponibilite, Disponibilite nouvelleDisponibilite) {
        int index = this.disponibilites.indexOf(ancienneDisponibilite);
        if (index != -1) {
            this.disponibilites.set(index, nouvelleDisponibilite);
        }
    }

    public String afficher_Disponibilites() {
        StringBuilder sb = new StringBuilder();
        if (disponibilites.isEmpty()) {
            return "Aucune disponibilité enregistrée.";    
        }
        for (int i = 0; i<disponibilites.size(); i++) {
            sb.append("Disponibilité ").append(i + 1).append(" : \n");
            Disponibilite disponibilite = disponibilites.get(i);
            sb.append(disponibilite.toString()).append("\n \n");
        }
        return sb.toString();
    }

    public static List<Professionnel_de_Sante> getTousLesProfessionnels() {
        List<Professionnel_de_Sante> pros = new ArrayList<>();

        String query = """
                SELECT U.idUser, U.nom, U.prenom, U.telephone, U.adresse, P.titre FROM Users U JOIN ProfessionnelSante P ON U.idUser = P.idPro;
                """; 
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Professionnel_de_Sante p = new Professionnel_de_Sante(
                    rs.getInt("idUser"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getString("titre")
                );
                pros.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pros;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder(128); // capacité initiale
        sb.append("Nom: ").append(nom).append(", \n");
        sb.append("Prénom: ").append(prenom).append(", \n");
        sb.append("Titre: ").append(titre).append(", \n");
        sb.append("Disponibilités: \n").append(afficher_Disponibilites());

        return sb.toString();
    }

    public void ajouter_Disponibilite(Disponibilite disponibilite) {
        for (Disponibilite d : disponibilites) {
            if (d.chevauche(disponibilite)) {
                throw new IllegalArgumentException("Cette disponibilité chevauche une disponibilité existante !");
            }
        }
        this.disponibilites.add(disponibilite);
    }
    public boolean save() {
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtPro = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            // INSERT / UPDATE Users et ProfessionnelSante
            if (this.exists(conn)) {

                String sqlUser = "UPDATE Users SET nom=?, prenom=?, adresse=? WHERE idUser=?";
                pstmtUser = conn.prepareStatement(sqlUser);
                pstmtUser.setString(1, nom);
                pstmtUser.setString(2, prenom);
                pstmtUser.setString(3, adresse);
                pstmtUser.setInt(4, idUser);
                pstmtUser.executeUpdate();

                String sqlPro = "UPDATE ProfessionnelSante SET titre=? WHERE idPro=?";
                pstmtPro = conn.prepareStatement(sqlPro);
                pstmtPro.setString(1, titre);
                pstmtPro.setInt(2, idUser);
                pstmtPro.executeUpdate();

            } else {

                String sqlUser = "INSERT INTO Users (nom, prenom, telephone, motDePasse, adresse, typeUser)"
                        + " VALUES (?, ?, ?, ?, ?, 'pro')";
                pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
                pstmtUser.setString(1, nom);
                pstmtUser.setString(2, prenom);
                pstmtUser.setString(3, telephone);
                pstmtUser.setString(4, motDePasse);
                pstmtUser.setString(5, adresse);
                pstmtUser.executeUpdate();

                try (ResultSet keys = pstmtUser.getGeneratedKeys()) {
                    if (keys.next()) {
                        idUser = keys.getInt(1);
                    }
                }

                String sqlPro = "INSERT INTO ProfessionnelSante (idPro, titre) VALUES (?, ?)";
                pstmtPro = conn.prepareStatement(sqlPro);
                pstmtPro.setInt(1, idUser);
                pstmtPro.setString(2, titre);
                pstmtPro.executeUpdate();
            }

            // SAVE / UPDATE DISPONIBILITES
            if (disponibilites != null) {
                for (Disponibilite d : disponibilites) {
                    if (d.getIdDispo() > 0) {
                        d.update(conn);
                    } else {
                        d.save(conn, idUser);
                    }
                }
            }

            // SUPPRIMER LES DISPONIBILITES QUI NE SONT PLUS EN MEMOIRE
            String sql = "SELECT idDispo FROM Disponibilites WHERE idPro = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUser);
            rs = ps.executeQuery();

            List<Integer> idsBDD = new ArrayList<>();
            while (rs.next()) {
                idsBDD.add(rs.getInt(1));
            }

            for (int idBDD : idsBDD) {
                boolean existe = false;

                if (disponibilites != null) {
                    for (Disponibilite d : disponibilites) {
                        if (d.getIdDispo() == idBDD) {
                            existe = true;
                            break;
                        }
                    }
                }

                if (!existe) {
                    // suppression
                    Disponibilite tmp = new Disponibilite(idBDD);
                    tmp.delete(conn);
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
            return false;

        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (pstmtUser != null) pstmtUser.close(); } catch (Exception ignored) {}
            try { if (pstmtPro != null) pstmtPro.close(); } catch (Exception ignored) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception ignored) {}
        }
    }

    public void afficherConsultationsPrevues() {
        System.out.println("=== Consultations prévues ===");

        List<Consultation> aVenir = this.getConsultationsAVenir();

        if (aVenir.isEmpty()) {
            System.out.println("Aucune consultation prévue.");
            return;
        }

    for (Consultation c : aVenir) {
        System.out.println(
            "Consultation #" + c.getIdConsultation() +
            " | Patient : " + c.getPatient().getNom() + " " + c.getPatient().getPrenom() +
            " | Date : " + c.getDatePrevue() +
            " | Service : " + c.getService()
        );
        System.out.println("---------------------------");
    }

    }


    public List<Consultation> getConsultationsAVenir() {
        List<Consultation> consultations = new ArrayList<>();

        String sql = "SELECT * FROM Consultations WHERE idPro = ? AND datePrevue >= date('now') ORDER BY datePrevue ASC";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, this.idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int idConsult = rs.getInt("idConsultation");
                    int idPatient = rs.getInt("idPatient");
                    String service = rs.getString("service");
                    String dateStr = rs.getString("datePrevue");
                    LocalDate date = LocalDate.parse(dateStr);
                    int idDispo = rs.getInt("idDispo");

                    // Charger le patient concerné
                    Patient patient = null;
                    String sqlPat = "SELECT nom, prenom, telephone, adresse FROM Users WHERE idUser = ?";
                    try (PreparedStatement psPat = conn.prepareStatement(sqlPat)) {
                        psPat.setInt(1, idPatient);
                        try (ResultSet rsPat = psPat.executeQuery()) {
                            if (rsPat.next()) {
                                patient = new Patient(
                                    idPatient,
                                    rsPat.getString("nom"),
                                    rsPat.getString("prenom"),
                                    rsPat.getString("telephone"),
                                    rsPat.getString("adresse")
                                );
                            }
                        }
                    }

                    // Charger le créneau
                    Disponibilite dispo = null;
                    if (idDispo > 0) {
                        String sqlDispo = "SELECT * FROM Disponibilites WHERE idDispo = ?";
                        try (PreparedStatement psDispo = conn.prepareStatement(sqlDispo)) {
                            psDispo.setInt(1, idDispo);
                            try (ResultSet rsDispo = psDispo.executeQuery()) {
                                if (rsDispo.next()) {
                                    Disponibilite.Jour jour = Disponibilite.Jour.valueOf(rsDispo.getString("jour"));
                                    String hDebut = rsDispo.getString("heureDebut");
                                    String hFin = rsDispo.getString("heureFin");
                                    boolean estRes = rsDispo.getInt("estReservee") == 1;

                                    dispo = new Disponibilite(jour, hDebut, hFin);
                                    if (estRes) dispo.reserver();
                                }
                            }
                        }
                    }

                    // Construire la consultation
                    Consultation consultation = new Consultation(
                        idConsult,
                        service,
                        date,
                        patient,
                        this,
                        dispo
                    );

                    consultations.add(consultation);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return consultations;
    }


}
