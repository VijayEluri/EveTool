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
 * Parses the result of an API request.
 * 
 * @author chris
 */
public class ApiParser {

    /**
     * Parses the specified data into a tree of {@link ApiResult}s.
     *
     * @param data The data to be parsed
     * @return A representation of the data as a tree of ApiResults
     * @throws IOException If the data cannot be read correctly
     * @throws JDOMException If the XML data cannot be parsed correctly
     * @throws ParserException If the API data cannot be processed correctly
     */
    public ApiResult parseResult(final String data) throws IOException, JDOMException,
            ParserException {
        return parseResult(new SAXBuilder().build(new StringReader(data)));
    }

    /**
     * Parses the specified document into a tree of {@link ApiResult}s.
     *
     * @param doc The document to be parsed
     * @return A representation of the document as a tree of ApiResults
     * @throws ParserException If the API data cannot be processed correctly
     */
    public ApiResult parseResult(final Document doc) throws ParserException {
        final Element root = doc.getRootElement();

        if (!"eveapi".equals(root.getName())) {
            throw new ParserException("Unexpected response; root element is not <eveapi/>");
        }

        final ApiResult result = new ApiResult();
        addElements(result, root);

        return result;
    }

    /**
     * Adds the specified XML {@link Element} and its children to the specified
     * {@link ApiElement}.
     *
     * @param result The ApiElement to append the results to
     * @param element The XML Element to be parsed and added
     */
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
            result.addChild(apiElement);

            addElements(apiElement, child);
        }
    }

}
