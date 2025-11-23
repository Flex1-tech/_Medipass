import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class PatientService {
    private final PatientRepo depotPatients;
    private final DossierRepo depotDossiers;

    PatientService(PatientRepo depotPatients, DossierRepo depotDossiers) {
        this.depotPatients = depotPatients;
        this.depotDossiers = depotDossiers;
    }

    Patient creerPatient(String nom, String prenom, Boolean estMale, String telephone, String adresse) {
        Patient patient = new Patient();
        patient.set_Nom(nom);
        patient.set_Prenom(prenom);
        if (estMale != null) patient.set_EstMale(estMale);
        patient.set_Telephone(telephone);
        patient.set_Adresse(adresse);
        depotPatients.enregistrer(patient);
        depotDossiers.enregistrer(new DossierMedical(patient.get_IdUser()));
        return patient;
    }

    Optional<Patient> obtenirPatient(int id) { return depotPatients.trouverParId(id); }
    List<Patient> listerPatients() { return depotPatients.listerTous(); }

    Optional<DossierMedical> obtenirDossier(int patientId) {
        Optional<DossierMedical> dossier = depotDossiers.trouverParPatientId(patientId);
        if (dossier.isPresent()) return dossier;
        DossierMedical cree = new DossierMedical(patientId);
        depotDossiers.enregistrer(cree);
        return Optional.of(cree);
    }

    Optional<Patient> mettreAJourPatient(int id, String nom, String prenom, Boolean estMale, String telephone, String adresse) {
        Optional<Patient> opt = depotPatients.trouverParId(id);
        if (opt.isEmpty()) return Optional.empty();
        Patient patient = opt.get();
        if (nom != null && !nom.isBlank()) patient.set_Nom(nom);
        if (prenom != null && !prenom.isBlank()) patient.set_Prenom(prenom);
        if (estMale != null) patient.set_EstMale(estMale);
        if (telephone != null) patient.set_Telephone(telephone);
        if (adresse != null) patient.set_Adresse(adresse);
        depotPatients.mettreAJour(patient);
        return Optional.of(patient);
    }

    Optional<Antecedent> ajouterAntecedent(int patientId, LocalDate date, String type, String description) {
        Optional<DossierMedical> dOpt = obtenirDossier(patientId);
        if (dOpt.isEmpty()) return Optional.empty();
        DossierMedical dossier = dOpt.get();
        Antecedent antecedent = new Antecedent(null, date, type, description);
        dossier.addAntecedent(antecedent);
        depotDossiers.mettreAJour(dossier);
        return Optional.of(antecedent);
    }

    Optional<DossierMedical> mettreAJourObservations(int patientId, String observations) {
        Optional<DossierMedical> dOpt = obtenirDossier(patientId);
        if (dOpt.isEmpty()) return Optional.empty();
        DossierMedical dossier = dOpt.get();
        dossier.setObservations(observations);
        depotDossiers.mettreAJour(dossier);
        return Optional.of(dossier);
    }

    Optional<Consultation> ajouterConsultation(int patientId, LocalDate date, String contenu) {
        Optional<DossierMedical> dOpt = obtenirDossier(patientId);
        Optional<Patient> pOpt = obtenirPatient(patientId);
        if (dOpt.isEmpty() || pOpt.isEmpty()) return Optional.empty();
        DossierMedical dossier = dOpt.get();
        Consultation consultation = new Consultation(null, date, contenu);
        dossier.addConsultation(consultation);
        depotDossiers.mettreAJour(dossier);
        Patient patient = pOpt.get();
        patient.set_Date_Derniere_Consultation(date.toString());
        depotPatients.mettreAJour(patient);
        return Optional.of(consultation);
    }
}