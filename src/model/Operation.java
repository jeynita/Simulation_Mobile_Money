package model;

import java.time.LocalDateTime;

public class Operation {

 
    public enum TypeOperation {
        DEPOT, RETRAIT, TRANSFERT, PAIEMENT
    }

   
    private int id;
    private TypeOperation typeOperation;
    private double montant;
    private LocalDateTime dateOperation;
    private String compteSource;
    private String compteDestination;
    private String marchand;

    public Operation() {}

    public Operation(String typeOperation, double montant, String compteSource) {
        this.typeOperation = TypeOperation.valueOf(typeOperation);
        this.montant = montant;
        this.compteSource = compteSource;
        this.dateOperation = LocalDateTime.now();
    }

    public Operation(int id, TypeOperation typeOperation, double montant,
                     LocalDateTime dateOperation, String compteSource,
                     String compteDestination, String marchand) {
        this.id = id;
        this.typeOperation = typeOperation;
        this.montant = montant;
        this.dateOperation = dateOperation;
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
        this.marchand = marchand;
    }

    public int getId() {
        return id;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public String getTypeOperationString() {
        return typeOperation != null ? typeOperation.name() : "";
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
        this.typeOperation = TypeOperation.valueOf(typeOperation);
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDateOperation(LocalDateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public void setCompteSource(String compteSource) {
        this.compteSource = compteSource;
    }

    public void setCompteDestination(String compteDestination) {
        this.compteDestination = compteDestination;
    }

    public void setMarchand(String marchand) {
        this.marchand = marchand;
    }

    @Override
    public String toString() {
        return String.format("Operation{type='%s', montant=%.2f, source='%s', destination='%s', marchand='%s'}",
                typeOperation != null ? typeOperation.name() : "?",
                montant,
                compteSource,
                compteDestination != null ? compteDestination : "non connu",
                marchand != null ? marchand : "non connu");
    }
}