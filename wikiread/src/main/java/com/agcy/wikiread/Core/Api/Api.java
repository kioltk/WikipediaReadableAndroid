package com.agcy.wikiread.Core.Api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by kiolt_000 on 06.03.14.
 */
@Root(name="api")
public class Api {

    @Element(required = false,name = "query-continue")
    public QueryContinue querycontinue;
    @Element(required = false)
    public Warning warnings;
    @Element(name="query")
    public Query query;
}
