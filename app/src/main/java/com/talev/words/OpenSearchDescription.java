package com.talev.words;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Created by Dimko Talev on 30.06.2016 г.
 */
@Root
public class OpenSearchDescription {

    @Element(name = "Query")
    public Query query;

    @Element
    public int totalResults;

    @ElementList
    public List<Move> movies;
}
