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
import uk.co.md87.evetool.api.wrappers.data.CertCategory;
import uk.co.md87.evetool.api.wrappers.data.CertClass;
import uk.co.md87.evetool.api.wrappers.data.CertInfo;
import uk.co.md87.evetool.api.wrappers.data.SkillRequirement;

/**
 *
 * TODO: Document CertificateTree
 * @author chris
 */
public class CertificateTree extends ArrayList<CertCategory> {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;
    
    protected final Map<Integer, CertInfo> certs = new HashMap<Integer, CertInfo>();

    public CertificateTree(final ApiElement resultElement) {
        for (ApiElement row : resultElement.getRowset("categories").getChildren()) {
            add(getCategory(row));
        }
    }

    protected CertCategory getCategory(final ApiElement row) {
        final int id = row.getNumericAttribute("categoryID");
        final String name = row.getStringAttribute("categoryName");
        final List<CertClass> classes = new ArrayList<CertClass>();

        for (ApiElement crow : row.getRowset("classes").getChildren()) {
            classes.add(getClass(crow));
        }

        return new CertCategory(id, name, classes);
    }

    protected CertClass getClass(final ApiElement row) {
        final int id = row.getNumericAttribute("classID");
        final String name = row.getStringAttribute("className");
        final List<CertInfo> certificates = new ArrayList<CertInfo>();

        for (ApiElement crow : row.getRowset("certificates").getChildren()) {
            certificates.add(getCert(crow));
        }

        return new CertClass(id, name, certificates);
    }

    protected CertInfo getCert(final ApiElement row) {
        final int id = row.getNumericAttribute("certificateID");
        final int grade = row.getNumericAttribute("grade");
        final int corp = row.getNumericAttribute("corporationID");
        final String desc = row.getStringAttribute("description");
        final List<SkillRequirement> skillReqs = new ArrayList<SkillRequirement>();
        final List<Integer> certReqs = new ArrayList<Integer>();

        for (ApiElement srow : row.getRowset("requiredSkills").getChildren()) {
            skillReqs.add(getSkillReq(srow));
        }

        for (ApiElement crow : row.getRowset("requiredCertificates").getChildren()) {
            certReqs.add(getCertReq(crow));
        }

        final CertInfo cert = new CertInfo(id, grade, corp, desc, skillReqs, certReqs);
        certs.put(id, cert);

        return cert;
    }

    protected SkillRequirement getSkillReq(final ApiElement row) {
        final int id = row.getNumericAttribute("typeID");
        final int level = row.getNumericAttribute("level");

        return new SkillRequirement(id, level);
    }

    protected Integer getCertReq(final ApiElement row) {
        return row.getNumericAttribute("certificateID");
    }

}
