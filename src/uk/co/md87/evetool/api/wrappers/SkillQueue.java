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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.api.parser.ApiElement;

/**
 *
 * TODO: Document SkillQueue
 * @author chris
 */
public class SkillQueue extends ArrayList<SkillInTraining> {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private static final Logger LOGGER = Logger.getLogger(SkillQueue.class.getName());

    public SkillQueue(final ApiElement resultElement) {
        super();

        final DateFormat datef = new SimpleDateFormat(EveApi.DATE_FORMAT);

        for (ApiElement row : resultElement.getRowset("skillqueue")) {
            try {
                final int id = row.getNumericAttribute("typeID");
                final int level = row.getNumericAttribute("level");
                final int startSP = row.getNumericAttribute("startSP");
                final int endSP = row.getNumericAttribute("endSP");
                final Date startTime = datef.parse(row.getStringAttribute("startTime"));
                final Date endTime = datef.parse(row.getStringAttribute("endTime"));
                final int position = row.getNumericAttribute("queuePosition");

                add(new SkillInTraining(startTime, endTime, id, startSP, endSP, level));
            } catch (ParseException ex) {
                LOGGER.log(Level.WARNING, "Unable to parse date", ex);
            }
        }
    }

}
