import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public abstract class User {
    protected static final String url = "jdbc:sqlite:data/medipass.db";
    protected int idUser;
    private static int compteur = 0;
    protected String nom;
    protected String prenom;
    protected boolean estMale;
    protected String telephone;
    protected String adresse;
    protected String motDePasse;

    public static User seConnecter(String telephone, String motDePasse) {
        try (Connection conn = DriverManager.getConnection(url)) {
        String query = """
                SELECT u.idUser, u.nom, u.prenom, u.adresse, u.motDePasse, u.typeUser
                FROM Users u
                WHERE u.telephone = ?
                """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, telephone);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Utilisateur non trouvé !");
                        return null;
                    }

                    String hash = rs.getString("motDePasse");
                    if (!BCrypt.checkpw(motDePasse, hash)) {
                        System.out.println("Mot de passe incorrect !");
                        return null;
                    }

                    int idUser = rs.getInt("idUser");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String adresse = rs.getString("adresse");
                    String typeUser = rs.getString("typeUser");

                    switch (typeUser) {
                        case "admin":
                            return new Administrateur(idUser, nom, prenom, telephone, adresse);

                        case "patient":
                            // Charger la date de dernière consultation
                            LocalDate dateDerniere = null;
                            String sqlDate = "SELECT dateDerniereConsultation FROM Patients WHERE idPatient = ?";
                            try (PreparedStatement psDate = conn.prepareStatement(sqlDate)) {
                                psDate.setInt(1, idUser);
                                try (ResultSet rsDate = psDate.executeQuery()) {
                                    if (rsDate.next()) {
                                        String dateStr = rsDate.getString("dateDerniereConsultation");
                                        if (dateStr != null) {
                                            dateDerniere = LocalDate.parse(dateStr);
                                        }
                                    }
                                }
                            }
                            
                            // Charger le dossier médical
                            Patient patient = new Patient(idUser, nom, prenom, telephone, adresse,new DossierMedical(), dateDerniere);
                            DossierMedical dossier = patient.getDossierMedical();
                            
                            // Charger antécédents
                            String sqlAnte = "SELECT texte FROM Antecedents WHERE idDossier = (SELECT idDossier FROM DossierMedical WHERE idPatient = ?)";
                            try (PreparedStatement psAnte = conn.prepareStatement(sqlAnte)) {
                                psAnte.setInt(1, idUser);
                                try (ResultSet rsAnte = psAnte.executeQuery()) {
                                    while (rsAnte.next()) {
                                        dossier.ajouterAntecedant(rsAnte.getString("texte"));
                                    }
                                }
                            }
                            // Charger consultations
                            String sqlCons = "SELECT * FROM Consultations WHERE idPatient = ?";
                            try (PreparedStatement psCons = conn.prepareStatement(sqlCons)) {
                                psCons.setInt(1, idUser);
                                try (ResultSet rsCons = psCons.executeQuery()) {
                                    while (rsCons.next()) {
                                        int idCons = rsCons.getInt("idConsultation");
                                        String service = rsCons.getString("service");
                                        String dateStr = rsCons.getString("datePrevue");
                                        int idDispo = rsCons.getInt("idDispo");
                                        LocalDate datePrevue = LocalDate.parse(dateStr);
                                        
                                        int idPro = rsCons.getInt("idPro");
                                        Professionnel_de_Sante pro = null;
                                        
                                        // Charger le professionnel correspondant
                                        String sqlPro = "SELECT U.nom, U.prenom, U.telephone, U.adresse, P.titre FROM Users U JOIN ProfessionnelSante P ON U.idUser = P.idPro WHERE U.idUser = ?";
                                        try (PreparedStatement psPro = conn.prepareStatement(sqlPro)) {
                                            psPro.setInt(1, idPro);
                                            try (ResultSet rsPro = psPro.executeQuery()) {
                                                if (rsPro.next()) {
                                                    pro = new Professionnel_de_Sante(
                                                        idPro,
                                                        rsPro.getString("nom"),
                                                        rsPro.getString("prenom"),
                                                        rsPro.getString("telephone"),
                                                        rsPro.getString("adresse"),
                                                        rsPro.getString("titre")
                                                    );
                                                }
                                            }
                                        }
                                        
                                        Disponibilite creneau = null;
                                        // Charger le créneau de disponibilité correspondant
                                        if (idDispo != 0) {
                                            String sqlDispo = "SELECT * FROM Disponibilites WHERE idDispo = ?";
                                            try (PreparedStatement psDispo = conn.prepareStatement(sqlDispo)) {
                                                psDispo.setInt(1, idDispo);
                                                try (ResultSet rsDispo = psDispo.executeQuery()) {
                                                    if (rsDispo.next()) {
                                                        Disponibilite.Jour jour = Disponibilite.Jour.valueOf(rsDispo.getString("jour"));
                                                        String hDebut = rsDispo.getString("heureDebut");
                                                        String hFin = rsDispo.getString("heureFin");
                                                        boolean estRes = rsDispo.getInt("estReservee") == 1;
                                                        creneau = new Disponibilite(jour, hDebut, hFin);
                                                        if (estRes) creneau.reserver();
                                                    }
                                                }
                                            }
                                        }

                                        if (pro != null) {
                                            Consultation cons = new Consultation(idCons, service, datePrevue, patient, pro, creneau);
                                            dossier.ajouterConsultation(cons);
                                        }
                                    }
                                }
                            }

                            return patient;

                        case "pro":
                            String sqlTitre = "SELECT titre FROM ProfessionnelSante WHERE idPro = ?";
                            String titre = null;
                            try (PreparedStatement psTitre = conn.prepareStatement(sqlTitre)) {
                                psTitre.setInt(1, idUser);
                                try (ResultSet rsTitre = psTitre.executeQuery()) {
                                    if (rsTitre.next()) {
                                        titre = rsTitre.getString("titre");
                                    }
                                }
                            }
                            if ("Gestionnaire de Patient".equalsIgnoreCase(titre)) {
                                GestionnaireDePatient gestionnaire =new GestionnaireDePatient(idUser,nom, prenom, telephone,adresse);
                                
                                chargerDisponibilites(conn, gestionnaire, idUser);
                                return gestionnaire;
                            }

                            Professionnel_de_Sante pro = new Professionnel_de_Sante(idUser, nom, prenom, telephone, adresse, titre);

                            chargerDisponibilites(conn, pro, idUser);                            
                            return pro;

                        default:
                            System.out.println("Type d'utilisateur inconnu.");
                            return null;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void chargerDisponibilites(Connection conn, Professionnel_de_Sante pro, int idUser) 
        throws SQLException {

        String sqlDispo = "SELECT * FROM Disponibilites WHERE idPro = ?";
        try (PreparedStatement psDispo = conn.prepareStatement(sqlDispo)) {
            psDispo.setInt(1, idUser);
            try (ResultSet rsDispo = psDispo.executeQuery()) {
                while (rsDispo.next()) {
                    Disponibilite.Jour jour = Disponibilite.Jour.valueOf(rsDispo.getString("jour"));
                    String hDebut = rsDispo.getString("heureDebut");
                    String hFin = rsDispo.getString("heureFin");
                    boolean estRes = rsDispo.getInt("estReservee") == 1;

                    Disponibilite dispo = new Disponibilite(jour, hDebut, hFin);
                    if (estRes) dispo.reserver();
                    pro.ajouter_Disponibilite(dispo);
                }
            }
        }
    }


    public User() {
    this.idUser = ++compteur;  // Attribuer un ID unique à chaque utilisateur
    }
    
    public int getIdUser() {
        return idUser;
    }

    public String getNom() {
        return nom;
    }
    protected void setNom(String nom) {
        this.nom = nom;
    }
    public boolean getEstMale() {
        return estMale;
    }
    protected void setEstMale(boolean estMale) {
        this.estMale = estMale;
    }
    public String getAdresse() {
        return adresse;
    }
    protected void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getPrenom() {
        return prenom;
    }
    protected void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getTelephone() {
        return telephone;
    }
    protected void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    protected void setMotDePasse(String motDePasse) {
        this.motDePasse = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }

    public boolean exists(Connection conn) {
        String sql = "SELECT idUser FROM Users WHERE telephone = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.telephone);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.idUser = rs.getInt("idUser");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


        
} 