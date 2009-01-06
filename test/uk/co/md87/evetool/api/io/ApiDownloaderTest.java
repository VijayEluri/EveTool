/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.io;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class ApiDownloaderTest {

    /**
     * Test of getUrl method, of class ApiDownloader.
     */
    @Test
    public void testGetUrl() {
        assertEquals("http://api.eve-online.com/eve/CertificateTree.xml.aspx",
                ApiDownloader.getUrl("/eve/CertificateTree.xml.aspx"));
    }

}