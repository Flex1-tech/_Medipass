
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Archive {

    public static void main(String[] args) {

        ArchivageDossierMedical dossierArchiver = new ArchivageDossierMedical();

        List<DossierMedical.Consultation> consultations1 = new ArrayList<>();
        DossierMedical dossier1 = new DossierMedical(1, 70.5f, 0, consultations1, "Dossier IRIS");

        dossier1.ajouterInformation("Allergie aux pénicillines");
        dossier1.afficherContenu();

        DossierMedical.Antecedant antecedant1 = new DossierMedical.Antecedant(
                1,
                DossierMedical.Antecedant.Type.CHIRURGIE,
                "Crise cardiaque",
                new DatePerso(5, 11, 2003, 0, 0)
        );

        System.out.println(antecedant1.toString());

        List<DossierMedical.Consultation> consultations2 = new ArrayList<>();
        DossierMedical dossier2 = new DossierMedical(2, 60.0f, 0, consultations2, "Dossier BERNICE");

        dossierArchiver.ajouter(
                1, "ADE", "IRIS",
                new DatePerso(19, 1, 2009, 11, 2),
                new DatePerso(9, 1, 2040, 11, 2),
                "transfert",
                "inactive",
                dossier1
        );

        dossierArchiver.ajouter(
                2, "AGIE", "BERNICE",
                new DatePerso(29, 1, 2002, 11, 2),
                new DatePerso(9, 1, 2040, 11, 2),
                "transfert",
                "inactive",
                dossier2
        );

        List<DossierMedical.Consultation> consultations3 = new ArrayList<>();
        DossierMedical dossier3 = new DossierMedical(3, 82.0f, 0, consultations3, "Dossier DAMIEN");

        dossierArchiver.ajouter(
                3, "BOKO", "DAMIEN",
                new DatePerso(9, 1, 2001, 11, 2),
                new DatePerso(9, 1, 2040, 11, 2),
                "transfert",
                "inactive",
                dossier3
        );

        List<DossierMedical.Consultation> consultations4 = new ArrayList<>();
        DossierMedical dossier4 = new DossierMedical(4, 55.0f, 0, consultations4, "Dossier IRIS");

        dossierArchiver.ajouter(
                4, "ABBE", "IRIS",
                new DatePerso(1, 1, 2005, 11, 2),
                new DatePerso(9, 1, 2040, 11, 2),
                "transfert",
                "inactive",
                dossier4
        );

        List<DossierMedical.Consultation> consultations5 = new ArrayList<>();
        DossierMedical dossier5 = new DossierMedical(5, 65.0f, 0, consultations5, "Dossier INES");

        dossierArchiver.ajouter(
                5, "ATHAOUE", "INES",
                new DatePerso(9, 1, 2003, 11, 2),
                new DatePerso(9, 1, 2040, 11, 2),
                "transfert",
                "inactive",
                dossier5
        );

    }

    static class Patient {
        private int idUser;
        private String nom;
        private String prenoms;
        private String sexe;
        private int telephone;
        private String motDePasse;
        private String droitsAcces;
        private DatePerso dateDerniereConsultation;

        public Patient(int idUser, String nom, String prenoms, String sexe, int telephone,
                       String motDePasse, String droitsAcces, DatePerso dateDerniereConsultation) {
            this.idUser = idUser;
            this.nom = nom;
            this.prenoms = prenoms;
            this.sexe = sexe;
            this.telephone = telephone;
            this.motDePasse = motDePasse;
            this.droitsAcces = droitsAcces;
            this.dateDerniereConsultation = dateDerniereConsultation;
        }

        public int getIdUser() { return idUser; }
        public String getNom() { return nom; }
        public String getPrenoms() { return prenoms; }
        public String getSexe() { return sexe; }
        public int getTelephone() { return telephone; }
        public String getMotDePasse() { return motDePasse; }
        public String getDroitsAcces() { return droitsAcces; }
        public DatePerso getDateDerniereConsultation() { return dateDerniereConsultation; }
    }

    static class ArchivageDossierMedical {
        int idDossierArchiver;
        String nom;
        String prenom;
        DatePerso dateDeNaissance;
        DatePerso dateArchivage;
        String motifArchivage;
        String etatDuDossier;
        DossierMedical dossierMedicalArchiver;

        // Liste pour gérer plusieurs dossiers
        List<DossierMedical> archives = new ArrayList<>();

        public ArchivageDossierMedical() { }

        public ArchivageDossierMedical(int idDossierArchiver, String nom, String prenom,
                                       DatePerso dateDeNaissance, DatePerso dateArchivage,
                                       String motifArchivage, String etatDuDossier,
                                       DossierMedical dossierMedicalArchiver) {
            this.idDossierArchiver = idDossierArchiver;
            this.nom = nom;
            this.prenom = prenom;
            this.dateDeNaissance = dateDeNaissance;
            this.dateArchivage = dateArchivage;
            this.motifArchivage = motifArchivage;
            this.etatDuDossier = etatDuDossier;
            this.dossierMedicalArchiver = dossierMedicalArchiver;
        }

        // Méthode pour ajouter un dossier à la liste
        public void ajouter(int id, String nom, String prenom, DatePerso dateNaissance,
                            DatePerso dateArchivage, String motif, String etat,
                            DossierMedical dossier) {
            ArchivageDossierMedical ad = new ArchivageDossierMedical(id, nom, prenom, dateNaissance,
                    dateArchivage, motif, etat, dossier);
            archives.add(dossier);
        }

        public void afficherArchives() {
            if (archives.isEmpty()) {
                System.out.println("Aucun dossier archivé.");
            } else {
                for (DossierMedical d : archives) {
                    System.out.println(d.getContenu());
                }
            }
        }
    }

    static class DatePerso {
        private int jour;
        private int mois;
        private int annee;
        private int heure;
        private int minute;

        public DatePerso(int jour, int mois, int annee, int heure, int minute) {
            this.jour = jour;
            this.mois = mois;
            this.annee = annee;
            this.heure = heure;
            this.minute = minute;
        }

        public String getDatePerso() {
            return jour + "/" + mois + "/" + annee + " " + heure + ":" + minute;
        }
    }

    static class DossierMedical {

        private int idDossier;
        private float poids;
        private int nbConsultations;
        private List<Consultation> consultations;
        private String contenu;

        public DossierMedical(int idDossier, float poids, int nbConsultations,
                              List<Consultation> consultations, String contenu) {
            this.idDossier = idDossier;
            this.poids = poids;
            this.nbConsultations = nbConsultations;
            this.consultations = consultations;
            this.contenu = contenu;
        }

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

        public int getIdDossier() { return idDossier; }
        public float getPoids() { return poids; }
        public int getNbConsultations() { return nbConsultations; }
        public List<Consultation> getConsultations() { return consultations; }
        public String getContenu() { return contenu; }

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

            public int getIdConsultation() { return idConsultation; }
            public DatePerso getPrevuPour() { return prevuPour; }
            public boolean getEstPasse() { return estPasse; }
            public boolean getAEteFait() { return aEteFait; }
        }

        static class Antecedant {
            public enum Type {
                PATHOLOGIE, CHIRURGIE, ALLERGIE, TRAITEMENT, AUTRE
            }

            private int idAntecedant;
            private Type type;
            private String description;
            private DatePerso date;

            public Antecedant(int idAntecedant, Type type, String description, DatePerso date) {
                this.idAntecedant = idAntecedant;
                this.type = type;
                this.description = description;
                this.date = date;
            }

            public int getIdAntecedant() { return idAntecedant; }
            public Type getType() { return type; }
            public String getDescription() { return description; }
            public DatePerso getDate() { return date; }

            @Override
            public String toString() {
                return type + " : " + description + (date != null ? (" (" + date.getDatePerso() + ")") : "");
            }
        }
    }
}
