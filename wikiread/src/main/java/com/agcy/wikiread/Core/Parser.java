package com.agcy.wikiread.Core;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.view.View;

import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Core.Parsing.Nodes.BlockquotedNode;
import com.agcy.wikiread.Core.Parsing.Nodes.GroupNode;
import com.agcy.wikiread.Core.Parsing.Nodes.ItemsListNode;
import com.agcy.wikiread.Core.Parsing.Nodes.Node;
import com.agcy.wikiread.Core.Parsing.Nodes.ParagraphNode;
import com.agcy.wikiread.Core.Parsing.Nodes.PictureNode;
import com.agcy.wikiread.Core.Parsing.Nodes.PreformattedNode;
import com.agcy.wikiread.Core.Parsing.Nodes.SimpleWikiNode;
import com.agcy.wikiread.Core.Parsing.Nodes.TitleNode;
import com.agcy.wikiread.Models.Page;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 27.02.14.
 */
public class Parser {
    // EXAMPLE: http://ru.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=test
    // formating http://www.mediawiki.org/wiki/Help:Formatting

    public static String lang;
    Context context;
    ArrayList<Node> parsedNodes;
    Node currentNode;
    String nodeContent;
    public Parser(Context context){
        parsedNodes = new ArrayList<Node>();
        currentNode = null;
        nodeContent = "";
        this.context = context;
    }
    public ArrayList<View> parseWikitext(String wikiContent){
        ArrayList<View> views = new ArrayList<View>();


        for(Node node:getNodes(wikiContent)){
            View view = node.getView(context);
            views.add(view);
        }

        return views;
    }
    // the power is here
    public ArrayList<Node> getNodes(String content){
        content ="\n" + content;
        while (!content.equals("")){
            // till last comes ( without last! )
            //TODO: #REDIRECT [[ LINK ]] - LINK ERROR - MAYBE THIS?
            Character currentCharacter = content.charAt(0);
            Character nextChar;
            switch (currentCharacter){


                case '\n':
                    try{
                        currentCharacter = content.charAt(1);
                        switch (currentCharacter){

                            // we accept only two \n's in the text. Every single paragraph is a new TextView
                            case '\n':
                                saveCurrentNodeAsParagraph();
                                break;
                            case '-':
                                saveCurrentNodeAsParagraph();
                                currentNode = getDivider();
                                content = content.substring(5);

                                saveNode();
                                break;
                            // we cut all titles from content except \n.
                            case '=':
                                int endOfTitle = content.indexOf("=\n")+1;
                                nodeContent = content.substring(1,endOfTitle);

                                currentNode = getTitle(nodeContent);

                                content = "\n" + content.substring(endOfTitle);

                                saveNode();
                                break;
                            // list catched!
                            case '*':
                            case ';':
                            case ':':
                            case '#':
                                int listend = content.indexOf("\n\n");
                                int listStart=0;
                                if(nodeContent.equals(""))
                                    listStart = 1;

                                nodeContent += content.substring(listStart,listend);

                                currentNode = getItemsList(nodeContent);

                                content = content.substring(listend);

                                saveNode();
                                break;
                            // <nowiki/> full paragraph are preformated
                            // or <pre>
                            case '<':
                                int tagStart = content.indexOf('>')+1;
                                String tag = content.substring(1, tagStart);
                                if(tag.equals("<pre>")){
                                    saveCurrentNodeAsParagraph();
                                    int tagEnd = content.indexOf("\n</pre>\n");
                                    nodeContent = content.substring(tagStart + 1, tagEnd );
                                    currentNode = getPreformatted(nodeContent);
                                    content =  content.substring(tagEnd + 1 + tag.length());

                                    saveNode();
                                }else{
                                    if(tag.equals("<blockquote>")){
                                        saveCurrentNodeAsParagraph();
                                        int tagEnd = content.indexOf("</blockquote>");
                                        nodeContent = content.substring(tagStart , tagEnd);
                                        currentNode = getBlockquoted(nodeContent);
                                        content =  content.substring(tagEnd + tag.length());

                                        saveNode();
                                    }
                                    else{

                                    }
                                }
                                break;
                            case '{':
                                nextChar = content.charAt(2);
                                if(nextChar=='{'){

                                    saveCurrentNodeAsParagraph();
                                    int nodeEnd = content.indexOf("}}\n")+2;
                                    nodeContent = content.substring(1, nodeEnd);
                                    if(nodeContent.contains("\n") || nodeContent.contains("#switch")){
                                        nodeEnd = content.indexOf("\n}}\n")+3;
                                        nodeContent = content.substring(1,nodeEnd);
                                    }
                                    else{
                                        //nodeContent = content.substring(1,nodeEnd);
                                    }
                                    currentNode = getWikiNode(nodeContent);
                                    // todo: catched {{some wiki info}}
                                    content = "\n" + content.substring(nodeEnd);

                                    saveNode();
                                    break;
                                }
                                if(nextChar=='|'){
                                    //todo: catched {| |}
                                    saveCurrentNodeAsParagraph();
                                    int nodeEnd = content.indexOf("|}\n")+2;
                                    nodeContent = content.substring(0, nodeEnd);
                                    currentNode = getWikiNode(nodeContent);
                                    content = "\n" + content.substring(nodeEnd);

                                    saveNode();
                                    break;
                                }
                                break;
                            // it is just like preformated
                            case ' ':
                                //mabybe space+<nowiki> so it is preformated fully. like <code>
                                break;
                            case '|':
                                //DON'T SAVE NODE!
                                //todo: { | | }
                                break;
                            case '[':
                                nextChar = content.charAt(2);
                                if(nextChar=='[' && checkPicture(content.substring(3,20))){
                                    int imageEnd = content.indexOf("]]\n");
                                    nodeContent = content.substring(3,imageEnd).replace("Image","File");
                                    currentNode = getPicture(nodeContent);
                                    content ="\n"+ content.substring(imageEnd+2);
                                }
                                saveNode();
                                break;
                            // is it error \n?

                            default:
                                if (nodeContent!=""){
                                    currentNode = getParagraph(nodeContent);
                                    content = "\n"+content;
                                }
                                saveNode();
                                break;
                        }
                    }catch (Exception exp){
                        currentNode = new Node("Errored node");
                        saveNode();
                    }
                    break;
                /*case '<' :{

                    node = getHtmlNode(content);
                } break;
                // Continue till the wikinode
                case '\'':{}
                //case '#':{}
                //case '*':{}
                //case ';':{}
                //case ':':{}
                //case ' ':{}
                case '=':{ }
                case '[':{
                    //node = getWikiNode(content);
                } break;
                */

                case '<':
                    int tagStart = content.indexOf('>')+1;
                    String tag = content.substring(0, tagStart);
                    if(tag.equals("<ref>")){
                        int tagEnd = content.indexOf("</ref>");
                        String tagContent = content.substring(tagStart , tagEnd);
                        //todo: what should i do with the fucking refs? i should do tagHandler!

                        //currentNode = getBlockquoted(nodeContent);
                        content = " *ИСТОЧНИК*"+ content.substring(tagEnd + tag.length()+1);
                        break;
                    }
                default:{
                    nodeContent += Character.toString(currentCharacter);
                }
            }
            if(content.length()!=0)
                content = content.substring(1);

        }
        saveCurrentNodeAsParagraph();
        ArrayList<Node> response = new ArrayList<Node>();
        GroupNode groupNode = null;
        for(Node node:parsedNodes){
            if(node.getClass() == TitleNode.class && ((TitleNode)node).isExpandable()){
                if(groupNode!=null)
                    response.add(groupNode);
                groupNode = new GroupNode((TitleNode) node);
            }else{
                if(groupNode==null)
                    response.add(node);
                else
                    groupNode.add(node);
            }
        }
        if(groupNode!=null)
            response.add(groupNode);
        return response;
    }
    private void saveNode(){

        if(currentNode!=null){
            parsedNodes.add(currentNode);
            currentNode = null;
        }
        nodeContent = "";

    }
    private void saveCurrentNodeAsParagraph() {
        if(nodeContent!=null && !nodeContent.equals("") && !nodeContent.equals(" ") && !nodeContent.equals("\n")){
            currentNode = getParagraph(nodeContent);
            saveNode();
        }else{

        }
    }

