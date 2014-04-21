package com.agcy.wikiread.Core;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.view.View;
import android.widget.TextView;

import com.agcy.wikiread.Core.Api.Api;
import com.agcy.wikiread.Core.Parsing.Markup.Parser;
import com.agcy.wikiread.Core.Parsing.Nodes.BlockquotedNode;
import com.agcy.wikiread.Core.Parsing.Nodes.GroupNode;
import com.agcy.wikiread.Core.Parsing.Nodes.ItemsListNode;
import com.agcy.wikiread.Core.Parsing.Nodes.ListItemNode;
import com.agcy.wikiread.Core.Parsing.Nodes.Node;
import com.agcy.wikiread.Core.Parsing.Nodes.ParagraphNode;
import com.agcy.wikiread.Core.Parsing.Nodes.PictureNode;
import com.agcy.wikiread.Core.Parsing.Nodes.PreformattedNode;
import com.agcy.wikiread.Core.Parsing.Nodes.SimpleWikiNode;
import com.agcy.wikiread.Core.Parsing.Nodes.TitleNode;
import com.agcy.wikiread.Models.Legend;
import com.agcy.wikiread.Models.Page;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 27.02.14.
 */
public class Pager {
    // EXAMPLE: http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xmlfm&titles=Albert%20Einstein
    // formating http://www.mediawiki.org/wiki/Help:Formatting

    String lang;
    String content;
    Page page;
    public ArrayList<View> parsedViews;
    ArrayList<Node> parsedNodes;
    Node currentNode;
    String nodeContent;

    public Pager(Page page, String lang) {


        this.page = page;
        this.content =  page.revisions.get(0).content;
        this.lang = lang;
        parsedNodes = new ArrayList<Node>();
        parsedViews = new ArrayList<View>();
        currentNode = null;
        nodeContent = "";

    }

    public void parseViews(Context context) {



        parseNodes();
        ArrayList<View> views = new ArrayList<View>();
        for (Node node : parsedNodes) {
            View view = node.getView(context);
            views.add(view);
        }
        parsedViews = views;
    }

    public void parseNodes() {


        if(content.toLowerCase().startsWith("#redirect")){
            content = "The path does not exist. Maybe " + content.substring(9)+"?";
        }

        while (!content.equals("")) {
            Character currentCharacter = content.charAt(0);
            Character nextChar;

            switch (currentCharacter) {

                case '\n':
                    try {
                        currentCharacter = content.charAt(1);
                        switch (currentCharacter) {

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

                                int endOfTitle = content.indexOf("=\n") + 1;
                                nodeContent = content.substring(1, endOfTitle);

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
                                int listStart = 1;

                                saveCurrentNodeAsParagraph();

                                nodeContent += content.substring(listStart, listend);

                                currentNode = getItemsList(nodeContent);

                                content = content.substring(listend);

                                saveNode();
                                break;
                            // <nowiki/> full paragraph are preformated
                            // or <pre>
                            case '<':
                                int tagStart = content.indexOf('>') + 1;
                                String tag = content.substring(1, tagStart);
                                if (tag.equals("<pre>")) {
                                    saveCurrentNodeAsParagraph();
                                    int tagEnd = content.indexOf("\n</pre>\n");
                                    nodeContent = content.substring(tagStart + 1, tagEnd);
                                    currentNode = getPreformatted(nodeContent);
                                    content = content.substring(tagEnd + 1 + tag.length());

                                    saveNode();
                                } else {
                                    if (tag.equals("<blockquote>")) {
                                        saveCurrentNodeAsParagraph();
                                        int tagEnd = content.indexOf("</blockquote>");
                                        nodeContent = content.substring(tagStart, tagEnd);
                                        currentNode = getBlockquoted(nodeContent);
                                        content = content.substring(tagEnd + tag.length());

                                        saveNode();
                                    }
                                }
                                break;
                            case '{':
                                nextChar = content.charAt(2);
                                if (nextChar == '{' || nextChar == '|') {

                                    saveCurrentNodeAsParagraph();
                                    String correctedNode = NewParser.getCorrectedWikiTag(content.substring(1));
                                    int nodeEnd = correctedNode.length();
                                    nodeContent = correctedNode;

                                    currentNode = getWikiNode(nodeContent);
                                    // todo: catched {{some wiki info}}
                                    content = "\n" + content.substring(nodeEnd+1);

                                    saveNode();
                                    break;
                                }/*
                                if (nextChar == '|') {
                                    //todo: catched {| |}
                                    saveCurrentNodeAsParagraph();
                                    int nodeEnd = content.indexOf("|}\n") + 2;
                                    nodeContent = content.substring(0, nodeEnd);
                                    currentNode = getWikiNode(nodeContent);
                                    content = "\n" + content.substring(nodeEnd);

                                    saveNode();
                                    break;
                                }*/
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
                                if (nextChar == '[' && checkPicture(content.substring(3, 20))) {
                                    int imageEnd = content.indexOf("]]\n");
                                    nodeContent = content.substring(3, imageEnd).replace("Image:", "File:");
                                    currentNode = getPicture(nodeContent);
                                    content = "\n" + content.substring(imageEnd + 2);
                                }
                                saveNode();
                                break;
                            // is it error \n?

                            default:
                                if (!nodeContent.equals("")) {
                                    currentNode = getParagraph(nodeContent);
                                    content = "\n" + content;
                                }
                                saveNode();
                                break;
                        }
                    } catch (Exception exp) {
                        if (Helper.isTest()) {
                            currentNode = new Node("Errored node");
                            saveNode();
                        }
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



                    }
                */
                case '{':

                    // todo: catched {{some wiki info}}

                    String correctedNodeTemp = NewParser.getCorrectedWikiTag(content.substring(0));
                    int correctNodeEnd = correctedNodeTemp.length();

                    if(Helper.isTest())
                        nodeContent += "#unparsed wikinode#";

                    content = " " + content.substring(correctNodeEnd);

                    break;
                default: {
                    nodeContent += Character.toString(currentCharacter);
                }
            }
            if (content.length() != 0)
                content = content.substring(1);

        }
        saveCurrentNodeAsParagraph();
        ArrayList<Node> response = new ArrayList<Node>();
        GroupNode groupNode = null;
        for (Node node : parsedNodes) {
            if (node.getClass() == TitleNode.class && ((TitleNode) node).isExpandable()) {
                if (groupNode != null)
                    response.add(groupNode);
                groupNode = new GroupNode((TitleNode) node);
            } else {
                if (groupNode == null)
                    response.add(node);
                else
                    groupNode.add(node);
            }
        }
        if (groupNode != null)
            response.add(groupNode);
        parsedNodes = response;
    }



