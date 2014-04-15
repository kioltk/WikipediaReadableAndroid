package com.agcy.wikiread.Core.Api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by kiolt_000 on 19.03.14.
 */
public class QuerySearch {

    @Text(required = false)
    public String content;
    @Attribute(required = false)
    public String space;
}
