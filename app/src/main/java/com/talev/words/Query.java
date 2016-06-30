package com.talev.words;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by Dimko Talev on 30.06.2016 г.
 */
@Element
public class Query {

    @Attribute
    public String searchTerms;
}
