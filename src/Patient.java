
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Patient extends User {
    private String date_Derniere_Consultation;

    public boolean seConnecter() { return this.motDePasse != null && !this.motDePasse.isBlank(); }
    public void seDeconnecter() {}

    public String get_Date_Derniere_Consultation() { return date_Derniere_Consultation; }
    protected void set_Date_Derniere_Consultation(String date) { this.date_Derniere_Consultation = date; }
}

class Consultation {
    private final String id;
    private final LocalDate date;
    private final String contenu;

    Consultation(String id, LocalDate date, String contenu) {
        this.id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        this.date = date;
        this.contenu = contenu;
    }

    String getId() { return id; }
    LocalDate getDate() { return date; }
    String getContenu() { return contenu; }
}

class Antecedent {
    private final String id;
    private final LocalDate date;
    private final String type;
    private final String description;

    Antecedent(String id, LocalDate date, String type, String description) {
        this.id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    String getId() { return id; }
    LocalDate getDate() { return date; }
    String getType() { return type; }
    String getDescription() { return description; }
}

class DossierMedical {
    private final int patientId;
    private final List<Antecedent> antecedents = new ArrayList<>();
    private final List<Consultation> consultations = new ArrayList<>();
    private String observations = "";
    private LocalDate derniereConsultation;

    DossierMedical(int patientId) { this.patientId = patientId; }

    int getPatientId() { return patientId; }
    List<Antecedent> getAntecedents() { return Collections.unmodifiableList(antecedents); }
    List<Consultation> getConsultations() { return Collections.unmodifiableList(consultations); }
    int getNombreConsultations() { return consultations.size(); }
    String getObservations() { return observations; }
    void setObservations(String observations) { this.observations = observations == null ? "" : observations; }
    LocalDate getDerniereConsultation() { return derniereConsultation; }

    Antecedent addAntecedent(Antecedent a) { antecedents.add(a); return a; }
    Consultation addConsultation(Consultation c) { consultations.add(c); this.derniereConsultation = c.getDate(); return c; }
}
