package interfaceUtilisateur;

import model.Client;
import model.Compte;
import model.Operation;
import service.serviceClient;
import service.serviceCompte;
import service.serviceOperation;

import java.util.List;
import java.util.Scanner;

/**
 * Interface console principale de l'application Sama Money.
 * Gère la navigation et les interactions avec l'utilisateur.
 */
public class Menu {

    private Scanner scanner;
    private serviceClient serviceClient;
    private serviceCompte serviceCompte;
    private serviceOperation serviceOperation;

    private static final String RESET  = "\033[0m";
    private static final String JAUNE  = "\033[33m";
    private static final String CYAN   = "\033[36m";
    private static final String VERT   = "\033[32m";
    private static final String ROUGE  = "\033[31m";
    private static final String BLANC  = "\033[1;37m";
    private static final String GRIS   = "\033[90m";

    public Menu() {
        this.scanner          = new Scanner(System.in);
        this.serviceClient    = new serviceClient();
        this.serviceCompte    = new serviceCompte();
        this.serviceOperation = new serviceOperation();
    }

    // -------------------------------------------------------
    // Boucle principale
    // -------------------------------------------------------
    public void demarrer() {
        afficherBienvenue();
        int choix = -1;
        while (choix != 9) {
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
            case 9 -> afficherAuRevoir();
            default -> System.out.println("\n  " + ROUGE + "✗ Option invalide. Choisissez entre 1 et 9." + RESET + "\n");
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
            System.out.println("\n  " + VERT + "✓ Client ajouté avec succès !" + RESET);
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
            System.out.printf("  %-5s %-15s %-15s %-15s %-20s%n",
                    "ID", "NOM", "PRÉNOM", "TÉLÉPHONE", "ADRESSE");
            System.out.println(GRIS + "  " + "─".repeat(72) + RESET);
            for (Client c : clients) {
                System.out.printf("  %-5d %-15s %-15s %-15s %-20s%n",
                        c.getId(), c.getNom(), c.getPrenom(),
                        c.getTelephone(), c.getAdresse());
            }
            System.out.print(RESET);
        }
        pauseEtRetour();
    }

    
    private void menuCreerCompte() {
        afficherTitre("CRÉER UN COMPTE");
        int client = lireEntier("ID du client");
        serviceCompte.creerCompte(client);
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
        String marchand     = lireTexte("Nom du marchand");
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

        if (operations.isEmpty()) {
            System.out.println("\n  " + GRIS + "Aucune opération trouvée." + RESET);
        } else {
            System.out.println();
            System.out.print(CYAN);
            System.out.printf("  %-5s %-12s %-14s %-12s %-15s %-15s%n",
                    "ID", "TYPE", "MONTANT(FCFA)", "DATE", "COMPTE SRC", "DESTINATION");
            System.out.println(GRIS + "  " + "─".repeat(78) + RESET);
            for (Operation op : operations) {
                System.out.printf("  %-5d %-12s %-14.0f %-12s %-15s %-15s%n",
                        op.getId(),
                        op.getTypeOperation(),
                        op.getMontant(),
                        op.getDateOperation().toLocalDate().toString(),
                        op.getCompteSource(),
                        op.getCompteDestination() != null ? op.getCompteDestination()
                                : (op.getMarchand() != null ? op.getMarchand() : "-"));
            }
            System.out.print(RESET);
        }
        pauseEtRetour();
    }

