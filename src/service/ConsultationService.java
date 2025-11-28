package service;

import model.*;
import dao.*;

import java.time.LocalDate;

public class ConsultationService {

    private ConsultationDAO consultationDAO;

    public ConsultationService() {
        this.consultationDAO = new ConsultationDAO();
    }

    public Consultation programmerConsultation(
            Patient patient,
            Professionnel_de_Sante pro,
            String service,
            LocalDate date,
            Disponibilite creneau
    ) {
        if (creneau.getEstReservee())
            throw new IllegalArgumentException("Créneau déjà réservé.");

        Consultation c = new Consultation( patient, pro, service, date, creneau);

        creneau.reserver();

        consultationDAO.insert(c);
        return c;
    }

    public void marquerConsultationFaite(Consultation c, String diagnostic) {
        c.marquerCommeFaite(diagnostic);
        consultationDAO.update(c);
    }
}
