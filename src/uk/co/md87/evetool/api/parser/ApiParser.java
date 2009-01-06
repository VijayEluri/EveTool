/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiParser {

    public ApiResult parseResult(final String data) throws IOException, JDOMException,
            ParserException {
        return parseResult(new SAXBuilder().build(new StringReader(data)));
    }

    public ApiResult parseResult(final Document doc) throws IOException, JDOMException,
            ParserException {
        final Element root = doc.getRootElement();

        if (!"eveapi".equals(root.getName())) {
            throw new ParserException("Unexpected response; root element is not <eveapi/>");
        }

        final ApiResult result = new ApiResult();
        addElements(result, root);

        return result;
    }

    @SuppressWarnings("unchecked")
    protected void addElements(final ApiElement result, final Element element) {
        for (Attribute attr : (List<Attribute>) element.getAttributes()) {
            result.addAttribute(attr.getName(), attr.getValue());
        }

        if (element.getChildren().isEmpty()) {
            result.setContent(element.getTextTrim());
        }

        for (Element child : (List<Element>) element.getChildren()) {
            final ApiElement apiElement = new NamedApiElement(child.getName());
            // TODO: Parse rowsets correctly
            result.addChild(apiElement);

            addElements(apiElement, child);
        }
    }

}
