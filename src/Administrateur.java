import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;


public class Administrateur extends User{
	
	public Administrateur(String nom, String prenom, String telephone, String motDePasse, String adresse) {
		super();
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.set_MotDePasse(motDePasse);
        this.adresse = adresse;
	}
	
	public void creerUtilisateur() {
		Scanner scanner=new Scanner(System.in);
		
		System.out.print("Entrez le nom de l'utilisateur: ");
		String nomUser=scanner.nextLine();
		
		System.out.print("Entrez son/ses prénom(s): ");
		String prenomUser=scanner.nextLine();
		
		System.out.print("Entrez son numéro de téléphone: ");
		String telUser=scanner.nextLine();
		
		System.out.print("Entrez son adresse: ");
		String adressUser=scanner.nextLine();
		
		System.out.print("Entrez son mot de passe:");
		String passwordUser=scanner.nextLine();
		String hashedPassword=BCrypt.hashpw(passwordUser,BCrypt.gensalt());
		
		String table=null;
		String insertSql=null;
		String role=null;
		boolean estProSante=false;
		
		System.out.println("1 pour création de compte administrateur\n "
					+ "2 pour création de compte patient\n "
					+ "3 pour création de compte de professionnel de santé\n");
		int choix=scanner.nextInt();
		scanner.nextLine();
		
		if(choix==1) {
			table="Administrateurs";
			insertSql="INSERT INTO Administrateurs (nom,prenom,telephone,adresse,motDePasse) VALUES (?,?,?,?,?)";
			role="administrateur";
		}
		else if(choix==2) {
			table="Patients";
			insertSql="INSERT INTO Patients(nom,prenom,telephone,adresse,motDePasse) VALUES(?,?,?,?,?)";
			role="patient";
		}
		else if(choix==3) {
			table="ProfessionnelSante";
			insertSql="INSERT INTO ProfessionnelSante(nom,prenom,telephone,adresse,motDePasse,categorie) VALUES(?,?,?,?,?,?)";
			estProSante=true;
			role="professionnel de santé";
		}
		else {
			System.out.println("Choix invalide.");
		}
		
		
		String checkSql="SELECT COUNT(*) FROM "+table+" WHERE nom=? AND prenom=? AND telephone=?";
		
		try(Connection conn=DriverManager.getConnection(url);
			PreparedStatement checkStmt=conn.prepareStatement(checkSql)){
			checkStmt.setString(1, nomUser);
			checkStmt.setString(2, prenomUser);
			checkStmt.setString(3, telUser);
			
			try(ResultSet rs=checkStmt.executeQuery()){
				if(rs.next() && rs.getInt(1)>0) {
					System.out.println("L'utilisateur "+nomUser+" "+prenomUser+" existe déja.");
					return;
				}
			}//Vérifier unicité du compte
			
			try(PreparedStatement stmt=conn.prepareStatement(insertSql)){
						stmt.setString(1,nomUser);
						stmt.setString(2,prenomUser);
						stmt.setString(3,telUser);
						stmt.setString(4,adressUser);
						stmt.setString(5,hashedPassword);
						
						if(estProSante) {
							System.out.print("Entrez la catégorie du professionnel de santé: ");
							String categorie=scanner.nextLine();
							stmt.setString(6, categorie);
						}
						
						int rows=stmt.executeUpdate();
						if(rows>0) {
							System.out.println(nomUser+"a été créé avec succès en tant que "+role);
						}
			 }
			
		}
			
			catch(SQLException e) {
				e.printStackTrace();
			}
    }
	
