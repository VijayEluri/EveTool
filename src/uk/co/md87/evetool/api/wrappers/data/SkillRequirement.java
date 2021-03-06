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
 * Represents a skill that is required to be trained to a certain level for
 * some purpose.
 *
 * @author chris
 */
public class SkillRequirement {

    /** The ID of the required skill. */
    private final int skillId;

    /** The required level of the skill. */
    private final int requiredLevel;

    /**
     * Creates a new skill requirement with the specified details.
     *
     * @param skillId The ID of the required skill
     * @param requiredLevel The required level of that skill
     */
    public SkillRequirement(final int skillId, final int requiredLevel) {
        this.skillId = skillId;
        this.requiredLevel = requiredLevel;
    }

    /**
     * Retrieves the required level of the skill.
     *
     * @return The skill's required level
     */
    public int getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * Retrieves the ID of the required skill.
     *
     * @return The skill's ID
     */
    public int getSkillId() {
        return skillId;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + skillId + "@" + requiredLevel + "]";
    }

}
