package com.agcy.wikiread.Core.Api;

import org.simpleframework.xml.Element;

/**
 * Created by kiolt_000 on 10.03.14.
 */
public class Warning {
    @Element(required = false)
    public WarningMain main;
    @Element(required = false)
    public WarningQuery query;
}
