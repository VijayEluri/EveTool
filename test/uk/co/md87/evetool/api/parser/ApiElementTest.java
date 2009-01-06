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
public class ApiElementTest {

    @Test
    public void testGetChild() throws Exception {
        final ApiParser parser = new ApiParser();
        final ApiResult result = parser.parseResult(new SAXBuilder().build(getClass()
                .getResourceAsStream("/uk/co/md87/evetool/api/data/sample-charsheet.xml")));

        assertEquals(result.getChildren().get(0), result.getChild("currentTime"));
        assertEquals(result.getChildren().get(2), result.getChild("cachedUntil"));
        assertNull(result.getChild("flubadee"));
    }

}
