package dao;
import domaine.*;
import org.neo4j.driver.*;

import java.text.ParseException;
import java.util.Random;

public class Bdd {
    public static Session connect_db() {
        /**
        *Connexion à la base de données
         * */
        return GraphDatabase.driver("bolt://localhost:7687").session();
    }

    public static void setup_env(Session bdd) throws ParseException {
        /**
         *Cette méthode va permettre de construire l'environnement de base avec les différents nœuds
         * */
        //1. LECTURE DES CSV
        Data.chargerDonnees("src\\csv\\armes.csv");
        Data.chargerDonnees("src\\csv\\animal.csv");
        Data.chargerDonnees("src\\csv\\classe.csv");
        Data.chargerDonnees("src\\csv\\evenement.csv");
        Data.chargerDonnees("src\\csv\\guilde.csv");
        Data.chargerDonnees("src\\csv\\skill.csv");
        Data.chargerDonnees("src\\csv\\users.csv");

        //2. MISE EN PLACE DANS LA BDD par noeud
        //armes
        for (Arme a : Data.lst_armes) {
            bdd.run("CREATE (ar:Arme {nom:'" + a.getNom() + "', niveau:'" + a.getNiveau() + "', puissance:'" + a.getPuissance()+ "', rarete:'" + a.getRarete() + " '})");
        }
        //animal
        for (AnimalCompagnie a : Data.lst_animal) {
            bdd.run("CREATE (ac:Animal {type:'" + a.getType() + "', nom:'" + a.getNom() + "', niveau:'" + a.getNiveau() + "', age:'" + a.getAge()+ "', effet:'" + a.getEffet() + " '})");
        }
        //classe
        for (Classe cl : Data.lst_classe) {
            bdd.run("CREATE (cla:Classe {nom:'" + cl.getNom() + "'})");
        }
        //evenement
        for (EvenementJeu e : Data.lst_event) {
            bdd.run("CREATE (e:Event {nom:'" + e.getNom() + "', dateDebut:'" + e.getDateDebut() + "', dateFin:'" + e.getDateFin()+ "', lieu:'" + e.getLieu() + " '})");
        }
        //guilde
        for (Guilde g : Data.lst_guilde) {
            bdd.run("CREATE (g:Guilde {nom:'" + g.getNom() + "', niveau:'" + g.getNiveau() + " '})");
        }
        //skill
        for (Competence c : Data.lst_skill) {
            bdd.run("CREATE (c:Competence {nom:'" + c.getNom() + "', nSort:'" + c.getnSort() + "', puissance:'" + c.getPuissance()+ "', cMagie:'" + c.getcMagie() + "', tRechargement:'" + c.gettRechargement() + " '})");
        }
        //users
        for (Utilisateur u : Data.lst_users) {
                bdd.run("CREATE (u:Utilisateur {nom:'" + u.getPseudo() + "', email:'" + u.getEmail() + "', niveau:'" + u.getNiveau()+ "', pm:'" + u.getPm() + "', pv:'" + u.getPv() + " '})");
        }
        LinkUtilisateurGuilde(bdd);
        resteEnvironnement(bdd);
        LinkUserClasse(bdd);
        LinkUserArme(bdd);
        LinkUserAnimal(bdd);


    }
    public static void LinkUtilisateurGuilde(Session bdd)
    {
        /**
         *Fais la relation entre l'utilisateur et sa guilde (avec la relation APPARTIENT)
         **/
        System.out.println("debut link user-guilde");
        Random rand = new Random();
        int limite_user = 100;
        int limite_guilde = 50;
        int rdm_number = rand.nextInt(limite_user);

            for (int j = 0; j < rdm_number; j++) {
                int rdm_guilde = rand.nextInt(limite_guilde);
                int rdm_utilisateur = rand.nextInt(limite_user);
                Result result = bdd.run("MATCH (g:Guilde) <– [ :APPARTIENT] – (u:Utilisateur {nom:'"+Data.lst_users.get(rdm_utilisateur).getPseudo()+"'}) RETURN g, u");
                if (result.hasNext())
                {}
                else
                {
                    bdd.run("MATCH (g:Guilde), (u:Utilisateur) WHERE g.nom='" + Data.lst_guilde.get(rdm_guilde).getNom() + "' AND u.nom='" + Data.lst_users.get(rdm_utilisateur).getPseudo() + "' CREATE (u) –[:APPARTIENT]-> (g)");
                }
            }
        System.out.println("fin link user-guilde");
         }
    public static void resteEnvironnement (Session bdd)
    {
        /**
        *Construit le reste de l'environnement
         */
        System.out.println("debut link classe-skill");
        /*Rappel:
        Les Guerriers
            Lancer de bouclier
            Tourbillon d’épée
        Les Mages
            Bouclier Aqueux
            Boule de Feu
        Les Soigneurs
            Régénération
            Réanimation
        Les Assassins
            Assassinat
            Empoisonnement
         */
        for (Classe c : Data.lst_classe)
        {
           if (c.getNom().equals("guerrier"))
           {
            for (int i = 0; i <= 1; i++)
            {
                System.out.println("ajout skill guerrier");
                bdd.run("MATCH (cl:Classe), (cp:Competence) WHERE cl.nom contains '"+c.getNom()+"' AND cp.nom contains '" + Data.lst_skill.get(i).getNom() + "' CREATE (cl) –[:MAITRISE]-> (cp)");
            }
           } else if (c.getNom().equals("mage") )
           {
               for (int i = 2; i <= 3; i++)
               {
                   System.out.println("ajout skill mage");
                   bdd.run("MATCH (cl:Classe), (cp:Competence) WHERE cl.nom contains '"+c.getNom()+"' AND cp.nom contains '" + Data.lst_skill.get(i).getNom() + "' CREATE (cl) –[:MAITRISE]-> (cp)");
               }
           }
           else if (c.getNom().equals("soigneur"))
           {
               for (int i = 4; i <= 5; i++)
               {
                   System.out.println("ajout skill soigneur");
                   bdd.run("MATCH (cl:Classe), (cp:Competence) WHERE cl.nom contains '"+c.getNom()+"' AND cp.nom contains '" + Data.lst_skill.get(i).getNom() + "' CREATE (cl) –[:MAITRISE]-> (cp)");
               }
           }
            else if (c.getNom().equals("assassin") )
           {
               for (int i = 6; i <= 7; i++)
               {
                   System.out.println("ajout skill assassin");
                   bdd.run("MATCH (cl:Classe), (cp:Competence) WHERE cl.nom contains'"+c.getNom()+"' AND cp.nom contains'" + Data.lst_skill.get(i).getNom() + "' CREATE (cl) –[:MAITRISE]-> (cp)");
               }
           }
        }
        System.out.println("fin link classe-skill");
    }
    public static void LinkUserClasse(Session bdd)
    {
        /**
        *Fait le lien entre l'utilisateur et sa classe
         */
        System.out.println("debut link user-classe");
        Random rand = new Random();
        int limite_Classe = 4;
        for (Utilisateur u: Data.lst_users)
        {
            int rdm_classe = rand.nextInt(limite_Classe);
            Result result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN c, u");
            if (result.hasNext())
            {}
            else
            {
                bdd.run("MATCH (c:Classe), (u:Utilisateur) WHERE c.nom contains'" + Data.lst_classe.get(rdm_classe).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:EST]-> (c)");

            }
        }
        System.out.println("fin link user-classe");
    }
    public static void LinkUserArme(Session bdd)
    {
        /**
        *Lie l'utilisateur et son arme en fonction de sa classe
         * */
        int min = 0;
        int limite_Arme = 5;
        System.out.println("debut link user-arme");
        Random rand = new Random();
        for (Utilisateur u: Data.lst_users) {
            //check classe user et donner arme aléatoire.
            Result result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'guerrier') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Arme) <– [ :UTILISE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 0;
                    limite_Arme = 5;
                    int rdm_arme = rand.nextInt(min,limite_Arme);
                    bdd.run("MATCH (a:Arme), (u:Utilisateur) WHERE a.nom contains'" + Data.lst_armes.get(rdm_arme).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:UTILISE]-> (a)");
                }
            }
            result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'mage') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Arme) <– [ :UTILISE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 6;
                    limite_Arme = 10;
                    int rdm_arme = rand.nextInt(min,limite_Arme);
                    bdd.run("MATCH (a:Arme), (u:Utilisateur) WHERE a.nom contains'" + Data.lst_armes.get(rdm_arme).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:UTILISE]-> (a)");
                }
            }
            result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'soigneur') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Arme) <– [ :UTILISE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 11;
                    limite_Arme = 15;
                    int rdm_arme = rand.nextInt(min,limite_Arme);
                    bdd.run("MATCH (a:Arme), (u:Utilisateur) WHERE a.nom contains'" + Data.lst_armes.get(rdm_arme).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:UTILISE]-> (a)");
                }
            }
            result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'assassin') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Arme) <– [ :UTILISE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 16;
                    limite_Arme = 20;
                    int rdm_arme = rand.nextInt(min,limite_Arme);
                    bdd.run("MATCH (a:Arme), (u:Utilisateur) WHERE a.nom contains'" + Data.lst_armes.get(rdm_arme).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:UTILISE]-> (a)");
                }
            }
        }
        System.out.println("fin link user-arme");
    }
    public static void LinkUserAnimal(Session bdd)
    {
        /** Lie l'utilisateur à son animal en fonction de sa classe
        Dragon (Guerrier)
        Griffon (Soigneur)
        Cerbère (Mage)
        Basilic (Assassin)
         **/
        int min = 0;
        int limite_Animal = 4;
        System.out.println("debut link user-animal");
        Random rand = new Random();
        for (Utilisateur u: Data.lst_users) {
            Result result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'guerrier') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Animal) <– [ :POSSEDE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 0;
                    limite_Animal = 3;
                    int rdm_animal = rand.nextInt(min,limite_Animal);
                    bdd.run("MATCH (a:Animal), (u:Utilisateur) WHERE a.nom contains '" + Data.lst_animal.get(rdm_animal).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:POSSEDE]-> (a)");
                }
            }
            result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'mage') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Animal) <– [ :POSSEDE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 4;
                    limite_Animal = 7;
                    int rdm_animal = rand.nextInt(min,limite_Animal);
                    bdd.run("MATCH (a:Animal), (u:Utilisateur) WHERE a.nom contains'" + Data.lst_animal.get(rdm_animal).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:POSSEDE]-> (a)");
                }
            }
            result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'soigneur') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Animal) <– [ :POSSEDE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 8;
                    limite_Animal = 11;
                    int rdm_animal = rand.nextInt(min,limite_Animal);
                    bdd.run("MATCH (a:Animal), (u:Utilisateur) WHERE a.nom contains'" + Data.lst_animal.get(rdm_animal).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:POSSEDE]-> (a)");
                }
            }
            result = bdd.run("MATCH (c:Classe) <– [ :EST] – (u:Utilisateur {nom:'"+u.getPseudo()+"'} where c.nom contains 'assassin') RETURN c, u");
            if (result.hasNext())
            {
                Result res2 = bdd.run("MATCH (a:Animal) <– [ :POSSEDE] – (u:Utilisateur {nom:'"+u.getPseudo()+"'}) RETURN a, u");
                if (res2.hasNext())
                {}
                else
                {
                    min = 12;
                    limite_Animal = 15;
                    int rdm_animal = rand.nextInt(min,limite_Animal);
                    bdd.run("MATCH (a:Animal), (u:Utilisateur) WHERE a.nom contains'" + Data.lst_animal.get(rdm_animal).getNom() + "' AND u.nom contains '" + u.getPseudo() + "' CREATE (u) –[:POSSEDE]-> (a)");
                }
            }
        }
        System.out.println("fin link user-animal");
    }
    public static void delete_all(Session bdd){
        /**
        *Permet à chaque lancement de script de supprimer les noeuds et les relations existantes
         */
        bdd.run("match (a) -[r] -> () delete a, r");
        bdd.run("match (a) delete a");
    }
}
