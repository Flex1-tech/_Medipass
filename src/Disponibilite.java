import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Disponibilite {
    
    // les jours de la semaine
    public enum Jour {
        LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE
    }
    
    private Jour jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    
    // constructeur avec des strings pour les heures
    public Disponibilite(Jour jour, String heureDebut, String heureFin) {
        this.jour = jour;
        this.heureDebut = LocalTime.parse(heureDebut);
        this.heureFin = LocalTime.parse(heureFin);
        
        // verification que l'heure de debut est avant l'heure de fin
        if (this.heureDebut.isAfter(this.heureFin)) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin !");
        }
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
    
    // setters
    public void setJour(Jour jour) {
        this.jour = jour;
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
        return !(this.heureFin.isBefore(autre.heureDebut) || 
                 this.heureFin.equals(autre.heureDebut) ||
                 this.heureDebut.isAfter(autre.heureFin) ||
                 this.heureDebut.equals(autre.heureFin));
    }
    
    // calcule la duree en minutes
    public long getDureeEnMinutes() {
        return java.time.Duration.between(heureDebut, heureFin).toMinutes();
    }
    
    // affiche la disponibilite
    public void afficher() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println(jour + " de " + heureDebut.format(formatter) + 
                          " à " + heureFin.format(formatter));
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return jour + " : " + heureDebut.format(formatter) + 
               " - " + heureFin.format(formatter);
    }
}