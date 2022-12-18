package domaine;

public class AnimalCompagnie {
    private String type;
    private String nom;
    private int niveau;
    private int age;
    private String effet;

    public AnimalCompagnie(String type,String nom, int niveau, int age, String effet) {
        this.type = type;
        this.nom = nom;
        this.niveau = niveau;
        this.age = age;
        this.effet = effet;
    }
    public String getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getAge() {
        return age;
    }

    public String getEffet() {
        return effet;
    }
}
