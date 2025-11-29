
import java.util.Date;
import java.util.ArrayList ;
import java.util.List ;

public class StatistiqueDuSystem  {

    public static void main(String[] args) {
    	StatistiquesDuSystem  system = new StatistiquesDuSystem();
    	// ajout de patients
    	Patient p1 = new Patient (2007,"DOSSOU","Patrick","M",47567654 ,"pa4756@","1" ,new DatePerso(12,01,2007 ,11,22));
    	Patient p2 = new Patient (2014,"EZIH","Prince","M",48890536 ,"pr789@","1", new DatePerso(13 ,07,2005 ,11 ,43));
    	Patient p3 = new Patient (2001,"AMERRI","Juste","M",26980432,"ju@123","1", new DatePerso(02,04,2023, 18 ,43));
    
    	system.ajouterPatient(p1);
    	system.ajouterPatient(p2);
    	system.ajouterPatient(p3);
    	
    	// programmer les professionnels de Sante 
    	
    	ProfessionnelDeSante pr1 = new ProfessionnelDeSante ( 1990,  "ANDE","Destin","M",90876900,"opfr2309@","2","Medecin");
    	ProfessionnelDeSante pr2 = new ProfessionnelDeSante (3098,"ANDE","Destin","M",90876900,"opfr2309@","2","Medecin");
    	ProfessionnelDeSante pr3 = new ProfessionnelDeSante (3096,"ANDE","Destin","M",90876900,"opfr2309@","2","Infirmier");
    	ProfessionnelDeSante pr4 = new ProfessionnelDeSante (1910,"ANDE","Destin","M",90876900,"opfr2309@","2","Infirmier");
    	ProfessionnelDeSante pr5 = new ProfessionnelDeSante (1920,"ANDE","Destin","M",90876900,"opfr2309@","2","Pharmacien");
    	ProfessionnelDeSante pr6 = new ProfessionnelDeSante (1930,"ANDE","Destin","M",90876900,"opfr2309@","2","Pharmacien");
    	ProfessionnelDeSante pr7 = new ProfessionnelDeSante (1940  ,"ANDE","Destin","M",90876900,"opfr2309@","2","AideSoignante");
    	ProfessionnelDeSante pr8 = new ProfessionnelDeSante (1950 ,"ANDE","Destin","M",90876900,"opfr2309@","2","AideSoignante");
    	
    	
    	system.programmerProfessionnelDeSante (pr1);
    	system.programmerProfessionnelDeSante (pr2);
    	system.programmerProfessionnelDeSante (pr3);
    	system.programmerProfessionnelDeSante (pr4);
    	system.programmerProfessionnelDeSante (pr5);
    	system.programmerProfessionnelDeSante (pr6);
    	system.programmerProfessionnelDeSante (pr7);
    	system.programmerProfessionnelDeSante (pr8);
    	
    	
    	
    	Medecin m1 = new Medecin (1990,  "ANDE","Destin","M",90876900,"opfr2309@","2","Medecin");
    	Medecin m2 = new Medecin (3098,"ANDE","Destin","M",90876900,"opfr2309@","2","Medecin");
    	Infirmier i1 = new Infirmier (3096,"ANDE","Destin","M",90876900,"opfr2309@","2","Infirmier");
    	Infirmier i2 = new Infirmier (1910,"ANDE","Destin","M",90876900,"opfr2309@","2","Infirmier");
    	Pharmacien ph1 = new Pharmacien (1920,"ANDE","Destin","M",90876900,"opfr2309@","2","Pharmacien");
    	Pharmacien ph2 = new Pharmacien (1930,"ANDE","Destin","M",90876900,"opfr2309@","2","Pharmacien");
    	AideSoignante A1 = new AideSoignante (1940  ,"ANDE","Destin","M",90876900,"opfr2309@","2","AideSoignante");
    	AideSoignante A2 = new AideSoignante (1950 ,"ANDE","Destin","M",90876900,"opfr2309@","2","AideSoignante");
    	
    	
    	system.programmerMedecin(m1);
    	system.programmerMedecin(m2);
    	system.programmerInfirmier(i1);
    	system.programmerInfirmier(i2);
    	system.programmerPharmacien(ph1);
    	system.programmerPharmacien(ph2);
    	system.programmerAideSoignante(A1);
    	system.programmerAideSoignante(A2);
    	
    	
    	
    	
    	// programmer les consultations
    	
    	
    	
    	
    	Consultation c1 = new Consultation(1000,new DatePerso(11,02,2009 ,12,14) ,true,true );
    	Consultation c2 = new Consultation(1002 ,new DatePerso(11,02,2009 ,12,14) ,true,false );
    	Consultation c3 = new Consultation(1003,new DatePerso(11,02,2009 ,12,14) ,true,true );
    	Consultation c4 = new Consultation(1004 ,new DatePerso(11,02,2009 ,12,14) ,true,false );
    	
    	
    	
    	
    	system.programmerConsultation(c1);
    	system.programmerConsultation(c2);
    	system.programmerConsultation(c3);
    	system.programmerConsultation(c4);
    	
    	
    	ConsultationMois cm1 = new ConsultationMois(1000,new DatePerso(11,02,2009,12,14) ,true,true,"Aout");
    	ConsultationMois cm2 = new ConsultationMois(1002,new DatePerso(11,02,2009 ,12,14) ,true,false,"Janvier" );
    	
    	
    	
    	system.programmerConsultationMois(cm1);
    	system.programmerConsultationMois(cm2);
    	
    	
    	ConsultationAnnee cA1 = new ConsultationAnnee(1000,new DatePerso(11,02,2009 ,12,14) ,true,true,"2007" );
    	ConsultationAnnee cA2 = new ConsultationAnnee(1002 ,new DatePerso(11,02,2009 ,12,14) ,true,false,"2007" );
    	ConsultationAnnee cA3 = new ConsultationAnnee(1003,new DatePerso(11,02,2009 ,12,14) ,true,true,"2008" );
    	ConsultationAnnee cA4 = new ConsultationAnnee(1004 ,new DatePerso(11,02,2009 ,12,14) ,true,false,"2008" );
    	
    	
    	system.programmerConsultationAnnee(cA1);
    	system.programmerConsultationAnnee(cA2);
    	system.programmerConsultationAnnee(cA3);
    	system.programmerConsultationAnnee(cA4);
    	
    	
    	
    	
    	
    	
    	//Affichage du Statistique du System 
    	
    	
    	System.out.println(" ===STATISTIQUE DU SYSTEM ===\n");
    	System.out.println(" Nombre de Patients:" + system.Patients.size () );
    	System.out.println(" Nombre de Professionnels de sante : " +system.ProfessionnelDeSantes.size ());
    	System.out.println("Nombre de Medecin: "+ system.Medecins.size());
    	System.out.println("Nombre d'Infimier : "+ system.Infirmiers.size());
    	System.out.println("Nombre de Pharmacien :" + system.Pharmaciens.size());
    	System.out.println("Nombre d'AideSoignante :"+ system.AideSoignantes.size());
    	System.out.println("Nombre de Consultation :" + system.Consultations.size());
    	System.out.println("Nombre de Consultation par Mois :" + system.ConsultationMois.size());
    	System.out.println("Nombre de Consultation par Annee:" + system.ConsultationAnnee.size());
    	
    	
    	
    }

