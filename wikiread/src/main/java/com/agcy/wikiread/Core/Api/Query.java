package com.agcy.wikiread.Core.Api;

import com.agcy.wikiread.Models.Page;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by kiolt_000 on 06.03.14.
 */

public class Query {
    @Element(required = false)
    public Normalized normalized;

    @ElementList
    public List<Page> pages;

}