	public void modifierInfosUtilisateur() {
		Scanner scanner=new Scanner(System.in);
		
		System.out.print("Entrez le nom actuel de l'utilisateur à modifier: ");
		String nomUser=scanner.nextLine();
		
		System.out.print("Entrez son/ses prénom(s) actuel: ");
		String prenomUser=scanner.nextLine();
		
		String table=null;
		String updateSql=null;
		boolean estProSante=false;
		
			System.out.println("1 pour modifier compte administrateur\n"
					+ "2 pour modifier compte patient\n"
					+ "3 pour modifier compte professionnel de santé\n");
			int choix=scanner.nextInt();
			
			if(choix==1) {
				table="Administrateurs";
				updateSql="UPDATE Administrateurs SET nom=?,prenom=?,adresse=?,telephone=? WHERE nom=? AND prenom=?";
			}
			else if(choix==2) {
				table="Patients";
				updateSql="UPDATE Patients SET nom=?,prenom=?,adresse=?,telephone=?,motDePasse=? WHERE nom=? AND prenom=?";
			}
			else if(choix==3) {
				table="ProfessionnelSante";
				updateSql="UPDATE ProfessionnelSante SET nom=?,prenom=?,telephone=?,motDePasse=? WHERE nom=? AND prenom=?";
				estProSante=true;
			}
			else {
				System.out.println("Choix invalide.Veuillez réesayez.");
			}
			
		
		String checkExistSql="SELECT COUNT(*) FROM "+table+" WHERE nom=? AND prenom=?";
		
		try(Connection conn=DriverManager.getConnection(url);
				PreparedStatement checkExistStmt=conn.prepareStatement(checkExistSql)){
			checkExistStmt.setString(1, nomUser);
			checkExistStmt.setString(2, prenomUser);
			
			try(ResultSet rs=checkExistStmt.executeQuery()){
				if(rs.next() || rs.getInt(1)==0) {
					System.out.println("Aucun utilisateur trouvé avec ces nom et prénom(s).");
					return;
				}
			}//Vérifier que l'utilisateur existe
			
			System.out.print("Nouveau nom: ");
			String newNomUser=scanner.nextLine();
			
			System.out.println("Nouveau prénom: ");
			String newPrenomUser=scanner.nextLine();
			
			System.out.print("Nouvelle adresse: ");
			String newAdressUser=scanner.nextLine();
			
			System.out.print("Nouveau numéro de téléphone: ");
			String newTelUser=scanner.nextLine();
			
			System.out.print("Nouveau mot de passe: ");
			String newPasswordUser=scanner.nextLine();
			String hashedPassword=BCrypt.hashpw(newPasswordUser,BCrypt.gensalt());
			
			
			String checkUniqueSql = "SELECT COUNT(*) FROM " + table + " WHERE nom=? AND prenom=? AND telephone=?";
	        try (PreparedStatement checkUniqueStmt = conn.prepareStatement(checkUniqueSql)) {
	            checkUniqueStmt.setString(1, newNomUser);
	            checkUniqueStmt.setString(2, newPrenomUser);
	            checkUniqueStmt.setString(3, newTelUser);
	            try (ResultSet rs = checkUniqueStmt.executeQuery()) {
	                if (rs.next() && rs.getInt(1) > 0) {
	                    System.out.println("Erreur : un utilisateur avec ce nom, prénom et numéro de téléphone existe déja.");
	                    return;
	                }
	            }
	        }//Vérifier unicité 


			try(PreparedStatement stmt=conn.prepareStatement(updateSql)){
				stmt.setString(1, newNomUser);
	            stmt.setString(2, newPrenomUser);
	            stmt.setString(3, newAdressUser);
	            stmt.setString(4, newTelUser);
	            stmt.setString(5, hashedPassword);
	            
	            if(estProSante){
	            	System.out.print("Nouvelle catégorie: ");
	            	String newCategorie=scanner.nextLine();
	            	stmt.setString(6, newCategorie);
	            	stmt.setString(7, nomUser);
	            	stmt.setString(8, prenomUser);
	            }
	            
	            else{
	            	stmt.setString(6, nomUser);
	            	stmt.setString(7, prenomUser);
	            }

	            int rows = stmt.executeUpdate();
	            if (rows > 0) {
	                System.out.println("Informations mises à jour avec succès !");
	            }
	        }
			
		}

	        catch (SQLException e) {
	            e.printStackTrace();
	        }
			
	 }
		
	
	
