import java.io.Console;
import java.util.Scanner;

public class Interface {
    public static int accueil(){
        System.out.println("=====Bienvenue sur Medipass=====\n");
        System.out.println("Veuillez declarer votre identité : ");
        System.out.println("1. Patient");
        System.out.println("2. Professionnel de santé");
        System.out.println("3. Gestionnaire de patient");
        System.out.println("4. Administrateur");
        System.out.println("5. Quitter");

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
                    } else if (user instanceof Professionnel_de_Sante) {
                        menuPro((Professionnel_de_Sante) user);
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
        System.out.println("=== Menu Patient === \n");
        // options: voir dossier, rendez-vous, etc.
    }

    private static void menuPro(Professionnel_de_Sante pro) {
        System.out.println("=== Menu Professionnel de Santé === \n");
        // options: créer consultations, gérer patients, disponibilités, etc.
    }

    private static void menuGestionnaire(GestionnaireDePatient gestionnaire) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Menu Gestionnaire de Patient === \n");
        System.out.print("Voulez-vous créer un patient ? (O/N) : ");
        
        if (sc.nextLine().equalsIgnoreCase("O")) {
            Console console = System.console();
            if (console == null) {
                System.out.println("Console non disponible. Impossible de sécuriser la saisie du mot de passe.");
                return;
            }

            System.out.println("=== Création d'un nouveau patient ===");
            System.out.println("Nb : La création d'un patient crée aussi un dossier médical vierge.\n");

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


            gestionnaire.creerPatient(nom, prenoms, telephone, motDePasse, adresse);
        } else {
            System.out.println("Retour au menu principal.");
        }
    }

    public static int menuAdministrateur(Administrateur admin) {
        Scanner scanner=new Scanner(System.in);
        
        while(true) {
        	System.out.println("=== Menu Administrateur ===");
        	System.out.println("1.Créer un utilisateur");
        	System.out.println("2.Modifier les informations d'un utilisateur");
        	System.out.println("3.Supprimer un utilisateur");
        	System.out.println("4.Afficher les statistiques du système");
        	System.out.println("5.Déconnexion");
        	System.out.print("Choix: ");
        	
        	try {
        		int choix=Integer.parseInt(scanner.nextLine());
        		if(choix>=1 && choix<=5) {
        			return choix;
        		}
        		else System.out.println("Choix invalide, veuillez entrer un nombre entre 1 et 5");
        	}
        	catch(NumberFormatException e) {
        		System.out.println("Erreur: veuillez entrer un nombre valide.");
        	}
        	
        }
        
    }
    
    public static void startAdministrateur(Administrateur admin) {
    	while(true) {
    		int choix=menuAdministrateur(admin);
    		switch(choix) {
    		    case 1:
    		    	try {
    		    		admin.creerUtilisateur();
    		    	} catch(Exception e) {
    		    		System.out.println("Erreur lors de la création de l'utilisateur");
    		    	}
    		    	break;
    		    	
    		    case 2:
    		    	try {
    		    		admin.modifierInfosUtilisateur();
    		    	} catch(Exception e) {
    		    		System.out.println("Erreur lors de la modification des informations de l'utilisateur");
    		    	}
    		    	break;
    		    	
    		    case 3:
    		    	try {
    		    		admin.supprimerUtilisateur();
    		    	} catch(Exception e) {
    		    		System.out.println("Erreur lors de la suppression de l'utilisateur");
    		    	}
    		    	break;
    		    	
    		    case 4:
    		    	try {
    		    		admin.afficherStatsSysteme();
    		    	} catch(Exception e) {
    		    		System.out.println("Erreur lors de l'affichage des statistiques du système");
    		    	}
    		    	break;
    		    	
    		    case 5: System.out.println("Déconnexion réussie.Retour à l'accueil...");
    		            start();
    		            return;
    		}
    		
    	}
    	
    }


}
