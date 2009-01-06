/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.md87.evetool.api.EveApi;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiResult extends ApiElement {

    private static final Logger LOGGER = Logger.getLogger(ApiResult.class.getName());

    protected static final long ERROR_OFFSET = 600000;

    public boolean wasSuccessful() {
        return getChild("result") != null && getChild("error") == null;
    }

    public ApiElement getResultElement() {
        return getChild("result");
    }

    public String getError() {
        return getChild("error").getContent();
    }

    public Date getCachedSince() {
        final NamedApiElement ctElement = getChild("currentTime");

        if (ctElement == null) {
            return new Date(System.currentTimeMillis());
        } else {
            try {
                return new SimpleDateFormat(EveApi.DATE_FORMAT).parse(ctElement.getContent());
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing cached from date", ex);
                return new Date(System.currentTimeMillis());
            }
        }
    }

    public Date getCachedUntil() {
        final NamedApiElement cuElement = getChild("cachedUntil");

        if (cuElement == null) {
            return new Date(System.currentTimeMillis() + ERROR_OFFSET);
        } else {
            try {
                return new SimpleDateFormat(EveApi.DATE_FORMAT).parse(cuElement.getContent());
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing cached until date", ex);
                return new Date(System.currentTimeMillis() + ERROR_OFFSET);
            }
        }
    }

}
