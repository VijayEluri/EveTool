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

package uk.co.md87.evetool.api.wrappers.data;

import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.ui.listable.ListableImpl;
import uk.co.md87.evetool.ui.listable.Retrievable;

/**
 * Represents a skill that has been trained by a player.
 * 
 * @author chris
 */
public class TrainedSkillInfo extends ListableImpl {

    /** The information for this skill, if it has been associated. */
    private SkillInfo skill;

    /** The character sheet this skill belongs to. */
    private final CharacterSheet sheet;
    /** The ID of the skill. */
    private final int id;
    /** The level the skill is trained to. */
    private final int level;
    /** The current number of skill points. */
    private final int skillpoints;

    /**
     * Creates a new TrainedSkillInfo with the specified ID, level and number
     * of skill points.
     *
     * @param sheet The CharacterSheet that this skill belongs to
     * @param id The ID of the skill
     * @param level The level the skill is trained to
     * @param skillpoints The current number of skill points
     */
    public TrainedSkillInfo(final CharacterSheet sheet, final int id,
            final int level, final int skillpoints) {
        this.sheet = sheet;
        this.id = id;
        this.level = level;
        this.skillpoints = skillpoints;
    }

    /**
     * Retrieve the ID of this skill.
     *
     * @return This skill's ID
     */
    @Retrievable
    public int getId() {
        return id;
    }

    /**
     * Retrieves the total number of skillpoints trained for this skill.
     *
     * @return This skill's number of skillpoints
     */
    @Retrievable(name="Trained skillpoints")
    public int getSP() {
        return skillpoints;
    }

    /**
     * Determines if this skill is partially trained (i.e., not at a complete
     * level).
     *
     * @return True if the skill is partially trained, false otherwise
     */
    @Retrievable(name="Is partially trained?")
    public boolean isPartiallyTrained() {
        return getSP() == getSkillInfo().getSkillpointsForLevel(getLevel());
    }

    /**
     * Associates the specified {@link SkillInfo} object with this skill.
     *
     * @param skill The corresponding {@link SkillInfo} object
     */
    public void setSkill(final SkillInfo skill) {
        this.skill = skill;
    }

    /**
     * Determines whether or not this skill can be trained further.
     *
     * @return True if the skill can be trained fruther, false otherwise
     */
    @Retrievable(name="Can train further?")
    public boolean canTrainFurther() {
        return level < 5;
    }

    /**
     * Retrieves the level that this skill will be trained to next.
     *
     * @return The next trainable level
     */
    @Retrievable(name="Next level")
    public int getNextLevel() {
        return level + 1;
    }

    /**
     * Retrieves the current level this skill is trained at.
     *
     * @return This skill's current level
     */
    @Retrievable(name="Current level")
    public int getLevel() {
        return level;
    }

    /**
     * Calculates the number of skillpoints needed for the next level.
     *
     * @return The total number of skillpoints for the next level.
     */
    @Retrievable
    public int getSPForNextLevel() {
        return skill.getSkillpointsForLevel(getNextLevel());
    }

    /**
     * Calculates the number of extra skillpoints needed for the next level.
     *
     * @return The number of SP that need to be attained to level up.
     */
    @Retrievable
    public int getExtraSPForNextLevel() {
        return getSPForNextLevel() - getSP();
    }

    /**
     * Calculates the number of skill points which will be earned per minute
     * by training this skill.
     *
     * @return The number of skill points earned per minute
     */
    @Retrievable
    public double getSkillpointsPerMinute() {
        return skill.getSkillpointsPerMinute(sheet.getAttributes());
    }

    /**
     * Returns the time in seconds to reach the next level of this skill.
     *
     * @return The training time in seconds
     */
    @Retrievable
    public int getTimeToNextLevel() {
        return getTimeToLevel(getNextLevel());
    }

    /**
     * Returns the time in seconds to reach the specified level of this skill.
     *
     * @param level The level to reach
     * @return The reamining time in seconds to that level
     */
    @Retrievable
    public int getTimeToLevel(final int level) {
        final int remsp = skill.getSkillpointsForLevel(level) - getSP();
        final double sppm = getSkillpointsPerMinute();
        final double minutes = remsp / sppm;
        return (int) Math.ceil(minutes * 60);
    }

    /**
     * Retrieves the associated {@link SkillInfo} object. Note that this will
     * be null unless {@link #setSkill(SkillInfo)} has been called.
     *
     * @return A SkillInfo object if associated
     */
    @Retrievable(deferred=true)
    public SkillInfo getSkillInfo() {
        return skill;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + id + " @ " + skillpoints + " (lvl " + level + ")"
                + (skill == null ? "" : " -- " + skill) + "]";
    }

}
