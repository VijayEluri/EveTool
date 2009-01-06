/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.wrappers.data;

/**
 *
 * @author chris
 */
public class BasicCharInfo {

    private final String name;
    private final int id;
    private final BasicCorpInfo corp;

    public BasicCharInfo(String name, int id, BasicCorpInfo corp) {
        this.name = name;
        this.id = id;
        this.corp = corp;
    }

}
