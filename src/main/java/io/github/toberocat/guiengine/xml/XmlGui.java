package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class XmlGui {
    @JacksonXmlProperty(isAttribute = true)
    private String title;
    @JacksonXmlProperty(isAttribute = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private String interpreter = "default";
    @JacksonXmlProperty(isAttribute = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private int width = 9;
    @JacksonXmlProperty(isAttribute = true)
    private int height = 5;

    @JacksonXmlProperty(localName = "component")
    @JacksonXmlElementWrapper(useWrapping = false)
    private XmlComponent[] components;

    public XmlGui() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public XmlComponent[] getComponents() {
        return components;
    }

    public void setComponents(XmlComponent[] components) {
        this.components = components;
    }


    @Override
    public String toString() {
        return "XmlGui{" +
                "title='" + title + '\'' +
                ", interpreter='" + interpreter + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", components=" + Arrays.toString(components) +
                '}';
    }
}
