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
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
	}
	public Administrateur(int idUser, String nom, String prenom, String telephone, String adresse) {
		this.idUser=idUser;
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
		this.adresse = adresse;
	}
	
	public void creerUtilisateur() {
		Scanner scanner=new Scanner(System.in);
		
		System.out.print("\nEntrez le nom de l'utilisateur: ");
		String nomUser=scanner.nextLine().trim();
		
		System.out.print("Entrez son/ses prénom(s): ");
		String prenomUser=scanner.nextLine().trim();
		
		System.out.print("Entrez son numéro de téléphone: ");
		String telUser=scanner.nextLine().trim();
		
		System.out.print("Entrez son adresse: ");
		String adressUser=scanner.nextLine().trim();
		
		System.out.print("Entrez son mot de passe: ");
		String passwordUser=scanner.nextLine().trim();
		String hashedPassword=BCrypt.hashpw(passwordUser,BCrypt.gensalt());
		
		String typeUser=null;
		String role=null;
		String titre=null;
		boolean estProSante=false;
		
		while(true) {
			System.out.println("\n1 pour création de compte administrateur\n"
					+"2 pour création de compte patient\n"
					+"3 pour création de compte de professionnel de santé\n");
			System.out.print("Choix: ");
			
			if(!scanner.hasNextInt()) {
				System.out.println("Vous devez entrer un nombre entre 1 , 2 ou 3");
				scanner.nextLine();
				continue;
			}
			
			int choix=scanner.nextInt();
			scanner.nextLine();
			
			switch(choix) {
            case 1: typeUser="admin"; 
                    role="administrateur";
                    break;
                    
            case 2: typeUser="patient"; 
                    role="patient";
                    break;
                    
            case 3: typeUser="pro";
                    estProSante=true;
                    role="professionnel de santé";
                    System.out.print("\nEntrez la catégorie du professionnel de santé: ");
                    titre = scanner.nextLine().trim();
                    break;
                    
            default: System.out.println("Choix invalide"); 
                     continue;
        }
			
        break;
        
        }

        String checkSql="SELECT COUNT(*) FROM Users WHERE nom=? AND prenom=?";
        String checkTelSql="SELECT COUNT(*) FROM Users WHERE telephone=? AND (nom<>? OR prenom<>?)";
        String insertSql="INSERT INTO Users(nom, prenom, telephone, adresse, motDePasse, typeUser) VALUES(?,?,?,?,?,?)";

        try(Connection conn=DriverManager.getConnection(url);
             PreparedStatement checkStmt=conn.prepareStatement(checkSql)) {

               checkStmt.setString(1, nomUser);
               checkStmt.setString(2, prenomUser);

               try(ResultSet rs=checkStmt.executeQuery()) {
                       if(rs.next() && rs.getInt(1) > 0) {
                              System.out.println("\nL'utilisateur " + nomUser + " " + prenomUser + " existe déjà.");
                              return;
                        }
                }
               
               try(PreparedStatement stmt=conn.prepareStatement(checkTelSql)) {
                   stmt.setString(1, telUser);
                   stmt.setString(2, nomUser);
                   stmt.setString(3, prenomUser);
                   try (ResultSet rs=stmt.executeQuery()) {
                       if (rs.next() && rs.getInt(1) > 0) {
                           System.out.println("\nAttention : ce numéro de téléphone est déjà utilisé par un autre utilisateur.");
                           return;
                       }
                   }
               }//Vérifier unicité


               int userId=-1;
               try(PreparedStatement stmt=conn.prepareStatement(insertSql)) {
                      stmt.setString(1, nomUser);
                      stmt.setString(2, prenomUser);
                      stmt.setString(3, telUser);
                      stmt.setString(4, adressUser);
                      stmt.setString(5, hashedPassword);
                      stmt.setString(6, typeUser);
                      
                      stmt.executeUpdate();

                      try (ResultSet keys=stmt.getGeneratedKeys()) {
                          if (keys.next()) {
                              userId=keys.getInt(1);
                          }
                      }
                  
               }
               
               if(userId!=-1) {
                   if(estProSante) {
                       try (PreparedStatement stmt=conn.prepareStatement(
                               "INSERT INTO ProfessionnelSante(user_id, titre) VALUES(?, ?)")) {
                           stmt.setInt(1, userId);
                           stmt.setString(2, titre);
                           stmt.executeUpdate();
                       }
                   } 
                   
                   else if("admin".equals(typeUser)) {
                       try (PreparedStatement stmt=conn.prepareStatement(
                               "INSERT INTO Administrateurs(user_id) VALUES(?)")) {
                           stmt.setInt(1, userId);
                           stmt.executeUpdate();
                       }
                   } 
                   
                   else if("patient".equals(typeUser)) {
                       try (PreparedStatement stmt=conn.prepareStatement(
                               "INSERT INTO Patients(user_id) VALUES(?)")) {
                           stmt.setInt(1, userId);
                           stmt.executeUpdate();
                       }
                   }
                   
                   System.out.println("\n"+nomUser+" " +prenomUser+" a été créé avec succès en tant que "+role);
               }

        } 
      
        catch (SQLException e) {
             e.printStackTrace();
        }

    }
	
	public void modifierInfosUtilisateur() {
		Scanner scanner=new Scanner(System.in);
		
		System.out.print("\nEntrez le nom actuel de l'utilisateur à modifier: ");
		String nomUser=scanner.nextLine().trim();
		
		System.out.print("Entrez son/ses prénom(s) actuel: ");
		String prenomUser=scanner.nextLine().trim();
		
		String typeUser=null;
		boolean estProSante=false;
		
		while(true) {
			System.out.println("\n1 pour modifier compte administrateur\n"
					+ "2 pour modifier compte patient\n"
					+ "3 pour modifier compte professionnel de santé\n");
			System.out.print("Choix: ");
			
			if(!scanner.hasNextInt()) {
				System.out.println("Vous devez entrer un nombre entre 1 , 2 ou 3");
				scanner.nextLine();
				continue;
			}
			
			int choix=scanner.nextInt();
			scanner.nextLine();
			
			switch(choix){
		    case 1: typeUser="admin";
			        break;
			        
		    case 2: typeUser="patient";
			        break;
			        
		    case 3: typeUser="pro";
		            estProSante=true;
		            break;
		            
		    default: System.out.println("Choix invalide, veuillez entrer un nombre entre 1, 2 ou 3");
		             continue;
		    }
		
		    break;
		    
		}
		
		String checkExistSql="SELECT idUser FROM Users WHERE nom=? AND prenom=? AND typeUser=?";
		
		try(Connection conn=DriverManager.getConnection(url);
				PreparedStatement checkExistStmt=conn.prepareStatement(checkExistSql)){
			checkExistStmt.setString(1, nomUser);
			checkExistStmt.setString(2, prenomUser);
			checkExistStmt.setString(3, typeUser);
			
			Integer userId=null;
			try(ResultSet rs=checkExistStmt.executeQuery()){
				if(rs.next()) {
					userId=rs.getInt("idUser");
				}
				else {
					System.out.println("\nAucun utilisateur trouvé avec ces nom et prénom(s).");
					return;
				}
			}//Vérifier que l'utilisateur existe
			
			System.out.print("Nouveau nom: ");
			String newNomUser=scanner.nextLine().trim();
			
			System.out.print("Nouveau prénom(s): ");
			String newPrenomUser=scanner.nextLine().trim();
			
			System.out.print("Nouvelle adresse: ");
			String newAdressUser=scanner.nextLine().trim();
			
			System.out.print("Nouveau numéro de téléphone: ");
			String newTelUser=scanner.nextLine().trim();
			
			System.out.print("Nouveau mot de passe: ");
			String newPasswordUser=scanner.nextLine().trim();
			String hashedPassword=BCrypt.hashpw(newPasswordUser,BCrypt.gensalt());
			
			String checkFullSql="SELECT COUNT(*) FROM Users WHERE nom=? AND prenom=? AND telephone=? AND idUser<>?";
			try(PreparedStatement stmt=conn.prepareStatement(checkFullSql)){
				stmt.setString(1, newNomUser);
				stmt.setString(2, newPrenomUser);
				stmt.setString(3, newTelUser);
				stmt.setInt(4, userId);
				try(ResultSet rs=stmt.executeQuery()) {
                    if(rs.next() && rs.getInt(1) > 0) {
                           System.out.println("\nL'utilisateur " + nomUser + " " + prenomUser + " existe déjà.");
                           return;
                     }
                 }
			}
			
			String checkTelSql="SELECT COUNT(*) FROM Users WHERE telephone=? AND idUser<>?";
			try(PreparedStatement stmt=conn.prepareStatement(checkTelSql)) {
                stmt.setString(1, newTelUser);
                stmt.setInt(2, userId);
                try (ResultSet rs=stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("\nAttention : ce numéro de téléphone est déjà utilisé par un autre utilisateur.");
                        return;
                    }
                }
            }//Vérifier unicité
			
			String updateUserSql="UPDATE Users SET nom=?, prenom=?, adresse=?, telephone=?, motDePasse=? WHERE nom=? AND prenom=?";
			try(PreparedStatement stmt=conn.prepareStatement(updateUserSql)){
				stmt.setString(1, newNomUser);
	            stmt.setString(2, newPrenomUser);
	            stmt.setString(3, newAdressUser);
	            stmt.setString(4, newTelUser);
	            stmt.setString(5, hashedPassword);
	            stmt.setString(6, nomUser);
	            stmt.setString(7, prenomUser);
	            
	            int rows=stmt.executeUpdate();
	            
	            if(rows>0) {
	            	if(estProSante) {
	            		System.out.print("\nNouvelle catégorie: ");
		            	String newCategorie=scanner.nextLine().trim();
		            	
		            	String updateProSql="UPDATE ProfessionnelSante SET titre=? WHERE user_id=?";
		            	try(PreparedStatement stmtPro=conn.prepareStatement(updateProSql)){
		            		stmtPro.setString(1, newCategorie);
		            		stmtPro.setInt(2, userId);
		            		int rowsPro=stmtPro.executeUpdate();
		            	}
	            	}
	            	System.out.println("\nInformations mises à jour avec succès.");
	            }
	            else {
	            	System.out.println("\nUne erreur s'est produite lors de la mise à jour des informations.");
	            }
			}
			
		}
		
		catch(SQLException e) {
			if(e.getSQLState()!= null && e.getSQLState().startsWith("08")) {
        		System.out.println("Erreur: impossible de se connecter à la base de données.");
        	}
        	else {
        		System.out.println("Une erreur SQL est survenue: "+e.getMessage());
        	}
            e.printStackTrace();
		}
		
    }
		
	
	
    public void supprimerUtilisateur() {
		Scanner scanner=new Scanner(System.in);
		
		System.out.print("\nEntrez le nom de l'utilisateur à supprimer: ");
		String nomUser=scanner.nextLine().trim();
		
		System.out.print("Entrez son/ses prénoms(s): ");
		String prenomUser=scanner.nextLine().trim();
		
		String table=null;
		String role=null;
		String typeUser=null;
		
		while(true) {
			System.out.println("\n1 pour suppression de compte administrateur\n"
					+ "2 pour suppression de compte patient\n"
					+ "3 pour suppression de compte de professionnel de santé\n");
			System.out.print("Choix: ");
			
			if(!scanner.hasNextInt()) {
				System.out.println("Vous devez entrer un nombre entre 1, 2 ou 3");
				scanner.nextLine();
				continue;
			}
			
			int choix=scanner.nextInt();
			scanner.nextLine();
			
			switch(choix){
			    case 1: table="Administrateurs";
			            typeUser="admin";
				        role="administrateur";
				        break;
				        
			    case 2: table="Patients";
			            typeUser="patient";
				        role="patient";
				        break;
				        
			    case 3: table="ProfessionnelSante";
			            typeUser="pro";
			            role="professionnel de santé";
			            break;
			            
			    default: System.out.println("Choix invalide, veuillez entrer un nombre entre 1 , 2 ou 3");
			             continue;
			}
			
			break;
			
		}
		
		
		
		String checkSql="SELECT idUser FROM Users WHERE nom=? AND prenom=? AND typeUser=?";
		
		try(Connection conn=DriverManager.getConnection(url);
				PreparedStatement checkStmt=conn.prepareStatement(checkSql)){
			
			checkStmt.setString(1, nomUser);
			checkStmt.setString(2, prenomUser);
			checkStmt.setString(3, typeUser);
			
			Integer userId=null;
			try(ResultSet rs=checkStmt.executeQuery()){
				if(rs.next()) {
					userId=rs.getInt("idUser");
				}
				else {
					System.out.println("\nAucun utilisateur trouvé avec ces nom et prénom(s).");
					return;
				}
			}//Vérifier que l'utilisateur existe
			
			String deleteRoleSql="DELETE FROM "+table+" WHERE user_id=?";
			
			try(PreparedStatement stmtRole=conn.prepareStatement(deleteRoleSql)){
				stmtRole.setInt(1, userId);
				stmtRole.executeUpdate();
			}
			
			String deleteUserSql="DELETE FROM Users WHERE idUser=?";
			try(PreparedStatement stmtUser=conn.prepareStatement(deleteUserSql)){
				stmtUser.setInt(1, userId);
				int rows=stmtUser.executeUpdate();
				if(rows>0) {
					System.out.println("\n"+nomUser+" "+prenomUser+" a été supprimé en tant que "+role+" avec succès.");
				}
				else {
					System.out.println("\nUne erreur s'est produite lors de la suppression.");
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
    	String sqlCategories="SELECT titre AS categorie, COUNT(*) AS total FROM ProfessionnelSante GROUP BY titre";
    	
    	try(Connection conn=DriverManager.getConnection(url)){
    		
    		
    		try(PreparedStatement stmt=conn.prepareStatement(sqlPatients);
    				ResultSet rs=stmt.executeQuery()){
    			
    			if(rs.next()) {
    				System.out.println("\nNombre total de patients: "+rs.getInt("total"));
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