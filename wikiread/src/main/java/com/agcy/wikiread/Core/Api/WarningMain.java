package com.agcy.wikiread.Core.Api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by kiolt_000 on 10.03.14.
 */
public class WarningMain {
    @Attribute(required = false)
    public String space;
    @Text(required = false)
    public String content;
}
