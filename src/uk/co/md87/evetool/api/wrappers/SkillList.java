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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.md87.evetool.api.parser.ApiElement;
import uk.co.md87.evetool.api.wrappers.data.Attribute;
import uk.co.md87.evetool.api.wrappers.data.RequirementsList;
import uk.co.md87.evetool.api.wrappers.data.SkillGroup;
import uk.co.md87.evetool.api.wrappers.data.SkillInfo;
import uk.co.md87.evetool.api.wrappers.data.SkillRequirement;

/**
 *
 * TODO: Document SkillList
 * @author chris
 */
public class SkillList extends ArrayList<SkillGroup> {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    protected final Map<Integer, SkillInfo> skills = new HashMap<Integer, SkillInfo>();

    public SkillList(final ApiElement resultElement) {
        for (ApiElement row : resultElement.getRowset("skillGroups")) {
            add(getSkillGroup(row));
        }
    }

    public SkillInfo getSkillById(final int id) {
        return skills.get(id);
    }

    protected SkillGroup getSkillGroup(final ApiElement row) {
        final String name = row.getStringAttribute("groupName");
        final int id = row.getNumericAttribute("groupID");

        final SkillGroup group = new SkillGroup(name, id);

        for (ApiElement skillrow : row.getRowset("skills")) {
            final SkillInfo skill = getSkill(group, skillrow);
            group.add(skill);
            skills.put(skill.getId(), skill);
        }

        return group;
    }

    protected SkillInfo getSkill(final SkillGroup group, final ApiElement row) {
        final String skillName = row.getStringAttribute("typeName");
        final int typeId = row.getNumericAttribute("typeID");
        final String desc = row.getChildContent("description");
        final int rank = row.getNumericChildContent("rank");
        final RequirementsList reqs = getReqs(row.getRowset("requiredSkills"));
        final Attribute primaryAttribute = Attribute.valueOf(row.getChild("requiredAttributes")
                .getChildContent("primaryAttribute").toUpperCase());
        final Attribute secondaryAttribute = Attribute.valueOf(row.getChild("requiredAttributes")
                .getChildContent("secondaryAttribute").toUpperCase());
        final Map<String, String> bonuses = getBonuses(row.getRowset("skillBonusCollection"));

        return new SkillInfo(group, skillName, typeId, desc, rank, reqs, primaryAttribute,
                secondaryAttribute, bonuses);
    }

    protected RequirementsList getReqs(final List<ApiElement> rowset) {
        final RequirementsList reqs = new RequirementsList();

        for (ApiElement row : rowset) {
            reqs.add(new SkillRequirement(row.getNumericAttribute("typeID"),
                    row.getNumericAttribute("skillLevel")));
        }

        return reqs;
    }

    protected Map<String, String> getBonuses(final List<ApiElement> rowset) {
        final Map<String, String> bonuses = new HashMap<String, String>();

        for (ApiElement child : rowset) {
            bonuses.put(child.getStringAttribute("bonusType"),
                    child.getStringAttribute("bonusValue"));
        }

        return bonuses;
    }

}
