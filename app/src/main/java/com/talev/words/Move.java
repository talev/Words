package com.talev.words;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

/**
 * Created by Dimko Talev on 30.06.2016 г.
 */
@Element(name = "movie")
public class Move {

    @Element(required = false)
    public String score;

    @Element(required = false)
    public String popularity;

    @Element(required = false)
    public String name;

    @Element(required = false)
    public String id;

    @Element(required = false)
    public String biography;

    @Element(required = false)
    public String url;

    @Element(required = false)
    public String version;

    @Element(required = false)
    public String lastModifiedAt;

    @ElementList
    public List<Image> images;
}
