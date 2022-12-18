package domaine;

public class Arme {
    private String nom;
    private int niveau;
    private int puissance;
    private String rarete;

    public Arme(String nom, int niveau, int puissance, String rarete) {
        this.nom = nom;
        this.niveau = niveau;
        this.puissance = puissance;
        this.rarete = rarete;
    }

    public String getNom() {
        return nom;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getPuissance() {
        return puissance;
    }

    public String getRarete() {
        return rarete;
    }
}
