

public class GestionnaireDePatient extends Professionnel_de_Sante {

    public GestionnaireDePatient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(nom, prenom, telephone, motDePasse, adresse, "Gestionnaire de Patient");
        
    }

    public Patient creerPatient(String nom, String prenoms, String telephone,String motDePasse,String adresse) {
        Patient patient = new Patient(nom, prenoms, telephone, motDePasse, adresse);
        return patient;
    }

    public DossierMedical creerDossier(int nbConsultations,
                                       List<Consultation> consultations,
                                       String contenu,
                                       List<Antecedant> antecedants) {
        DossierMedical dossier = new DossierMedical(0, 0f, nbConsultations, consultations, contenu, antecedants);
        return dossier;
    }
}