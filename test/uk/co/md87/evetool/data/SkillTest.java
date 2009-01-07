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

package uk.co.md87.evetool.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class SkillTest {

    @Test
    public void testGetSkillpointsForLevel() {
        final int[][] data = new int[][]{
            {1, 3, 8000},
            {1, 4, 45255},
            {1, 5, 256000},
            {3, 1, 750},
            {3, 5, 768000},
            {8, 5, 2048000},
            {10, 3, 80000},
        };

        for (int[] datum : data) {
            final Skill skill = new Skill(null, null, 0, null, datum[0],
                    null, null, null, null);
            assertEquals(datum[2], skill.getSkillpointsForLevel(datum[1]));
        }
    }

}