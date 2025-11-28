package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Consultation {
    private static int compteur = 0;

    private final int idConsultation;
    private LocalDate datePrevue;
    private String service;
    private String diagnostic;
    private List<String> observations = new ArrayList<>();
    private boolean estPassee;
    private boolean aEteFaite;
    private Disponibilite creneau;
    private Double poids;
    private List<Prescription> prescriptions = new ArrayList<>();
    private List<ResultatAnalyse> resultats = new ArrayList<>();
    private Patient patient;
    private Professionnel_de_Sante professionnelDeSante;

    public Consultation(Patient patient, Professionnel_de_Sante professionnelDeSante, String service,
            LocalDate datePrevue, Disponibilite creneau) {

        this.idConsultation = ++compteur;
        this.datePrevue = datePrevue;
        this.service = service;
        this.patient = patient;
        this.professionnelDeSante = professionnelDeSante;
        this.creneau = creneau;
        mettreAJourEtat();
    }

    public void marquerCommeFaite(String diagnostic) {
        this.aEteFaite = true;
        this.diagnostic = diagnostic;
    }

    public void mettreAJourEtat() {
        this.estPassee = !this.datePrevue.isAfter(LocalDate.now());
        if (!estPassee) {
            this.aEteFaite = false;
        }
        if (estPassee && !aEteFaite) {
            this.diagnostic = "Consultation non réalisée.";
        } else if (!aEteFaite) {
            this.diagnostic = null;
        }
    }

    // --- Gestion des listes ---
    public void ajouterPrescription(Prescription p) {
        if (p != null)
            prescriptions.add(p);
    }

    public void ajouterResultat(ResultatAnalyse r) {
        if (r != null)
            resultats.add(r);
    }

    public void ajouterObservation(String observation) {
        if (observation != null && !observation.isEmpty())
            observations.add(observation);
    }

    public Disponibilite getCreneau() {
        return creneau;
    }

    public void setCreneau(Disponibilite creneau) {
        this.creneau = creneau;
    }

    public boolean supprimerObservation(int index) {
        if (index < 0 || index >= observations.size())
            return false;
        observations.remove(index);
        return true;
    }

    public boolean supprimerResultat(int index) {
        if (index < 0 || index >= resultats.size())
            return false;
        resultats.remove(index);
        return true;
    }

    public boolean supprimerPrescription(int index) {
        if (index < 0 || index >= prescriptions.size())
            return false;
        prescriptions.remove(index);
        return true;
    }

    public void supprimerToutesLesObservations() {
        observations.clear();
    }

    public void supprimerTousLesResultats() {
        resultats.clear();
    }

    public void supprimerToutesLesPrescriptions() {
        prescriptions.clear();
    }

    // --- Getters / Setters ---
    public int getIdConsultation() {
        return idConsultation;
    }

    public LocalDate getDatePrevue() {
        return datePrevue;
    }

    public void setDatePrevue(LocalDate datePrevue) {
        if (aEteFaite)
            throw new IllegalStateException("Impossible de modifier la date d'une consultation déjà faite.");
        this.datePrevue = datePrevue;
        mettreAJourEtat();
    }

    public boolean isEstPassee() {
        return estPassee;
    }

    public boolean isAEteFait() {
        return aEteFaite;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        if (poids != null && poids <= 0)
            throw new IllegalArgumentException("Le poids doit être positif.");
        this.poids = poids;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        if (service == null || service.isEmpty())
            throw new IllegalArgumentException("Le service ne peut pas être vide.");
        this.service = service;
    }

    public Patient getPatient() {
        return patient;
    }

    public Professionnel_de_Sante getProfessionnelDeSante() {
        return professionnelDeSante;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        if (!aEteFaite)
            throw new IllegalStateException("Impossible de définir un diagnostic avant la réalisation.");
        this.diagnostic = diagnostic;
    }

    public List<Prescription> getPrescriptions() {
        return List.copyOf(prescriptions);
    }

    public List<ResultatAnalyse> getResultats() {
        return List.copyOf(resultats);
    }

    public List<String> getObservations() {
        return List.copyOf(observations);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatee = (datePrevue != null) ? datePrevue.format(formatter) : "N/A";
        String nomPatient = (patient != null && patient.getNom() != null) ? patient.getNom() : "N/A";
        String nomPro = (professionnelDeSante != null && professionnelDeSante.getNom() != null)
                ? professionnelDeSante.getNom()
                : "N/A";
        String diag = (diagnostic != null) ? diagnostic : "";
        String poidsStr = (poids != null) ? String.format("%.2f", poids) + " kg" : "N/A";

        return "Consultation {\n" +
                "  id               : " + idConsultation + "\n" +
                "  date prévue      : " + dateFormatee + "\n" +
                "  créneau          : " + creneau.getHeureDebut() + " à " + creneau.getHeureFin() + "\n" +
                "  service          : '" + service + "'\n" +
                "  patient          : '" + nomPatient + "'\n" +
                "  professionnel    : '" + nomPro + "'\n" +
                "  est passée       : " + estPassee + "\n" +
                "  a été faite      : " + aEteFaite + "\n" +
                "  poids            : " + poidsStr + "\n" +
                "  diagnostic       : '" + diag + "'\n" +
                "  Observations     : " + observations + "\n" +
                "  Prescriptions    : " + prescriptions + "\n" +
                "  Résultats        : " + resultats + "\n" +
                "}";
    }
}