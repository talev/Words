package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Root
public class Kvtml {

    @Attribute
    public String version;

    @Element
    public Information information;

    @ElementList
    public List<Identifier> identifiers;

    @ElementList
    public List<Entry> entries;

    @ElementList
    public List<Lessons> lessons;

}
