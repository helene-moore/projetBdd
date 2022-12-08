package metier;

import dao.Bdd;
import org.neo4j.driver.Session;

import java.text.ParseException;

public class Applic {
    public Applic() throws ParseException {
        Session bdd = Bdd.connect_db();
        Bdd.setup_env(bdd);
        Bdd.delete_all(bdd);

    }
}