    private void saveNode() {

        if (currentNode != null) {
            parsedNodes.add(currentNode);
            currentNode = null;
        }
        nodeContent = "";

    }

    private void saveCurrentNodeAsParagraph() {
        if (nodeContent != null && !nodeContent.equals("") && !nodeContent.equals(" ") && !nodeContent.equals("\n")) {
            currentNode = getParagraph(nodeContent);
            saveNode();
        }
    }

    Boolean checkPicture(String content) {
        if (content.contains(":")) {

            int endIndex = content.indexOf(":");
            String fileCheckingTemp = content.substring(0, endIndex);
            String image = Parser.Image.get(lang);
            String file = Parser.File.get(lang);
            return fileCheckingTemp.equals(file) || fileCheckingTemp.equals(image);
        }
        return false;
    }

    //region markupParsers
    static String getWikiLink(String lang) {
        return "http://" + lang + ".wikipedia.org/wiki/";
    }

    ArrayList<Legend> parseWikiLegend(String content) {
        ArrayList<Legend> response = new ArrayList<Legend>();
        while (content.toLowerCase().contains("{{legend")) {
            int startIndex = content.toLowerCase().indexOf("{{legend"),
                    endIndex = content.indexOf("}}\n") + 2;
            if (endIndex < 0)
                endIndex = content.indexOf("}}") + 2;
            String unparsedLegend = content.substring(startIndex, endIndex);

            String[] strings = unparsedLegend.split("\\|");
            String description = strings[2],
                    color = strings[1];

            Legend parsedLegend = new Legend(color, description);
            content = content.substring(endIndex);
            response.add(parsedLegend);
        }
        return response;
    }

    String parseWikiLinks(String content) {

        while (content.contains("[[")) {
            String href = content.substring(content.indexOf("[[") + 2, content.indexOf("]]"));

            String link = href;
            String text = href;
            if (href.contains("|")) {
                String[] strings = href.split("\\|");

                link = strings[0];
                text = strings[1];

            }
            content = content.replace("[[" + href + "]]", "<a href=\"" + getWikiLink(lang) + link + "\">" + text + "</a>");
            //todo: check file\link
        }
        while (content.contains("[")) {
            String href = content.substring(content.indexOf("[") + 1, content.indexOf("]"));

            int firstSpace = href.indexOf(" ");
            String link = href.substring(0, firstSpace);
            String text = href.substring(firstSpace + 1);

            content = content.replace("[" + href + "]", "<a href=\"" + getWikiLink(lang) + link + "\">" + text + "</a>");

        }
        return content;
    }

