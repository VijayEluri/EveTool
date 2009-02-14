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

import com.dmdirc.util.ConfigFile;
import com.dmdirc.util.InvalidConfigFileException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TODO: Document ConfigManager
 * @author chris
 */
public class ConfigManager {

    /** Logger to use for this class. */
    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());

    private final ConfigFile configFile;
 
    public ConfigManager() {
        final File dir = new File(getConfigDir());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        configFile = new ConfigFile(new File(dir, "evetool.config"));
        configFile.setAutomake(true);
        
        try {
            configFile.read();
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Error reading config file", ex);
        } catch (InvalidConfigFileException ex) {
            LOGGER.log(Level.WARNING, "Error parsing config file", ex);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
    }

    public String getGeneralSetting(final String key) {
        return configFile.getKeyDomain("general").get(key);
    }

    public void save() {
        try {
            configFile.write();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error writing config file", ex);
        }
    }

    /**
     * Returns the application's config directory.
     *
     * @return configuration directory
     */
    public String getConfigDir() {
        final String fs = System.getProperty("file.separator");
        final String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            return System.getProperty("user.home") + fs + "Library" + fs
                    + "Preferences" + fs + "evetool" + fs;
        } else if (osName.startsWith("Windows")) {
            if (System.getenv("APPDATA") == null) {
                return System.getProperty("user.home") + fs + "evetool" + fs;
            } else {
                return System.getenv("APPDATA") + fs + "evetool" + fs;
            }
        } else {
            return System.getProperty("user.home") + fs + ".evetool" + fs;
        }
    }

    private class ShutdownHook implements Runnable {

        /** {@inheritDoc} */
        @Override
        public void run() {
            save();
        }

    }
}
