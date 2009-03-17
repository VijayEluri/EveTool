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

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;
import uk.co.md87.evetool.api.listable.ListableImpl;
import uk.co.md87.evetool.api.listable.Retrievable;

/**
 *
 * @author chris
 */
public class TrainedSkillInfoSurrogate extends ListableImpl {

    private static Image completeImage;
    private static Image trainingImage;
    private static Image normalImage;
    private static Image untrainedImage;

    static {
        try {
            completeImage = ImageIO.read(TrainedSkillInfoSurrogate.class
                    .getResource("/uk/co/md87/evetool/ui/res/icon50_14.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            trainingImage = ImageIO.read(TrainedSkillInfoSurrogate.class
                    .getResource("/uk/co/md87/evetool/ui/res/icon50_12.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            normalImage = ImageIO.read(TrainedSkillInfoSurrogate.class
                    .getResource("/uk/co/md87/evetool/ui/res/icon50_13.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            untrainedImage = ImageIO.read(TrainedSkillInfoSurrogate.class
                    .getResource("/uk/co/md87/evetool/ui/res/icon50_11.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            // Do nothing
        }
    }

    private final TrainedSkillInfo target;

    public TrainedSkillInfoSurrogate(final TrainedSkillInfo target) {
        this.target = target;

        if (target.isPartiallyTrained()) {
            updateImage(new ImageIcon(trainingImage));
        } else if (!target.canTrainFurther()) {
            updateImage(new ImageIcon(completeImage));
        } else if (target.getSP() < 0) {
            updateImage(new ImageIcon(untrainedImage));
        } else {
            updateImage(new ImageIcon(normalImage));
        }
    }

    @Retrievable(deferred=true)
    public TrainedSkillInfo getTarget() {
        return target;
    }


}
