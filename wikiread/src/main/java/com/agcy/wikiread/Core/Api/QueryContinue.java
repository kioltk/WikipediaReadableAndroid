package com.agcy.wikiread.Core.Api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

/**
 * Created by kiolt_000 on 08.03.14.
 */
public class QueryContinue {
    @Attribute(required = false)
    public String iistart;
    @ElementList(required = false)
    public QueryContinue imageinfo;
}
