package model;
public class Administrateur extends User {
	public Administrateur(String nom, String prenom, String telephone, String motDePasse, String adresse) {
		super();
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
	}
}
