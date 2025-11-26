import java.util.Date;
import java.util.List;

public class GestionnaireDePatient {
    public Patient creerPatient(String nom, String prenoms, String telephone,
                                String motDePasse,
                                Date dateDerniereConsultation) {
        Patient patient = new Patient(nom, prenoms, telephone, motDePasse, null);
        return patient;
    }

    public DossierMedical creerDossier(int nbConsultations,
                                       List<Consultation> consultations,
                                       String contenu,
                                       List<String> antecedants) {
        DossierMedical dossier = new DossierMedical(0, 0f, nbConsultations, null, contenu);
        return dossier;
    }
}