    static class Utilisateur {
       private int idUser;
       private String nom;
       private String prenoms;
       private String sexe;
       private int telephone;
       private String motDePasse;
       private String droitsAcces;

        public Utilisateur(int idUser, String nom, String prenoms, String sexe, int telephone,
                           String motDePasse, String droitsAcces) {
            this.idUser = idUser;
            this.nom = nom;
            this.prenoms = prenoms;
            this.sexe = sexe;
            this.telephone = telephone;
            this.motDePasse = motDePasse;
            this.droitsAcces = droitsAcces;
        }
        public int  getIdUser () {
        	return idUser ;
        }
        public String getNom () {
        	return nom ;
        }
        public String getPrenoms () {
        	return prenoms ;
        }
        public String getSexe () {
        	return sexe ;
        }
        public int  getTelephone () {
        	return telephone ;
        }
        public String getMotDePasse () {
        	return motDePasse ;
        }
        public String getDroitsAcces () {
        	return droitsAcces ;
        }
        
        	
        }

       
    

    static class Patient extends Utilisateur {
        private DatePerso dateDerniereConsultation;

        public Patient(int idUser, String nom, String prenoms, String sexe, int telephone,
                       String motDePasse, String droitsAcces, DatePerso dateDerniereConsultation) {
            super(idUser, nom, prenoms, sexe, telephone, motDePasse, droitsAcces);
            this.dateDerniereConsultation = dateDerniereConsultation;
        }
    }

