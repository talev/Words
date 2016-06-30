package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Dimko Talev on 30.06.2016 г..
 */
@Element(name = "image")
public class Image {

    @Attribute
    public String type;

    @Attribute
    public String url;

    @Attribute
    public String size;

    @Attribute
    public int width;

    @Attribute
    public int height;

    @Attribute
    public String id;
}
