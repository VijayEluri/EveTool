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

import java.util.List;

import uk.co.md87.evetool.ui.listable.Retrievable;

/**
 *
 * TODO: Document CertInfo
 * @author chris
 */
public class CertInfo {

    private final int id;
    private final int grade;
    private final int corp;
    private final String desc;
    private final RequirementsList skillReqs;
    private final List<Integer> certReqs;

    public CertInfo(final int id, final int grade, final int corp,
            final String desc, final RequirementsList skillReqs,
            final List<Integer> certReqs) {
        this.id = id;
        this.grade = grade;
        this.corp = corp;
        this.desc = desc;
        this.skillReqs = skillReqs;
        this.certReqs = certReqs;
    }

    public List<Integer> getCertReqs() {
        return certReqs;
    }

    @Retrievable(name="Issuing corporation ID")
    public int getCorp() {
        return corp;
    }

    @Retrievable(name="Description")
    public String getDesc() {
        return desc;
    }

    @Retrievable
    public int getGrade() {
        return grade;
    }

    @Retrievable
    public int getId() {
        return id;
    }

    public RequirementsList getSkillReqs() {
        return skillReqs;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + id + ": grade " + grade + "]";
    }

}
