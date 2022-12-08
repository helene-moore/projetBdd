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
}
