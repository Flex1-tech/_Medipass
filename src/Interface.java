import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.List;

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
        Console console = System.console();
        String motDePasse;

        System.out.print("Entrez votre numéro de téléphone : ");
        String telephone = scanner.nextLine();

        System.out.print("Entrez votre mot de passe : ");

        if (console != null) {
            motDePasse = new String(console.readPassword());
        } else {
            motDePasse = scanner.nextLine();
        }

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
        User user = connexion();
        if (user != null){
            switch (choix) {
                case 1: // Patient
                    if (user instanceof Patient){
                        menuPatient((Patient) user);
                    }else{
                        System.out.println("Erreur : vous n'êtes pas un patient !");
                    }
                    break;
                case 2: // Professionnel de santé
                    if (user instanceof Professionnel_de_Sante){
                        boolean retourAccueil = menuPro((Professionnel_de_Sante) user);
                        if (retourAccueil) {
                            start();
                            return;
                        }
                    }else {
                        System.out.println("Erreur : vous n'êtes pas un professionnel de Santé !");
                    }
                    break;
                case 3:
                    if (user instanceof GestionnaireDePatient){
                        menuGestionnaire((GestionnaireDePatient) user);
                    }else{
                        System.out.println("Erreur : vous n'êtes pas un gestionnaire de patient !");
                    }
                    break;
                case 4: // Administrateur

                    if (user instanceof Administrateur) {
                        startAdministrateur((Administrateur) user);
                    }else {
                        System.err.println("Erreur : vous n'êtes pas un Administrateur !");
                    }
                    break;
                    case 5:
                        System.out.println("Merci d'avoir utilisé Medipass. Au revoir !");
                        System.exit(0);
                        break;
                        default:
                            System.out.println("Choix invalide.");
                            start();
                        }
                        
                }else {
                    System.out.println("Connexion échouée. Retour à l'accueil...");
                    start();
                    return; 
                }
    }


    private static void menuPatient(Patient patient) {
    	Scanner scanner = new Scanner(System.in);
    	
        //System.out.println("== Menu Patient === \n");
     	int choix = 0;
     	
     	while (choix != 3){
             System.out.println("\n=== Menu Patient ==="); 
             System.out.println("Bienvenue, " + patient.getNom() + " " + patient.getPrenom() + "!\n");
             System.out.println("1. Consulter mon dossier médical");
             System.out.println("2. Programmer une consultation");
             System.out.println("3. Exporter votre dossier medical");
             System.out.println("4. Déconnexion");
             
             while (true) {
                 System.out.print("Votre choix : ");

                 try {
                     choix = Integer.parseInt(scanner.nextLine());

                     if (choix >= 1 && choix <= 3) {
                         break;  // Sortie de la boucle , choix valide
                     } else {
                         System.out.println("Choix invalide, veuillez entrer un nombre entre 1 et 3.");
                     }
                     
                 } catch (NumberFormatException e) {
                     System.out.println("Erreur : veuillez entrer un nombre valide");
                 }
             }
             
             switch (choix) 
             {
             case 1:
                System.out.println("\n--- Programmation d'une Consultation ---");
                //Appelle de la méthode programmer consultation de l'ojet patient
                Consultation nouvelleConsultation = patient.programmer_Consultation(); 
                
                if(nouvelleConsultation == null) {
                    System.out.println("\n Programmation annulée ou impossible.");
                }
                break;
             case 2:
                programmerConsultation(patient); 
                break;
             case 3:
                patient.exporterDossierMedicalTXT();
                break;
             case 4:
                System.out.println("Déconnexion...");
                break; 
             default:
                break; 
             }
     	}
     	
     	start(); // Retour à l'accueil
     }

    

     private static void programmerConsultation(Patient patient) {
         
         System.out.println("\n--- Programmation d'une Consultation ---");
         //Appelle de la méthode programmer consultation de l'ojet patient
         Consultation nouvelleConsultation = patient.programmer_Consultation(); 
         
         if (nouvelleConsultation == null) {
             System.out.println("\n💢 Programmation annulée ou impossible.");
            }
         
     }

    private static boolean menuPro(Professionnel_de_Sante pro) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Menu Professionnel de Santé ===\n");
        System.out.println("Bienvenue, " + pro.getNom() + " " + pro.getPrenom() + "!\n");

        while (true) {

        System.out.println("Veuillez choisir votre action : ");
        System.out.println("1. Voir les consultations prévues");
        System.out.println("2. Gérer les disponibilités");
        System.out.println("3. Gérer / éditer une consultation");
        System.out.println("4. Afficher un dossier médical");
        System.out.println("5. Déconnexion");
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
                    pro.afficherConsultationsEditables();
                    Consultation cible = null;
                    System.out.print("Veuillez saisir l’ID de la consultation : ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine()); // nextLine pour éviter les problèmes de buffer
                        for (Consultation c : pro.getConsultationsEditables()) {
                            if (c.getIdConsultation() == id) {
                                cible = c;
                                break;
                            }
                        }

                        if (cible == null) {
                            System.out.println("Aucune consultation trouvée avec cet ID.");
                            break;
                        }

                        // --- Diagnostic ---
                        System.out.print("Veuillez entrer le diagnostic : ");
                        cible.setDiagnostic(scanner.nextLine().trim());

                        // --- Observations ---
                        cible.getObservations().clear();
                        System.out.println("Entrez les observations (tapez 'X' pour terminer) :");
                        while (true) {
                            String obs = scanner.nextLine().trim();
                            if (obs.equalsIgnoreCase("X")) break;
                            if (!obs.isEmpty()) cible.getObservations().add(obs);
                        }

                        // --- Prescriptions ---
                        cible.getPrescriptions().clear();
                        System.out.println("Entrez les prescriptions (tapez 'X' pour terminer) :");
                        while (true) {
                            System.out.print("Médicament (ou X pour terminer) : ");
                            String medicament = scanner.nextLine().trim();
                            if (medicament.equalsIgnoreCase("X")) break;

                            System.out.print("Posologie : ");
                            String posologie = scanner.nextLine().trim();

                            System.out.print("Durée : ");
                            String duree = scanner.nextLine().trim();

                            Prescription p = new Prescription(medicament, posologie, duree);
                            cible.getPrescriptions().add(p);
                        }

                        // --- Résultats d'analyse ---
                        cible.getResultats().clear();
                        System.out.println("Entrez les résultats d'analyse (tapez 'X' pour terminer) :");
                        while (true) {
                            System.out.print("Type d'analyse (ou X pour terminer) : ");
                            String type = scanner.nextLine().trim();
                            if (type.equalsIgnoreCase("X")) break;

                            System.out.print("Valeur : ");
                            String valeur = scanner.nextLine().trim();

                            System.out.print("Unité (laisser vide si aucune) : ");
                            String unite = scanner.nextLine().trim();
                            if (unite.isEmpty()) unite = null;

                            System.out.print("Interprétation : ");
                            String interpretation = scanner.nextLine().trim();

                            ResultatAnalyse r = new ResultatAnalyse(type, valeur, unite, interpretation);
                            cible.getResultats().add(r);
                        }

                        // Marquer la consultation comme faite
                        cible.marquerCommeFaite();

                        // --- Sauvegarde en base ---
                        Connection conn = DriverManager.getConnection(User.url);
                        if (conn != null) {
                            cible.save(conn, cible.getPatient().getIdUser());
                            System.out.println("\nConsultation mise à jour et sauvegardée avec succès !");
                        } else {
                            System.out.println("\nConsultation mise à jour mais connexion à la base non disponible.");
                        }

                    } catch (Exception e) {
                        System.out.println("Erreur ! Veuillez réessayer !");
                        e.printStackTrace();
                    }

                    break;
                case "4":
                    List<Patient> patients = pro.getPatientsLies();
                    if (patients.isEmpty()) {
                        System.out.println("Aucun patient enregistré pour vos consultations.");
                        return true;
                    }

                    System.out.println("=== Sélectionnez un patient ===");
                    for (int i = 0; i < patients.size(); i++) {
                        System.out.println(i + ") " + patients.get(i).getNom() + " " + patients.get(i).getPrenom());
                    }

                    int choix = -1;
                    while (true) {
                        System.out.print("Choisissez un patient (index) ou X pour annuler : ");
                        String choixStr = scanner.nextLine().trim();

                        if (choixStr.equalsIgnoreCase("X")) 
                            return true;

                        try {
                            choix = Integer.parseInt(choixStr);

                            if (choix >= 0 && choix < patients.size()) {
                                break; 
                            } else {
                                System.out.println("Index hors limites !");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Veuillez entrer un nombre.");
                        }
                    }

                    Patient patientChoisi = patients.get(choix);
                    System.out.println("Vous avez sélectionné : " + patientChoisi.getNom() + " " + patientChoisi.getPrenom()+ "\n");
                    System.out.println(patientChoisi.getDossierMedical());
                    break;

                case "5":
                    System.out.println("Déconnexion réussie. Retour à l'accueil...");
                    return true;

                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
            return false;
        }
    }

    private static void menuGestionnaire(GestionnaireDePatient gestionnaire) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Menu Gestionnaire de Patient === \n");
        System.out.print("Voulez-vous créer un patient ? (O/N) : ");
        
        if (sc.nextLine().equalsIgnoreCase("O")) {
            Console console = System.console();
            Scanner scanner = new Scanner(System.in);

            System.out.println("=== Création d'un nouveau patient ===");
            System.out.println("Nb : La création d'un patient crée aussi un dossier médical vierge.\n");

            System.out.print("Entrez le nom : ");   
            String nom = sc.nextLine();

            System.out.print("Entrez les prénoms : ");
            String prenoms = sc.nextLine();

            System.out.print("Entrez le téléphone : ");
            String telephone = sc.nextLine();

            String motDePasse;
            String confirmation;
            while (true) {
                System.out.print("Entrez le mot de passe : ");
                if (console != null) {       
                    motDePasse = new String(console.readPassword());
                    System.out.print("Confirmez le mot de passe : ");
                    confirmation = new String(console.readPassword());
                }else{
                    motDePasse = scanner.nextLine();
                    System.out.print("Confirmez le mot de passe : ");
                    confirmation = scanner.nextLine();
                }
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
        	System.out.println("=== Menu Administrateur ===\n");
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
