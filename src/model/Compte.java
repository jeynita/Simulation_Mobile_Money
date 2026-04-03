package model;

public class Compte {
    private int id;
    private String numeroCompte;
    private double solde;
    private Client client;

    public Compte() {}

    public Compte(String numeroCompte, double solde, Client client) {
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.client = client;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroCompte() { return numeroCompte; }
    public void setNumeroCompte(String numeroCompte) { this.numeroCompte = numeroCompte; }

    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    @Override
    public String toString() {
        return String.format("Compte{id=%d, numero='%s', solde=%.3f FCFA, client=%d }",
            id, numeroCompte, solde, client
        );
    }

}