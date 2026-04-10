
package model;

public class Marchand {
    private int id;
    private String nomEnseigne;
    private String codeMarchand;

    public Marchand() {}

    public Marchand(int id, String nomEnseigne, String codeMarchand) {
        this.id = id;
        this.nomEnseigne = nomEnseigne;
        this.codeMarchand = codeMarchand;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomEnseigne() { return nomEnseigne; }
    public void setNomEnseigne(String nomEnseigne) { this.nomEnseigne = nomEnseigne; }

    public String getCodeMarchand() { return codeMarchand; }
    public void setCodeMarchand(String codeMarchand) { this.codeMarchand = codeMarchand; }
}