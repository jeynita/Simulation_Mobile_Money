package model;

public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;

    // Constructeurs, Getters et Setters (Encapsulation)
    public Client() {}
    public Client(int id, String nom, String prenom, String telephone, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
    }
    
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }

    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }

    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public String toString () {
        return String.format("Client{id=%d ,  nom='%s', prenom='%s', tel='%s', adresse='%s'}" , 
        id, nom , prenom , telephone , adresse
    );
    }
}