    // -------------------------------------------------------
    // Affichage utilitaires
    // -------------------------------------------------------
    private void afficherBienvenue() {
        // Efface la console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // ASCII Art SAMA en jaune
        System.out.println(JAUNE);
        System.out.println("   ███████╗ █████╗ ███╗   ███╗ █████╗ ");
        System.out.println("   ██╔════╝██╔══██╗████╗ ████║██╔══██╗");
        System.out.println("   ███████╗███████║██╔████╔██║███████║");
        System.out.println("   ╚════██║██╔══██║██║╚██╔╝██║██╔══██║");
        System.out.println("   ███████║██║  ██║██║ ╚═╝ ██║██║  ██║");
        System.out.println("   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝");

        // ASCII Art MONEY en cyan
        System.out.println(CYAN);
        System.out.println("   ███╗   ███╗ ██████╗ ███╗   ██╗███████╗██╗   ██╗");
        System.out.println("   ████╗ ████║██╔═══██╗████╗  ██║██╔════╝╚██╗ ██╔╝");
        System.out.println("   ██╔████╔██║██║   ██║██╔██╗ ██║█████╗   ╚████╔╝ ");
        System.out.println("   ██║╚██╔╝██║██║   ██║██║╚██╗██║██╔══╝    ╚██╔╝  ");
        System.out.println("   ██║ ╚═╝ ██║╚██████╔╝██║ ╚████║███████╗   ██║   ");
        System.out.println("   ╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝   ╚═╝  ");
        System.out.println(RESET);

        // Encadré infos
        System.out.println(GRIS + "  ╔══════════════════════════════════════════════════╗");
        System.out.println(       "  ║  " + BLANC + " Système de Mobile Money  |  Dakar, Sénégal  " + GRIS + "  ║");
        System.out.println(       "  ║  " + BLANC + "        Version 1.0  —  Groupe Java 2026      " + GRIS + "║");
        System.out.println(       "  ╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void afficherMenu() {
        System.out.println(GRIS + "  ┌─────────────────────────────────────────────┐");
        System.out.println(       "  │         " + BLANC + "MENU PRINCIPAL" + GRIS + "                        │");
        System.out.println(       "  ├─────────────────────────────────────────────┤" + RESET);
        System.out.println("  │  " + JAUNE + "[1]" + RESET + "  " + CYAN + "Ajouter un client" + RESET + "                   " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[2]" + RESET + "  " + CYAN + "Afficher les clients" + RESET + "                " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[3]" + RESET + "  " + CYAN + "Créer un compte" + RESET + "                     " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[4]" + RESET + "  " + CYAN + "Dépôt" + RESET + "                               " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[5]" + RESET + "  " + CYAN + "Retrait" + RESET + "                             " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[6]" + RESET + "  " + CYAN + "Transfert" + RESET + "                           " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[7]" + RESET + "  " + CYAN + "Paiement marchand" + RESET + "                   " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[8]" + RESET + "  " + CYAN + "Historique des opérations" + RESET + "           " + GRIS + "│" + RESET);
        System.out.println("  │  " + JAUNE + "[9]" + RESET + "  " + ROUGE + "Quitter" + RESET + "                             " + GRIS + "│" + RESET);
        System.out.println(GRIS + "  └─────────────────────────────────────────────┘" + RESET);
        System.out.println();
    }

    private void afficherTitre(String titre) {
        System.out.println();
        System.out.println(JAUNE + "  ╔══════════════════════════════════════════════════╗");
        System.out.println(        "  ║  " + BLANC + titre + JAUNE + " ".repeat(Math.max(0, 48 - titre.length())) + "║");
        System.out.println(        "  ╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void afficherAuRevoir() {
        System.out.println();
        System.out.println(CYAN + "  ╔══════════════════════════════════════════════════╗");
        System.out.println(       "  ║   " + BLANC + "Au revoir ! Merci d'utiliser Sama Money." + CYAN + "      ║");
        System.out.println(       "  ║   " + BLANC + "           Ak Jaam — Avec paix.          " + CYAN + "      ║");
        System.out.println(       "  ╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void pauseEtRetour() {
        System.out.println("\n  " + GRIS + "Appuyez sur Entrée pour revenir au menu..." + RESET);
        scanner.nextLine();
    }

    // -------------------------------------------------------
    // Lecture sécurisée des entrées
    // -------------------------------------------------------
    private String lireTexte(String label) {
        System.out.print("  " + VERT + label + " : " + RESET);
        return scanner.nextLine().trim();
    }

    private int lireEntier(String label) {
        while (true) {
            System.out.print("  " + VERT + label + " : " + RESET);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  " + ROUGE + "✗ Entrez un nombre entier valide." + RESET);
            }
        }
    }

    private double lireDouble(String label) {
        while (true) {
            System.out.print("  " + VERT + label + " : " + RESET);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  " + ROUGE + "✗ Entrez un montant valide (ex: 5000)." + RESET);
            }
        }
    }
}