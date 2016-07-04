package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Element
public class Entrie {

    @Attribute
    private  int id;

    @ElementList(required = false)
    public List<Translation> translationsId;

}
