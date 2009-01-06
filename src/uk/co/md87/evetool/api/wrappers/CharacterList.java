/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.wrappers;

import java.util.ArrayList;
import java.util.Map;

import uk.co.md87.evetool.api.parser.ApiElement;
import uk.co.md87.evetool.api.wrappers.data.BasicCharInfo;
import uk.co.md87.evetool.api.wrappers.data.BasicCorpInfo;

/**
 *
 * TODO: Document
 * @author chris
 */
public class CharacterList extends ArrayList<BasicCharInfo> {

    public CharacterList(final ApiElement resultElement) {
        super();

        for (ApiElement child : resultElement.getChild("rowset").getChildren()) {
            add(getCharInfo(child));
        }
    }

    protected BasicCharInfo getCharInfo(final ApiElement row) {
        final Map<String, String> attr = row.getAttributes();
        final BasicCorpInfo corpInfo = new BasicCorpInfo(attr.get("corporationName"),
                Integer.parseInt(attr.get("corporationID")));
        final BasicCharInfo charInfo = new BasicCharInfo(attr.get("name"),
                Integer.parseInt(attr.get("characterID")), corpInfo);

        return charInfo;
    }

}