    static class DatePerso { 
        private int jour;
        private int mois;
        private int annee;
        private int heure;
        private int minute;

        public DatePerso(int jour, int mois, int annee, int heure, int minute) {
            this.jour = jour;
            this.mois = mois;
            this.annee = annee;
            this.heure = heure;
            this.minute = minute;
        }
        public String getDatePerso() {
        	return jour + "/" +  mois + "/" + annee  + " " + heure + " " + minute    ; 
        }
        
        }
    

    static class ProfessionnelDeSante extends Utilisateur {
        private String titre;

        public ProfessionnelDeSante(int idUser, String nom, String prenoms, String sexe, int telephone,
                                    String motDePasse, String droitsAcces, String titre) {
            super(idUser, nom, prenoms, sexe, telephone, motDePasse, droitsAcces);
            this.titre = titre;
        }
        public String getTitre () {
        	return titre ;
        }
    }

    static class Medecin extends ProfessionnelDeSante {
        public Medecin(int idUser, String nom, String prenoms, String sexe, int telephone,
                       String motDePasse, String droitsAcces, String titre) {
            super(idUser, nom, prenoms, sexe, telephone, motDePasse, droitsAcces, titre);
        }
        
    }

   static class Infirmier extends ProfessionnelDeSante {
        public Infirmier(int idUser, String nom, String prenoms, String sexe, int telephone,
                         String motDePasse, String droitsAcces, String titre) {
            super(idUser, nom, prenoms, sexe, telephone, motDePasse, droitsAcces, titre);
        }
    }

    static class Pharmacien extends ProfessionnelDeSante {
        public Pharmacien(int idUser, String nom, String prenoms, String sexe, int telephone,
                         String motDePasse, String droitsAcces, String titre) {
            super(idUser, nom, prenoms, sexe, telephone, motDePasse, droitsAcces, titre);
        }
    }

    static class AideSoignante extends ProfessionnelDeSante {
        public AideSoignante(int idUser, String nom, String prenoms, String sexe, int telephone,
                             String motDePasse, String droitsAcces, String titre) {
            super(idUser, nom, prenoms, sexe, telephone, motDePasse, droitsAcces, titre);
        }
    }

    static class Consultation {
        private int idConsultation;
        private DatePerso prevuPour;
        private boolean estPasse;
        private boolean aEteFait;

        public Consultation(int idConsultation, DatePerso prevuPour, boolean estPasse, boolean aEteFait) {
            this.idConsultation = idConsultation;
            this.prevuPour = prevuPour;
            this.estPasse = estPasse;
            this.aEteFait = aEteFait;
        }
        public int  getIdConsultation () {
        	return idConsultation ;
        }
        public DatePerso getPrevuPour () {
        	return prevuPour ;
        }
        public boolean getEstPasse () {
        	return estPasse ;
        }
        public boolean getAEteFait() {
        	return aEteFait ;
        }
        
        
    }
    static class ConsultationMois  extends Consultation {
    	String periodeM ;
    	public ConsultationMois (int idConsultation,DatePerso prevuPour,boolean estPasse,boolean aEteFait,String periodeM){
    		super ( idConsultation,  prevuPour,  estPasse,  aEteFait);
    		
            this.periodeM = periodeM ;
           
    	}
    	public  String getPeriodeM () {
    	   return periodeM ;
    	}
    	
        	
    	
    }
    
