package com.agcy.wikiread.Core;

/**
 * Created by kiolt_000 on 19.03.14.
 */
public class NewParser {
    public static String getCorrectedWikiTag(String content){

        String correctedTag = "{" + getDivedCorrectedWikiTag(content.substring(1));
        return correctedTag;

    }
    private static String getDivedCorrectedWikiTag(String content) {
        int i = -1;
        Character character;
        do{
            i++;
            character = content.charAt(i);
            if(character=='{') {
                String correctedTag = getDivedCorrectedWikiTag(content.substring(i + 1));
                i += correctedTag.length();
            }
        }while(character!='}');
        content = content.substring(0,i+1);
        return content;
    }
}
