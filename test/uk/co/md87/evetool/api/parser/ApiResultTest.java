/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.parser;

import java.text.SimpleDateFormat;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class ApiResultTest {

    @Test
    public void testDates() throws Exception {
        final ApiParser parser = new ApiParser();
        final ApiResult result = parser.parseResult(new SAXBuilder().build(getClass()
                .getResourceAsStream("/uk/co/md87/evetool/api/data/sample-charsheet.xml")));

        assertEquals("2007-06-18 22:49:01", new SimpleDateFormat(ApiResult.DATE_FORMAT)
                .format(result.getCachedSince()));
        assertEquals("2007-06-18 23:49:01", new SimpleDateFormat(ApiResult.DATE_FORMAT)
                .format(result.getCachedUntil()));
    }

}
