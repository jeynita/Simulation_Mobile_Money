package service;

import dao.CompteDAO;
import model.Client;
import model.Compte;

import java.time.Year;
import java.util.Random;

/**
 * Service de gestion des comptes Mobile Money.
 * Gère la génération de numéro de compte et les règles métier.
 */
public class ServiceCompte {

    private CompteDAO compteDAO;

    public ServiceCompte() {
        this.compteDAO = new CompteDAO();
    }

    /**
     * Crée un compte Mobile Money pour un client donné.
     * Génère automatiquement un numéro unique format : MM-YYYY-XXXXX
     * Appelé par : Menu.menuCreerCompte()
     */
    public void creerCompte(int clientId) {
        if (clientId <= 0) {
            System.out.println("  \033[31m✗ ID client invalide.\033[0m");
            return;
        }

        // Générer un numéro de compte unique
        String numeroCompte = genererNumeroCompte(clientId);

        // Vérifier que le numéro n'existe pas déjà (sécurité)
        while (compteDAO.consulterParNumero(numeroCompte) != null) {
            numeroCompte = genererNumeroCompte(clientId);
        }

        // Créer l'objet client avec l'ID (le DAO n'a besoin que de l'ID)
        Client client = new Client();
        client.setId(clientId);

        // Créer le compte avec solde initial à 0
        Compte compte = new Compte(numeroCompte, 0.0, client);
        int id = compteDAO.creer(compte);

        if (id > 0) {
            System.out.println("  \033[32m✓ Compte créé avec succès !\033[0m");
            System.out.println("  \033[36mNuméro de compte : " + numeroCompte + "\033[0m");
            System.out.println("  Solde initial    : 0 FCFA");
        } else {
            System.out.println("  \033[31m✗ Échec de la création du compte.");
            System.out.println("    Vérifiez que le client avec l'ID " + clientId + " existe.\033[0m");
        }
    }

    /**
     * Recherche un compte par son numéro.
     * Utilisé par ServiceOperation pour vérifier l'existence d'un compte.
     */
    public Compte consulterCompte(String numeroCompte) {
        if (numeroCompte == null || numeroCompte.trim().isEmpty()) {
            System.out.println("  \033[31m✗ Numéro de compte invalide.\033[0m");
            return null;
        }
        return compteDAO.consulterParNumero(numeroCompte.trim());
    }

    /**
     * Vérifie si le solde d'un compte est suffisant pour une opération.
     * Utilisé par ServiceOperation avant retrait/transfert.
     */
    public boolean soldeEstSuffisant(String numeroCompte, double montant) {
        double solde = compteDAO.afficherSolde(numeroCompte);
        if (solde < 0) {
            // afficherSolde retourne -1 si compte non trouvé
            return false;
        }
        if (solde < montant) {
            System.out.printf("  \033[31m✗ Solde insuffisant ! Solde actuel : %.0f FCFA\033[0m%n", solde);
            return false;
        }
        return true;
    }

    // -------------------------------------------------------
    // Utilitaire : génération de numéro de compte
    // -------------------------------------------------------
    private String genererNumeroCompte(int clientId) {
        int annee  = Year.now().getValue();
        int random = new Random().nextInt(90000) + 10000;
        return String.format("MM-%d-%05d", annee, random);
    }
}