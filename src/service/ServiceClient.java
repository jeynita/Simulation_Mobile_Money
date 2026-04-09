
package service;

import dao.ClientDAO;
import model.Client;
import java.util.List;

public class ServiceClient {

    private ClientDAO clientDAO;

    public ServiceClient() {
        this.clientDAO = new ClientDAO();
    }

    public boolean ajouterClient(String nom, String prenom, String telephone, String adresse) {
        if (nom == null || nom.trim().isEmpty()) {
            System.out.println("  \033[31m✗ Le nom est obligatoire.\033[0m");
            return false;
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            System.out.println("  \033[31m✗ Le prénom est obligatoire.\033[0m");
            return false;
        }
        if (!validerTelephone(telephone)) {
            return false;
        }
        if (adresse == null || adresse.trim().isEmpty()) {
            System.out.println("  \033[31m✗ L'adresse est obligatoire.\033[0m");
            return false;
        }

        Client existant = clientDAO.rechercherParTelephone(telephone.trim());
        if (existant != null) {
            System.out.println("  \033[31m✗ Ce numéro de téléphone est déjà enregistré.\033[0m");
            return false;
        }

        Client client = new Client(0, nom.trim(), prenom.trim(), telephone.trim(), adresse.trim());
        int id = clientDAO.ajouter(client);

        if (id > 0) {
            System.out.println("  \033[32m✓ Client enregistré avec l'ID : " + id + "\033[0m");
            return true;
        } else {
            System.out.println("  \033[31m✗ Échec de l'enregistrement en base de données.\033[0m");
            return false;
        }
    }

    public List<Client> listerClients() {
        return clientDAO.listerTous();
    }

    private boolean validerTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            System.out.println("  \033[31m✗ Le numéro de téléphone est obligatoire.\033[0m");
            return false;
        }

        String tel = telephone.trim().replaceAll("\\s+", "");
        if (!tel.matches("^7[0-9]{8}$")) {
            System.out.println("  \033[31m✗ Numéro invalide. Format attendu : 7XXXXXXXX (ex: 771234567)\033[0m");
            return false;
        }
        
        return true;
    }
}