/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.ui.data;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;
import uk.co.md87.evetool.ui.listable.ListableImpl;
import uk.co.md87.evetool.ui.listable.Retrievable;

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
                    .getResource("../res/icon50_14.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            trainingImage = ImageIO.read(TrainedSkillInfoSurrogate.class
                    .getResource("../res/icon50_12.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            normalImage = ImageIO.read(TrainedSkillInfoSurrogate.class
                    .getResource("../res/icon50_13.png"))
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            untrainedImage = ImageIO.read(TrainedSkillInfoSurrogate.class
                    .getResource("../res/icon50_11.png"))
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
