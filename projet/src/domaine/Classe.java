package domaine;

public class Classe {
    private String nom;

    public Classe(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Classe{" +
                "nom='" + nom + '\'' +
                '}';
    }
}
