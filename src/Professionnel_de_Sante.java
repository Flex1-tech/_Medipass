import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Professionnel_de_Sante extends User {
    private String titre;
    
    private List<Disponibilite> disponibilites = new ArrayList<>();

    public String get_titre() {
        return this.titre;
    }

    public void set_titre(String titre) {
        this.titre = titre;
    }

    public Professionnel_de_Sante(String nom, String prenom, String telephone, String motDePasse, String adresse,
            String titre) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
        this.titre = titre;
    }

    public List<Disponibilite> get_Disponibilites() {
        return disponibilites;
    }

    public void supprimer_Disponibilite(Disponibilite disponibilite) {
        this.disponibilites.remove(disponibilite);
    }

    public void modifier_Disponibilite(Disponibilite ancienneDisponibilite, Disponibilite nouvelleDisponibilite) {
        int index = this.disponibilites.indexOf(ancienneDisponibilite);
        if (index != -1) {
            this.disponibilites.set(index, nouvelleDisponibilite);
        }
    }

    public String afficher_Disponibilites() {
        StringBuilder sb = new StringBuilder();
        for (Disponibilite disponibilite : disponibilites) {
            sb.append(disponibilite.toString()).append("\n");
        }
        return sb.toString();
    }

    public static List<Professionnel_de_Sante> getTousLesProfessionnels() {
        List<Professionnel_de_Sante> pros = new ArrayList<>();

        String query = "SELECT * FROM ProfessionnelDeSante";
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Professionnel_de_Sante p = new Professionnel_de_Sante(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("mot_de_passe"),
                    rs.getString("adresse"),
                    rs.getString("titre")
                );
                pros.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pros;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder(128); // capacité initiale
        sb.append("Nom: ").append(nom).append(", \n");
        sb.append("Prénom: ").append(prenom).append(", \n");
        sb.append("Titre: ").append(titre).append(", \n");
        sb.append("Disponibilités: \n").append(afficher_Disponibilites());

        return sb.toString();
    }

    public void ajouter_Disponibilite(Disponibilite disponibilite) {
        for (Disponibilite d : disponibilites) {
            if (d.chevauche(disponibilite)) {
                throw new IllegalArgumentException("Cette disponibilité chevauche une disponibilité existante !");
            }
        }
        this.disponibilites.add(disponibilite);
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

        // --- CHOIX PROFESSIONNEL ---
        Professionnel_de_Sante pro;
        while (true) {
            System.out.print("\nChoisissez un professionnel (index) ou X pour annuler : ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("X")) return null;

            try {
                int choixPro = Integer.parseInt(input);
                if (choixPro >= 0 && choixPro < pros.size()) {
                    pro = pros.get(choixPro);
                    break;
                } else {
                    System.out.println("Index invalide. Réessayez.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrez un nombre valide.");
            }
        }

        List<Disponibilite> dispo = pro.get_Disponibilites();
        if (dispo.isEmpty()) {
            System.out.println("Ce professionnel n’a aucune disponibilité.");
            return null;
        }

        // --- CHOIX DU CRÉNEAU ---
        Disponibilite d = null;
        while (true) {
            System.out.println("\n=== DISPONIBILITÉS DE " + pro.getNom() + " ===");
            for (int i = 0; i < dispo.size(); i++) {
                System.out.println(i + ") " + dispo.get(i) + (dispo.get(i).getEstReservee() ? " (Réservé)" : ""));
            }
            System.out.print("\nChoisissez un créneau (index) ou X pour annuler : ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("X")) return null;

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
            System.out.print("\nEntrez le service (Cardiologie, Dermatologie, ...) ou X pour annuler : ");
            service = scanner.nextLine().trim();
            if (service.equalsIgnoreCase("X")) return null;
            if (!service.isEmpty()) break;
            System.out.println("Le service ne peut pas être vide.");
        }

        // --- CRÉATION DE LA CONSULTATION ---
        Consultation consultation = new Consultation(this, pro, service, dateString, d);

        // --- RÉSERVATION DU CRÉNEAU ---
        d.reserver();

        // --- AJOUT DANS LE DOSSIER MÉDICAL ---
        this.getDossierMedical().ajouterConsultation(consultation);

        this.date_Dernière_Consultation = consultation.getDatePrevue();

        System.out.println("\n✓ Consultation programmée avec succès !");
        System.out.println(consultation);

        return consultation;
    }

    

}
