import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Consultation {
    private static int compteur = 0;

    private final int idConsultation;
    private LocalDate datePrevue;
    private String service; // Cardiologie, Dermatologie, etc.
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
            String prevuPour, Disponibilite creneau) {
        this.idConsultation = ++compteur;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.datePrevue = LocalDate.parse(prevuPour, formatter);

        this.service = service;
        this.patient = patient;
        this.professionnelDeSante = professionnelDeSante;
        this.creneau = creneau;
        mettreAJourEtat();
    }

    public Consultation(int idConsultation, String service, LocalDate prevuPour,Patient patient,Professionnel_de_Sante pro, Disponibilite creneau) {
        this.idConsultation = idConsultation;
        this.datePrevue = prevuPour;
        this.service = service;
        this.patient = patient;
        this.professionnelDeSante = pro;
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
            this.aEteFaite = false; // ne peut pas être faite si elle n'est pas encore passée
        }
        if (estPassee && !aEteFaite) {
            this.diagnostic = "Consultation non réalisée.";
        } else if (!aEteFaite) {
            this.diagnostic = null;
        }

    }

    public void ajouterPrescription(Prescription p) {
        if (p == null)
            return;
        prescriptions.add(p);
    }

    public void ajouterResultat(ResultatAnalyse r) {
        if (r == null)
            return;
        resultats.add(r);
    }

    public void ajouterObservation(String observation) {
        if (observation == null || observation.isEmpty())
            return;
        observations.add(observation);
    }

    // --- Getters / Setters ---

    public int getIdConsultation() {
        return idConsultation;
    }

    public LocalDate getDatePrevue() {
        return datePrevue;
    }

    public void setDatePrevue(LocalDate datePrevue) {
        if (aEteFaite) {
            throw new IllegalStateException("Impossible de modifier la date d'une consultation déjà faite.");
        }
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
        if (poids != null && poids <= 0) {
            throw new IllegalArgumentException("Le poids doit être positif.");
        }
        this.poids = poids;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("Le service ne peut pas être vide.");
        }
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
        if (!aEteFaite) {
            throw new IllegalStateException(
                    "Impossible de définir un diagnostic avant la réalisation de la consultation.");
        }
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

    public void AfficherObservations() {
        if (observations.isEmpty()) {
            System.out.println("Aucune Observation pour cette consultation.");
            return;
        }
        System.out.println("Obervation de la consultation :");
        for (int i = 0; i < observations.size(); i++) {
            System.out.println(i + " : " + observations.get(i) + "\n");
        }
    }

    public boolean supprimerObservation(int index) {
        if (index < 0 || index >= observations.size()) {
            System.out.println("Indice invalide. Aucune observation supprimée.");
            return false;
        }

        String noteASupprimer = observations.get(index);
        System.out.println("Vous allez supprimer l'observation suivante :");
        System.out.println(noteASupprimer);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Confirmez-vous la suppression ? (O/N) : ");
        String reponse = scanner.nextLine().trim().toUpperCase();

        if (reponse.equals("O")) {
            observations.remove(index);
            System.out.println("Observation supprimée.");
            return true;
        } else {
            System.out.println("Suppression annulée.");
            return false;
        }
    }

    public void supprimerToutesLesObservations() {
        observations.clear();
    }

    public void afficherResultats() {
        if (resultats.isEmpty()) {
            System.out.println("Aucun résultat d'analyse pour cette consultation.");
            return;
        }

        System.out.println("Résultats d'analyses :");
        for (int i = 0; i < resultats.size(); i++) {
            System.out.println(i + " : " + resultats.get(i) + "\n");
        }
    }

    public boolean supprimerResultat(int index) {
        if (index < 0 || index >= resultats.size()) {
            System.out.println("Indice invalide. Aucun résultat supprimé.");
            return false;
        }

        ResultatAnalyse aSupprimer = resultats.get(index);
        System.out.println("Vous allez supprimer le résultat suivant :");
        System.out.println(aSupprimer);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Confirmez-vous la suppression ? (O/N) : ");
        String reponse = scanner.nextLine().trim().toUpperCase();

        if (reponse.equals("O")) {
            resultats.remove(index);
            System.out.println("Résultat supprimé.");
            return true;
        } else {
            System.out.println("Suppression annulée.");
            return false;
        }
    }

    public void supprimerTousLesResultats() {
        resultats.clear();
    }

    public void afficherPrescriptions() {
        if (prescriptions.isEmpty()) {
            System.out.println("Aucune prescription pour cette consultation.");
            return;
        }

        System.out.println("Prescriptions :");
        for (int i = 0; i < prescriptions.size(); i++) {
            System.out.println(i + " : " + prescriptions.get(i) + "\n");
        }

    }

    public boolean supprimerPrescription(int index) {
        if (index < 0 || index >= prescriptions.size()) {
            System.out.println("Indice invalide. Aucune prescription supprimée.");
            return false;
        }

        Prescription aSupprimer = prescriptions.get(index);
        System.out.println("Vous allez supprimer la prescription suivante :");
        System.out.println(aSupprimer);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Confirmez-vous la suppression ? (O/N) : ");
        String reponse = scanner.nextLine().trim().toUpperCase();

        if (reponse.equals("O")) {
            prescriptions.remove(index);
            System.out.println("Prescription supprimée.");
            return true;
        } else {
            System.out.println("Suppression annulée.");
            return false;
        }
    }

    public void supprimerToutesLesPrescriptions() {
        prescriptions.clear();
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

        StringBuilder sb = new StringBuilder();
        sb.append("Consultation {\n");
        sb.append("  id               : ").append(idConsultation).append("\n");
        sb.append("  date prévue      : ").append(dateFormatee).append("\n");
        sb.append("  créneau          : ").append(creneau.getHeureDebut()).append(" à ").append(creneau.getHeureFin())
                .append("\n");
        sb.append("  service          : '").append(service != null ? service : "").append("'\n");
        sb.append("  patient          : '").append(nomPatient).append("'\n");
        sb.append("  professionnel    : '").append(nomPro).append("'\n");
        sb.append("  est passée       : ").append(estPassee).append("\n");
        sb.append("  a été faite      : ").append(aEteFaite).append("\n");
        sb.append("  poids            : ").append(poidsStr).append("\n");
        sb.append("  diagnostic       : '").append(diag).append("'\n");

        sb.append("  Observations :\n");
        observations.forEach(o -> sb.append("    - ").append(o).append("\n"));

        sb.append("\n  Prescriptions :\n");
        prescriptions.forEach(p -> sb.append("    - ").append(p).append("\n"));

        sb.append("\n  Résultats d'analyse :\n");
        resultats.forEach(r -> sb.append("    - ").append(r).append("\n"));
        sb.append("}");

        return sb.toString();
    }

    public void save(Connection conn, int idUser) {
        try {

            // Vérifier si la consultation existe déjà

            boolean existe = false;

            String sqlCheck = "SELECT idConsultation FROM Consultations WHERE idConsultation = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCheck)) {
                pstmt.setInt(1, this.idConsultation);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next())
                        existe = true;
                }
            }

            // INSERT OU UPDATE dans Consultations

            if (!existe) {
                // INSERT
                String sqlInsert = "INSERT INTO Consultations(idConsultation, idPatient, idPro, datePrevue, service, diagnostic, poids, idDispo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                    pstmt.setInt(1, this.idConsultation);
                    pstmt.setInt(2, idUser);
                    pstmt.setInt(3, this.professionnelDeSante.getIdUser());
                    pstmt.setString(4, this.datePrevue.toString());
                    pstmt.setString(5, this.service);
                    pstmt.setString(6, this.diagnostic);
                    pstmt.setObject(7, this.poids);
                    pstmt.setObject(8, this.creneau != null ? this.creneau.getIdDispo() : null);
                    pstmt.executeUpdate();
                }

            } else {
                // UPDATE
                String sqlUpdate = "UPDATE Consultations SET idPatient=?, idPro=?, datePrevue=?, service=?, diagnostic=?, poids=?, idDispo=? WHERE idConsultation=?";

                try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                    pstmt.setInt(1, idUser);
                    pstmt.setInt(2, this.professionnelDeSante.getIdUser());
                    pstmt.setString(3, this.datePrevue.toString());
                    pstmt.setString(4, this.service);
                    pstmt.setString(5, this.diagnostic);
                    pstmt.setObject(6, this.poids);
                    pstmt.setObject(7, this.creneau != null ? this.creneau.getIdDispo() : null);
                    pstmt.setInt(8, this.idConsultation);
                    pstmt.executeUpdate();
                }
            }

            // Observations : delete + réinsertion

            try (PreparedStatement del = conn.prepareStatement("DELETE FROM Observations WHERE idConsultation=?")) {
                del.setInt(1, this.idConsultation);
                del.executeUpdate();
            }

            String sqlInsertObs = "INSERT INTO Observations(idConsultation, texte) VALUES (?, ?)";
            try (PreparedStatement ins = conn.prepareStatement(sqlInsertObs)) {
                for (String obs : observations) {
                    ins.setInt(1, this.idConsultation);
                    ins.setString(2, obs);
                    ins.executeUpdate();
                }
            }

            // Sauvegarder les prescriptions via leur propre méthode

            try (PreparedStatement del = conn.prepareStatement("DELETE FROM Prescriptions WHERE idConsultation=?")) {
                del.setInt(1, this.idConsultation);
                del.executeUpdate();
            }

            if (prescriptions != null) {
                for (Prescription p : prescriptions) {
                p.save(conn, this.idConsultation);
            }
            }

            // 5) Sauvegarder les résultats d’analyse via leur propre méthode

            try (PreparedStatement del = conn.prepareStatement("DELETE FROM ResultatsAnalyse WHERE idConsultation=?")) {
                del.setInt(1, this.idConsultation);
                del.executeUpdate();
            }

            if (resultats != null) {
                for (ResultatAnalyse r : resultats) {
                r.save(conn, this.idConsultation);
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}