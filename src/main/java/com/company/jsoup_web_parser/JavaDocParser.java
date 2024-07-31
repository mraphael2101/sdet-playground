package com.company.jsoup_web_parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

public class JavaDocParser {

    public static void main(String[] args) throws Exception {
        String filePath = "/Users/mark.raphael/Documents/workspaces/java_workspace/sdet_playground/src/main/java/com" +
                "/company/jsoup_web_parser/site_files/chrome.html";
        File input = new File(filePath);
        Document doc = Jsoup.parse(input, "UTF-8");

        // Select sections based on IDs
        Elements sections = doc.select("#related-package-summary, #class-summary");

        for (Element section : sections) {
            assert section.parent() != null;
            Element headingElement = section.parent();
            //headingElement = section.parent().previousElementSibling();

            // Prints first link in each section
            //System.out.println(headingElement.selectFirst("a").text());

            // Prints all links in each section
            //System.out.println(headingElement.select("a").text());

            // Select elements starting with <div class="col-first">
            Elements elementsToExtract = section.select("div[class^=col-first]");

            // Process elements
            for (Element element : elementsToExtract) {
                // Extract and print links within the element
                Elements links = element.select("a");
                for (Element link : links) {
                    String linkText = link.text();
                    String linkHref = link.absUrl("href");
                    System.out.println("\t- Link Text: " + linkText);
                    System.out.println("\t- Link Href: " + linkHref);
                }
            }
            System.out.println(); // Add newline between sections
        }
    }

}
