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

package uk.co.md87.evetool.api.parser;

import org.jdom.input.SAXBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class ApiParserTest {

    /**
     * Test of parseResult method, of class ApiParser.
     */
    @Test
    public void testParseResult() throws Exception {
        final ApiParser parser = new ApiParser();
        final ApiResult result = parser.parseResult(new SAXBuilder().build(getClass()
                .getResourceAsStream("/uk/co/md87/evetool/api/data/sample-charsheet.xml")));

        assertTrue(result.getAttributes().containsKey("version"));
        assertEquals("1", result.getAttributes().get("version"));

        assertEquals(3, result.getChildren().size());

        assertEquals("currentTime", ((NamedApiElement) result.getChildren().get(0)).getName());
        assertEquals("result", ((NamedApiElement) result.getChildren().get(1)).getName());
        assertEquals("cachedUntil", ((NamedApiElement) result.getChildren().get(2)).getName());

        assertTrue(result.getChildren().get(0).getChildren().isEmpty());
        assertEquals(11, result.getChildren().get(1).getChildren().size());
        assertTrue(result.getChildren().get(2).getChildren().isEmpty());

        assertEquals("2007-06-18 22:49:01", result.getChildren().get(0).getContent());
    }

}