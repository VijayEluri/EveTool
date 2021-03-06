/*
 * Copyright (c) 2009 Chris Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.co.md87.evetool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.util.TableCreator;
import uk.co.md87.evetool.ui.MainWindow;

/**
 * Main class for the program. Handles initialisation of the core assets
 * and UI.
 *
 * @author chris
 */
public class Main {

    /** The version of this instance of EVE Tool. */
    public static String version;

    /** Tables that are needed by the application. */
    private static final String[] TABLES
            = new String[]{"Accounts", "PageConfigElements", "PageConfigs"};

    /**
     * Main program entry point.
     * 
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        initLogging();

        final ConfigManager config = new ConfigManager();
        final ApiFactory factory = new ApiFactory(config);
        final ImageManager imagem = new ImageManager(config.getConfigDir() + "images");

        config.checkDatabase(factory.getConnection());

        readVersion();
        initTables(factory);

        final AccountManager manager = new AccountManager(factory.getConnection());

        new MainWindow(manager, factory, config, imagem).setVisible(true);
    }

    /**
     * Initialises the logging system.
     */
    protected static void initLogging() {
        Logger.getLogger("uk").setLevel(Level.ALL);

        for (Handler handler : Logger.getLogger("").getHandlers()) {
            handler.setLevel(Level.ALL);
        }
    }

    /**
     * Initialises tables used by the program.
     *
     * @param factory The ApiFactory to use for the database connection
     */
    protected static void initTables(final ApiFactory factory) {
        new TableCreator(factory.getConnection(), "/uk/co/md87/evetool/sql/",
                TABLES).checkTables();
    }

    /**
     * Reads the version string from the bundled version.txt file.
     */
    protected static void readVersion() {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(
                    Main.class.getResourceAsStream("version.txt")));
            version = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
