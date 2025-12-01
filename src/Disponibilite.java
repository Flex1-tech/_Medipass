import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    public boolean save(Connection conn, int idPro) {
        String sql = "INSERT INTO Disponibilites (idPro, jour, heureDebut, heureFin, estReservee) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idPro);
            pstmt.setString(2, jour.toString());
            pstmt.setString(3, heureDebut.toString());
            pstmt.setString(4, heureFin.toString());
            pstmt.setInt(5, estReservee ? 1 : 0);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Échec de l'insertion : aucune ligne affectée.");
                return false;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.idDispo = generatedKeys.getInt(1);
                } else {
                    System.err.println("Échec de l'insertion : aucun ID généré.");
                    return false;
                }
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'insertion : " + e.getMessage());
            e.printStackTrace();
            return false;
            }
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

}