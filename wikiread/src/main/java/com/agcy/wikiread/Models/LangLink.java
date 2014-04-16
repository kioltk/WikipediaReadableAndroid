package com.agcy.wikiread.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by kiolt_000 on 06.03.14.
 */
public class LangLink {
    @Attribute
    public String lang;
    @Attribute
    public String space;
    @Text
    public String title;



}
