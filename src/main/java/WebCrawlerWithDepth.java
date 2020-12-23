import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class WebCrawlerWithDepth {
    private static final int MAX_DEPTH = 20;

    public HashSet<String> getLinks() {
        return links;
    }

    private HashSet<String> links;

    public WebCrawlerWithDepth() {
        links = new HashSet<>();
    }

    public HashSet<String> getPageLinks(String URL, int depth){
        getPageLinks(URL,depth,URL);
        return getLinks();
    }
    private void getPageLinks(String URL, int depth,String baseURL) {
        if ((!links.contains(URL) && (depth < MAX_DEPTH)) && URL.startsWith(baseURL)) {
            try {
                links.add(URL);

                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");

                depth++;
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"), depth,baseURL);
                }
            } catch (IOException e) {
                //System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }
}