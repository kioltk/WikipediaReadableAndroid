package com.agcy.wikiread.Core.Api;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by kiolt_000 on 19.03.14.
 */
public class SearchItem {
    @Element(required = false, name = "Text")
    public Text title;
    @Element(required = false, name ="Description")
    public Text description;
    @Element(required = false, name = "Url")
    public Text url;
    @Element(required = false, name = "Image")
    public Image image;



    public static class Image{
        @Attribute
        public String source;
        @Attribute(required = false)
        public int width;
        @Attribute(required = false)
        public int height;

    }

    public static class Text {
        @Attribute(required = false)
        public String space;
        @org.simpleframework.xml.Text(required = false)
        public String content;
    }
}
