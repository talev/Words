package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Element
public class Translation {

    @Attribute
    public int id;

    @Element
    public String text;

}
