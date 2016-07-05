package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Element(name = "entry")
public class Entry {

    @Attribute
    public int id;

    @ElementList(inline = true)
    public List<Translation> translations;

}
