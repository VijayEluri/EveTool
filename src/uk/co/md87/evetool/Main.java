/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.md87.evetool.api.EveApi;

/**
 *
 * @author chris
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger.getLogger("uk").setLevel(Level.ALL);

        for (Handler handler : Logger.getLogger("").getHandlers()) {
            handler.setLevel(Level.ALL);
        }
        
        final EveApi api = ApiFactory.getApi();
        api.setApiKey("yaISaqXrSnaQPnRSFi4ODeWjSzWu2gNq1h6F0tVevtSGr5dzoEkZ6YrzHeBzzgNg");
        api.setUserID("403848");
        api.setCharID("113499922");
        System.out.println(api.getCharacterList());
        System.out.println(api.getSkillInTraining());
    }

}
