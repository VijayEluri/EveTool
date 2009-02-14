/*
 * Copyright (c) 2009 Chris Smith
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

package uk.co.md87.evetool.api.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.md87.evetool.api.parser.ApiElement;
import uk.co.md87.evetool.api.wrappers.data.BasicRaceInfo;
import uk.co.md87.evetool.api.wrappers.data.BasicShipInfo;
import uk.co.md87.evetool.api.wrappers.data.SkillRequirement;
import uk.co.md87.evetool.api.wrappers.data.TypeGroup;

/**
 *
 * TODO: Document ShipList
 * @author chris
 */
public class ShipList {

    private final Map<Integer, TypeGroup> groups = new HashMap<Integer, TypeGroup>();
    private final Map<Integer, BasicShipInfo> ships = new HashMap<Integer, BasicShipInfo>();

    public ShipList(final ApiElement resultElement) {
        for (ApiElement grouprow : resultElement.getRowset("groups")) {
            final int groupID = grouprow.getNumericAttribute("groupID");
            final String groupName = grouprow.getStringAttribute("groupName");

            final TypeGroup group = new TypeGroup(groupID, groupName);
            groups.put(groupID, group);

            for (ApiElement ship : grouprow.getRowset("types")) {
                final int typeID = ship.getNumericAttribute("typeID");
                final String typeName = ship.getStringAttribute("typeName");
                final int graphicID = ship.getNumericAttribute("graphicID");
                final int raceID = ship.getNumericAttribute("raceID");
                final List<SkillRequirement> reqs = new ArrayList<SkillRequirement>();
                
                final int[][] values = new int[ship.getRowset("attributes").size() / 2][2];
                for (ApiElement req : ship.getRowset("attributes")) {
                    String atName = req.getStringAttribute("attributeName").substring(13);

                    int index = 0;

                    if (atName.endsWith("Level")) {
                        index = 1;
                        
                        atName = atName.substring(0, atName.length() - 5);
                    }

                    int number = Integer.parseInt(atName) - 1;
                    values[number][index] = req.getNumericAttribute("value");
                }

                for (int[] value : values) {
                    reqs.add(new SkillRequirement(value[0], value[1]));
                }

                final BasicShipInfo info = new BasicShipInfo(typeID, typeName,
                        new BasicRaceInfo(raceID), group, graphicID, reqs);
                group.add(info);
                ships.put(typeID, info);
            }
        }
    }

    public Map<Integer, TypeGroup> getGroups() {
        return groups;
    }

    public Map<Integer, BasicShipInfo> getShips() {
        return ships;
    }

    @Override
    public String toString() {
        return groups.toString();
    }

}
