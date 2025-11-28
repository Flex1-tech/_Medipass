package model;


public class GestionnaireDePatient extends Professionnel_de_Sante {

    public GestionnaireDePatient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(nom, prenom, telephone, motDePasse, adresse, "Gestionnaire de Patient");
        
    }

    public Patient creerPatient(String nom, String prenoms, String telephone,String motDePasse,String adresse) {
        Patient patient = new Patient(nom, prenoms, telephone, motDePasse, adresse);
        return patient;
    }
}