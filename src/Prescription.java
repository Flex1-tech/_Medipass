public class Prescription {
    private String medicament;
    private String posologie;

    public Prescription(String medicament, String posologie) {
        this.medicament = medicament;
        this.posologie = posologie;
    }

    // getters/setters

    public String getMedicament() {
        return medicament;
    }

    public String getPosologie() {
        return posologie;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Prescription {\n");

        sb.append("  medicament  : ").append(medicament != null ? medicament : "N/A").append("\n");
        sb.append("  posologie   : ").append(posologie != null ? posologie : "N/A").append("\n");
        
        sb.append("}");

        return sb.toString();
    }
    
}