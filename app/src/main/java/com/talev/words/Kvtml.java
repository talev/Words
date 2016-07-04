package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Root
public class Kvtml {

    @Attribute
    public String version;

    @ElementList
    public List<Information> informations;

    @ElementList
    public List<Identifier> indentifierses;

    @ElementList
    public List<Entry> entries;
}
