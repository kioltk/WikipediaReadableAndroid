package com.agcy.wikiread.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

/**
 * Created by kiolt_000 on 27.02.14.
 */
public class Revision {
    @Text
    public String content;
    @Attribute
    public String contentformat;
    @Attribute
    public String space;
    @Attribute
    public String contentmodel;
}
