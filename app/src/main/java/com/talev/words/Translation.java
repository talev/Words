package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Element(name = "translation")
public class Translation {

    @Attribute
    public int id;

    @Element(required = false)
    public String text;

}