    static Boolean checkPicture(String content){


        if(content.contains(":")){

            int endIndex= content.indexOf(":");
            String fileCheckingTemp = content.substring(0,endIndex);
            return fileCheckingTemp.equals("File") || fileCheckingTemp.equals("Image"); //todo: file: or файл:?
        }
        return false;
    }

    //region textParsers
    static String getWikiLink(String lang){
        return "http://"+lang+".wikipedia.org/wiki/";
    }
    static String parseWikiLink(String content) {
        while(content.contains("[[")){
            String href = content.substring(content.indexOf("[[")+2,content.indexOf("]]"));

            String link = href;
            String text = href;
            if(href.contains("|")){
                String[] strings = href.split("\\|");

                link = strings[0];
                text = strings[1];

            }
            content = content.replace("[["+href+"]]", "<a href=\""+getWikiLink(lang)+link+"\">"+text+"</a>");
            //todo: check file\link
        }
        return content;
    }


    static String parseWikiText(String content){

        for (int i = 0; i < content.length();i++){
            Character character = content.charAt(i);
            if(character=='{'){
                content = content.substring(0, i) + parseWikiText(content.substring(i + 2));
            }
            else
                if(character=='}'){

                    String wikiParsedText = "";

                    String[] wikinode = content.substring(0, i).split("\\|");
                    String wikiNodeTitle = wikinode[0];
                    // todo: PARSE It!
                    do{ // it's like switch(String) lol
                        if(wikiNodeTitle.equalsIgnoreCase("About")){
                            wikiParsedText = "This article is about <i>" + wikinode[1]+"</i>. ";
                            if(wikinode.length>2){
                                wikiParsedText += parseWikiLink("For <i>"+ wikinode[2] + "</i>, see " + "<i>[[" + wikinode[3]+"]]</i>");
                            }
                            break;
                        }
                        if(wikiNodeTitle.equalsIgnoreCase("efn")||wikiNodeTitle.equalsIgnoreCase("sfn")){
                            wikiParsedText = "[proof]";
                            break;
                        }
                        if(wikiNodeTitle.equalsIgnoreCase("pp-semi-indef")){
                            wikiParsedText = "Protected article.";
                            break;
                        }
                        if(wikiNodeTitle.equalsIgnoreCase("main")){
                            wikiParsedText = parseWikiLink("<i>Main page: [["+wikinode[1]+"]]</i>");
                            break;
                        }
                        if(wikiNodeTitle.equalsIgnoreCase("further")){
                            wikiParsedText = "Further information: ";
                            for(int j=1;j<wikinode.length-1;j++){
                                wikiParsedText += parseWikiLink("[["+wikinode[j]+"]], ");
                            }
                            wikiParsedText += parseWikiLink("and [["+wikinode[wikinode.length-1]+"]]");
                            break;
                        }
                        if(wikiNodeTitle.equalsIgnoreCase("see also")){

                            wikiParsedText = parseWikiLink("<i>See also: [["+wikinode[1]+"]]</i>");
                            break;
                        }
                        if(wikiNodeTitle.toLowerCase().contains("ipa")){
                            //todo: voice?
                            wikiParsedText = "#wikivoice#";
                            break;
                        }
                        if(wikiNodeTitle.equalsIgnoreCase("Unicode")||wikiNodeTitle.equalsIgnoreCase("IAST")){
                            wikiParsedText = wikinode[1];
                            break;
                        }
                        if(wikiNodeTitle.equalsIgnoreCase("wiktionary")){
                            wikiParsedText = getWikiLink("You can look this also on [["+"wiktionary"+"]]");
                            break;
                        }
                        if(wikiNodeTitle.toLowerCase().contains("lang")){
                            if(wikiNodeTitle.equalsIgnoreCase("lang")){
                                wikiParsedText = parseWikiLink(wikinode[2]);
                            }else{
                                String[] langNodes = wikiNodeTitle.split("-");
                                wikiParsedText = getWikiLangLink(langNodes[1]) + wikinode[1];
                            }
                            break;
                        }
                        wikiParsedText = "#unparsed wikimarkup#";
                    }while (false);
                    //wikiParsedText = " PARSED WIKI "+ wikiNodeTitle.toUpperCase()+" ";

                    return wikiParsedText + parseWikiText(content.substring(i + 2));
                }
        }


        return content;
    }
    static String getWikiLangLink(String content){
        return "";
        //return parseWikiLink("[["+content+"|"+content+":]] ");todo: languages?
    }
    static String parseBold(String content){
        while(content.contains("'''")){
            content = content.replaceFirst("'''","<b>");
            content = content.replaceFirst("'''","</b>");
        }
        return content;
    }
    //endregion

