package com.agcy.wikiread.Models;


import com.agcy.wikiread.Core.ImageUrlFetcher;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by kiolt_000 on 27.02.14.
 */
public class Page {

    @Element(required = false)
    public ImageUrlFetcher imageUrlFetcher;



    @Attribute(required = false)
    public int pageid;
    @Attribute
    public int ns;
    @Attribute
    public String title;
    @Attribute(required = false)
    public String missing;
    // public Object touched;
    @ElementList(required = false)
    public List<Revision> revisions;
    @ElementList(required = false)
    public List<LangLink> langlinks;
    @ElementList(required = false)
    public List<Image> images;

    @Attribute(required = false)
    public String imagerepository;
    @ElementList(required = false)
    public List<Image> imageinfo;


}
