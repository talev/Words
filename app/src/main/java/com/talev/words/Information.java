package com.talev.words;

import org.simpleframework.xml.Element;

/**
 * Created by Dimko Talev on 04.07.2016 г.
 */
@Element
public class Information {

    @Element(required = false)
    public String generator;

    @Element(required = false)
    public String title;

    @Element(required = false)
    public String date;
}
