package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Element
public class Identifier {

    @Attribute
    public int id;

    @Element(required = false)
    public String name;

    @Element(required = false)
    public String locale;
}
