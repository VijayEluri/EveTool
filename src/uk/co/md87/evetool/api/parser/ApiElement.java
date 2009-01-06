/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class ApiElement {

    private List<ApiElement> children = new ArrayList<ApiElement>();

    public void addChild(final ApiElement e) {
        children.add(e);
    }

    public List<ApiElement> getChildren() {
        return children;
    }

}
