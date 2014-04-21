package com.agcy.wikiread.Core.Parsing.Markup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kiolt_000 on 19-Apr-14.
 */
public class Parser {

    public static ArrayList<String> getBlocks(String content){

        ArrayList<String> blocks = new ArrayList<String>();

        blocks.add("block");

        return blocks;

    }
    public static HashMap<String,String> Panorama = new HashMap<String, String>(){
        {

        }
    };
    public static HashMap<String,String> Image = new HashMap<String, String>(){
        {
            put("en","Image");
            put("ru","Изображение");
        }
    };
    public static HashMap<String,String> File = new HashMap<String, String>(){
        {
            put("en","File");
            put("ru","Файл");
            put("uk","Файл");
            put("de","Datei");
            put("ko","파일");
        }
    };


}