    public void supprimerUtilisateur() {
		Scanner scanner=new Scanner(System.in);
		
		System.out.print("Entrez le nom de l'utilisateur à supprimer: ");
		String nomUser=scanner.nextLine();
		
		System.out.print("Entrez son/ses prénoms(s): ");
		String prenomUser=scanner.nextLine();
		
		
		System.out.println("1 pour suppression de compte administrateur\n "
				+ "2 pour suppression de compte patient\n "
				+ "3 pour suppression de compte de professionnel de santé\n");
		int choix=scanner.nextInt();
		
		String table=null;
		String role=null;
		
		if(choix==1) {
			table="Administrateurs";
			role="administrateur";
		}
		else if(choix==2) {
			table="Patients";
			role="patient";
		}
		else if(choix==3) {
			table="ProfessionnelSante";
			role="professionnel de santé";
		}
		else {
			System.out.println("Choix invalide.");
		}
		
		String checkSql="SELECT COUNT(*) FROM "+table+" WHERE nom=? AND prenom=?";
		String deleteSql="DELETE FROM "+table+" WHERE nom=? AND prenom=?";
		
		try(Connection conn=DriverManager.getConnection(url);
				PreparedStatement checkStmt=conn.prepareStatement(checkSql)){
			
			checkStmt.setString(1, nomUser);
			checkStmt.setString(2, prenomUser);
			
			try(ResultSet rs=checkStmt.executeQuery()){
				if(!rs.next() || rs.getInt(1)==0) {
					System.out.print("Aucun utilisateur trouvé avec ces nom et prénom(s).");
					return;
				}
			}//vérifier l'existence de l'utilisateur
			
			try(PreparedStatement stmt=conn.prepareStatement(deleteSql)){
				stmt.setString(1, nomUser);
				stmt.setString(2, prenomUser);
				
				int rows=stmt.executeUpdate();
				if(rows>0) {
					System.out.println(nomUser+" "+prenomUser+" a été supprimé en tant que "+role+" avec succès.");
				}
			}
				
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
    
    
    public void afficherStatsSysteme() {
    	
    	String sqlPatients= "SELECT COUNT(*) AS total FROM Patients";
    	String sqlProSante="SELECT COUNT(*) AS total FROM ProfessionnelSante";
    	String sqlAdmins="SELECT COUNT(*) AS total FROM Administrateurs";
    	String sqlCategories="SELECT categorie, COUNT(*) AS total FROM ProfessionnelSante GROUP BY categorie";
    	
    	try(Connection conn=DriverManager.getConnection(url)){
    		
    		
    		try(PreparedStatement stmt=conn.prepareStatement(sqlPatients);
    				ResultSet rs=stmt.executeQuery()){
    			
    			if(rs.next()) {
    				System.out.println("Nombre total de patients: "+rs.getInt("total"));
    			}
    		}
    		
    		
    		try(PreparedStatement stmt=conn.prepareStatement(sqlProSante);
    				ResultSet rs=stmt.executeQuery()){
    			if(rs.next()) {
    				System.out.println("Nombre total de professionnels de santé: "+rs.getInt("total"));
    			}
    		}
    		
    		
    		try(PreparedStatement stmt=conn.prepareStatement(sqlAdmins);
    				ResultSet rs=stmt.executeQuery()){
    			if(rs.next()) {
    				System.out.println("Nombre total d'administrateurs: "+rs.getInt("total"));
    			}
    		}
    		
    		//Nombre de pros par categories
    		System.out.println("\nRépartition des professionnels de santé par catégorie: ");
    		try(PreparedStatement stmt=conn.prepareStatement(sqlCategories);
    				ResultSet rs=stmt.executeQuery()){
    			while(rs.next()) {
    				String categorie=rs.getString("categorie");
    				int total=rs.getInt("total");
    				System.out.println(categorie+" : "+total);
    			}
    		}
    		
    	}
    	
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	
    }
	
		
}
 
	
	
