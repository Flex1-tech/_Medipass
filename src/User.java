import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class User {
    protected int idUser;
    private static int compteur = 0;
    protected String nom;
    protected String prenom;
    protected boolean estMale;
    protected String telephone;
    protected String adresse;
    protected String motDePasse;
    protected String droitsAcces;

    public abstract boolean seConnecter();
    public abstract void seDeconnecter();
    
    public int get_IdUser() { return idUser; }
    protected void set_IdUser(int idUser) { this.idUser = idUser; }
    public String get_Nom() { return nom; }
    protected void set_Nom(String nom) { this.nom = nom; }
    public boolean get_EstMale() { return estMale; }
    protected void set_EstMale(boolean estMale) { this.estMale = estMale; }
    public String get_Adresse() { return adresse; }
    protected void set_Adresse(String adresse) { this.adresse = adresse; }
    public String get_Prenom() { return prenom; }
    protected void set_Prenom(String prenom) { this.prenom = prenom; }
    public String get_Telephone() { return telephone; }
    protected void set_Telephone(String telephone) { this.telephone = telephone; }
    public String get_DroitsAcces() { return droitsAcces; }
    protected void set_DroitsAcces(String droitsAcces) { this.droitsAcces = droitsAcces; }
    protected void set_MotDePasse(String motDePasse) { this.motDePasse = hash(motDePasse); }
    protected boolean verif_MotDePasse(String motDePasse) { return hash(motDePasse).equals(this.motDePasse); }
    
    private static String hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(s == null ? new byte[0] : s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                String h = Integer.toHexString(b & 0xff);
                if (h.length() == 1) sb.append('0');
                sb.append(h);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return s == null ? "" : s;
        }
    }
    

}