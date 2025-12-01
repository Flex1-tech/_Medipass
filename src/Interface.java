import java.io.Console;
import java.sql.SQLException;
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
                    }
                    else if (user instanceof GestionnaireDePatient) {
                        menuGestionnaire((GestionnaireDePatient) user);
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
        Scanner scanner = new Scanner(System.in);
        boolean quitterMenu = false;

        System.out.println("=== Menu Professionnel de Santé ===\n");
        System.out.println("Bienvenue, " + pro.getNom() + " " + pro.getPrenom() + "!\n");

        while (!quitterMenu) {
            System.out.println("Veuillez choisir votre action : ");
            System.out.println("1. Voir les consultations prévues");
            System.out.println("2. Gérer les disponibilités");
            System.out.println("3. Déconnexion");
            System.out.print("Votre choix : ");

            String input = scanner.nextLine().trim();
            switch (input) {

                case "1":
                    pro.afficherConsultationsPrevues();
                    break;

                case "2":
                    boolean quitterGestion = false;
                    while (!quitterGestion) {
                        System.out.println("\n=== Gestion des disponibilités ===");
                        System.out.println("1. Ajouter une disponibilité");
                        System.out.println("2. Supprimer une disponibilité");
                        System.out.println("3. Libérer une disponibilité");
                        System.out.println("4. Quitter la gestion des disponibilités");
                        System.out.print("Votre choix : ");

                        String choixAction = scanner.nextLine().trim();
                        switch (choixAction) {

                            case "1": // Ajouter une disponibilité
                                System.out.println("Ajout d'une disponibilité");
                                Disponibilite.Jour jour = null;

                                while (jour == null) {
                                    System.out.println("Entrez le jour (LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE) ou X pour annuler :");
                                    String jourStr = scanner.nextLine().trim();

                                    if (jourStr.equalsIgnoreCase("X")) {
                                        System.out.println("Ajout annulé.");
                                        break;
                                    }

                                    try {
                                        jour = Disponibilite.Jour.valueOf(jourStr.toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("⚠ Jour invalide. Veuillez réessayer.");
                                    }
                                }

                                if (jour != null) { 
                                    String pattern = "^[0-2][0-9]:[0-5][0-9]$";
                                    String heureDebut;
                                    String heureFin;

                                    // Heure de début
                                    do {
                                        System.out.print("Entrez l'heure de début (format HH:MM) : ");
                                        heureDebut = scanner.nextLine().trim();
                                        if (!heureDebut.matches(pattern)) {
                                            System.out.println("Format invalide ! Exemple : 08:30");
                                        }
                                    } while (!heureDebut.matches(pattern));

                                    // Heure de fin
                                    do {
                                        System.out.print("Entrez l'heure de fin (format HH:MM) : ");
                                        heureFin = scanner.nextLine().trim();
                                        if (!heureFin.matches(pattern)) {
                                            System.out.println("Format invalide ! Exemple : 17:45");
                                        }
                                    } while (!heureFin.matches(pattern));

                                    try {
                                        Disponibilite nouvelleDispo = new Disponibilite(jour, heureDebut, heureFin);
                                        pro.ajouter_Disponibilite(nouvelleDispo);
                                        pro.save();
                                        System.out.println("Disponibilité ajoutée avec succès !");
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("⚠ Erreur lors de l'ajout : " + e.getMessage());
                                    }
                                }
                                break;

                            case "2": // Supprimer une disponibilité
                                System.out.println("Suppression d'une disponibilité");
                                System.out.println(pro.afficher_Disponibilites());
                                System.out.print("Entrez le numéro de la disponibilité à supprimer (ou X pour annuler) : ");
                                String inputSuppr = scanner.nextLine().trim();
                                if (inputSuppr.equalsIgnoreCase("X")) {
                                    System.out.println("Suppression annulée.");
                                    break;
                                }
                                try {
                                    int indexSuppr = Integer.parseInt(inputSuppr) - 1;
                                    Disponibilite DispSupp = pro.get_Disponibilites().get(indexSuppr);
                                    try {
                                        pro.supprimer_Disponibilite(null, DispSupp);
                                        pro.save();
                                        System.out.println("Disponibilité supprimée avec succès !");
                                    } catch (SQLException e) {
                                        System.out.println("Erreur SQL lors de la suppression : " + e.getMessage());
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("⚠ Entrée invalide. Veuillez entrer un numéro valide.");
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("⚠ Numéro invalide. Veuillez réessayer.");
                                }
                                break;

                            case "3": // Libérer une disponibilité
                                System.out.println("Libération d'une disponibilité");
                                System.out.println(pro.afficher_Disponibilites());
                                System.out.print("Entrez le numéro de la disponibilité à libérer (ou X pour annuler) : ");
                                String inputLiberer = scanner.nextLine().trim();
                                if (inputLiberer.equalsIgnoreCase("X")) {
                                    System.out.println("Libération annulée.");
                                    break;
                                }
                                try {
                                    int indexLiberer = Integer.parseInt(inputLiberer) - 1;
                                    Disponibilite DispLiberer = pro.get_Disponibilites().get(indexLiberer);
                                    DispLiberer.liberer();
                                    pro.save();
                                    System.out.println("Disponibilité libérée avec succès !");
                                } catch (NumberFormatException e) {
                                    System.out.println("⚠ Entrée invalide. Veuillez entrer un numéro valide.");
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("⚠ Numéro invalide. Veuillez réessayer.");
                                }
                                break;

                            case "4":
                                quitterGestion = true;
                                System.out.println("Quitter la gestion des disponibilités.");
                                break;

                            default:
                                System.out.println("Choix invalide, veuillez réessayer.");
                        }
                    }
                    break;

                case "3":
                    System.out.println("Déconnexion réussie. Retour à l'accueil...");
                    quitterMenu = true;
                    break;

                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
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
