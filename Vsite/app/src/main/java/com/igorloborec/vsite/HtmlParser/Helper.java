/*
 * Copyright (C) 2015  Bikonja
 */

package com.igorloborec.vsite.HtmlParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 * Created by Bikonja on 5.3.2015..
 */
public class Helper {
    public static String GetNodeTextWithNewLines(Node node) {
        String text = "";

        if (node != null) {
            for (Node childNode : node.childNodes()) {
                    if (childNode.nodeName().toLowerCase().equals("br")) {
                        text += "\n";
                    } else {
                        if (childNode instanceof TextNode) {
                            text += childNode;
                        } else {
                            text += GetNodeTextWithNewLines(childNode);
                        }
                    }
            }
        }

        return text;
    }
}
