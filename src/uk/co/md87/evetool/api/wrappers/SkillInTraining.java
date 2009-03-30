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

package uk.co.md87.evetool.api.wrappers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.api.parser.ApiElement;
import uk.co.md87.evetool.api.wrappers.data.SkillInfo;

/**
 *
 * TODO: Document SkillInTraining
 * @author chris
 */
public class SkillInTraining {

    private static final Logger LOGGER = Logger.getLogger(SkillInTraining.class.getName());

    private SkillInfo skill;
    private final boolean inTraining;
    private Date startTime, endTime;
    private int typeId;
    private int startSP, targetSP;
    private int targetLevel;

    public SkillInTraining(Date startTime, Date endTime, int typeId, int startSP,
            int targetSP, int targetLevel) {
        this.inTraining = false;
        this.startTime = startTime;
        this.endTime = endTime;
        this.typeId = typeId;
        this.startSP = startSP;
        this.targetSP = targetSP;
        this.targetLevel = targetLevel;
    }

    public SkillInTraining(final ApiElement resultElement) {
        super();

        if ("0".equals(resultElement.getChildContent("skillInTraining"))) {
            inTraining = false;
        } else {
            final DateFormat datef = new SimpleDateFormat(EveApi.DATE_FORMAT);
            inTraining = true;

            try {
                startTime = datef.parse(resultElement
                        .getChildContent("trainingStartTime"));
                endTime = datef.parse(resultElement
                        .getChildContent("trainingEndTime"));
                startSP = resultElement.getNumericChildContent("trainingStartSP");
                targetSP = resultElement.getNumericChildContent("trainingDestinationSP");
                targetLevel = resultElement.getNumericChildContent("trainingToLevel");
                typeId = resultElement.getNumericChildContent("trainingTypeID");
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing skill data", ex);
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing skill data", ex);
            }
        }
    }

    public void setSkill(final SkillInfo skill) {
        this.skill = skill;
    }

    public SkillInfo getSkill() {
        return skill;
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
