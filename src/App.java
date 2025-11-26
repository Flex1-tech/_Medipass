import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private static final DateTimeFormatter FORMATEUR = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static LocalDate parseDate(String s){ return LocalDate.parse(s, FORMATEUR); }
    private static String valeurOuNull(String s){ return s==null||s.isBlank()?null:s; }
    private static Boolean estMasculin(String s){ String k=s==null?"":s.trim().toLowerCase(); return k.isEmpty()?null:k.startsWith("m"); }

    public static void main(String[] args){
        PatientService service = new PatientService(new PatientRepo(), new DossierRepo()); Scanner entree = new Scanner(System.in);
        boolean enCours = true; while(enCours){
            System.out.println("=== Système d'information médical (Patients) ===\n1. Créer un patient\n2. Lister les patients\n3. Consulter un dossier médical\n4. Mettre à jour un patient\n5. Ajouter un antécédent\n6. Mettre à jour les observations du dossier\n"+"7. Ajouter une consultation\n8. Quitter");
            System.out.print("Choix: "); String choix = entree.nextLine();
            switch(choix){ case "1": creer(entree, service); break; case "2": lister(service); break; case "3": consulterDossier(entree, service); break; case "4": mettreAJourPatient(entree, service); break; case "5": ajouterAntecedent(entree, service); break; case "6": mettreAJourObservations(entree, service); break; case "7": ajouterConsultation(entree, service); break; case "8": enCours = false; break; default: System.out.println("Choix invalide"); }
            System.out.println(); }
    }

    private static void creer(Scanner entree, PatientService service){ System.out.print("Nom: "); String nom = entree.nextLine(); System.out.print("Prénom: "); String prenom = entree.nextLine(); System.out.print("Sexe (M/F ou masculin/feminin): "); Boolean masculin = estMasculin(entree.nextLine()); System.out.print("Téléphone: "); String tel = entree.nextLine(); System.out.print("Adresse: "); String adr = entree.nextLine();
        Patient p = service.creerPatient(nom, prenom, masculin, tel, adr); System.out.println("Patient créé avec ID: " + p.get_IdUser()); }

    private static void lister(PatientService service){ List<Patient> patients = service.listerPatients(); if(patients.isEmpty()) System.out.println("Aucun patient"); else for(Patient p : patients) System.out.println(p.get_IdUser()+" - "+p.get_Nom()+" "+p.get_Prenom()); }

    private static void consulterDossier(Scanner entree, PatientService service){ System.out.print("ID patient: "); int id = Integer.parseInt(entree.nextLine()); Optional<DossierMedical> dOpt = service.obtenirDossier(id); if(dOpt.isEmpty()){ System.out.println("Dossier introuvable"); return; } DossierMedical d = dOpt.get(); System.out.println("Observations: " + d.getObservations()); System.out.println("Consultations (" + d.getNombreConsultations() + "):"); if(d.getConsultations().isEmpty()) System.out.println("  Aucune"); else for(Consultation c : d.getConsultations()) System.out.println("  " + c.getDate() + " - " + c.getContenu()); System.out.println("Antécédents:"); if(d.getAntecedents().isEmpty()) System.out.println("  Aucun"); else for(Antecedent a : d.getAntecedents()) System.out.println("  " + a.getDate() + " - " + a.getType() + " - " + a.getDescription()); }

    private static void mettreAJourPatient(Scanner entree, PatientService service){ System.out.print("ID patient: "); int id = Integer.parseInt(entree.nextLine()); System.out.print("Nouveau nom (laisser vide pour ne pas modifier): "); String nom = valeurOuNull(entree.nextLine()); System.out.print("Nouveau prénom (laisser vide pour ne pas modifier): "); String prenom = valeurOuNull(entree.nextLine());
        System.out.print("Nouveau sexe (M/F, vide): "); String s = entree.nextLine(); Boolean masculin = s.isBlank()? null : estMasculin(s); System.out.print("Nouveau téléphone (laisser vide): "); String tel = valeurOuNull(entree.nextLine()); System.out.print("Nouvelle adresse (laisser vide): "); String adr = valeurOuNull(entree.nextLine());
        Optional<Patient> p = service.mettreAJourPatient(id, nom, prenom, masculin, tel, adr); System.out.println(p.isEmpty()? "Patient introuvable" : "Mis à jour: " + p.get().get_IdUser()); }

    private static void ajouterAntecedent(Scanner entree, PatientService service){ System.out.print("ID patient: "); int id = Integer.parseInt(entree.nextLine()); System.out.print("Date (yyyy-MM-dd): "); LocalDate date = parseDate(entree.nextLine());
        System.out.print("Type: "); String type = entree.nextLine(); System.out.print("Description: "); String desc = entree.nextLine(); Optional<Antecedent> a = service.ajouterAntecedent(id, date, type, desc); System.out.println(a.isEmpty()? "Patient introuvable" : "Antécédent ajouté"); }

    private static void mettreAJourObservations(Scanner entree, PatientService service){ System.out.print("ID patient: "); int id = Integer.parseInt(entree.nextLine()); System.out.print("Observations: "); String obs = entree.nextLine(); Optional<DossierMedical> d = service.mettreAJourObservations(id, obs); System.out.println(d.isEmpty()? "Patient introuvable" : "Observations mises à jour"); }

    private static void ajouterConsultation(Scanner entree, PatientService service){ System.out.print("ID patient: "); int id = Integer.parseInt(entree.nextLine()); System.out.print("Date (yyyy-MM-dd): "); LocalDate date = parseDate(entree.nextLine());
        System.out.print("Contenu: "); String contenu = entree.nextLine(); Optional<Consultation> c = service.ajouterConsultation(id, date, contenu); System.out.println(c.isEmpty()? "Patient introuvable" : "Consultation ajoutée"); }
}