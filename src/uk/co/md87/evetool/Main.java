/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        ApiFactory.getApi();
    }

}
