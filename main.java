import interfaceUtilisateur.Menu;

public class Main {
    public static void main(String[] args) {
        try {
            database.DatabaseConnection.getConnection();
            
            Menu menu = new Menu();
            menu.demarrer();
            
        } catch (Exception e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}


