/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

        assertEquals(3, result.getChildren().size());

        assertEquals("currentTime", ((NamedApiElement) result.getChildren().get(0)).getName());
        assertEquals("result", ((NamedApiElement) result.getChildren().get(1)).getName());
        assertEquals("cachedUntil", ((NamedApiElement) result.getChildren().get(2)).getName());

        assertTrue(result.getChildren().get(0).getChildren().isEmpty());
        assertEquals(11, result.getChildren().get(1).getChildren().size());
        assertTrue(result.getChildren().get(2).getChildren().isEmpty());
    }

}