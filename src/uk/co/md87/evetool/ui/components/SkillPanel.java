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

package uk.co.md87.evetool.ui.components;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;
import uk.co.md87.evetool.ui.util.Formatter;

/**
 *
 * TODO: Document SkillPanel
 * @author chris
 */
public class SkillPanel extends JPanel {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private static Image completeImage;
    private static Image trainingImage;
    private static Image normalImage;
    private static Image untrainedImage;

    static {
        try {
            completeImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_14.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            trainingImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_12.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            normalImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_13.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            untrainedImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_11.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            // Do nothing
        }
    }

    public SkillPanel(final TrainedSkillInfo skill) {
        super(new MigLayout("ins 0, fillx"));
        
        Image image;

        if (skill.canTrainFurther()) {
            if (skill.getSP() < 0) {
                image = untrainedImage;
            } else if (skill.isPartiallyTrained()) {
                image = normalImage;
            } else {
                image = trainingImage;
            }
        } else {
            image = completeImage;
        }

        add(new JLabel(new ImageIcon(image)), "spany 2");
        add(new JLabel(skill.getSkillInfo().getName() + " level " + skill.getLevel()),
                "growx, pushx, gaptop 7");
        add(new JLabel(String.format("%,d/%,d", skill.getSP(),
                skill.getSkillInfo().getSkillpointsForLevel(5)), JLabel.RIGHT),
                "growx, pushx, al right, wrap");
        add(new JLabel(skill.getSkillInfo().getGroup().getName()), "growx, pushx");
        add(new JLabel(skill.canTrainFurther() ? 
            Formatter.formatDuration(skill.getTimeToNextLevel()) : "Complete", JLabel.RIGHT),
                "growx, pushx, al right, gapbottom 8");
    }

}
