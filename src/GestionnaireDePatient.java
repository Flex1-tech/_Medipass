import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class GestionnaireDePatient {

    public GestionnaireDePatient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(nom, prenom, telephone, motDePasse, adresse, "Gestionnaire de Patient");
        
    }

    public Patient creerPatient(String nom, String prenoms, String telephone,String motDePasse, LocalDate dateDerniereConsultation) {
        Patient patient = new Patient(nom, prenoms, telephone, motDePasse, null);
        return patient;
    }

}