    String parseWikiTags(String content) {

        for (int i = 0; i < content.length(); i++) {
            Character character = content.charAt(i);
            if (character == '{') {
                content = content.substring(0, i) + parseWikiTags(content.substring(i + 2));
            } else if (character == '}') {

                String wikiParsedTag = "";

                String[] wikinode = content.substring(0, i).split("\\|");
                String wikiNodeTitle = wikinode[0];
                // todo: PARSE It!
                do { // it's like switch(String) lol
                    if (wikiNodeTitle.toLowerCase().contains("legend")) {

                        break;
                    }
                    if (wikiNodeTitle.contains("uses")) {
                        if (wikinode.length > 1) {
                            wikiParsedTag = "For other uses, see";
                            for (int j = 1; j < wikinode.length - 1; j++)
                                wikiParsedTag += "<i>" +
                                        parseWikiLinks("[[" + wikinode[j] + "]]") +
                                        "</i>";
                        } else
                            //todo: {{Other uses}}?
                            //wikiParsedTag =
                            //        parseWikiLinks( "[[" + pageTitle + "_(disambiguation)]]")
                            //        + "</i>. ";
                            wikiParsedTag = "";
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("About")) {
                        wikiParsedTag = "This article is about <i>" + wikinode[1] + "</i>. ";
                        if (wikinode.length == 3) {
                            wikiParsedTag += "For <i>" + wikinode[2] + "</i>, see " + "<i>" +
                                    parseWikiLinks("[[" + wikinode[3] + "]]") +
                                    "</i>";
                        }
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("efn") || wikiNodeTitle.equalsIgnoreCase("sfn")) {
                        wikiParsedTag = "[proof]";
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("pp-semi-indef")) {
                        if(Helper.isTest())
                            wikiParsedTag = "Protected article.";
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("main")) {
                        wikiParsedTag = parseWikiLinks("Main page: <i>[[" + wikinode[1] + "]]</i>");
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("further")) {
                        wikiParsedTag = "Further information: ";
                        for (int j = 1; j < wikinode.length - 1; j++) {
                            wikiParsedTag += parseWikiLinks("<i>[[" + wikinode[j] + "]]</i>, ");
                        }
                        wikiParsedTag += parseWikiLinks("and <i>[[" + wikinode[wikinode.length - 1] + "]]</i>");
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("see also")) {

                        wikiParsedTag = parseWikiLinks("See also: <i>[[" + wikinode[1] + "]]</i>");
                        break;
                    }
                    if (wikiNodeTitle.toLowerCase().contains("ipa")) {
                        //todo: transcription?
                        if(Helper.isTest())
                            wikiParsedTag = "#transcription#";
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("Unicode") || wikiNodeTitle.equalsIgnoreCase("IAST")) {
                        wikiParsedTag = wikinode[1];
                        break;
                    }
                    if (wikiNodeTitle.equalsIgnoreCase("wiktionary")) {
                        //todo: page title?
                        wikiParsedTag = getWikiLink("You can look this also on <i>[[" + "wiktionary" + "]]</i>");
                        break;
                    }
                    if (wikiNodeTitle.toLowerCase().contains("lang")) {
                        if (wikiNodeTitle.equalsIgnoreCase("lang")) {
                            wikiParsedTag = parseWikiLinks(wikinode[2]);
                        } else {
                            String[] langNodes = wikiNodeTitle.split("-");
                            wikiParsedTag = getWikiLangLink(langNodes[1]) + wikinode[1];
                        }
                        break;
                    }
                    if (wikiNodeTitle.toLowerCase().contains("convert")) {
                        //{{convert|8,000|km|mi|abbr=on}}
                        //todo: convert?
                        if (wikiNodeTitle.equalsIgnoreCase("convert"))
                            wikiParsedTag = wikinode[1] + " " + wikinode[2];
                        else {
                            int j = 1;
                            do {
                                wikiParsedTag += wikinode[j] + wikinode[j + 1];
                                if (valueChecker(wikinode[j + 2])) {
                                    j += 2;
                                } else {
                                    wikiParsedTag += wikinode[j + 2];
                                    break;
                                }
                            } while (j != 0);
                        }
                        break;
                    }
                    if (Helper.isTest())
                        wikiParsedTag = "#unparsed " + wikiNodeTitle + "#";
                    else
                        wikiParsedTag = "";
                } while (false);
                //wikiParsedTag = " PARSED WIKI "+ wikiNodeTitle.toUpperCase()+" ";

                return wikiParsedTag + parseWikiTags(content.substring(i + 2));
            }
        }


        return content;
    }

