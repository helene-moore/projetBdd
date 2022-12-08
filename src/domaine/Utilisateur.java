package domaine;

public class Utilisateur {
    private String pseudo;
    private String email;
    private int niveau;
    private int pv;
    private int pm;

    public Utilisateur(String pseudo, String email, int niveau, int pv, int pm) {
        this.pseudo = pseudo;
        this.email = email;
        this.niveau = niveau;
        this.pv = pv;
        this.pm = pm;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }

    public int getNiveau() {
        return niveau;
    }

    public int getPv() {
        return pv;
    }

    public int getPm() {
        return pm;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", niveau=" + niveau +
                ", pv=" + pv +
                ", pm=" + pm +
                '}';
    }
}
