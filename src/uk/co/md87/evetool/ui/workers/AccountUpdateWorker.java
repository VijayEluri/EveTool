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
import javax.swing.JSeparator;
import javax.swing.SwingWorker;

import uk.co.md87.evetool.api.ApiResponse;
import uk.co.md87.evetool.api.wrappers.CharacterList;
import uk.co.md87.evetool.api.wrappers.data.BasicCharInfo;
import uk.co.md87.evetool.ui.pages.OverviewPage;

/**
 *
 * TODO: Document AccountUpdateWorker
 * @author chris
 */
public class AccountUpdateWorker extends SwingWorker<ApiResponse<CharacterList>, Object> {

    private final String account;
    private final OverviewPage overview;

    public AccountUpdateWorker(final String account, OverviewPage overview) {
        super();
        this.overview = overview;
        this.account = account;
    }

    /** {@inheritDoc} */
    @Override
    protected ApiResponse<CharacterList> doInBackground() throws Exception {
        Logger.getLogger(OverviewPage.class.getName()).log(Level.FINEST, "doInBackground()");
        return overview.getApi().getCharacterList();
    }

    @Override
    protected void done() {
        Logger.getLogger(OverviewPage.class.getName()).log(Level.FINEST, "done() - ");
        try {
            final ApiResponse<CharacterList> res = get();
            System.out.println(Thread.currentThread().getName());
            if (res.wasSuccessful()) {
                overview.getPanels().get(account).removeAll();
                boolean first = true;

                for (BasicCharInfo character : res.getResult()) {
                    if (first) {
                        first = false;
                    } else {
                        overview.getPanels().get(account).add(new JSeparator(),
                                "span, growx, pushx, gaptop 5, gapbottom 5");
                    }

                    final JLabel portrait = new JLabel("Loading...");
                    final JLabel nameLabel = new JLabel(character.getName());
                    nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
                    
                    overview.getPanels().get(account)
                            .add(portrait, "spany 2, height 64!, width 64!");
                    overview.getPanels().get(account)
                            .add(nameLabel, "height 20!");
                    overview.getPanels().get(account)
                            .add(new JLabel(character.getCorp().getName(), JLabel.RIGHT), "wrap");
                    overview.getPanels().get(account)
                            .add(new JLabel("Training XXXX to level 5 " +
                            "(3 years 1 second...)"), "gaptop 20, height 20!");
                    overview.getPanels().get(account)
                            .add(new JLabel("1,333,337 ISK", JLabel.RIGHT), "wrap");

                    new PortraitLoaderWorker(character.getId(), portrait, 64).execute();
                }
            } else {
                overview.getPanels().get(account).removeAll();
                overview.getPanels().get(account).add(new JLabel("Error!: " + res.getError()));
            }
            overview.getPanels().get(account).revalidate();
        } catch (Exception ex) {
            Logger.getLogger(OverviewPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
