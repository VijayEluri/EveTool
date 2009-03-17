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

import java.util.Map;

import uk.co.md87.evetool.api.listable.Retrievable;
import uk.co.md87.evetool.api.listable.formatters.NumberFormatter;

/**
 *
 * TODO: Document SkillInfo
 * @author chris
 */
public class SkillInfo {

    protected final SkillGroup group;
    protected final String name;
    protected final int id;
    protected final String description;
    protected final int rank;
    protected final RequirementsList requirements;
    protected final Attribute primaryAttribute;
    protected final Attribute secondaryAttribute;
    protected final Map<String, String> bonuses;

    public SkillInfo(final SkillGroup group, final String name, final int id,
            final String description, final int rank,
            final RequirementsList requirements,
            final Attribute primaryAttribute, final Attribute secondaryAttribute,
            final Map<String, String> bonuses) {
        this.group = group;
        this.name = name;
        this.id = id;
        this.description = description;
        this.rank = rank;
        this.requirements = requirements;
        this.primaryAttribute = primaryAttribute;
        this.secondaryAttribute = secondaryAttribute;
        this.bonuses = bonuses;
    }

    @Retrievable
    public int getId() {
        return id;
    }

    @Retrievable
    public String getName() {
        return name;
    }

    @Retrievable
    public int getRank() {
        return rank;
    }

    @Retrievable(formatWith=NumberFormatter.class)
    public int getMaxSkillpoints() {
        return getSkillpointsForLevel(5);
    }

    public int getSkillpointsForLevel(final int level) {
        return (int) Math.ceil(250 * rank * Math.pow(32, ((double) level - 1) / 2));
    }

    public double getSkillpointsPerMinute(final Map<Attribute, Double> attributes) {
        return attributes.get(primaryAttribute) + attributes.get(secondaryAttribute) / 2.0;
    }

    public Map<String, String> getBonuses() {
        return bonuses;
    }

    @Retrievable(deferred=true,name="Group")
    public SkillGroup getGroup() {
        return group;
    }

    @Retrievable(deferred=true,name="Requirements:")
    public RequirementsList getRequirements() {
        return requirements;
    }

    @Override
    public String toString() {
        return "[" + name + " (" + id + ") | " + bonuses + "]";
    }

}
