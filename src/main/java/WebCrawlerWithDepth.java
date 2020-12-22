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

    public void getPageLinks(String URL, int depth) {
        if ((!links.contains(URL) && (depth < MAX_DEPTH)) && URL.startsWith("https://mstajbakhsh.ir/")) {
            //System.out.println(">> Depth: " + depth + " [" + URL + "]");
            try {
                links.add(URL);

                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");

                depth++;
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"), depth);
                }
            } catch (IOException e) {
                //System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        WebCrawlerWithDepth crawler= new WebCrawlerWithDepth();
        crawler.getPageLinks("https://mstajbakhsh.ir/", 0);
        ArrayList<String> links=new ArrayList<>();
        links.addAll(crawler.getLinks());

        System.out.println(links);

        /*
        try {
            PrintWriter pw=new PrintWriter("links.txt");
            for (String link : crawler.getLinks()) {
                pw.println(link);
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
    }
}