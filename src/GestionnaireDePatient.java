import java.util.Scanner;
import java.io.Console;


public class GestionnaireDePatient extends Professionnel_de_Sante {

    public GestionnaireDePatient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(nom, prenom, telephone, motDePasse, adresse, "Gestionnaire de Patient");
        
    }

    public Patient creerPatient() {
        Scanner sc = new Scanner(System.in);
        Console console = System.console();
        if (console == null) {
            System.out.println(" Console non disponible. Impossible de sécuriser la saisie du mot de passe.");
            return null;
        }



        System.out.print("Entrez le nom : ");
        String nom = sc.nextLine();

        System.out.print("Entrez les prénoms : ");
        String prenoms = sc.nextLine();

        System.out.print("Entrez le téléphone : ");
        String telephone = sc.nextLine();

    String motDePasse;
    while (true) {
        System.out.print("Entrez le mot de passe : ");
        motDePasse = new String(console.readPassword());

        System.out.print("Confirmez le mot de passe : ");
        String confirmation = new String(console.readPassword());

        if (motDePasse.equals(confirmation)) {
            break;
        }

        System.out.println(" Les mots de passe ne correspondent pas. Veuillez réessayer.\n");
    }

        System.out.print("Entrez l'adresse : ");
        String adresse = sc.nextLine();

        Patient patient = new Patient(nom, prenoms, telephone, motDePasse, adresse);
        System.out.println(" Patient créé avec succès : " + patient.getNom() + " " + patient.getPrenom());
        return patient;
    }

}