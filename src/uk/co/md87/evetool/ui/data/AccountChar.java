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

package uk.co.md87.evetool.ui.data;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import uk.co.md87.evetool.api.ApiResponse;
import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.api.wrappers.SkillInTraining;
import uk.co.md87.evetool.api.wrappers.data.BasicCharInfo;
import uk.co.md87.evetool.ui.data.interfaces.Retrievable;
import uk.co.md87.evetool.ui.util.Formatter;

/**
 *
 * @author chris
 */
public class AccountChar {

    protected final BasicCharInfo charInfo;
    protected final JLabel portrait, balance, skill, name;
    protected ApiResponse<SkillInTraining> training;
    protected ApiResponse<CharacterSheet> sheet;

    public AccountChar(final BasicCharInfo charInfo, final JLabel portrait,
            final JLabel name, final JLabel balance, final JLabel skill) {
        this.charInfo = charInfo;
        this.portrait = portrait;
        this.balance = balance;
        this.name = name;
        this.skill = skill;
    }

    @Retrievable(name = "Character name")
    public String getName() {
        return charInfo.getName();
    }

    public BasicCharInfo getCharInfo() {
        return charInfo;
    }

    public ApiResponse<CharacterSheet> getSheet() {
        return sheet;
    }

    public void setSheet(final ApiResponse<CharacterSheet> sheet) {
        this.sheet = sheet;
        
        if (sheet.wasSuccessful()) {
            this.balance.setText(String.format("%,.2f", sheet.getResult().getBalance()));

            if (sheet.getResult().getSkillPoints() > sheet.getResult().getClone().getSpLimit()) {
                name.setIcon(new ImageIcon(getClass().getResource("../res/error.png")));
                name.setToolTipText(
                        String.format("Character's clone is %,d SP too low",
                        sheet.getResult().getSkillPoints()
                        - sheet.getResult().getClone().getSpLimit()));
            }
        } else {
            this.balance.setText("(Error)");
        }
    }

    public synchronized void setTraining(final ApiResponse<SkillInTraining> training) {
        this.training = training;

        updateSkillInfo(true);
    }

    public synchronized void updateSkillInfo(final boolean initial) {
        if (training == null && initial) {
            this.skill.setText("Loading...");
        } else if (training != null && training.wasSuccessful()) {
            if (training.getResult().isInTraining()) {
                final int duration = (int) (training.getResult().getEndTime().getTime()
                        - System.currentTimeMillis()) / 1000;
                this.skill.setText("Training "
                        + training.getResult().getSkill().getName() + " to level "
                        + training.getResult().getTargetLevel() + " ("
                        + Formatter.formatDuration(duration) + ")");
            } else if (initial) {
                this.skill.setText("Nothing training");
            }
        } else if (initial) {
            this.skill.setText("(Error)");
        }
    }

}
