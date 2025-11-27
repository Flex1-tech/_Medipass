import java.time.LocalDate;

public class Antecedant {

    public enum Type {
        PATHOLOGIE,
        CHIRURGIE,
        ALLERGIE,
        TRAITEMENT,
        AUTRE
    }

    private int idAntecedant;
    private Type type;
    private String description;
    private LocalDate date;

    public Antecedant(int idAntecedant, Type type, String description, LocalDate date) {
        this.idAntecedant = idAntecedant;
        this.type = type;
        this.description = description;
        this.date = date;
    }

    public int getIdAntecedant() {
        return idAntecedant;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return (type + " : " + description + (date != null ? (" (" + date + ")") : ""));
    }
}