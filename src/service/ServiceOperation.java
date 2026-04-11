package service;

import dao.CompteDAO;
import dao.OperationDAO;
import dao.MarchandDAO; 
import model.Compte;
import model.Operation;
import model.Marchand; 

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class ServiceOperation {

    private CompteDAO    compteDAO;
    private OperationDAO operationDAO;
    private ServiceCompte serviceCompte;
    private MarchandDAO  marchandDAO; 

    public ServiceOperation() {
        this.compteDAO     = new CompteDAO();
        this.operationDAO  = new OperationDAO();
        this.serviceCompte = new ServiceCompte();
        this.marchandDAO   = new MarchandDAO(); 
    }

    public void effectuerDepot(String numeroCompte, double montant) {
        if (!validerMontant(montant)) return;

        Compte compte = compteDAO.consulterParNumero(numeroCompte);
        if (compte == null) {
            System.out.println("  \033[31m✗ Compte introuvable : " + numeroCompte + "\033[0m");
            return;
        }

        double nouveauSolde = compte.getSolde() + montant;

        boolean maj = compteDAO.mettreAJourSolde(numeroCompte, nouveauSolde);
        if (!maj) {
            System.out.println("  \033[31m✗ Erreur lors de la mise à jour du solde.\033[0m");
            return;
        }

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
        Compte destination = compteDAO.consulterParNumero(compteDestination);

        if (source == null || destination == null) {
            System.out.println("  \033[31m✗ Un des comptes est introuvable.\033[0m");
            return;
        }

        if (!serviceCompte.soldeEstSuffisant(compteSource, montant)) return;

        compteDAO.mettreAJourSolde(compteSource, source.getSolde() - montant);
        compteDAO.mettreAJourSolde(compteDestination, destination.getSolde() + montant);

        Operation op = new Operation("TRANSFERT", montant, compteSource);
        op.setCompteDestination(compteDestination);
        operationDAO.enregistrer(op);

        System.out.printf("  \033[32m✓ Transfert de %.0f FCFA effectué avec succès.\033[0m%n", montant);
        System.out.println("  De   : \033[36m" + compteSource + "\033[0m");
        System.out.println("  Vers : \033[36m" + compteDestination + "\033[0m");
    }

    public void effectuerPaiement(String numeroCompte, String nomMarchand, double montant) {
        if (!validerMontant(montant)) return;

        Marchand marchand = marchandDAO.trouverParNom(nomMarchand.trim());
        if (marchand == null) {
            System.out.println("  \033[31m✗ Le marchand '" + nomMarchand + "' n'est pas répertorié.\033[0m");
            return;
        }

        Compte compte = compteDAO.consulterParNumero(numeroCompte);
        if (compte == null) {
            System.out.println("  \033[31m✗ Compte introuvable : " + numeroCompte + "\033[0m");
            return;
        }

        if (!serviceCompte.soldeEstSuffisant(numeroCompte, montant)) return;

        double nouveauSolde = compte.getSolde() - montant;
        boolean maj = compteDAO.mettreAJourSolde(numeroCompte, nouveauSolde);

        if (maj) {
            Operation op = new Operation("PAIEMENT", montant, numeroCompte);
            op.setMarchand(marchand.getNomEnseigne());
            operationDAO.enregistrer(op);

            System.out.printf("  \033[32m✓ Paiement de %.0f FCFA à %s (%s) effectué.\033[0m%n", 
                               montant, marchand.getNomEnseigne(), marchand.getCodeMarchand());
            System.out.printf("  Nouveau solde : \033[33m%.0f FCFA\033[0m%n", nouveauSolde);
        }
    }

    public List<Operation> listerToutesOperations() {
        return operationDAO.listerToutes();
    }

    public List<Operation> listerOperationsParCompte(String numeroCompte) {
        if (numeroCompte == null || numeroCompte.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return operationDAO.listerParCompte(numeroCompte.trim());
    }

    public List<Operation> listerOperationsParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return operationDAO.listerParPeriode(debut, fin);
    }

    public void afficherStatistiquesGlobales() {
        List<Operation> toutes = operationDAO.listerToutes();
        double totalDepots = 0, totalRetraits = 0, totalTransferts = 0, totalPaiements = 0;

        for (Operation op : toutes) {
            switch (op.getTypeOperation()) {
                case DEPOT -> totalDepots += op.getMontant();
                case RETRAIT -> totalRetraits += op.getMontant();
                case TRANSFERT -> totalTransferts += op.getMontant();
                case PAIEMENT -> totalPaiements += op.getMontant();
            }
        }

        System.out.println("\n\033[34m========== STATISTIQUES GLOBALES ==========\033[0m");
        System.out.printf("Total Dépôts    : %.0f FCFA\n", totalDepots);
        System.out.printf("Total Retraits  : %.0f FCFA\n", totalRetraits);
        System.out.printf("Total Transferts: %.0f FCFA\n", totalTransferts);
        System.out.printf("Total Paiements : %.0f FCFA\n", totalPaiements);
        System.out.println("\033[34m===========================================\033[0m");
    }

    private boolean validerMontant(double montant) {
        if (montant <= 0) {
            System.out.println("  \033[31m✗ Le montant doit être supérieur à 0 FCFA.\033[0m");
            return false;
        }
        return true;
    }
}