    String getWikiLangLink(String content) {
        return "";
        //return parseWikiLinks("[["+content+"|"+content+":]] ");todo: languages?
    }

    String parseBold(String content) {
        while (content.contains("'''")) {
            content = content.replaceFirst("'''", "<b>");
            content = content.replaceFirst("'''", "</b>");
        }
        return content;
    }

    Boolean valueChecker(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    //endregion

    //region nodeParsers

    PictureNode getPicture(String content) {
        String description = "";
        content = parseWikiLinks(content);
        ArrayList<Legend> legend = parseWikiLegend(content);
        content = parseWikiTags(content);
        String[] strings = content.split("\\|");

        String imageName = strings[0];
        description = strings[strings.length - 1];

        PictureNode pictureNode = new PictureNode(imageName, (Spannable) Html.fromHtml(description), legend);
        return pictureNode;
    }

    ParagraphNode getParagraph(String content) {



        content = content.replace("\n", "<br>");
        content = parseBold(content);
        while (content.contains("''")) {
            content = content.replaceFirst("''", "<i>");
            content = content.replaceFirst("''", "</i>");
        }
        while (content.contains("{{")) {
            content = parseWikiTags(content);
        }
        content = parseWikiLinks(content);
        return new ParagraphNode(content);
    }

     Node getWikiNode(String content) {

        if (content.contains("\n")) {
            if (Helper.isTest())
                content = "#WIKIHARDNODE?#";
            else
                content = "";
        }
        return new SimpleWikiNode(parseWikiTags(content));
    }

    TitleNode getTitle(String content) {
        // == TITLE ==

        int level = (content.length() - content.replace("=", "").length()) / 2;
        content = content.replace("=", "");
        return new TitleNode(content, level - 2);// the biggest title has 0 level, but the biggest title of wikimarkup has 2
    }

    ItemsListNode getItemsList(String content) {

        String[] items = content.split("\\n");
        ArrayList<ListItemNode> nodes = new ArrayList<ListItemNode>();
        for (String item : items) {
            int type = ListItemNode.SIMPLE;
            int deep = 0;
            Character firstChar = item.charAt(0);
            Character secondChar = item.charAt(1);
            Character typeChar = ' ';

            if (firstChar == ';' && secondChar == ' ') {
                int indexOfContent = item.indexOf(":");
                String itemTitle = item.substring(2, indexOfContent);
                String itemContent = item.substring(indexOfContent + 2);
                ListItemNode node = new ListItemNode(ListItemNode.TITLE, 0, getParagraph(itemTitle));
                nodes.add(node);
                node = new ListItemNode(ListItemNode.DEFINITION, 2, getParagraph(itemContent));
                nodes.add(node);
                continue;
            }

            Boolean dived = false;

            if (firstChar == '#' || firstChar == ';' || firstChar == ':' || firstChar == '*')
                do {

                    Character nextChar = item.charAt(deep + 1);
                    if (nextChar == '#' || nextChar == ';' || nextChar == ':' || nextChar == '*') {
                        deep++;
                    } else {
                        typeChar = item.charAt(deep);
                        if (nextChar == ' ') {
                            item = item.substring(deep + 2);
                        } else {
                            item = item.substring(deep + 1);
                        }
                        dived = true;
                    }

                } while (!dived);

            if (dived) {

                switch (typeChar) {
                    case '#':
                        type = ListItemNode.ITEM;
                        break;
                    case ';':
                        type = ListItemNode.TITLE;
                        break;
                    case ':':
                        type = ListItemNode.DEFINITION;
                        break;
                    case '*':
                        type = ListItemNode.ITEM;
                        break;
                }
            }
            ListItemNode node = new ListItemNode(type, deep, getParagraph(item));

            nodes.add(node);
        }

        return new ItemsListNode(nodes);
    }

    PreformattedNode getPreformatted(String content) {
        //todo: preformat it -_-
        return new PreformattedNode(content);
    }

    BlockquotedNode getBlockquoted(String content) {
        return new BlockquotedNode(content);
    }

    Node getDivider() {
        return new Node();
    }
    //endregion

    public static Page getPage(Api apiResponse) {
        try {
            Page page = apiResponse.query.pages.get(0);
            return page;
        } catch (Exception ex) {
            throw new NullPointerException();
        }
    }



}