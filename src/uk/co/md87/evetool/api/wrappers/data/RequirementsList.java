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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.md87.evetool.api.wrappers.SkillList;
import uk.co.md87.evetool.ui.listable.Retrievable;

/**
 *
 * TODO: Document RequirementsList
 * @author chris
 */
public class RequirementsList extends ArrayList<SkillRequirement> {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    public void expandRequirements(final SkillList skillList) {
        final List<SkillRequirement> unprocessed = new ArrayList<SkillRequirement>();
        final Map<Integer, Integer> needed = new HashMap<Integer, Integer>();
        unprocessed.addAll(this);

        while (!unprocessed.isEmpty()) {
            final SkillRequirement target = unprocessed.remove(0);

            unprocessed.addAll(skillList.getSkillById(target.getSkillId()).getRequirements());

            final int level =  Math.max(needed.containsKey(target.getSkillId())
                    ? needed.get(target.getSkillId()) : 0, target.getRequiredLevel());
            needed.put(target.getSkillId(), level);
        }

        clear();

        for (Map.Entry<Integer, Integer> entry : needed.entrySet()) {
            add(new SkillRequirement(entry.getKey(), entry.getValue()));
        }
    }

    @Retrievable(name="total number")
    public int getNumSkills() {
        return size();
    }

}
