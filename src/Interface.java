import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class Interface {
    public static int accueil(){
        System.out.println("=====Bienvenue sur Medipass=====\n");
        System.out.println("Veuillez declarer votre identité : ");
        System.out.println("1. Patient");
        System.out.println("2. Professionnel de santé");
        System.out.println("3. Administrateur");
        System.out.println("4. Quitter");

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Votre choix : ");

            try {
                int choix = Integer.parseInt(scanner.nextLine());

                if (choix >= 1 && choix <= 4) {
                    return choix;  // choix valide
                } else {
                    System.out.println("Choix invalide, veuillez entrer un nombre entre 1 et 4.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Erreur : veuillez entrer un nombre valide.");
            }
        }
        
    }
    
    public static User connexion() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre numéro de téléphone : ");
        String telephone = scanner.nextLine();
        
        System.out.print("Entrez votre mot de passe : ");
        String motDePasse = scanner.nextLine();

        User userConnecte = User.seConnecter(telephone, motDePasse);
        if (userConnecte != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Échec de la connexion !");
        }
        return userConnecte;
    }
       

   
    public static void start() {
        int choix = accueil();
        switch (choix) {
            case 1: // Patient
            case 2: // Professionnel de santé
            case 3: // Administrateur
                User user = connexion();
                if (user != null) {
                    if (user instanceof Patient) {
                        menuPatient((Patient) user);
                    } else if (user instanceof ProfessionnelDeSante) {
                        menuPro((ProfessionnelDeSante) user);
                    } else if (user instanceof Administrateur) {
                        menuAdministrateur((Administrateur) user);
                    }
                } else {
                    System.out.println("Connexion échouée. Retour à l'accueil...");
                    start(); // relancer l'accueil
                }
                break;
            case 4:
                System.out.println("Merci d'avoir utilisé Medipass. Au revoir !");
                System.exit(0);
                break;
            default:
                System.out.println("Choix invalide.");
                start();
        }
    }


    private static void menuPatient(Patient patient) {
        System.out.println("=== Menu Patient ===");
        // options: voir dossier, rendez-vous, etc.
    }

    private static void menuPro(Professionnel_de_Sante pro) {
        System.out.println("=== Menu Professionnel de Santé ===");
        // options: créer consultations, gérer patients, disponibilités, etc.
    }

    private static void menuAdministrateur(Administrateur admin) {
        System.out.println("=== Menu Administrateur ===");
        // options: gérer utilisateurs, statistiques, etc.
    }


}
