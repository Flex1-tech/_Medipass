import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            String query = "SELECT * FROM Users WHERE telephone = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, telephone);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("motDePasse");
                    if (!BCrypt.checkpw(motDePasse, hash)) {
                        System.out.println("Mot de passe incorrect !");
                        return null;
                    }

                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String adresse = rs.getString("adresse");
                    int idUser = rs.getInt("idUser");
                    String typeUser = rs.getString("typeUser");

                    switch (typeUser) {
                        case "admin":
                            return new Administrateur(idUser, nom, prenom, telephone, adresse);
                        case "patient":
                            return new Patient(idUser, nom, prenom, telephone, adresse);
                        case "pro":
                            String titre = rs.getString("titre");
                            return new Professionnel_de_Sante(idUser, nom, prenom, telephone, adresse, titre);
                        default:
                            System.out.println("Type d'utilisateur inconnu.");
                            return null;
                    }
                } else {
                    System.out.println("Utilisateur non trouvé !");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
    
} 