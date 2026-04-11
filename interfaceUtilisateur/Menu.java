package interfaceUtilisateur;

import model.Client;
import model.Operation;
import service.ServiceClient;
import service.ServiceCompte;
import service.ServiceOperation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private Scanner scanner;
    private ServiceClient serviceClient;
    private ServiceCompte serviceCompte;
    private ServiceOperation serviceOperation;

    private static final String RESET  = "\033[0m";
    private static final String JAUNE  = "\033[33m";
    private static final String CYAN   = "\033[36m";
    private static final String VERT   = "\033[32m";
    private static final String ROUGE  = "\033[31m";
    private static final String BLANC  = "\033[1;37m";
    private static final String GRIS   = "\033[90m";

    public Menu() {
        this.scanner          = new Scanner(System.in);
        this.serviceClient    = new ServiceClient();
        this.serviceCompte    = new ServiceCompte();
        this.serviceOperation = new ServiceOperation();
    }

    public void demarrer() {
        afficherBienvenue();
        int choix = -1;
        while (choix != 0) {
            afficherMenu();
            choix = lireEntier("Votre choix");
            traiterChoix(choix);
        }
    }

    private void traiterChoix(int choix) {
        switch (choix) {
            case 1 -> menuAjouterClient();
            case 2 -> menuAfficherClients();
            case 3 -> menuCreerCompte();
            case 4 -> menuDepot();
            case 5 -> menuRetrait();
            case 6 -> menuTransfert();
            case 7 -> menuPaiementMarchand();
            case 8 -> menuListeOperations();
            case 9 -> menuRechercheParDate();
            case 10 -> menuStatistiques();
            case 0 -> afficherAuRevoir();
            default -> System.out.println("\n                  " + ROUGE + "✗ Option invalide. Entrez un nombre entre 0 et 10." + RESET + "\n");
        }
    }

    private void menuAjouterClient() {
        afficherTitre("AJOUTER UN CLIENT");
        String nom       = lireTexte("Nom");
        String prenom    = lireTexte("Prénom");
        String telephone = lireTexte("Téléphone (7XXXXXXXX)");
        String adresse   = lireTexte("Adresse");

        boolean succes = serviceClient.ajouterClient(nom, prenom, telephone, adresse);
        if (succes) {
            System.out.println("\n  " + VERT + "Client ajouté avec succès !" + RESET);
        }
        pauseEtRetour();
    }

    private void menuAfficherClients() {
        afficherTitre("LISTE DES CLIENTS");
        List<Client> clients = serviceClient.listerClients();

        if (clients.isEmpty()) {
            System.out.println("  " + GRIS + "Aucun client enregistré." + RESET);
        } else {
            System.out.println(CYAN);
            System.out.printf("  %-5s %-15s %-15s %-15s %-20s%n", "ID", "NOM", "PRÉNOM", "TÉLÉPHONE", "ADRESSE");
            System.out.println(GRIS + "  " + "─".repeat(72) + RESET);
            for (Client c : clients) {
                System.out.printf("  %-5d %-15s %-15s %-15s %-20s%n", c.getId(), c.getNom(), c.getPrenom(), c.getTelephone(), c.getAdresse());
            }
            System.out.print(RESET);
        }
        pauseEtRetour();
    }

    private void menuCreerCompte() {
        afficherTitre("CRÉER UN COMPTE");
        int idClient = lireEntier("ID du client");
        serviceCompte.creerCompte(idClient);
        pauseEtRetour();
    }

    private void menuDepot() {
        afficherTitre("DÉPÔT D'ARGENT");
        String numeroCompte = lireTexte("Numéro de compte");
        double montant      = lireDouble("Montant (FCFA)");
        serviceOperation.effectuerDepot(numeroCompte, montant);
        pauseEtRetour();
    }

    private void menuRetrait() {
        afficherTitre("RETRAIT D'ARGENT");
        String numeroCompte = lireTexte("Numéro de compte");
        double montant      = lireDouble("Montant (FCFA)");
        serviceOperation.effectuerRetrait(numeroCompte, montant);
        pauseEtRetour();
    }

    private void menuTransfert() {
        afficherTitre("TRANSFERT D'ARGENT");
        String compteSource = lireTexte("Compte source");
        String compteDest   = lireTexte("Compte destination");
        double montant      = lireDouble("Montant (FCFA)");
        serviceOperation.effectuerTransfert(compteSource, compteDest, montant);
        pauseEtRetour();
    }

    private void menuPaiementMarchand() {
        afficherTitre("PAIEMENT MARCHAND");
        String numeroCompte = lireTexte("Numéro de compte");
        String marchand     = lireTexte("Nom ou Code Marchand");
        double montant      = lireDouble("Montant (FCFA)");
        serviceOperation.effectuerPaiement(numeroCompte, marchand, montant);
        pauseEtRetour();
    }

    private void menuListeOperations() {
        afficherTitre("HISTORIQUE DES OPÉRATIONS");
        System.out.println("  " + JAUNE + "[1]" + RESET + " Toutes les opérations");
        System.out.println("  " + JAUNE + "[2]" + RESET + " Opérations d'un compte");
        int choix = lireEntier("Votre choix");

        List<Operation> operations;
        if (choix == 2) {
            String numeroCompte = lireTexte("Numéro de compte");
            operations = serviceOperation.listerOperationsParCompte(numeroCompte);
        } else {
            operations = serviceOperation.listerToutesOperations();
        }
        afficherTableauOperations(operations);
        pauseEtRetour();
    }

    private void menuRechercheParDate() {
        afficherTitre("RECHERCHE PAR DATE");
        System.out.println("  " + GRIS + "(Format attendu : YYYY-MM-DD)" + RESET);
        try {
            String dateStr = lireTexte("Date cible");
            LocalDate dateChoisie = LocalDate.parse(dateStr);
            
            LocalDateTime debut = dateChoisie.atStartOfDay();
            LocalDateTime fin = dateChoisie.atTime(LocalTime.MAX);

            List<Operation> result = serviceOperation.listerOperationsParPeriode(debut, fin);
            
            if (result.isEmpty()) {
                System.out.println("\n  " + ROUGE + "Aucune opération trouvée pour cette date." + RESET);
            } else {
                afficherTableauOperations(result);
            }
        } catch (DateTimeParseException e) {
            System.out.println("\n  " + ROUGE + "✗ Format de date incorrect. Utilisez YYYY-MM-DD." + RESET);
        }
        pauseEtRetour();
    }

    private void menuStatistiques() {
        afficherTitre("STATISTIQUES FINANCIÈRES");
        serviceOperation.afficherStatistiquesGlobales();
        pauseEtRetour();
    }

    private void afficherTableauOperations(List<Operation> operations) {
        if (operations == null || operations.isEmpty()) {
            System.out.println("\n  " + GRIS + "Aucune opération à afficher." + RESET);
            return;
        }
        System.out.println();
        System.out.print(CYAN);
        System.out.printf("  %-5s %-12s %-14s %-12s %-15s %-15s%n", "ID", "TYPE", "MONTANT", "DATE", "SRC", "DEST/MARCH");
        System.out.println(GRIS + "  " + "─".repeat(82) + RESET);
        for (Operation op : operations) {
            String destination = op.getCompteDestination() != null ? op.getCompteDestination() : 
                                (op.getMarchand() != null ? op.getMarchand() : "-");
            
            System.out.printf("  %-5d %-12s %-14.0f %-12s %-15s %-15s%n",
                    op.getId(), 
                    op.getTypeOperation(), 
                    op.getMontant(),
                    op.getDateOperation().toLocalDate().toString(), 
                    op.getCompteSource(),
                    destination);
        }
        System.out.print(RESET);
    }

    private void afficherBienvenue() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String pad = "                  ";
        System.out.println("\n\n");
        System.out.println(pad + VERT + " ███████╗ █████╗ ███╗   ███╗ █████╗ " + RESET);
        System.out.println(pad + VERT + " ██╔════╝██╔══██╗████╗ ████║██╔══██╗" + RESET);
        System.out.println(pad + VERT + " ███████╗███████║██╔████╔██║███████║" + RESET);
        System.out.println(pad + VERT + " ╚════██║██╔══██║██║╚██╔╝██║██╔══██║" + RESET);
        System.out.println(pad + VERT + " ███████║██║  ██║██║ ╚═╝ ██║██║  ██║" + RESET);
        System.out.println(pad + CYAN  + " ███╗   ███╗ ██████╗ ███╗   ██╗███████╗██╗    ██╗" + RESET);
        System.out.println(pad + CYAN  + " ████╗ ████║██╔═══██╗████╗  ██║██╔════╝╚██╗  ██╔╝" + RESET);
        System.out.println(pad + CYAN  + " ██╔████╔██║██║   ██║██╔██╗ ██║█████╗   ╚████╔╝ " + RESET);
        System.out.println(pad + CYAN  + " ██║╚██╔╝██║██║   ██║██║╚██╗██║██╔══╝    ╚██╔╝  " + RESET);
        System.out.println(pad + CYAN  + " ██║ ╚═╝ ██║╚██████╔╝██║ ╚████║███████╗   ██║   " + RESET);
        System.out.println("\n" + pad + GRIS + "╔══════════════════════════════════════════════════╗");
        System.out.println(pad + "║  " + BLANC + "            BIENVENUE DANS SAMA MONEY        " + GRIS + "   ║");
        System.out.println(pad + "╚══════════════════════════════════════════════════╝" + RESET + "\n");
    }

    private void afficherMenu() {
        String pad = "                  ";
        System.out.println(pad + GRIS + "┌─────────────────────────────────────────────┐");
        System.out.println(pad + "│         " + BLANC + "MENU PRINCIPAL (SAMA MONEY)" + GRIS + "         │");
        System.out.println(pad + "├─────────────────────────────────────────────┤" + RESET);
        System.out.println(pad + "│  " + JAUNE + "[1]" + RESET + "  " + CYAN + "Ajouter un client" + RESET + "                     " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[2]" + RESET + "  " + CYAN + "Afficher les clients" + RESET + "                  " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[3]" + RESET + "  " + CYAN + "Créer un compte" + RESET + "                       " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[4]" + RESET + "  " + CYAN + "Dépôt d'argent" + RESET + "                        " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[5]" + RESET + "  " + CYAN + "Retrait d'argent" + RESET + "                      " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[6]" + RESET + "  " + CYAN + "Transfert d'argent" + RESET + "                    " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[7]" + RESET + "  " + CYAN + "Paiement marchand" + RESET + "                     " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[8]" + RESET + "  " + CYAN + "Liste des operations" + RESET + "                    " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[9]" + RESET + "  " + VERT + "Rechercher par date" + RESET + "                   " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[10]" + RESET + " " + VERT + "Statistiques globales" + RESET + "                 " + GRIS + "│");
        System.out.println(pad + "│  " + JAUNE + "[0]" + RESET + "  " + ROUGE + "Quitter le système" + RESET + "                    " + GRIS + "│");
        System.out.println(pad + "└─────────────────────────────────────────────┘" + RESET);
    }

    private void afficherTitre(String titre) {
        String pad = "                  ";
        System.out.println("\n" + pad + JAUNE + "╔" + "═".repeat(50) + "╗");
        System.out.println(pad + "║  " + BLANC + titre + " ".repeat(Math.max(0, 48 - titre.length())) + JAUNE + "║");
        System.out.println(pad + "╚" + "═".repeat(50) + "╝" + RESET);
    }

    private void afficherAuRevoir() {
        String pad = "                  ";
        System.out.println("\n" + pad + CYAN + "╔══════════════════════════════════════════════════╗");
        System.out.println(pad + "║   " + BLANC + "Au revoir ! Merci d'utiliser Sama Money.       " + CYAN + "║");
        System.out.println(pad + "╚══════════════════════════════════════════════════╝" + RESET);
    }

    private void pauseEtRetour() {
        System.out.println("\n  " + GRIS + "Appuyez sur Entrée pour revenir au menu...avant de poursuivre" + RESET);
        scanner.nextLine();
    }

    private String lireTexte(String label) {
        System.out.print("  " + VERT + label + " : " + RESET);
        return scanner.nextLine().trim();
    }

    private int lireEntier(String label) {
        while (true) {
            System.out.print("  " + VERT + label + " : " + RESET);
            String input = scanner.nextLine().trim();
            try { 
                return Integer.parseInt(input); 
            } catch (NumberFormatException e) { 
                System.out.println("  " + ROUGE + "✗ Veuillez donner un nombre entier." + RESET); 
            }
        }
    }

    private double lireDouble(String label) {
        while (true) {
            System.out.print("  " + VERT + label + " : " + RESET);
            String input = scanner.nextLine().trim();
            try { 
                return Double.parseDouble(input); 
            } catch (NumberFormatException e) { 
                System.out.println("  " + ROUGE + "✗ Veuillez renseigner un montant valide." + RESET); 
            }
        }
    }
}