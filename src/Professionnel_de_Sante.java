import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public boolean supprimer_Disponibilite(Connection conn, Disponibilite dispo) {
        if (dispo.getIdDispo() > 0) {
            dispo.delete(conn);   // Supprime dans la BDD
        }
        return this.disponibilites.remove(dispo); // Supprime localement
    }


    public void modifier_Disponibilite(Disponibilite ancienneDisponibilite, Disponibilite nouvelleDisponibilite) {
        int index = this.disponibilites.indexOf(ancienneDisponibilite);
        if (index != -1) {
            this.disponibilites.set(index, nouvelleDisponibilite);
        }
    }

    public String afficher_Disponibilites() {
        StringBuilder sb = new StringBuilder();
        for (Disponibilite disponibilite : disponibilites) {
            sb.append(disponibilite.toString()).append("\n");
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

            String sqlUser = "UPDATE Users SET nom=?, prenom=?, adresse=?, motDePasse=? WHERE idUser=?";
            pstmtUser = conn.prepareStatement(sqlUser);
            pstmtUser.setString(1, nom);
            pstmtUser.setString(2, prenom);
            pstmtUser.setString(3, adresse);
            pstmtUser.setString(4, motDePasse);
            pstmtUser.setInt(5, idUser);
            pstmtUser.executeUpdate();

            String sqlPro = "UPDATE ProfessionnelSante SET titre=? WHERE idPro=?";
            pstmtPro = conn.prepareStatement(sqlPro);
            pstmtPro.setString(1, titre);
            pstmtPro.setInt(2, idUser);
            pstmtPro.executeUpdate();

        } else {

            String sqlUser = "INSERT INTO Users (nom, prenom, telephone, adresse, motDePasse, typeUser)"
                    + " VALUES (?, ?, ?, ?, ?, 'pro')";
            pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            pstmtUser.setString(1, nom);
            pstmtUser.setString(2, prenom);
            pstmtUser.setString(3, telephone);
            pstmtUser.setString(4, adresse);
            pstmtUser.setString(5, motDePasse);
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

}
