//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.02 at 08:45:21 AM GMT 
//


package com.rjc.sportfiles.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Watts" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "watts"
})
@XmlRootElement(name = "TPX", namespace = "http://www.garmin.com/xmlschemas/ActivityExtension/v2")
public class TPX {

    @XmlElement(name = "Watts", namespace = "http://www.garmin.com/xmlschemas/ActivityExtension/v2")
    @XmlSchemaType(name = "unsignedByte")
    protected short watts;

    /**
     * Gets the value of the watts property.
     * 
     */
    public short getWatts() {
        return watts;
    }

    /**
     * Sets the value of the watts property.
     * 
     */
    public void setWatts(short value) {
        this.watts = value;
    }

}
