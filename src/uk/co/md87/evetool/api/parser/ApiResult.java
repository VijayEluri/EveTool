/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.parser;

import java.util.Date;

/**
 *
 * @author chris
 */
public class ApiResult extends ApiElement {

    private Date cachedSince;
    private Date cachedUntil;

    public Date getCachedSince() {
        return cachedSince;
    }

    public Date getCachedUntil() {
        return cachedUntil;
    }

}
