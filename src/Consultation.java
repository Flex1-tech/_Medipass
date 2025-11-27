import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Consultation {
    private static int compteur = 0;

    private final int idConsultation;
    private LocalDate datePrevue;
    private String service; // Cardiologie, Dermatologie, etc.
    private String diagnostic;
    private boolean estPassee;
    private boolean aEteFaite;

    private Double poids;

    private List<Prescription> prescriptions = new ArrayList<>();
    private List<ResultatAnalyse> resultats = new ArrayList<>();

    private Patient patient;
    private Professionnel_de_Sante professionnelDeSante;

    public Consultation(Patient patient, Professionnel_de_Sante professionnelDeSante, String service, String prevuPour) {
        this.idConsultation = ++compteur;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.datePrevue = LocalDate.parse(prevuPour, formatter);

        this.service = service;
        this.patient = patient;
        this.professionnelDeSante = professionnelDeSante;

        mettreAJourEtat();
    }

    public void marquerCommeFaite(String diagnostic) {
        this.aEteFaite = true;
        this.diagnostic = diagnostic;
    }

    public void mettreAJourEtat() {
        this.estPassee = !this.datePrevue.isAfter(LocalDate.now());
        if (!estPassee) {
            this.aEteFaite = false; // ne peut pas être faite si elle n'est pas encore passée
        }
        if (estPassee && !aEteFaite) {
            this.diagnostic = "Consultation non réalisée.";
        } else if (!aEteFaite) {
            this.diagnostic = null;
        }

    }

    public void ajouterPrescription(Prescription p) {
        prescriptions.add(p);
    }

    public void ajouterResultat(ResultatAnalyse r) {
        resultats.add(r);
    }

    // --- Getters / Setters ---
    
    public int getIdConsultation() {
        return idConsultation;
    }

    public LocalDate getDatePrevue() {
        return datePrevue;
    }

    public void setDatePrevue(LocalDate datePrevue) {
    if(aEteFaite) {
        throw new IllegalStateException("Impossible de modifier la date d'une consultation déjà faite.");
    }
    this.datePrevue = datePrevue;
    mettreAJourEtat();
    }

    
    public boolean isEstPassee() {
        return estPassee;
    }

    public boolean isaEteFait() {
        return aEteFaite;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        if (poids != null && poids <= 0) {
            throw new IllegalArgumentException("Le poids doit être positif.");
        }
        this.poids = poids;
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

    public List<Prescription> getPrescriptions() {
        return List.copyOf(prescriptions);
    }

    public List<ResultatAnalyse> getResultats() {
        return List.copyOf(resultats);
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String dateFormatee = (datePrevue != null) ? datePrevue.format(formatter) : "N/A";
        String nomPatient = (patient != null && patient.getNom() != null) ? patient.getNom() : "N/A";
        String nomPro = (professionnelDeSante != null && professionnelDeSante.getNom() != null) ? professionnelDeSante.getNom() : "N/A";
        String diag = (diagnostic != null) ? diagnostic : "";
        
        StringBuilder sb = new StringBuilder();
        sb.append("Consultation {\n");
        sb.append("  id               : ").append(idConsultation).append("\n");
        sb.append("  date prévue      : ").append(dateFormatee).append("\n");
        sb.append("  service          : '").append(service != null ? service : "").append("'\n");
        sb.append("  patient          : '").append(nomPatient).append("'\n");
        sb.append("  professionnel    : '").append(nomPro).append("'\n");
        sb.append("  est passée       : ").append(estPassee).append("\n");
        sb.append("  a été faite      : ").append(aEteFaite).append("\n");
        sb.append("  poids            : ").append(String.format("%.2f", poids)).append(" kg\n");
        sb.append("  diagnostic       : '").append(diag).append("'\n");
        sb.append("}");
        
        return sb.toString();
    }

}