import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

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

    public Patient(int idUser, String nom, String prenom, String telephone, String adresse) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dossierMedical = new DossierMedical();
    }

    public LocalDate get_Date_Dernière_Consultation() {
        return date_Derniere_Consultation;
    }

    public void set_Date_Dernière_Consultation(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.date_Derniere_Consultation = LocalDate.parse(date, formatter);
    }

    public String afficher_Date_Dernière_Consultation() {
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

        // --- CHOIX PROFESSIONNEL ---

        while (true) {
            System.out.print("\nChoisissez un professionnel (index) ou X pour annuler : ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("X"))
                return null;

            try {
                choixPro = Integer.parseInt(input);
                if (choixPro >= 0 && choixPro < pros.size())
                    break;
                System.out.println("Index invalide. Réessayez.");
            } catch (NumberFormatException e) {
                System.out.println("Entrez un nombre valide.");
            }
        }

        Professionnel_de_Sante pro = pros.get(choixPro);

        List<Disponibilite> dispo = pro.get_Disponibilites();
        Disponibilite d = null;

        if (dispo.isEmpty()) {
            System.out.println("Ce professionnel n’a aucune disponibilité.");
            return null;
        }

        System.out.println("\n=== DISPONIBILITÉS DE " + pro.getNom() + " ===");
        for (int i = 0; i < dispo.size(); i++) {
            System.out.println(i + ") " + dispo.get(i) + (dispo.get(i).getEstReservee() ? " (Réservé)" : ""));
        }
        while (true) {
            System.out.print("\nChoisissez un créneau (index) ou X pour annuler : ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("X"))
                return null;

            try {
                int choixDispo = Integer.parseInt(input);
                if (choixDispo >= 0 && choixDispo < dispo.size()) {
                    if (!dispo.get(choixDispo).getEstReservee()) {
                        d = dispo.get(choixDispo);

                        break;
                    } else {
                        System.out.println("Ce créneau est déjà réservé. Choisissez-en un autre.");
                    }
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
            if (service.equalsIgnoreCase("X"))
                return null;
            if (!service.isEmpty())
                break;
            System.out.println("\nLe service ne peut pas être vide.");
        }

        // --- DATE ---
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateString;
        while (true) {
            System.out.print("\nEntrez la date de consultation (format dd/MM/yyyy) ou X pour annuler : ");
            String entree = scanner.nextLine().trim();
            if (entree.equalsIgnoreCase("X"))
                return null;

            try {
                LocalDate date = LocalDate.parse(entree, formatter);
                dateString = date.format(formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Format invalide ! Veuillez réessayer.\n");
            }
        }

        // --- CREATION CONSULTATION ---
        Consultation consultation = new Consultation(this, pro, service, dateString, d);
        // On réserve le créneau
        d.reserver();

        // On ajoute dans le dossier médical
        this.getDossierMedical().ajouterConsultation(consultation);

        this.date_Derniere_Consultation = consultation.getDatePrevue();

        System.out.println("\n Consultation programmée avec succès !");
        System.out.println(consultation);

        return consultation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128); // capacité initiale
        sb.append("Nom: ").append(nom).append(", \n");
        sb.append("Prénom: ").append(prenom).append(", \n");
        sb.append("Date de la dernière consultation: ").append(afficher_Date_Dernière_Consultation()).append(", \n");

        return sb.toString();
    }
}
