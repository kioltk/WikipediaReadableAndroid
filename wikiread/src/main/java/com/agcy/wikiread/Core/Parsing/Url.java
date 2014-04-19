package com.agcy.wikiread.Core.Parsing;

/**
 * Created by kiolt_000 on 19-Apr-14.
 */
public class Url {
    static String apiUrl = ".wikipedia.org/w/api.php?action=query&format=xml" +
            "&prop=revisions|images|langlinks" +
            "&rvprop=content" +
            "&imlimit=500" +
            "&lllimit=500" +
            "&titles=";

    public static String parseUrl(String url) {
        if(url.contains("wikipedia.org/w/api"))
            return url;
        String lang = parseLangFromUrl(url);
        String pageTitle = parseTitleFromUrl(url);
        return getApiUrl(pageTitle, lang);
    }

    public static String parseLangFromUrl(String url){
        int langStartIndex = url.indexOf("://")+3;
        int langEndIndex = url.indexOf(".wikipedia.org");
        return url.substring(langStartIndex, langEndIndex);
    }

    public static String parseTitleFromUrl(String url){
        int titleIndex = url.indexOf("/wiki/") + 5;
        return url.substring(titleIndex + 1);
    }

    public static String getApiUrl(String title, String lang) {

        String url = "http://" + lang + apiUrl + title;

        return url;
    }

    public static String getSearchUrl(String searchQuery, String lang) {
        String string = "http://" +
                lang +
                ".wikipedia.org/w/api.php?action=opensearch&limit=30&namespace=0&format=xml" +
                "&search=" + searchQuery;
        return string;
    }

    public static String getRandomUrl(String lang) {

        String apiUrl =
                "http://" +
                        lang +
                        ".wikipedia.org/w/api.php?action=query&rnnamespace=0&format=xml&list=random&rnlimit=1";

        return apiUrl;
    }
}
