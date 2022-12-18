package domaine;

public class Guilde {
    private String nom;
    private int niveau;

    public Guilde(String nom, int niveau) {
        this.nom = nom;
        this.niveau = niveau;
    }

    public String getNom() {
        return nom;
    }

    public int getNiveau() {
        return niveau;
    }
}
