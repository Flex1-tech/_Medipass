import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Professionnel_de_Sante extends User {
    private String titre;
    
    private List<Disponibilite> disponibilites = new ArrayList<>();

    public String get_titre() {
        return this.titre;
    }

    public void set_titre(String titre) {
        this.titre = titre;
    }

    public Professionnel_de_Sante(String nom, String prenom, String telephone, String motDePasse, String adresse,
            String titre) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
        this.titre = titre;
    }

    public List<Disponibilite> get_Disponibilites() {
        return disponibilites;
    }

    public void supprimer_Disponibilite(Disponibilite disponibilite) {
        this.disponibilites.remove(disponibilite);
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

        String query = "SELECT * FROM ProfessionnelDeSante";
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Professionnel_de_Sante p = new Professionnel_de_Sante(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("mot_de_passe"),
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

}
