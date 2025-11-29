public class Prescription {
    private String medicament;
    private String posologie;
    private String duree;

    public Prescription(String medicament, String posologie, String duree) {
        this.medicament = medicament;
        this.posologie = posologie;
        this.duree = duree;
    }

    // getters/setters

    public String getMedicament() {
        return medicament;
    }

    public String getPosologie() {
        return posologie;
    }
    public String getDuree() {
        return duree;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }
    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }
    public void setDuree(String duree) {
        this.duree = duree;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Prescription {\n");

        sb.append("  medicament  : ").append(medicament != null ? medicament : "N/A").append("\n");
        sb.append("  posologie   : ").append(posologie != null ? posologie : "N/A").append("\n");
        sb.append("  duree       : ").append(duree != null ? duree : "N/A").append("\n");
        sb.append("}");

        return sb.toString();
    }
    
}