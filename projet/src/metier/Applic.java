package metier;

import dao.Bdd;
import dao.Data;
import domaine.EvenementJeu;
import domaine.Utilisateur;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Applic {
    public Applic() throws ParseException {
        Session bdd = Bdd.connect_db();
        Bdd.delete_all(bdd);
        System.out.println("-----Création de l'environnement-----");
        Bdd.setup_env(bdd);
        System.out.println("-----Requêtes-----");
        System.out.println("-----Participation des guildes aux évènements aux critères-----");
        participerEvent(bdd,"Terre Brulee","mage ","Bouclier Aqueux");
        participerEvent(bdd,"Au Vent","Dragon","Griffon");
        System.out.println("-----Trouver des parrains-----");
        trouverParrain(bdd);
        System.out.println("-----Calcul du nombre de joueurs par classes dans chaque guilde-----");
        nombreJoueurParGuilde(bdd);
        System.out.println("-----Évènement des spécialistes-----");
        classeMoinsUtilisee(bdd);
    }

    public static void participerEvent(Session session, String nomEvenement, String requis1, String requis2) {
        /*Pour participer à l’évènement “Terre Brûlée”, la guilde doit avoir au moins un joueur possédant la compétence de “Bouclier aqueux”. Si celle-ci l’a, elle est inscrite automatiquement à l’évènement.
        Pour participer à l’évènement “Au Vent”, la guilde doit avoir au moins un joueur possédant soit un Dragon ou Griffon
        */
        if (nomEvenement.equals("Terre Brulee")) {
            Result res = session.run("MATCH (g:Guilde) <- [ :APPARTIENT] -(u:Utilisateur) - [ :EST] -> (c:Classe {nom:'" + requis1 + "'}) - [ :MAITRISE] -> (com:Competence {nom:'" + requis2 + "'}) return g,u,c,com");
            while (res.hasNext()) {
                Record rec = res.next();
                String nomGuilde = String.valueOf(rec.get("g").get("nom"));
                nomGuilde = nomGuilde.replace("\"", "");
                Result res2 = session.run("MATCH (gu:Guilde {nom:'" + nomGuilde + "'}) -[ :PARTICIPE] -> (e:Event {nom:'" + nomEvenement + "'}) return gu,e");
                if (res2.hasNext()) {
                } else {
                    String xxx = "MATCH (gu:Guilde),(e:Event) WHERE gu.nom contains '" + nomGuilde + "' AND e.nom contains '" + nomEvenement + "' CREATE (gu) –[:PARTICIPE]-> (e)";
                    session.run(xxx);
                }
            }

        } else if (nomEvenement.equals("Au Vent")) {
            Result resVent = session.run("MATCH (g:Guilde) <- [ :APPARTIENT] -(u:Utilisateur) - [ :POSSEDE] -> (a:Animal) WHERE a.type='" + requis1 + "' or a.type ='" + requis2 + "'return g,u,a");
            while (resVent.hasNext()) {
                Record rec = resVent.next();
                String nomGuilde = String.valueOf(rec.get("g").get("nom"));
                nomGuilde = nomGuilde.replace("\"", "");
                Result res2 = session.run("MATCH (gu:Guilde {nom:'" + nomGuilde + "'}) -[ :PARTICIPE] -> (e:Event {nom:'" + nomEvenement + "'}) return gu,e");
                if (res2.hasNext()) {
                } else {
                    String xxx = "MATCH (gu:Guilde),(e:Event) WHERE gu.nom contains '" + nomGuilde + "' AND e.nom contains '" + nomEvenement + "' CREATE (gu) –[:PARTICIPE]-> (e)";
                    session.run(xxx);
                }
            }
        }
    }

    private static String randomJoueur(Session session){
        /*Création et ajout dans la BDD d'un joueur aléatoire: on simule un nouvel utilisateur qui entre dans le jeu. Celui-ci est créé et va avoir une classe de manière aléatoire.
        * On retourne la classe du joueur afin de l'utiliser sa classe dans la fonction trouverParrain
        */
        Utilisateur nouveauJoueur = new Utilisateur("BddSlayer01", "BddSlayer01@gmail.com", 1, 100, 100);
        session.run("CREATE (u:Utilisateur {nom:'" + nouveauJoueur.getPseudo() + "', email:'" + nouveauJoueur.getEmail() + "', niveau:'" + nouveauJoueur.getNiveau() + "', pm:'" + nouveauJoueur.getPm() + "', pv:'" + nouveauJoueur.getPv() + " '})");
        Random rand = new Random();
        int randClasse = rand.nextInt(4);
        String classeJoueur = Data.lst_classe.get(randClasse).getNom();
        session.run("MATCH (c:Classe), (u:Utilisateur) WHERE c.nom contains'" + classeJoueur + "' AND u.nom contains '" + nouveauJoueur.getPseudo() + "' CREATE (u) –[:EST]-> (c)");
        return nouveauJoueur.getPseudo();
    }

    public static void trouverParrain(Session session) {
        /* Parrainage : Un joueur de niveau 1, lors de son inscription/création dans le jeu, sera parrainé par un joueur de niveau 60 ayant la même classe que lui. Si on ne trouve pas de parrain, il y a aura un message alertant sur le fait que le joueur est premier dans sa catégorie. Si on trouve un "formateur", le joueur ainsi que le futur parrain seront alerté
         * */
        //On cherche la classe du joueur qu'on a créé
        Result chercherJoueur = session.run("MATCH (u:Utilisateur) –[:EST]-> (c:Classe) WHERE u.nom='"+randomJoueur(session)+"' return u, c");
        String classeJoueur = String.valueOf(chercherJoueur.next().get("c").get("nom"));
        classeJoueur = classeJoueur.replace("\"", "");
        //On cherche un parrain dont la classe est la même que le joueur et ayant un niveau de 60
        Result res = session.run("MATCH (u:Utilisateur), (c:Classe) WHERE u.niveau = '60' AND c.nom contains '" + classeJoueur + "' return u,c");
        if (res.hasNext()) {
            System.out.println("Nous vous avons trouvé des parrains!");
            while (res.hasNext()) {
                Record rec = res.next();
                System.out.println("    - " + rec.get("u").get("nom"));
            }
        } else {
            System.out.println("Aucun parrain trouvé...");
        }
    }

    public static void nombreJoueurParGuilde(Session session) {
        /*
        Statistique : Les créateurs du jeu souhaitent connaitre le nombre de joueur par guilde regroupé par classe
        */
        Result res = session.run("MATCH (g:Guilde) return g");
        while (res.hasNext()) {
            Record rec = res.next();
            String nomGuilde = String.valueOf(rec.get("g").get("nom"));
            nomGuilde = nomGuilde.replace("\"", "");
            System.out.println("La guilde " + nomGuilde + " :");
            compterJoueurGuilde(session, nomGuilde);
        }
    }

    private static void compterJoueurGuilde(Session session, String nomGuilde) {
        /*
        Cette fonction permet de compter le nom de joueur dans la guile par Classe
        */
        int totalJoueur = 0;
        int totalMage = 0;
        int totalGuerrier = 0;
        int totalSoigneur = 0;
        int totalAssassin = 0;

        Result res = session.run("MATCH (g:Guilde) <- [ :APPARTIENT] -(u:Utilisateur) - [ :EST] -> (c:Classe) WHERE g.nom ='" + nomGuilde + "' return g, u, c");
        while (res.hasNext()) {
            Record rec = res.next();
            totalJoueur += 1;
            String nomClasse = String.valueOf(rec.get("c").get("nom"));
            nomClasse = nomClasse.replace("\"", "");
            switch (nomClasse) {
                case "mage" -> {
                    totalMage += 1;
                    totalGuerrier += 0;
                    totalSoigneur += 0;
                    totalAssassin += 0;
                }
                case "guerrier" -> {
                    totalGuerrier += 1;
                    totalMage += 0;
                    totalSoigneur += 0;
                    totalAssassin += 0;
                }
                case "soigneur" -> {
                    totalSoigneur += 1;
                    totalMage += 0;
                    totalGuerrier += 0;
                    totalAssassin += 0;
                }
                case "assassin" -> {
                    totalAssassin += 1;
                    totalGuerrier += 0;
                    totalSoigneur += 0;
                    totalMage += 0;
                }
            }
        }
        System.out.println("Total de joueurs : " + totalJoueur + " joueurs. Sur ces joueurs, elle contient:");
        System.out.println("\t - Mage     : " + totalMage);
        System.out.println("\t - Guerrier : " + totalGuerrier);
        System.out.println("\t - Soigneur : " + totalSoigneur);
        System.out.println("\t - Assassin : " + totalAssassin);
    }

    public static void classeMoinsUtilisee(Session session) throws ParseException {
        /*
        Équilibrage : Les créateurs souhaitent faire du tri et savoir quelle classe est la moins utilisée et créer un évènement entre guilde ayant des spécialistes de la classe (niveau 50 minimum pour être considéré être un spécialiste).
        */
        //1. Compter le nombre de joueurs par classe + total de joueur sur la plateforme
        float totalMage = 0;
        float totalGuerrier = 0;
        float totalSoigneur = 0;
        float totalAssassin = 0;
        Result res = session.run("MATCH (u:Utilisateur) -[:EST]->(c:Classe) RETURN u,c");
        Result totalJoueur = session.run("MATCH (u:Utilisateur) RETURN count(u) AS cpt");
        int nbJoueurs = Integer.parseInt(String.valueOf(totalJoueur.next().get("cpt")));

        while (res.hasNext()) {
            Record rec = res.next();
            String nomClasse = String.valueOf(rec.get("c").get("nom"));
            nomClasse = nomClasse.replace("\"", "");
            switch (nomClasse) {
                case "mage" -> {
                    totalMage += 1;
                    totalGuerrier += 0;
                    totalSoigneur += 0;
                    totalAssassin += 0;
                }
                case "guerrier" -> {
                    totalGuerrier += 1;
                    totalMage += 0;
                    totalSoigneur += 0;
                    totalAssassin += 0;
                }
                case "soigneur" -> {
                    totalSoigneur += 1;
                    totalMage += 0;
                    totalGuerrier += 0;
                    totalAssassin += 0;
                }
                case "assassin" -> {
                    totalAssassin += 1;
                    totalGuerrier += 0;
                    totalSoigneur += 0;
                    totalMage += 0;
                }
            }
        }

        HashMap<String,Float> totalParClasse = new HashMap<String, Float>();
        totalParClasse.put("mage",totalMage);
        totalParClasse.put("guerrier",totalGuerrier);
        totalParClasse.put("soigneur",totalSoigneur);
        totalParClasse.put("assassin",totalAssassin);

        //2. Faire le pourcentage des joueurs et l'afficher
        float pourcentageMage = calculePourcentage(totalMage,nbJoueurs);
        float pourcentageGuerrier = calculePourcentage(totalGuerrier,nbJoueurs) ;
        float pourcentageSoigneur = calculePourcentage(totalSoigneur,nbJoueurs);
        float pourcentageAssassin = calculePourcentage(totalAssassin,nbJoueurs);

        System.out.println("\t - Mage     : " + (int) totalMage + " (soit " + pourcentageMage +" % des joueurs)");
        System.out.println("\t - Guerrier : " + (int) totalGuerrier + " (soit " + pourcentageGuerrier +" % des joueurs)");
        System.out.println("\t - Soigneur : " + (int) totalSoigneur + " (soit " + pourcentageSoigneur +" % des joueurs)");
        System.out.println("\t - Assassin : " + (int) totalAssassin + " (soit " + pourcentageAssassin +" % des joueurs)");

        //3. Création de l'évènement "Les spécialistes"
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy"); //format de la date
        Date dateDebut = formatter1.parse("19/12/2022");
        Date dateFin = formatter1.parse("25/12/2022");
        EvenementJeu eventSpecialistes = new EvenementJeu("Autour des lacs",dateDebut,dateFin,"Grottes Macabres");
        session.run("CREATE (e:Event {nom:'" + eventSpecialistes.getNom() + "', dateDebut:'" + eventSpecialistes.getDateDebut() + "', dateFin:'" + eventSpecialistes.getDateFin()+ "', lieu:'" + eventSpecialistes.getLieu() + " '})");
        //4. Inscription à l'évènement
        Result resEvent = session.run("MATCH (g:Guilde) <- [ :APPARTIENT] -(u:Utilisateur) - [ :EST] -> (c:Classe {nom:'"+trouverMinimum(totalParClasse)+"'}) WHERE u.niveau >= 50 return g, u, c");
        while (resEvent.hasNext()){
            Record rec = resEvent.next();
            String nomGuilde = String.valueOf(rec.get("g").get("nom"));
            nomGuilde = nomGuilde.replace("\"", "");
            Result res2 = session.run("MATCH (gu:Guilde {nom:'" + nomGuilde + "'}) -[ :PARTICIPE] -> (e:Event {nom:'" + eventSpecialistes.getNom() + "'}) return gu,e");
            if (res2.hasNext()) {
            } else {
                String xxx = "MATCH (gu:Guilde),(e:Event) WHERE gu.nom contains '" + nomGuilde + "' AND e.nom contains '" + eventSpecialistes.getNom() + "' CREATE (gu) –[:PARTICIPE]-> (e)";
                session.run(xxx);
            }
        }
    }

    private static String trouverMinimum(HashMap<String,Float> dico){
        /*
        Trouve la valeur minimum dans un dictionnaire et trouver sa clef. Le but est de trouver dans le dictionnaire des classes, la plus petite classe et de retourner le nom de celle-ci.
         */
        float min = (float) dico.values().toArray()[0]; //On prend la première clef
        String minimum = "";

        for (Float f: dico.values()) {
            /*
            Trouve la valeur la plus petite
             */
            if (min > f){
                min = f;
            }
        }

        for (String f : dico.keySet()){
            /*
            Trouve la clef correspondante à cette valeur minimum
             */
            if (dico.get(f) == min){
                minimum = f;
            }
        }
        return minimum;
    }

    private static float calculePourcentage(float totalClasse, int totalJoueurs){
        /*
        Fonction permettant de calculer le pourcentage de joueur dans la classe
        */
        float result = 0;
        try {
            result = (totalClasse * 100) / totalJoueurs;
        } catch (ArithmeticException erreur){
            System.out.println("Le calcul n'est pas possible car le nombre de joueur ne peut être égal à 0");
        }
        return result;
    }
}
