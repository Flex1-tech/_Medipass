import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Patient extends User {

    private DossierMedical dossierMedical;
    private LocalDate date_Derniere_Consultation;

    public Patient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
        this.dossierMedical = new DossierMedical();
    }

    public Patient(String nom, String prenom, String telephone, String adresse, DossierMedical dossierMedical, LocalDate dateDerniereConsultation) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dossierMedical = dossierMedical;
        this.date_Derniere_Consultation = dateDerniereConsultation;
    }

    public Patient(int idUser, String nom, String prenom, String telephone, String adresse) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dossierMedical = new DossierMedical();
    }

    public Patient(int idUser, String nom, String prenom, String telephone, String adresse, DossierMedical dossier, LocalDate dateDerniere) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dossierMedical = dossier;
        this.date_Derniere_Consultation = dateDerniere;

    }

    public LocalDate get_Date_Derniere_Consultation() {
        return date_Derniere_Consultation;
    }

    public void set_Date_Derniere_Consultation(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.date_Derniere_Consultation = LocalDate.parse(date, formatter);
    }

    public String afficher_Date_Derniere_Consultation() {
        if (date_Derniere_Consultation == null) {
            return "Aucune consultation enregistrée.";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date_Derniere_Consultation.format(formatter);
    }

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }
    public Consultation programmer_Consultation() {
        Scanner scanner = new Scanner(System.in);
        List<Professionnel_de_Sante> pros = Professionnel_de_Sante.getTousLesProfessionnels();

        if (pros == null || pros.isEmpty()) {
            System.out.println("Aucun professionnel de santé disponible.");
            return null;
        }

        System.out.println("=== LISTE DES PROFESSIONNELS DE SANTÉ ===");
        for (int i = 0; i < pros.size(); i++) {
            System.out.println(i + ") " + pros.get(i).getNom() + " " + pros.get(i).getPrenom()
                    + " — " + pros.get(i).get_titre());
        }

        int choixPro = -1;
        while (true) {
            System.out.print("\nChoisissez un professionnel (index) ou X pour annuler : ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("X")) return null;
            try {
                choixPro = Integer.parseInt(input);
                if (choixPro >= 0 && choixPro < pros.size()) break;
                System.out.println("Index invalide. Réessayez.");
            } catch (NumberFormatException e) {
                System.out.println("Entrez un nombre valide.");
            }
        }

        Professionnel_de_Sante pro = pros.get(choixPro);
        Disponibilite d = null;

        try (Connection conn = DriverManager.getConnection(User.url)) {
            // Début de transaction
            conn.setAutoCommit(false);

            // Charger les disponibilités
            User.chargerDisponibilites(conn, pro, pro.getIdUser());

            // Filtrer les créneaux libres
            List<Disponibilite> dispoLibres = pro.get_Disponibilites().stream()
                    .filter(c -> !c.getEstReservee())
                    .collect(Collectors.toList());

            if (dispoLibres.isEmpty()) {
                System.out.println("Ce professionnel n’a aucune disponibilité libre.");
                return null;
            }

            System.out.println("\n=== DISPONIBILITÉS LIBRES DE " + pro.getNom() + "pour la semaine ===");
            for (int i = 0; i < dispoLibres.size(); i++) {
                System.out.println(i + ") " + dispoLibres.get(i));
            }

            // Choix du créneau
            while (true) {
                System.out.print("\nChoisissez un créneau (index) ou X pour annuler : ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("X")) return null;
                try {
                    int choixDispo = Integer.parseInt(input);
                    if (choixDispo >= 0 && choixDispo < dispoLibres.size()) {
                        d = dispoLibres.get(choixDispo);
                        break;
                    } else {
                        System.out.println("Index invalide. Réessayez.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrez un nombre valide.");
                }
            }

            // --- SERVICE ---
            String service;
            while (true) {
                System.out.print("\nEntrez le service (Cardiologie, Dermatologie, ...) : ");
                service = scanner.nextLine().trim();
                if (service.equalsIgnoreCase("X")) return null;
                if (!service.isEmpty()) break;
                System.out.println("\nLe service ne peut pas être vide.");
            }

            // --- DATE ---
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateChoisie;
            while (true) {
                System.out.print("\nEntrez la date de consultation (format dd/MM/yyyy) ou X pour annuler : ");
                String entree = scanner.nextLine().trim();
                if (entree.equalsIgnoreCase("X")) return null;

                try {
                    dateChoisie = LocalDate.parse(entree, formatter);
                    if (dateChoisie.isBefore(LocalDate.now())) {
                        System.out.println("Impossible de prendre un rendez-vous dans le passé.");
                        continue;
                    }

                    // Vérification du jour
                    Disponibilite.Jour jour = switch(dateChoisie.getDayOfWeek()) {
                        case MONDAY -> Disponibilite.Jour.LUNDI;
                        case TUESDAY -> Disponibilite.Jour.MARDI;
                        case WEDNESDAY -> Disponibilite.Jour.MERCREDI;
                        case THURSDAY -> Disponibilite.Jour.JEUDI;
                        case FRIDAY -> Disponibilite.Jour.VENDREDI;
                        case SATURDAY -> Disponibilite.Jour.SAMEDI;
                        case SUNDAY -> Disponibilite.Jour.DIMANCHE;
                    };

                    if (d.getJour() != jour) {
                        System.out.println("Le créneau choisi ne correspond pas au jour sélectionné.");
                        continue;
                    }

                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Format invalide ! Veuillez réessayer.\n");
                }
            }

            // --- Création de la consultation ---
            Consultation consultation = new Consultation(this, pro, service, dateChoisie.format(formatter), d);

            // Réserver le créneau et sauvegarder
            d.reserver();
            d.save(conn, pro.getIdUser());

            // Ajouter au dossier médical et sauvegarder
            this.getDossierMedical().ajouterConsultation(consultation);
            this.getDossierMedical().save(conn, this.getIdUser());

            // Commit de la transaction
            conn.commit();

            // Recharger les disponibilités pour mise à jour
            User.chargerDisponibilites(conn, pro, pro.getIdUser());

            this.date_Derniere_Consultation = consultation.getDatePrevue();

            System.out.println("\nConsultation programmée avec succès !");
            System.out.println(consultation);

            return consultation;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la programmation de la consultation.");
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128); // capacité initiale
        sb.append("Nom: ").append(nom).append(", \n");
        sb.append("Prénom: ").append(prenom).append(", \n");
        sb.append("Date de la dernière consultation: ").append(afficher_Date_Derniere_Consultation()).append(", \n");

        return sb.toString();
    }

    public void afficherConsultationsPrevues() {
        System.out.println("=== Vos consultations prévues ===");

        List<Consultation> aVenir = this.getConsultationsAVenir();

        if (aVenir.isEmpty()) {
            System.out.println("Aucune consultation prévue.");
            return;
        }

        for (Consultation c : aVenir) {
            System.out.println(
                "Consultation #" + c.getIdConsultation() +
                "\n  Date      : " + c.getDatePrevue() +
                "\n  Service   : " + c.getService() +
                "\n  Docteur   : " + c.getProfessionnelDeSante().getNom() + " " + c.getProfessionnelDeSante().getPrenom() +
                "\n-------------------------------------"
            );
        }
    }

    public List<Consultation> getConsultationsAVenir() {
        List<Consultation> resultat = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Consultation c : this.getDossierMedical().getConsultations()) {
            if (c.getDatePrevue().isAfter(today)) {
                resultat.add(c);
            }
        }

        return resultat;
    }


    public boolean save() {
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtPatient = null;

        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            boolean existe = this.exists(conn); 

            // INSERT ou UPDATE USER
            if (existe) {

                String sqlUser = "UPDATE Users SET nom=?, prenom=?, telephone=?, adresse=? WHERE idUser=?";
                pstmtUser = conn.prepareStatement(sqlUser);
                pstmtUser.setString(1, nom);
                pstmtUser.setString(2, prenom);
                pstmtUser.setString(3, telephone);
                pstmtUser.setString(4, adresse);
                pstmtUser.setInt(5, idUser);
                pstmtUser.executeUpdate();

            } else {

                String sqlUser = "INSERT INTO Users (nom, prenom, telephone, motDePasse, adresse, typeUser)"
                        + " VALUES (?, ?, ?, ?, ?, 'patient')";
                pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
                pstmtUser.setString(1, nom);
                pstmtUser.setString(2, prenom);
                pstmtUser.setString(3, telephone);
                pstmtUser.setString(4, motDePasse);
                pstmtUser.setString(5, adresse);
                pstmtUser.executeUpdate();

                try (ResultSet keys = pstmtUser.getGeneratedKeys()) {
                    if (keys.next()) {
                        idUser = keys.getInt(1);
                    }
                }
            }

            // --- INSERT / UPDATE PATIENT ---
            if (existe) {
                String sqlPatient = "UPDATE Patients SET dateDerniereConsultation=? WHERE idPatient=?";
                pstmtPatient = conn.prepareStatement(sqlPatient);
                pstmtPatient.setString(1,
                        date_Derniere_Consultation != null
                                ? date_Derniere_Consultation.toString()
                                : null);
                pstmtPatient.setInt(2, idUser);
                pstmtPatient.executeUpdate();

            } else {
                String sqlPatient = "INSERT INTO Patients (idPatient, dateDerniereConsultation) VALUES (?, ?)";
                pstmtPatient = conn.prepareStatement(sqlPatient);
                pstmtPatient.setInt(1, idUser);
                pstmtPatient.setString(2,
                        date_Derniere_Consultation != null
                                ? date_Derniere_Consultation.toString()
                                : null);
                pstmtPatient.executeUpdate();
            }

            // --- SAUVEGARDE DU DOSSIER MÉDICAL ---
            if (dossierMedical != null) {
                dossierMedical.save(conn, idUser);  
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
            return false;

        } finally {
            try { if (pstmtUser != null) pstmtUser.close(); } catch (Exception ignored) {}
            try { if (pstmtPatient != null) pstmtPatient.close(); } catch (Exception ignored) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception ignored) {}
        }
    }
    
    public void afficherDossierMedical(){
        System.out.println(this.getDossierMedical());
    }

    public void exporterDossierMedicalTXT() {
        // Créer le nom du fichier automatiquement
        String nomFichier = "Dossier_" + this.getNom() + "_" + this.getPrenom() + ".txt";
        
        File fichier = new File(nomFichier);

        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write("Patient : " + this.getNom() + " " + this.getPrenom() + "\n");
            writer.write(this.getDossierMedical().toString());

            System.out.println("Dossier médical exporté avec succès !");
            System.out.println("Chemin complet : " + fichier.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
}


}
