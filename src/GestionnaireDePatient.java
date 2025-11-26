import java.util.Date;
import java.util.List;

public class GestionnaireDePatient {
    public Patient creerPatient(String nom, String prenoms, String telephone,
                                String sexe, String motDePasse, String droitsAcces,
                                Date dateDerniereConsultation) {
        Patient patient = new Patient(nom, prenoms, telephone);
        patient.setSexe(sexe);
        patient.setMotDePasse(motDePasse);
        patient.setDateDerniereConsultation(dateDerniereConsultation);
        DossierMedical dossier = creerDossier(0, null, "", null);
        patient.setDossierMedical(dossier);
        return patient;
    }

    public DossierMedical creerDossier(int nbConsultations,
                                       List<Consultation> consultations,
                                       String contenu,
                                       List<String> antecedants) {
        DossierMedical dossier = new DossierMedical();
        dossier.setNbConsultations(nbConsultations);
        dossier.setConsultations(consultations);
        dossier.setContenu(contenu);
        dossier.setAntecedants(antecedants);
        return dossier;
    }
}