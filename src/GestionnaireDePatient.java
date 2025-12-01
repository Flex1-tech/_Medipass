public class GestionnaireDePatient extends Professionnel_de_Sante {

    public GestionnaireDePatient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(nom, prenom, telephone, motDePasse, adresse, "Gestionnaire de Patient");
    }

    public GestionnaireDePatient(int idUser, String nom, String prenom, String telephone, String adresse) {
        super(idUser,nom,prenom,telephone,adresse,"Gestionnaire de Patient");

    }

    public void creerPatient( String nom, String prenoms, String telephone, String motDePasse, String adresse) {
        Patient patient = new Patient(nom, prenoms, telephone, motDePasse, adresse);
        patient.save();
        System.out.println(" Patient créé avec succès : " + patient.getNom() + " " + patient.getPrenom());
        return;
    }

}