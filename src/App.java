<<<<<<< HEAD

public class App {
    public static void main(String[] args) throws Exception {
        Administrateur monAdmin=new Administrateur("Jean","Pierre","55244885","Fiyegnon","jp34@siu&");
        monAdmin.creerUtilisateur();
    }
}

=======
public class App {
    public static void main(String[] args) throws Exception {
        // Interface.start();
        Professionnel_de_Sante pro = new Professionnel_de_Sante("AKPLOGA", "Seth", "0196893597", "toto", "Rue T", "gestionnaire de patients");

        // Ajout des disponibilités
        pro.ajouter_Disponibilite(new Disponibilite(Disponibilite.Jour.LUNDI, "09:00", "12:00"));
        pro.ajouter_Disponibilite(new Disponibilite(Disponibilite.Jour.MARDI, "14:00", "18:00"));

        // Sauvegarde complète en base
        pro.save();

    }
}
>>>>>>> fb7bf1020222c06445d9634d4cf95c2ff45e9651
