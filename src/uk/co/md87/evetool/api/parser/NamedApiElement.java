/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.parser;

/**
 *
 * TODO: Document
 * @author chris
 */
public class NamedApiElement extends ApiElement {

    private final String name;

    public NamedApiElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
