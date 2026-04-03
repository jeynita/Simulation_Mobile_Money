package model;

import java.time.LocalDateTime;
import java.util.*;

public class Operation {
    private int id;
    private String typeOperation; 
    private double montant;
    private LocalDateTime dateOperation;
    private String compteSource;
    private String compteDestination;
    private String marchand;


    public Operation() {}

    public Operation(String typeOperation, double montant, String compteSource) {
        this.typeOperation = typeOperation;
        this.montant = montant;
        this.compteSource = compteSource;
        this.dateOperation = LocalDateTime.now();
    }

    public int getId() 
    { 
        return id; 
    }

    public String getTypeOperation() { 
        return typeOperation;
    
    }
    public double getMontant() {
        return montant; 
    }

    public LocalDateTime getDateOperation() { 
        return dateOperation; 
    }
    public String getCompteSource() { 
        return compteSource; 
    }
    public String getCompteDestination() { 
        return compteDestination; 
    }
    public String getMarchand() { 
        return marchand; 
    }

    public void setId(int id) {
        this.id = id; 
        }
    public void setTypeOperation(String typeOperation) { 
        this.typeOperation = typeOperation; 
    }
    public void setMontant(double montant) { 
        this.montant = montant; 
    }
    public void setDateOperation(LocalDateTime dateOperation) { 
        
        this.dateOperation = dateOperation; }
    public void setCompteSource(String compteSource) { 
        this.compteSource = compteSource;
    }
    public void setCompteDestination(String compteDestination) { this.compteDestination = compteDestination; }
    public void setMarchand(String marchand) { 
        this.marchand = marchand; 
    }

    @Override
    public String toString() {
        return String.format("Operation{type='%s', montant=%.2f, source='%s', destination='%s', marchand='%s'}",
                typeOperation, montant, compteSource,
                compteDestination != null ? compteDestination : "non connu",
                marchand != null ? marchand : "non connu");
    }

}