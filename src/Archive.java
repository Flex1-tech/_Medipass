import StatistiqueDuSystem.DatePerso;

public class Archive {

	public static void main(String[] args) {
		

	}
	
	class Patient {
		 private int idUser;
	       private String nom;
	       private String prenoms;
	       private String sexe;
	       private int telephone;
	       private String motDePasse;
	       private String droitsAcces;
	       private DatePerso dateDerniereConsultation;
	       public Patient (int idUser, String nom, String prenoms, String sexe, int telephone,
                       String motDePasse, String droitsAcces, DatePerso dateDerniereConsultation) {
	    	   
	    	   this.idUser = idUser;
	            this.nom = nom;
	            this.prenoms = prenoms;
	            this.sexe = sexe;
	            this.telephone = telephone;
	            this.motDePasse = motDePasse;
	            this.droitsAcces = droitsAcces;
	            this.dateDerniereConsultation = dateDerniereConsultation;
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
	

}
