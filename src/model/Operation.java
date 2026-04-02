package model;

import java.util.Date;

public class Operation {
    private int id;
    private String type; // DEPOT, RETRAIT, etc.
    private double montant;
    private Date date;
    private int compteSourceId;

    public Operation() {}
    // Ajoutez les Getters/Setters ici...
}