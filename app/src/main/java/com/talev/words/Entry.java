package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import java.util.List;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Element(name = "entry")
public class Entry {

    @Attribute
    public int id;

    /*@ElementListUnion(Attribute)
    public List<Translation> translation;*/
}
