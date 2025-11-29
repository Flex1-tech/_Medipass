
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class DossierMedical {
    
    private int idDossier;
    private float poids;
    private int nbConsultations;
    private List<Consultation> consultations;
    private String contenu;

    // Constructeur
    public DossierMedical(int idDossier, float poids, int nbConsultations,
                          List<Consultation> consultations, String contenu) {
        this.idDossier = idDossier;
        this.poids = poids;
        this.nbConsultations = nbConsultations;
        this.consultations = consultations;
        this.contenu = contenu;
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

    // Getters
    public int getIdDossier() {
        return idDossier;
    }

    public float getPoids() {
        return poids;
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

    // Méthode main
    public static void main(String[] args) {
        List<Consultation> consultations = new ArrayList<>();
        DossierMedical dossier = new DossierMedical(1, 70.5f, 0, consultations, "Dossier initial");
        
        dossier.ajouterInformation("Allergie aux pénicillines");
        dossier.afficherContenu();
        
        Antecedant antecedantDossierMedical = new Antecedant(1, Antecedant.Type.CHIRURGIE, 
                                                              "Crise cardiaque", 
                                                              LocalDate.of(2003, 11, 5));
        
        System.out.println(antecedantDossierMedical.toString());
    }   
    
    // Classe interne Consultation
    static class Consultation {
        private int idConsultation;
        private DatePerso prevuPour;
        private boolean estPasse;
        private boolean aEteFait;

        public Consultation(int idConsultation, DatePerso prevuPour, boolean estPasse, boolean aEteFait) {
            this.idConsultation = idConsultation;
            this.prevuPour = prevuPour;
            this.estPasse = estPasse;
            this.aEteFait = aEteFait;
        }
        
        public int getIdConsultation() {
            return idConsultation;
        }
        
        public DatePerso getPrevuPour() {
            return prevuPour;
        }
        
        public boolean getEstPasse() {
            return estPasse;
        }
        
        public boolean getAEteFait() {
            return aEteFait;
        }
    }
    
    // Classe interne DatePerso
    static class DatePerso {
        private int jour;
        private int mois;
        private int annee;
        
        public DatePerso(int jour, int mois, int annee) {
            this.jour = jour;
            this.mois = mois;
            this.annee = annee;
        }
        
        public int getJour() {
            return jour;
        }
        
        public int getMois() {
            return mois;
        }
        
        public int getAnnee() {
            return annee;
        }
        
        @Override
        public String toString() {
            return jour + "/" + mois + "/" + annee;
        }
    }
    
    // Classe interne Antecedant - AJOUT DE "static"
    static class Antecedant {

        public enum Type {
            PATHOLOGIE,
            CHIRURGIE,
            ALLERGIE,
            TRAITEMENT,
            AUTRE
        }

        private int idAntecedant;
        private Type type;
        private String description;
        private LocalDate date;

        public Antecedant(int idAntecedant, Type type, String description, LocalDate date) {
            this.idAntecedant = idAntecedant;
            this.type = type;
            this.description = description;
            this.date = date;
        }

        public int getIdAntecedant() {
            return idAntecedant;
        }

        public Type getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public String toString() {
            return (type + " : " + description + (date != null ? (" (" + date + ")") : ""));
        }
    }
}