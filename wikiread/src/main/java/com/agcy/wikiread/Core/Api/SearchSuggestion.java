package com.agcy.wikiread.Core.Api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by kiolt_000 on 19.03.14.
 */
public class SearchSuggestion {

    @Attribute(required = false)
    public String version;
    @Element(required = false, name = "Query")
    public QuerySearch query;
    @ElementList(required = false, name = "Section")
    public List<SearchItem> searchItemList;
}
