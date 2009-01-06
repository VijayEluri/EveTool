/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.wrappers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.api.parser.ApiElement;

/**
 *
 * TODO: Document
 * @author chris
 */
public class SkillInTraining {

    private static final Logger LOGGER = Logger.getLogger(SkillInTraining.class.getName());

    private final boolean inTraining;
    private Date startTime, endTime;
    private int typeId;
    private int startSP, targetSP;
    private int targetLevel;

    public SkillInTraining(final ApiElement resultElement) {
        super();

        if ("0".equals(resultElement.getChild("skillInTraining").getContent())) {
            inTraining = false;
        } else {
            final DateFormat df = new SimpleDateFormat(EveApi.DATE_FORMAT);
            inTraining = true;

            try {
                startTime = df.parse(resultElement
                        .getChild("trainingStartTime").getContent());
                endTime = df.parse(resultElement
                        .getChild("trainingEndTime").getContent());
                startSP = Integer.parseInt(resultElement
                        .getChild("trainingStartSP").getContent());
                targetSP = Integer.parseInt(resultElement
                        .getChild("trainingDestinationSP").getContent());
                targetLevel = Integer.parseInt(resultElement
                        .getChild("trainingToLevel").getContent());
                typeId = Integer.parseInt(resultElement
                        .getChild("trainingTypeID").getContent());
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing skill data", ex);
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing skill data", ex);
            }
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isInTraining() {
        return inTraining;
    }

    public int getStartSP() {
        return startSP;
    }

    public Date getStartTime() {
        return startTime;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public int getTargetSP() {
        return targetSP;
    }

    public int getTypeId() {
        return typeId;
    }

    @Override
    public String toString() {
        return "[" + (isInTraining() ? "in training: " + typeId + " to level "
                + targetLevel + " (" + startSP + "->" + targetSP + "; "
                + startTime + "->" + endTime + ")"
                : "not training") + "]";
    }

}
