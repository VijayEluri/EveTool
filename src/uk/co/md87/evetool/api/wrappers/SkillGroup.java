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

import uk.co.md87.evetool.api.wrappers.data.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import uk.co.md87.evetool.api.parser.ApiElement;

/**
 *
 * TODO: Document
 * @author chris
 */
public class SkillGroup extends ArrayList<Skill> {

    private final int id;
    private final String name;

    public SkillGroup(final ApiElement rowElement) {
        this.name = rowElement.getAttributes().get("groupName");
        this.id = Integer.parseInt(rowElement.getAttributes().get("groupID"));

        for (ApiElement row : rowElement.getChild("rowset").getChildren()) {
            addSkill(row);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected void addSkill(final ApiElement row) {
        final String skillName = row.getAttributes().get("typeName");
        final int typeId = Integer.parseInt(row.getAttributes().get("typeID"));
        final String desc = row.getChild("description").getContent();
        final int rank = Integer.parseInt(row.getChild("rank").getContent());
        final List<SkillRequirement> reqs = getReqs(row.getRowset("requiredSkills"));
        final String primaryAttribute = row.getChild("requiredAttributes")
                .getChild("primaryAttribute").getContent();
        final String secondaryAttribute = row.getChild("requiredAttributes")
                .getChild("secondaryAttribute").getContent();
        final Map<String, String> bonuses = getBonuses(row.getRowset("skillBonusCollection"));

        add(new Skill(this, skillName, typeId, desc, rank, reqs, primaryAttribute,
                secondaryAttribute, bonuses));
    }

    protected List<SkillRequirement> getReqs(final ApiElement rowset) {
        final List<SkillRequirement> reqs = new ArrayList<SkillRequirement>();

        for (ApiElement row : rowset.getChildren()) {
            reqs.add(new SkillRequirement(
                    Integer.parseInt(row.getAttributes().get("typeID")),
                    Integer.parseInt(row.getAttributes().get("skillLevel"))));
        }

        return reqs;
    }

    protected Map<String, String> getBonuses(final ApiElement rowset) {
        return null; // TODO: Implement
    }

}
