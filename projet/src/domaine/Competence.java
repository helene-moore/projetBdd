package domaine;

public class Competence {
   private String nom;
   private int nSort;
   private int puissance;
   private int cMagie;
   private int tRechargement;

    public Competence(String nom, int nSort, int puissance, int cMagie, int tRechargement) {
        this.nom = nom;
        this.nSort = nSort;
        this.puissance = puissance;
        this.cMagie = cMagie;
        this.tRechargement = tRechargement;
    }

    public String getNom() {
        return nom;
    }

    public int getnSort() {
        return nSort;
    }

    public int getPuissance() {
        return puissance;
    }

    public int getcMagie() {
        return cMagie;
    }

    public int gettRechargement() {
        return tRechargement;
    }

    @Override
    public String toString() {
        return "Competence{" +
                "nom='" + nom + '\'' +
                ", nSort=" + nSort +
                ", puissance=" + puissance +
                ", cMagie=" + cMagie +
                ", tRechargement=" + tRechargement +
                '}';
    }
}
