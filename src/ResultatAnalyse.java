public class ResultatAnalyse {
    private String typeAnalyse; // "Glycémie", "CRP", "VIH", etc.
    private String valeur; // "8.2", "Négatif", "++"
    private String unite; // "mg/L" ou null si aucune
    private String interpretation; // "Normal", "Élevé", "Réactif"

    public ResultatAnalyse(String typeAnalyse, String valeur, String unite, String interpretation) {
        this.typeAnalyse = typeAnalyse;
        this.valeur = valeur;
        this.unite = unite;
        this.interpretation = interpretation;
    }

    public String getTypeAnalyse() {
        return typeAnalyse;
    }

    public String getValeur() {
        return valeur;
    }

    public String getUnite() {
        return unite;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public String getValeurComplete() {
        return unite == null ? valeur : valeur + " " + unite;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ResultatAnalyse {\n");

        sb.append("  typeAnalyse     : ").append(typeAnalyse).append("\n");
        sb.append("  valeur          : ").append(valeur);

        if (unite != null && !unite.isBlank()) {
            sb.append(" ").append(unite); // Ajout de l’unité seulement si elle existe
        }
        sb.append("\n");

        sb.append("  interpretation  : ").append(
                interpretation != null ? interpretation : "N/A").append("\n");

        sb.append("}");

        return sb.toString();
    }

}