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

import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.comparators.SkillByRemainingSPComparator;
import uk.co.md87.evetool.ui.MainWindow;

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

        new MainWindow().setVisible(true);
        
        final EveApi api = ApiFactory.getApi();
        api.setApiKey("yaISaqXrSnaQPnRSFi4ODeWjSzWu2gNq1h6F0tVevtSGr5dzoEkZ6YrzHeBzzgNg");
        api.setUserID("403848");
        api.setCharID("113499922");

        final CharacterSheet cs = api.getCharacterSheet().getResult();
        cs.associateSkills(api.getSkillTree().getResult());
        Collections.sort(cs.getSkills(), new SkillByRemainingSPComparator(true));
        System.out.println(cs.getSkills());
    }

}
