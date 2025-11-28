package model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Patient extends User {

    private DossierMedical dossierMedical;
    private LocalDate date_Dernière_Consultation;

    public Patient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
        this.dossierMedical = new DossierMedical();
    }

public Consultation programmer_Consultation(int indexPro, int indexDispo, String service, LocalDate date) {

    List<Professionnel_de_Sante> pros = Professionnel_de_Sante.getTousLesProfessionnels();

    if (pros == null || pros.isEmpty()) {
        return null; // Aucun professionnel disponible
    }

    if (indexPro < 0 || indexPro >= pros.size()) {
        return null; // Index professionnel invalide
    }

    Professionnel_de_Sante pro = pros.get(indexPro);
    List<Disponibilite> dispo = pro.get_Disponibilites();

    if (dispo == null || dispo.isEmpty()) {
        return null; // Pas de disponibilités
    }

    if (indexDispo < 0 || indexDispo >= dispo.size()) {
        return null; // Index disponibilité invalide
    }

    Disponibilite d = dispo.get(indexDispo);
    if (d.getEstReservee()) {
        return null; // Créneau déjà réservé
    }

    // Formatage de la date
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dateString = date.format(formatter);

    // Création de la consultation
    Consultation consultation = new Consultation(this, pro, service, dateString, d);

    // Réserver le créneau
    d.reserver();

    // Ajouter au dossier médical
    this.getDossierMedical().ajouterConsultation(consultation);

    // Mettre à jour la dernière consultation
    this.date_Dernière_Consultation = consultation.getDatePrevue();

    return consultation;
}
    public LocalDate get_Date_Dernière_Consultation() {
        return date_Dernière_Consultation;
    }

    public void set_Date_Dernière_Consultation(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.date_Dernière_Consultation = LocalDate.parse(date, formatter);
    }

    public String afficher_Date_Dernière_Consultation() {
        if (date_Dernière_Consultation == null) {
            return "Aucune consultation enregistrée.";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date_Dernière_Consultation.format(formatter);
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128); // capacité initiale
        sb.append("Nom: ").append(nom).append(", \n");
        sb.append("Prénom: ").append(prenom).append(", \n");
        sb.append("Date de la dernière consultation: ").append(afficher_Date_Dernière_Consultation()).append(", \n");

        return sb.toString();
    }
}