    static class ConsultationAnnee  extends Consultation {
    	String periodeA ;
    	public ConsultationAnnee (int idConsultation,DatePerso prevuPour,boolean estPasse,boolean aEteFait,String periodeA){
    		super ( idConsultation,  prevuPour,  estPasse,  aEteFait);
    		
            this.periodeA = periodeA ;
           
    	}
    	public  String getPeriodeA () {
    	   return periodeA ;
    	}
        	
    	
    }
    
    

    
    
    
   static class StatistiquesDuSystem {
    	public List<Patient> Patients ;
    	public List<ProfessionnelDeSante> ProfessionnelDeSantes ;
    	public List<Medecin> Medecins ;
    	public List<Infirmier> Infirmiers ;
    	public List<Pharmacien> Pharmaciens ;
    	public List<AideSoignante> AideSoignantes ;
    	public List<Consultation> Consultations ;
    	public List<ConsultationMois> ConsultationMois ;
    	public List<ConsultationAnnee> ConsultationAnnee ;
    
    	
    	public StatistiquesDuSystem () {
    		this.Patients = new ArrayList<>();
    		this.ProfessionnelDeSantes = new ArrayList<>();
    		this.Medecins = new ArrayList<>() ;
    		this.Infirmiers = new ArrayList<>();
    		this.Pharmaciens = new ArrayList<>();
    		this.AideSoignantes = new ArrayList<>() ;
    		this.Consultations = new ArrayList<>();
    		this.ConsultationMois = new ArrayList<>();
    		this.ConsultationAnnee = new ArrayList<>();
    	
    		
    	}
    	public void ajouterPatient(Patient p) {
    		Patients.add(p);
    	
    	}
    	public void supprimerPatient(Patient patient) {
    		Patients.remove(patient);
    	}
    	
    	public void programmerProfessionnelDeSante(ProfessionnelDeSante professionnelDeSante) {
    		ProfessionnelDeSantes.add(professionnelDeSante);
    		
    	}
    	public void deprogrammerProfessionnelDeSante(ProfessionnelDeSante professionnelDeSante) {
    		ProfessionnelDeSantes.remove(professionnelDeSante);
    		
    	}
    	
    		
    	
    	public void programmerMedecin(Medecin medecin) {
    		Medecins.add(medecin);
    		
    	}
    	public void deprogrammerMedecin( Medecin medecin) {
    		Medecins.remove(medecin );
    		
    	}
    	
    		
    	
    	public void programmerInfirmier(Infirmier infirmier) {
    		Infirmiers.add(infirmier);
    		
    	}
    	public void deprogrammerInfirmier(Infirmier infirmier) {
    		Infirmiers.remove(infirmier);
    		
    	}
    	
    		
    	
    	public void programmerPharmacien(Pharmacien pharmacien) {
    		Pharmaciens.add(pharmacien);
    		
    	}

    	public void deprogrammerPharmacien(Pharmacien pharmacien) {
    		Pharmaciens.remove(pharmacien);
    		
    	}
    	public void programmerAideSoignante(AideSoignante aideSoignante) {
    		AideSoignantes.add(aideSoignante);
    		
    	}public void deprogrammerAideSoignante(AideSoignante aideSoignante) {
    		AideSoignantes.remove(aideSoignante);
    		
    	}
    	public void programmerConsultation(Consultation consultation){
    		Consultations.add(consultation);
    		
    	}
    	public void programmerConsultationMois( ConsultationMois consultationMois ) {
    		ConsultationMois.add(consultationMois);
    	}
    	public void programmerConsultationAnnee(ConsultationAnnee consultationAnnee) {
    		ConsultationAnnee.add(consultationAnnee);
    		
    	}
    	
    	

    	
    	
    	
    }
    
    
}
