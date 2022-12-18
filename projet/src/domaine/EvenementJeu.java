package domaine;

import java.util.Date;

public class EvenementJeu {
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;

    public EvenementJeu(String nom, Date dateDebut, Date dateFin, String lieu) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
    }

    public String getNom() {
        return nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public String getLieu() {
        return lieu;
    }
}
