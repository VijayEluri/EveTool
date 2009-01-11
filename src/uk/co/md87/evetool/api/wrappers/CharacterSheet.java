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
import uk.co.md87.evetool.api.parser.NamedApiElement;
import uk.co.md87.evetool.api.wrappers.data.Attribute;
import uk.co.md87.evetool.api.wrappers.data.BasicCharInfo;
import uk.co.md87.evetool.api.wrappers.data.BasicCloneInfo;
import uk.co.md87.evetool.api.wrappers.data.BasicCorpInfo;
import uk.co.md87.evetool.api.wrappers.data.Implant;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;

/**
 *
 * TODO: Document CharacterSheet
 * @author chris
 */
public class CharacterSheet {

    private final String race;
    private final String bloodline;
    private final String gender;
    private final BasicCloneInfo clone;
    private final double balance;
    private final List<Implant> implants;
    private final Map<Attribute, Integer> baseAttributes;
    private final Map<Attribute, Double> attributes;
    private final List<TrainedSkillInfo> skills;
    private final List<Integer> certificates;

    private final BasicCharInfo charInfo;

    public CharacterSheet(final ApiElement resultElement) {
        super();

        this.race = resultElement.getChildContent("race");
        this.bloodline = resultElement.getChildContent("bloodLine");
        this.gender = resultElement.getChildContent("gender");
        this.balance = Double.parseDouble(resultElement.getChildContent("balance"));

        this.clone = new BasicCloneInfo();
        // TODO: Clone

        this.implants = new ArrayList<Implant>();
        parseImplants(resultElement.getChild("attributeEnhancers"));

        this.attributes = new HashMap<Attribute, Double>();
        this.baseAttributes = new HashMap<Attribute, Integer>();
        parseAttributes(resultElement.getChild("attributes"));

        this.skills = new ArrayList<TrainedSkillInfo>();
        parseSkills(resultElement.getRowset("skills"));

        this.certificates = new ArrayList<Integer>();
        parseCertificates(resultElement.getRowset("certificates"));

        final int id = resultElement.getNumericChildContent("characterID");
        final String name = resultElement.getChildContent("name");

        final String corpName = resultElement.getChildContent("corporationName");
        final int corpId = resultElement.getNumericChildContent("corporationID");

        charInfo = new BasicCharInfo(name, id, new BasicCorpInfo(corpName, corpId));

        calculateAttributes();
    }

    public void associateSkills(final SkillList skilltree) {
        for (TrainedSkillInfo skill : skills) {
            skill.setSkill(skilltree.getSkillById(skill.getId()));
        }
    }

    protected void parseImplants(final ApiElement root) {
        for (ApiElement child : root.getChildren()) {
            if (child instanceof NamedApiElement
                    && ((NamedApiElement) child).getName().endsWith("Bonus")) {
                final String type = ((NamedApiElement) child).getName().replace("Bonus", "");
                final String name = child.getChildContent("augmentatorName");
                final int value = child.getNumericChildContent("augmentatorValue");
                implants.add(new Implant(name, Attribute.valueOf(type.toUpperCase()), value));
            }
        }
    }

    protected void parseAttributes(final ApiElement root) {
        for (Attribute attribute : Attribute.values()) {
            baseAttributes.put(attribute,
                    root.getNumericChildContent(attribute.name().toLowerCase()));
        }
    }

    protected void parseSkills(final ApiElement rowset) {
        // TODO: Nice way to quickly parse rowsets?
        for (ApiElement row : rowset.getChildren()) {
            final int id = row.getNumericAttribute("typeID");
            final int level = row.getNumericAttribute("level");
            final int sp = row.getNumericAttribute("skillpoints");
            skills.add(new TrainedSkillInfo(id, level, sp));
        }
    }

    protected void parseCertificates(final ApiElement rowset) {
        for (ApiElement row : rowset.getChildren()) {
            final int id = row.getNumericAttribute("certificateID");
            certificates.add(id);
        }
    }

    protected void calculateAttributes() {
        for (Attribute attribute : Attribute.values()) {
            final int base = baseAttributes.get(attribute);
            final int skillBonus = 0;
            final int implantBonus = 0;
            final int total = base + skillBonus + implantBonus;
            final double learningBonus = 1.0;
            
            attributes.put(attribute, total * learningBonus);
        }
        // (Base + skill bonuses + implant bonuses) * ( 1+ (learning bonus * 0.02))
    }

    public BasicCharInfo getBasicInformation() {
        return charInfo;
    }

    public Map<Attribute, Integer> getBaseAttributes() {
        return baseAttributes;
    }

    public Map<Attribute, Double> getAttributes() {
        return attributes;
    }

    public double getBalance() {
        return balance;
    }

    public String getBloodline() {
        return bloodline;
    }

    public List<Integer> getCertificates() {
        return certificates;
    }

    public BasicCharInfo getCharInfo() {
        return charInfo;
    }

    public BasicCloneInfo getClone() {
        return clone;
    }

    public String getGender() {
        return gender;
    }

    public List<Implant> getImplants() {
        return implants;
    }

    public String getRace() {
        return race;
    }

    public List<TrainedSkillInfo> getSkills() {
        return skills;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + charInfo + " (" + balance + ")]";
    }

}
