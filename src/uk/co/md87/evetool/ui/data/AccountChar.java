/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.ui.data;

import javax.swing.Icon;
import javax.swing.JLabel;
import uk.co.md87.evetool.api.ApiResponse;
import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.api.wrappers.SkillInTraining;
import uk.co.md87.evetool.api.wrappers.data.BasicCharInfo;
import uk.co.md87.evetool.ui.util.Formatter;

/**
 *
 * @author chris
 */
public class AccountChar {

    protected final BasicCharInfo charInfo;
    protected final JLabel portrait, balance, skill;
    protected ApiResponse<SkillInTraining> training;
    protected ApiResponse<CharacterSheet> sheet;

    public AccountChar(final BasicCharInfo charInfo, final JLabel portrait,
            final JLabel balance, final JLabel skill) {
        this.charInfo = charInfo;
        this.portrait = portrait;
        this.balance = balance;
        this.skill = skill;
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
