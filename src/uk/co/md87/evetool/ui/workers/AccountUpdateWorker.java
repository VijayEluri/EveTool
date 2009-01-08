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

package uk.co.md87.evetool.ui.workers;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingWorker;

import uk.co.md87.evetool.api.ApiResponse;
import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.api.wrappers.CharacterList;
import uk.co.md87.evetool.api.wrappers.data.BasicCharInfo;
import uk.co.md87.evetool.ui.pages.OverviewPage;

/**
 *
 * TODO: Document AccountUpdateWorker
 * @author chris
 */
public class AccountUpdateWorker extends SwingWorker<ApiResponse<CharacterList>, Object> {

    private final EveApi api;
    private final JPanel target;

    public AccountUpdateWorker(final EveApi api, final JPanel panel) {
        super();
        
        this.api = api;
        this.target = panel;
    }

    /** {@inheritDoc} */
    @Override
    protected ApiResponse<CharacterList> doInBackground() throws Exception {
        return api.getCharacterList();
    }

    @Override
    protected void done() {
        try {
            final ApiResponse<CharacterList> res = get();
            if (res.wasSuccessful()) {
                target.removeAll();
                boolean first = true;

                for (BasicCharInfo character : res.getResult()) {
                    if (first) {
                        first = false;
                    } else {
                        target.add(new JSeparator(),
                                "span, growx, pushx, gaptop 5, gapbottom 5");
                    }

                    final JLabel portrait = new JLabel("Loading...");
                    final JLabel nameLabel = new JLabel(character.getName());
                    final JLabel skillLabel = new JLabel("Loading...");
                    final JLabel iskLabel = new JLabel("Loading...", JLabel.RIGHT);
                    nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
                    
                    target.add(portrait, "spany 2, height 64!, width 64!");
                    target.add(nameLabel, "height 20!");
                    target.add(new JLabel(character.getCorp().getName(), JLabel.RIGHT), "wrap");
                    target.add(skillLabel, "span, split, gaptop 20, height 20!");
                    target.add(iskLabel, "wrap");

                    final EveApi newApi = api.clone();
                    newApi.setCharID(character.getId());

                    new PortraitLoaderWorker(character.getId(), portrait, 64).execute();
                    new CharacterBalanceUpdateWorker(newApi, iskLabel).execute();
                    new CharacterSkillUpdateWorker(newApi, skillLabel).execute();
                }
            } else {
                target.removeAll();
                target.add(new JLabel("Error: " + res.getError()));
            }
            target.revalidate();
        } catch (Exception ex) {
            Logger.getLogger(OverviewPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
