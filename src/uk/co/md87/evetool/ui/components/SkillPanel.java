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

/**
 *
 * TODO: Document SkillPanel
 * @author chris
 */
public class SkillPanel extends JPanel {

    private static Image completeImage;
    private static Image trainingImage;
    private static Image normalImage;
    private static Image untrainedImage;

    static {
        try {
            completeImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_14.png"));
            trainingImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_12.png"));
            normalImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_13.png"));
            untrainedImage = ImageIO.read(SkillPanel.class.getResource("../res/icon50_11.png"));
        } catch (IOException ex) {
            // Do nothing
        }
    }

    public SkillPanel(final TrainedSkillInfo skill) {
        super(new MigLayout());
        
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

        add(new JLabel(new ImageIcon(image)));
        add(new JLabel(skill.getSkillInfo().getName()));
    }

}
