package com.company.jsoup_web_parser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class JavaDocParserWithPlaywrightNavigation {

    public static void main(String[] args) throws Exception {
        // Create Playwright instance and launch Chromium browser
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch();

        // Parse local HTML file using Jsoup
        String filePath = "/Users/mark.raphael/Documents/workspaces/java_workspace/sdet_playground/src/main/java/com" +
                "/company/jsoup_web_parser/site_files/chrome.html";
        File input = new File(filePath);
        Document doc = Jsoup.parse(input, "UTF-8");

        // Select elements starting with <div class="col-first">
        Elements elementsToExtract = doc.select("div[class^=col-first]");

        Set<String> visitedLinks = new HashSet<>();
        int i = 0;

        for (Element element : elementsToExtract) {
            Elements links = element.select("a");
            for (Element link : links) {
                if (i == 0) {
                    i++;
                    continue;
                }
                // Iterates through extracted links, and if not visited calls processed link method
                String linkHref = link.absUrl("href");
                if (!visitedLinks.contains(linkHref)) {
                    visitedLinks.add(linkHref);
                    processLinkContent(browser, linkHref);
                }
            }
        }

        browser.close();
        playwright.close();
    }

    private static void processLinkContent(Browser browser, String url) throws Exception {
        Page page = browser.newPage();
        page.navigate(url);
        page.waitForLoadState();

        String title = page.title();
        String content;
        System.out.println("Page Title: " + title);

        try {
            // Extract the content as a String
            Locator section = page.locator("section.method-details#method-detail");
            content = section.textContent();
            System.out.println(content);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        page.close();
    }
}
