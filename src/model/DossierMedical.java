package model;
import java.util.List;
import java.util.ArrayList;

public class DossierMedical {
    private static int compteur = 0;
    private int idDossier;
    private int nbConsultations = 0;
    private List<Consultation> consultations = new ArrayList<>();
    private List<String> antecedants = new ArrayList<>();

    // Constructeur
    public DossierMedical() {
        this.idDossier = ++compteur;
    }

    // Méthodes
    public void ajouterAntecedant(String antecedant) {
        this.antecedants.add(antecedant);
    }

    // Getters
    public int getIdDossier() {
        return idDossier;
    }

    public int getNbConsultations() {
        return nbConsultations;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public List<String> getAntecedants() {
        return antecedants;
    }

    public void ajouterConsultation(Consultation consultation) {
        this.consultations.add(consultation);
        this.nbConsultations++;
        
        // On ajoute le diagnostic dans les antecedants
        this.antecedants.add(consultation.getDiagnostic());
    }

}
