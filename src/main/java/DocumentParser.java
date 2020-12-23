import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DocumentParser {


    public static WebDocument parse(String url){
        String str=DownloadWebPage(url);
        Document document = Jsoup.parse(str);

        return new WebDocument(getClasses(document),getBody(document));
    }


    public static String DownloadWebPage(String webpage)
    {
        try {
            URL url = new URL(webpage);
            BufferedReader readr =
                    new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder builder=new StringBuilder();
            String line;
            while ((line = readr.readLine()) != null) {
                builder.append(line);
                builder.append(' ');
            }

            readr.close();
            return builder.toString();
        }
        // Exceptions
        catch (MalformedURLException mue) {
            System.out.println("Malformed URL Exception raised");
        }
        catch (IOException ie) {
            System.out.println("IOException raised");
        }
        return "";
    }

    private static String getBody(Document document){
        Element body = document.getElementsByClass("post-body").first();
        ArrayList<String> classes=new ArrayList<>();
        for (int i = 0; i <body.select("p").size() ; i++) {

            Element link = body.select("p").get(i);
            if (!link.hasText())
                break;
            else
                classes.add(link.text());
        }
        for (int i = 0; i <body.select("ol").size() ; i++) {
            Element olLink=body.select("ol").get(i);
            for (int j = 0; j <olLink.select("li").size() ; j++) {
                Element link = olLink.select("li").get(j);
                if (!link.hasText())
                    break;
                else
                    classes.add(link.text());
            }

        }
        StringBuilder builder=new StringBuilder();
        for (String aClass : classes) {
            builder.append(aClass);
            builder.append(' ');
        }
        return builder.toString();

    }

    private static ArrayList<String> getClasses(Document document){
        ArrayList<String> classes=new ArrayList<>();
        for (int i = 16; i <document.select("a").size() ; i++) {

            Element link = document.select("a").get(i);
            if (!link.hasText())
                break;
            else
                classes.add(link.text());
        }
        return classes;
    }

    public static String getTestPageBody(String url){
        String str=DownloadWebPage(url);
        Document body = Jsoup.parse(str);
        ArrayList<String> classes=new ArrayList<>();
        for (int i = 0; i <body.select("p").size() ; i++) {

            Element link = body.select("p").get(i);
            if (!link.hasText())
                break;
            else
                classes.add(link.text());
        }
        for (int i = 0; i <body.select("ol").size() ; i++) {
            Element olLink=body.select("ol").get(i);
            for (int j = 0; j <olLink.select("li").size() ; j++) {
                Element link = olLink.select("li").get(j);
                if (!link.hasText())
                    break;
                else
                    classes.add(link.text());
            }

        }
        StringBuilder builder=new StringBuilder();
        for (String aClass : classes) {
            builder.append(aClass);
            builder.append(' ');
        }
        return builder.toString();

    }

}
