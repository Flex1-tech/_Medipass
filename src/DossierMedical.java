import java.util.List;
import java.util.ArrayList;

public class DossierMedical {
    
    private int idDossier;
    private int nbConsultations;
    private List<Consultation> consultations = new ArrayList<>();

    // Constructeur
    public DossierMedical(int idDossier, int nbConsultations,
                          List<Consultation> consultations, String contenu) {
        this.idDossier = idDossier;
        this.poids = poids;
        this.nbConsultations = nbConsultations;
        this.consultations = consultations;
        this.contenu = contenu;
        this.antecedants = antecedants != null ? antecedants : new ArrayList<>();
    }

    // Méthodes
    public void ajouterInformation(String info) {
        this.contenu += "\n" + info;
    }

    public void modifierInformation(String nouveauContenu) {
        this.contenu = nouveauContenu;
    }

    public void afficherContenu() {
        System.out.println("Contenu du dossier : ");
        System.out.println(this.contenu);
    }

    public void ajouterAntecedant(Antecedant antecedant) {
        if (this.antecedants == null) {
            this.antecedants = new ArrayList<>();
        }
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

    public String getContenu() {
        return contenu;
    }
}
