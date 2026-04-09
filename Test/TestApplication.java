//  package Test;
// import service.ServiceOperation;
// import dao.CompteDAO;
// import dao.ClientDAO;
// import model.Compte;
// import model.Client;
// import java.time.LocalDateTime;

// public class TestApplication {

//     public static void main(String[] args) {
//         ServiceOperation serviceOp = new ServiceOperation();
//         CompteDAO compteDAO = new CompteDAO();
//         ClientDAO clientDAO = new ClientDAO();

//         System.out.println("\033[34m=== DÉBUT DU TEST AUTOMATISÉ ===\033[0m\n");

//         try {
//             // 1. Création d'un client de test
//             Client client = new Client("Diallo", "Moussa", "771234567", "Dakar");
//             int clientId = clientDAO.creer(client);
//             System.out.println("1. Client créé avec ID : " + clientId);

//             // 2. Création de deux comptes de test
//             String numSource = "TEST-001";
//             String numDest = "TEST-002";
            
//             compteDAO.creer(new Compte(numSource, 5000.0, clientId));
//             compteDAO.creer(new Compte(numDest, 1000.0, clientId));
//             System.out.println("2. Comptes TEST-001 (5000) et TEST-002 (1000) créés.");

//             // 3. Test du Dépôt
//             System.out.println("\n--- Test Dépôt ---");
//             serviceOp.effectuerDepot(numSource, 2000.0);

//             // 4. Test du Retrait
//             System.out.println("\n--- Test Retrait ---");
//             serviceOp.effectuerRetrait(numSource, 1000.0);

//             // 5. Test du Transfert
//             System.out.println("\n--- Test Transfert ---");
//             serviceOp.effectuerTransfert(numSource, numDest, 1500.0);

//             // 6. Test du Paiement Marchand
//             System.out.println("\n--- Test Paiement Marchand ---");
//             serviceOp.effectuerPaiement(numDest, "POLYTECHNICIENNE", 500.0);

//             // 7. Test de l'Historique
//             System.out.println("\n--- Test Historique ---");
//             int nbOps = serviceOp.listerToutesOperations().size();
//             System.out.println("Nombre total d'opérations enregistrées : " + nbOps);

//             // 8. Test des Statistiques
//             System.out.println("\n--- Test Statistiques Globales ---");
//             serviceOp.afficherStatistiquesGlobales();

//             // 9. Test Recherche par Période
//             System.out.println("\n--- Test Recherche par Période (Aujourd'hui) ---");
//             LocalDateTime debut = LocalDateTime.now().withHour(0).withMinute(0);
//             LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59);
//             var opsDate = serviceOp.listerOperationsParPeriode(debut, fin);
//             System.out.println("Opérations trouvées pour aujourd'hui : " + opsDate.size());

//             System.out.println("\n\033[32m=== TOUS LES TESTS SONT TERMINÉS AVEC SUCCÈS ===\033[0m");

//         } catch (Exception e) {
//             System.err.println("\n\033[31mErreur durant le test : " + e.getMessage() + "\033[0m");
//             e.printStackTrace();
//         }
//     }
// }