    //region nodeParsers
    static PictureNode getPicture(String content){
        String[] strings = content.split("\\|");
        String imageName = strings[0];
        String description = "";
        if(strings.length==2)
            description =strings[1];
        else{
            Boolean restoreFlag = false;
            for(int i = 0;i<strings.length;i++){
                if(!restoreFlag){
                    if(strings[i].contains("[[")){
                        restoreFlag = true;
                        description = strings[i];
                    }
                }
                else{
                    description += strings[i];
                }

            }
            if(!restoreFlag)
                description = strings[strings.length-1];
        }
        description = parseWikiLink(description);
        PictureNode pictureNode = new PictureNode(imageName, (Spannable) Html.fromHtml(description));
        return pictureNode;
    }
    static ParagraphNode getParagraph(String content){

        content = content.replace("\n","<br>");
        content = parseBold(content);
        while(content.contains("''")){
            content = content.replaceFirst("''","<i>");
            content = content.replaceFirst("''","</i>");
        }/*
            while(content.contains("<ref>")){
                content = content.replace("<ref>", "#REF");
                content = content.replace("</ref>", "#REFEND");
            }
        */
        while(content.contains("{{")){
            content = parseWikiText(content);
        }
        content = parseWikiLink(content);
        return new ParagraphNode(content);
    }
    static Node getWikiNode(String content){

        if(content.contains("\n"))
            content = "#WIKIHARDNODE?#";
        else
            return new SimpleWikiNode(parseWikiText(content));
        return new Node(content);
    }
    static TitleNode getTitle(String content){
        // == TITLE ==

        int level = (content.length() - content.replace("=","").length()) / 2;
        content = content.replace("=","");
        return new TitleNode(content,level - 2);// the biggest title has 0 level, but the biggest title of wikimarkup has 2
    }
    static ItemsListNode getItemsList(String content){
        return new ItemsListNode(content);
    }
    static PreformattedNode getPreformatted(String content){
        //todo: preformat it -_-
        return new PreformattedNode(content);
    }
    static BlockquotedNode getBlockquoted(String content){
        return new BlockquotedNode(content);
    }
    static Node getDivider(){
        return new Node();
    }
    //endregion

    //region apiParser
    static String apiUrl = ".wikipedia.org/w/api.php?action=query&format=xml" +
            "&prop=revisions|images|langlinks" +
            "&rvprop=content" +
            "&imlimit=500" +
            "&lllimit=500" +
            "&titles=";

    public static Page getPage(Api apiResponse){
        try{
            Page page = apiResponse.query.pages.get(0);
            return page;
        }
        catch (Exception ex){
            throw new NullPointerException();
        }
    }
    public static String getApiUrl(String url){

        int langIndex= url.indexOf(".wikipedia.org");
        lang = url.substring(7,langIndex);
        int titleIndex = url.indexOf("/wiki/")+5;
        String pageTitle = url.substring(titleIndex+1);
        return "http://"+lang+apiUrl+pageTitle;
    }
    public static String getApiUrl(String title , String lang){

        String url = "http://" + lang + apiUrl + title;

        return url;
    }
    //endregion

}
