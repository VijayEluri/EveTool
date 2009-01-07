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
    private final Map<Attribute, Integer> attributes;
    private final List<TrainedSkillInfo> skills;
    private final List<Integer> certificates;

    private final BasicCharInfo charInfo;

    public CharacterSheet(final ApiElement resultElement) {
        super();

        this.race = resultElement.getChild("race").getContent();
        this.bloodline = resultElement.getChild("bloodLine").getContent();
        this.gender = resultElement.getChild("gender").getContent();
        this.balance = Double.parseDouble(resultElement.getChild("balance").getContent());

        this.clone = new BasicCloneInfo();
        // TODO: Clone

        this.implants = new ArrayList<Implant>();
        // TODO: Implants

        this.attributes = new HashMap<Attribute, Integer>();
        // TODO: Attributes

        this.skills = new ArrayList<TrainedSkillInfo>();
        parseSkills(resultElement.getRowset("skills"));

        this.certificates = new ArrayList<Integer>();
        parseCertificates(resultElement.getRowset("certificates"));

        final int id = Integer.parseInt(resultElement.getChild("characterID").getContent());
        final String name = resultElement.getChild("name").getContent();

        final String corpName = resultElement.getChild("corporationName").getContent();
        final int corpId = Integer.parseInt(resultElement
                .getChild("corporationID").getContent());

        charInfo = new BasicCharInfo(name, id, new BasicCorpInfo(corpName, corpId));
    }

    public void associateSkills(final SkillList skilltree) {
        for (TrainedSkillInfo skill : skills) {
            skill.setSkill(skilltree.getSkillById(skill.getId()));
        }
    }

    protected void parseSkills(final ApiElement rowset) {
        // TODO: Nice methods in ApiElement for getting [numeric] atts/children
        // TODO: Nice way to quickly parse rowsets?
        for (ApiElement row : rowset.getChildren()) {
            final int id = Integer.parseInt(row.getAttributes().get("typeID"));
            final int level = Integer.parseInt(row.getAttributes().get("level"));
            final int sp = Integer.parseInt(row.getAttributes().get("skillpoints"));
            skills.add(new TrainedSkillInfo(id, level, sp));
        }
    }

    protected void parseCertificates(final ApiElement rowset) {
        for (ApiElement row : rowset.getChildren()) {
            final int id = Integer.parseInt(row.getAttributes().get("certificateID"));
            certificates.add(id);
        }
    }

    public BasicCharInfo getBasicInformation() {
        return charInfo;
    }

    public Map<Attribute, Integer> getAttributes() {
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
