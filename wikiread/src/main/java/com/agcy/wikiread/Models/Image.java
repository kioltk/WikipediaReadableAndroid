package com.agcy.wikiread.Models;

import org.simpleframework.xml.Attribute;

/**
 * Created by kiolt_000 on 08.03.14.
 */
public class Image {

    @Attribute(required = false)
    public String ns;
    @Attribute(required = false)
    public String title;
    @Attribute(required = false)
    public String url;
    @Attribute(required = false)
    public String descriptionurl;
    @Attribute(required = false)
    public String sha1;

}
