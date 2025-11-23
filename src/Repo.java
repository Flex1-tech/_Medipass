import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class PatientRepo {
    private final Map<Integer, Patient> stockage = new HashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    Patient enregistrer(Patient patient) {
        if (patient.get_IdUser() == 0) patient.set_IdUser(sequence.getAndIncrement());
        stockage.put(patient.get_IdUser(), patient);
        return patient;
    }

    Optional<Patient> trouverParId(int id) { return Optional.ofNullable(stockage.get(id)); }
    List<Patient> listerTous() { return new ArrayList<>(stockage.values()); }
    Patient mettreAJour(Patient patient) { stockage.put(patient.get_IdUser(), patient); return patient; }
}

class DossierRepo {
    private final Map<Integer, DossierMedical> stockage = new HashMap<>();

    DossierMedical enregistrer(DossierMedical dossier) { stockage.put(dossier.getPatientId(), dossier); return dossier; }
    Optional<DossierMedical> trouverParPatientId(int id) { return Optional.ofNullable(stockage.get(id)); }
    DossierMedical mettreAJour(DossierMedical dossier) { stockage.put(dossier.getPatientId(), dossier); return dossier; }
}