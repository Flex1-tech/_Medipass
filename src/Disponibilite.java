import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Disponibilite {
    
    // les jours de la semaine
    public enum Jour {
        LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE
    }
    private int idDispo;
    private Jour jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private boolean estReservee = false;
    
    // constructeur avec des strings pour les heures
    public Disponibilite(Jour jour, String heureDebut, String heureFin) {
        LocalTime hDebut = LocalTime.parse(heureDebut);
        LocalTime hFin = LocalTime.parse(heureFin);
        if (hDebut.isAfter(hFin) || hDebut.equals(hFin)) {
        throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
        }
        this.jour = jour;
        this.heureDebut = hDebut;
        this.heureFin = hFin;
    }
    
    // constructeur avec des LocalTime directement
    public Disponibilite(Jour jour, LocalTime heureDebut, LocalTime heureFin) {
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;

        
        if (this.heureDebut.isAfter(this.heureFin)) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin !");
        }
    }
    
    public Disponibilite(int idDispo) {
        this.idDispo = idDispo;
    }

    // getters
    public Jour getJour() {
        return jour;
    }
    
    public LocalTime getHeureDebut() {
        return heureDebut;
    }
    
    public LocalTime getHeureFin() {
        return heureFin;
    }

    public boolean getEstReservee() {
        return estReservee;
    }

    public int getIdDispo() {
        return idDispo;
    }
    
    
    // setters
    public void setJour(Jour jour) {
        this.jour = jour;
    }
    
    public void setIdDispo(int idDispo) {
        this.idDispo = idDispo;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        if (heureDebut.isAfter(this.heureFin)) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin !");
        }
        this.heureDebut = heureDebut;
    }
    
    public void setHeureFin(LocalTime heureFin) {
        if (this.heureDebut.isAfter(heureFin)) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin !");
        }
        this.heureFin = heureFin;
    }
    
    // verifie si on est disponible a une heure donnee
    public boolean estDisponibleA(LocalTime heure) {
        return !heure.isBefore(heureDebut) && !heure.isAfter(heureFin);
    }
    
    // verifie si deux disponibilites se chevauchent
    public boolean chevauche(Disponibilite autre) {
        // si c'est pas le meme jour, pas de chevauchement
        if (this.jour != autre.jour) {
            return false;
        }
        
        // on verifie si les horaires se chevauchent
    return ((this.jour == autre.jour) && this.heureDebut.isBefore(autre.heureFin) && autre.heureDebut.isBefore(this.heureFin));
    }
    
    
    // affiche la disponibilite
    public void afficher() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println(jour + " de " + heureDebut.format(formatter) + 
                          " à " + heureFin.format(formatter));
    }

    public void reserver() {
        if (estReservee) throw new IllegalStateException("Créneau déjà réservé !");
        estReservee = true;
    }
    
    public void liberer() {
        estReservee = false;
    }

    public boolean save(Connection conn, int idPro) throws SQLException {
        if (conn == null) conn = DriverManager.getConnection(User.url);

        if (this.idDispo > 0) { 
            // Mise à jour
            String sql = "UPDATE Disponibilites SET estReservee=? WHERE idDispo=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, estReservee ? 1 : 0);
                ps.setInt(2, this.idDispo);
                ps.executeUpdate();
            }
        } else {
            // Insertion
            String sql = "INSERT INTO Disponibilites (idPro, jour, heureDebut, heureFin, estReservee) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idPro);
                ps.setString(2, jour.toString());
                ps.setString(3, heureDebut.toString());
                ps.setString(4, heureFin.toString());
                ps.setInt(5, estReservee ? 1 : 0);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) this.idDispo = rs.getInt(1);
                }
            }
        }
        return true;
    }

    public boolean update(Connection conn) {
        String sql = "UPDATE Disponibilites SET jour = ?, heureDebut = ?, heureFin = ?, estReservee = ? WHERE idDispo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jour.toString());
            pstmt.setString(2, heureDebut.toString());
            pstmt.setString(3, heureFin.toString());
            pstmt.setInt(4, estReservee ? 1 : 0);
            pstmt.setInt(5, idDispo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String statut = estReservee ? " (réservé)" : " (libre)";
        
        return jour + " : " 
            + heureDebut.format(formatter) 
            + " - " 
            + heureFin.format(formatter)
            + statut;
    }


    public boolean delete(Connection conn) {
        if (idDispo <= 0) return false;
        if (estReservee) {
            throw new IllegalStateException("Impossible de supprimer : créneau réservé.");
    }

        String sql = "DELETE FROM Disponibilites WHERE idDispo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idDispo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Disponibilite)) return false;
        Disponibilite other = (Disponibilite) obj;
        return this.idDispo == other.idDispo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDispo);
    }



}