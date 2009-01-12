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

/**
 * Represents a skill that has been trained by a player.
 * 
 * @author chris
 */
public class TrainedSkillInfo {

    /** The information for this skill, if it has been associated. */
    private SkillInfo skill;

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
     * @param id The ID of the skill
     * @param level The level the skill is trained to
     * @param skillpoints The current number of skill points
     */
    public TrainedSkillInfo(final int id, final int level, final int skillpoints) {
        this.id = id;
        this.level = level;
        this.skillpoints = skillpoints;
    }

    /**
     * Retrieve the ID of this skill.
     *
     * @return This skill's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the total number of skillpoints trained for this skill.
     *
     * @return This skill's number of skillpoints
     */
    public int getSP() {
        return skillpoints;
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
    public boolean canTrainFurther() {
        return level < 5;
    }

    /**
     * Retrieves the level that this skill will be trained to next.
     *
     * @return The next trainable level
     */
    public int getNextLevel() {
        return level + 1;
    }

    /**
     * Retrieves the current level this skill is trained at.
     *
     * @return This skill's current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Calculates the number of skillpoints needed for the next level.
     *
     * @return The total number of skillpoints for the next level.
     */
    public int getSPForNextLevel() {
        return skill.getSkillpointsForLevel(getNextLevel());
    }

    /**
     * Calculates the number of extra skillpoints needed for the next level.
     *
     * @return The number of SP that need to be attained to level up.
     */
    public int getExtraSPForNextLevel() {
        return getSPForNextLevel() - getSP();
    }

    /**
     * Retrieves the associated {@link SkillInfo} object. Note that this will
     * be null unless {@link #setSkill(SkillInfo)} has been called.
     *
     * @return A SkillInfo object if associated
     */
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
