package dao;
import domaine.*;
import org.neo4j.driver.*;

import java.text.ParseException;
import java.util.Date;

public class Bdd {
    public static Session connect_db() {

        return GraphDatabase.driver("bolt://localhost:7687").session();
    }

    public static void setup_env(Session bdd) throws ParseException {
        //LECTURE DES CSV
        Data.chargerDonnees("src\\csv\\armes.csv");
        Data.chargerDonnees("src\\csv\\animal.csv");
        Data.chargerDonnees("src\\csv\\classe.csv");
        Data.chargerDonnees("src\\csv\\evenement.csv");
        Data.chargerDonnees("src\\csv\\guilde.csv");
        Data.chargerDonnees("src\\csv\\skill.csv");
        Data.chargerDonnees("src\\csv\\users.csv");
        //MISE EN PLACE DANS LA BDD par noeud
        /*//armes
        for (Arme a : Data.lst_armes) {
            bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }
        //animal
        for (AnimalCompagnie a : Data.lst_animal) {
            bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }
        //classe
        for (Classe c : Data.lst_classe) {
            bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }
        //evenement
        for (EvenementJeu e : Data.lst_event) {
            bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }
        //guilde
        for (Guilde g : Data.lst_guilde) {
            bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }
        //skill
        for (Competence c : Data.lst_skill) {
            bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }*/
        //users
        for (Utilisateur u : Data.lst_users) {
                bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }


    }

    public static void delete_all(Session bdd){
        bdd.run("match (a) -[r] -> () delete a, r");
        bdd.run("match (a) delete a");
    }
}
