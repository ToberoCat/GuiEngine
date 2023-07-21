package io.github.toberocat.guiengine.xml;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Arrays;

/**
 * Represents an XML-based GUI configuration.
 * This class is used
 * to deserialize an XML configuration file that defines the properties of a graphical user interface.
 * It uses Jackson's annotations to map XML elements and attributes to Java class properties during deserialization.
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class XmlGui {
    /**
     * The title of the GUI.
     */
    @JacksonXmlProperty(isAttribute = true)
    private String title;

    /**
     * The interpreter used by the GUI.
     * This property is optional and will default to "default" if not specified in the XML configuration.
     */
    @JacksonXmlProperty(isAttribute = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private String interpreter = "default";

    /**
     * The width of the GUI.
     * This property is optional and will default to 9 if not specified in the XML configuration.
     */
    @JacksonXmlProperty(isAttribute = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private int width = 9;

    /**
     * The height of the GUI.
     * This property is mandatory and must be specified in the XML configuration.
     */
    @JacksonXmlProperty(isAttribute = true)
    private int height = 5;

    /**
     * An array of XmlComponent objects that define the components present in the GUI.
     */
    @JacksonXmlProperty(localName = "component")
    @JacksonXmlElementWrapper(useWrapping = false)
    private XmlComponent[] components;

    /**
     * Default constructor for the XmlGui class.
     * Constructs an empty XmlGui object with default property values.
     */
    public XmlGui() {
    }

    // Getters and Setters

    /**
     * Get the title of the GUI.
     *
     * @return The title of the GUI.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the GUI.
     *
     * @param title The title of the GUI.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the interpreter used by the GUI.
     *
     * @return The interpreter used by the GUI.
     */
    public String getInterpreter() {
        return interpreter;
    }

    /**
     * Set the interpreter used by the GUI.
     *
     * @param interpreter The interpreter to set.
     */
    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    /**
     * Get the width of the GUI.
     *
     * @return The width of the GUI.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the width of the GUI.
     *
     * @param width The width of the GUI.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get the height of the GUI.
     *
     * @return The height of the GUI.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the height of the GUI.
     *
     * @param height The height of the GUI.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the array of XmlComponent objects representing the GUI components.
     *
     * @return The array of XmlComponent objects.
     */
    public XmlComponent[] getComponents() {
        return components;
    }

    /**
     * Set the array of XmlComponent objects representing the GUI components.
     *
     * @param components The array of XmlComponent objects to set.
     */
    public void setComponents(XmlComponent[] components) {
        this.components = components;
    }

    /**
     * Convert the XmlGui object to a string representation.
     *
     * @return A string representation of the XmlGui object.
     */
    @Override
    public String toString() {
        return "XmlGui{" + "title='" + title + '\'' + ", interpreter='" + interpreter + '\'' + ", width=" + width + ", height=" + height + ", components=" + Arrays.toString(components) + '}';
    }
}
