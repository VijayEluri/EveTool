/*
 * Copyright (c) 2006-2009 Chris Smith, Shane Mc Cormack, Gregory Holmes
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

package uk.co.md87.evetool.api.io;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Allows easy downloading of files from HTTP sites.
 *
 * @author Chris
 */
public final class Downloader {
    
    /** Creates a new instance of Downloader. */
    private Downloader() {
        // Shouldn't be used
    }

    /**
     * Retrieves the specified page, sending the specified post data.
     *
     * @param url The URL to retrieve
     * @param postData The raw POST data to send
     * @return The response from the server
     * @throws java.io.IOException If there's an I/O error while downloading
     */    
    public static String getPage(final String url, final String postData)
            throws IOException {

        final StringBuilder res = new StringBuilder();
        
        final URLConnection urlConn = getConnection(url, postData);
        
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConn.getInputStream()));
        
        String line;
        
        do {
            line = in.readLine();
            
            if (line != null) {
                res.append(line);
            }
        } while (line != null);
        
        in.close();
        
        return res.toString();
    }
    
    /**
     * Retrieves the specified page, sending the specified post data.
     *
     * @param url The URL to retrieve
     * @param postData A map of post data that should be sent
     * @return The response from the server
     * @throws java.io.IOException If there's an I/O error while downloading
     */    
    public static String getPage(final String url, final Map<String, String> postData)
            throws IOException {        
        return getPage(url, encodeArguments(postData));
    }

    /**
     * Encodes the specified arguments for use as a POST payload or GET query
     * string.
     *
     * @param postData The arguments to be encoded
     * @return An URL-encoded version of the arguments
     */
    public static String encodeArguments(final Map<String, String> postData) {
        final StringBuilder data = new StringBuilder();

        try {
            for (Map.Entry<String, String> entry : postData.entrySet()) {
                data.append('&');
                data.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                data.append('=');
                data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException ex) {
            // Do nothing
        }

        return data.length() == 0 ? "" : data.substring(1);
    }

    /**
     * Downloads the specified page to disk.
     *
     * @param url The URL to retrieve
     * @param file The file to save the page to
     * @throws java.io.IOException If there's an I/O error while downloading
     */
    public static void downloadPage(final String url, final File file) throws IOException {

        final URLConnection urlConn = getConnection(url, "");

        final FileOutputStream output = new FileOutputStream(file);
        final InputStream input = urlConn.getInputStream();

        final byte[] buffer = new byte[512];
        int count;

        do {
            count = input.read(buffer);

            if (count > 0) {
                output.write(buffer, 0, count);
            }
        } while (count > 0);

        input.close();
        output.close();
    }
        
    /**
     * Creates an URL connection for the specified URL and data.
     * 
     * @param url The URL to connect to
     * @param postData The POST data to pass to the URL
     * @return An URLConnection for the specified URL/data
     * @throws java.io.IOException If an I/O exception occurs while connecting
     */
    private static URLConnection getConnection(final String url, final String postData)
            throws IOException {
        final URL myUrl = new URL(url);
        final URLConnection urlConn = myUrl.openConnection();
        
        urlConn.setUseCaches(false);
        urlConn.setDoInput(true);
        urlConn.setDoOutput(postData.length() > 0);
        urlConn.setConnectTimeout(10000);
        
        if (postData.length() > 0) {
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            final DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
            out.writeBytes(postData);
            out.flush();
            out.close();
        }
        
        return urlConn;
    }
    
}