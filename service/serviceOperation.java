package service;

import dao.CompteDAO;
import dao.OperationDAO;
import model.Compte;
import model.Operation;

import java.util.List;


public class ServiceOperation {

    private CompteDAO    compteDAO;
    private OperationDAO operationDAO;
    private ServiceCompte serviceCompte;

    public ServiceOperation() {
        this.compteDAO     = new CompteDAO();
        this.operationDAO  = new OperationDAO();
        this.serviceCompte = new ServiceCompte();
    }


    public void effectuerDepot(String numeroCompte, double montant) {
        if (!validerMontant(montant)) return;

        Compte compte = compteDAO.consulterParNumero(numeroCompte);
        if (compte == null) {
            System.out.println("  \033[31m✗ Compte introuvable : " + numeroCompte + "\033[0m");
            return;
        }

        double nouveauSolde = compte.getSolde() + montant;

        // 1. Mettre à jour le solde
        boolean maj = compteDAO.mettreAJourSolde(numeroCompte, nouveauSolde);
        if (!maj) {
            System.out.println("  \033[31m✗ Erreur lors de la mise à jour du solde.\033[0m");
            return;
        }

        // 2. Enregistrer l'opération
        Operation op = new Operation("DEPOT", montant, numeroCompte);
        operationDAO.enregistrer(op);

        System.out.printf("  \033[32m✓ Dépôt de %.0f FCFA effectué avec succès.\033[0m%n", montant);
        System.out.printf("  Nouveau solde : \033[33m%.0f FCFA\033[0m%n", nouveauSolde);
    }


    public void effectuerRetrait(String numeroCompte, double montant) {
        if (!validerMontant(montant)) return;

        Compte compte = compteDAO.consulterParNumero(numeroCompte);
        if (compte == null) {
            System.out.println("  \033[31m✗ Compte introuvable : " + numeroCompte + "\033[0m");
            return;
        }

        if (!serviceCompte.soldeEstSuffisant(numeroCompte, montant)) return;

        double nouveauSolde = compte.getSolde() - montant;

        boolean maj = compteDAO.mettreAJourSolde(numeroCompte, nouveauSolde);
        if (!maj) {
            System.out.println("  \033[31m✗ Erreur lors de la mise à jour du solde.\033[0m");
            return;
        }

        Operation op = new Operation("RETRAIT", montant, numeroCompte);
        operationDAO.enregistrer(op);

        System.out.printf("  \033[32m✓ Retrait de %.0f FCFA effectué avec succès.\033[0m%n", montant);
        System.out.printf("  Nouveau solde : \033[33m%.0f FCFA\033[0m%n", nouveauSolde);
    }


    public void effectuerTransfert(String compteSource, String compteDestination, double montant) {
        if (!validerMontant(montant)) return;

        if (compteSource.trim().equals(compteDestination.trim())) {
            System.out.println("  \033[31m✗ Le compte source et destination ne peuvent pas être identiques.\033[0m");
            return;
        }

        Compte source = compteDAO.consulterParNumero(compteSource);
        if (source == null) {
            System.out.println("  \033[31m✗ Compte source introuvable : " + compteSource + "\033[0m");
            return;
        }

        Compte destination = compteDAO.consulterParNumero(compteDestination);
        if (destination == null) {
            System.out.println("  \033[31m✗ Compte destination introuvable : " + compteDestination + "\033[0m");
            return;
        }

        // Vérifier solde suffisant sur le compte source
        if (!serviceCompte.soldeEstSuffisant(compteSource, montant)) return;

        // 1. Débiter la source
        compteDAO.mettreAJourSolde(compteSource, source.getSolde() - montant);

        // 2. Créditer la destination
        compteDAO.mettreAJourSolde(compteDestination, destination.getSolde() + montant);

        // 3. Enregistrer l'opération
        Operation op = new Operation("TRANSFERT", montant, compteSource);
        op.setCompteDestination(compteDestination);
        operationDAO.enregistrer(op);

        System.out.printf("  \033[32m✓ Transfert de %.0f FCFA effectué avec succès.\033[0m%n", montant);
        System.out.println("  De   : \033[36m" + compteSource + "\033[0m");
        System.out.println("  Vers : \033[36m" + compteDestination + "\033[0m");
        System.out.printf("  Nouveau solde source : \033[33m%.0f FCFA\033[0m%n", source.getSolde() - montant);
    }

    // -------------------------------------------------------
    // PAIEMENT MARCHAND
    // Appelé par : Menu.menuPaiementMarchand()
    // -------------------------------------------------------
    public void effectuerPaiement(String numeroCompte, String nomMarchand, double montant) {
        if (!validerMontant(montant)) return;

        if (nomMarchand == null || nomMarchand.trim().isEmpty()) {
            System.out.println("  \033[31m✗ Le nom du marchand est obligatoire.\033[0m");
            return;
        }

        Compte compte = compteDAO.consulterParNumero(numeroCompte);
        if (compte == null) {
            System.out.println("  \033[31m✗ Compte introuvable : " + numeroCompte + "\033[0m");
            return;
        }

        // Vérifier solde suffisant
        if (!serviceCompte.soldeEstSuffisant(numeroCompte, montant)) return;

        double nouveauSolde = compte.getSolde() - montant;

        // 1. Débiter le compte
        compteDAO.mettreAJourSolde(numeroCompte, nouveauSolde);

        // 2. Enregistrer l'opération
        Operation op = new Operation("PAIEMENT", montant, numeroCompte);
        op.setMarchand(nomMarchand.trim());
        operationDAO.enregistrer(op);

        System.out.printf("  \033[32m✓ Paiement de %.0f FCFA à %s effectué.\033[0m%n", montant, nomMarchand);
        System.out.printf("  Nouveau solde : \033[33m%.0f FCFA\033[0m%n", nouveauSolde);
    }

    // -------------------------------------------------------
    // HISTORIQUE
    // Appelé par : Menu.menuListeOperations()
    // -------------------------------------------------------
    public List<Operation> listerToutesOperations() {
        return operationDAO.listerToutes();
    }

    public List<Operation> listerOperationsParCompte(String numeroCompte) {
        if (numeroCompte == null || numeroCompte.trim().isEmpty()) {
            System.out.println("  \033[31m✗ Numéro de compte invalide.\033[0m");
            return new java.util.ArrayList<>();
        }
        return operationDAO.listerParCompte(numeroCompte.trim());
    }

    // -------------------------------------------------------
    // Validation interne
    // -------------------------------------------------------
    private boolean validerMontant(double montant) {
        if (montant <= 0) {
            System.out.println("  \033[31m✗ Le montant doit être supérieur à 0 FCFA.\033[0m");
            return false;
        }
        return true;
    }
}