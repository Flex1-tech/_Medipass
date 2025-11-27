import java.util.Date;
import java.util.List;

public class GestionnaireDePatient extends Professionnel_de_Sante {
    
    public GestionnaireDePatient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(nom, prenom, telephone, motDePasse, adresse, "Gestionnaire de Patient");
        
    }

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


}