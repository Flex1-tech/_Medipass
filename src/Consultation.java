import java.util.Date; 



public class Consultation {
    private int idConsultation;
    private Date prevuPour;
    private boolean estPassee;
    private boolean aEteFait;
    private Patient patient; 
    private Professionnel_de_Sante professionnelDeSante;
    
    
    public Consultation(int idConsultation, Patient patient, Professionnel_de_Sante professionnelDeSante, Date prevuPour, boolean estPassee, boolean aEteFait) {
        this.idConsultation = idConsultation;
        this.prevuPour = prevuPour;
        this.estPassee = estPassee;
        this.aEteFait = aEteFait;    
    }
    
    
    public void AfficherDossier() {
        System.out.println("Afficher le dossier médical du patient");
    }
    public void AjouterContenuDossier() {
        System.out.println("Ajout du compte rendu) au dossier médical");
    }
    public void ModifierDossier() {
        System.out.println("Modifier le dossier médical associé");
    }
    public int getIdConsultation() {
        return idConsultation;
    }
    public void setIdConsultation(int idConsultation) {
        this.idConsultation = idConsultation;
    }
    public Patient get_Patient() {
        return patient;
    }
    public void set_Patient(Patient patient) {
        this.patient = patient;
    }
    
    public Professionnel_de_Sante get_ProfessionnelDeSante() {
        return professionnelDeSante;
    }
    public void set_ProfessionnelDeSante(Professionnel_de_Sante professionnelDeSante) {
        this.professionnelDeSante = professionnelDeSante;
    }
    
    
    public Date getPrévuPour() {
        return prevuPour;
    }
    public void setPrévuPour(Date prevuPour) {
        this.prevuPour = prevuPour;
    }
    public boolean isEstPassée() {
        return estPassee;
    }
    public void setEstPassée(boolean estPassee) {
        this.estPassee = estPassee;
    }
    public boolean isaEteFait() {
        return aEteFait;
    }
    public void setaEteFait(boolean aEteFait) {
        this.aEteFait = aEteFait;
    }
}