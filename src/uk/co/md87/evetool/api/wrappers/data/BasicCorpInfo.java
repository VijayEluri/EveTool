/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.wrappers.data;

/**
 *
 * TODO: Document
 * @author chris
 */
public class BasicCorpInfo {

    private final String name;
    private final int id;

    public BasicCorpInfo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "[" + name + " (" + id + ")]";
    }

}