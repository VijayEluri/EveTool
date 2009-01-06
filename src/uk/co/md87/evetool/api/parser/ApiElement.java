/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiElement {

    private String content = null;
    private List<ApiElement> children = new ArrayList<ApiElement>();
    private Map<String, String> attributes = new HashMap<String, String>();

    public void addChild(final ApiElement e) {
        children.add(e);
    }

    public void addAttribute(final String key, final String value) {
        attributes.put(key, value);
    }

    public List<ApiElement> getChildren() {
        return children;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NamedApiElement getChild(final String name) {
        for (ApiElement child : getChildren()) {
            if (child instanceof NamedApiElement
                    && name.equals(((NamedApiElement) child).getName())) {
                return (NamedApiElement) child;
            }
        }

        return null;
    }

}
