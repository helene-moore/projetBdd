package dao;

import domaine.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static dao.FileToStr.lireCsv;

public class Data {
    public static List<AnimalCompagnie> lst_animal = new ArrayList<>();
    public static List<Arme> lst_armes = new ArrayList<>();
    public static List<Classe> lst_classe = new ArrayList<>();
    public static List<EvenementJeu> lst_event = new ArrayList<>();
    public static List<Guilde> lst_guilde = new ArrayList<>();
    public static List<Competence> lst_skill = new ArrayList<>();
    public static List<Utilisateur> lst_users = new ArrayList<>();

    public static void chargerDonnees(String filename) throws ParseException {
        /*
        Lis le CSV et créer les différentes entitées. Celles-ci sont ajoutées dans leur liste respective.
        */
        boolean isFirstRow = true;
        String[] fichier = lireCsv(filename);
        for (String data: fichier) {
            if (isFirstRow){ //pour sauter la première ligne d'entetête
                isFirstRow = false;
            } else {
            String[] valeurs = data.split(",");
            if (valeurs.length == 4){
                if(filename.equals("src\\csv\\evenement.csv")){
                //EVENEMENT
                    SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy"); //format de la date
                    Date dateDebut = formatter1.parse(valeurs[1]);
                    Date dateFin = formatter1.parse(valeurs[2]);
                    EvenementJeu eventJeu = new EvenementJeu(valeurs[0],dateDebut,dateFin,valeurs[3]);
                    lst_event.add(eventJeu);
                }
                else if (filename.equals("src\\csv\\armes.csv")) {
                    //ARME
                    Arme arme = new Arme(valeurs[0],Integer.parseInt(valeurs[1]),Integer.parseInt(valeurs[2]),valeurs[3]);
                    lst_armes.add(arme);
                }
            } else if (valeurs.length == 5) {
                //SKILLS
                if (filename.equals("src\\csv\\skill.csv")) {
                    Competence competence = new Competence(valeurs[0], Integer.parseInt(valeurs[1]), Integer.parseInt(valeurs[2]), Integer.parseInt(valeurs[3]), Integer.parseInt(valeurs[4]));
                    lst_skill.add(competence);
                }
                else if (filename.equals("src\\csv\\animal.csv")) {
                    //ANIMAL
                    AnimalCompagnie animal = new AnimalCompagnie(valeurs[0],valeurs[1],Integer.parseInt(valeurs[2]),Integer.parseInt(valeurs[3]),valeurs[4]);
                    lst_animal.add(animal);
                }
            } else if (valeurs.length == 6) {
                //USERS
                Utilisateur utilisateur = new Utilisateur(valeurs[1],valeurs[2],Integer.parseInt(valeurs[3]),Integer.parseInt(valeurs[4]),Integer.parseInt(valeurs[5]));
                lst_users.add(utilisateur);
            } else if (valeurs.length == 2) {
                //GUILDE
                Guilde guilde = new Guilde(valeurs[0],Integer.parseInt(valeurs[1]));
                lst_guilde.add(guilde);
            } else {
                //CLASSE
                Classe classe = new Classe(valeurs[0]);
                lst_classe.add(classe);
                }
            }
        }
    }
}
