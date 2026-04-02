public static void main(String[] args) {
    try {
        database.DatabaseConnection.getConnection();
        System.out.println("Connexion réussie à MySQL !");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
