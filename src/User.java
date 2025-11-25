import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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

    public boolean seConnecter() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez votre numéro de téléphone : ");
        String telephone = scanner.nextLine();

        System.out.print("Entrez votre mot de passe : ");
        String motDePasse = scanner.nextLine();

        String sql = "SELECT motDePasse FROM users WHERE telephone = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, telephone);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("motDePasse");
                    return BCrypt.checkpw(motDePasse, hash);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public User() {
    this.idUser = ++compteur;  // Attribuer un ID unique à chaque utilisateur
    }
    
    public int get_IdUser() {
        return idUser;
    }
    protected void set_IdUser(int idUser) {
        this.idUser = idUser;
    }
    public String get_Nom() {
        return nom;
    }
    protected void set_Nom(String nom) {
        this.nom = nom;
    }
    public boolean get_EstMale() {
        return estMale;
    }
    protected void set_EstMale(boolean estMale) {
        this.estMale = estMale;
    }
    public String get_Adresse() {
        return adresse;
    }
    protected void set_Adresse(String adresse) {
        this.adresse = adresse;
    }
    public String get_Prenom() {
        return prenom;
    }
    protected void set_Prenom(String prenom) {
        this.prenom = prenom;
    }
    public String get_Telephone() {
        return telephone;
    }
    protected void set_Telephone(String telephone) {
        this.telephone = telephone;
    }
    protected void set_MotDePasse(String motDePasse) {
        this.motDePasse = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }
    
} 