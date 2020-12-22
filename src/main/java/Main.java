import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String baseUrl="https://mstajbakhsh.ir/";
        ArrayList<String>links=getLinks(baseUrl);
        ArrayList<WebDocument> webDocuments=new ArrayList<>();
        for (String link : links) {
            if (links.indexOf(link)<22)
            webDocuments.add(DocumentParser.parse(link));
        }
        Set<String> distinctClasses=new HashSet<>();
        int record_count=0;
        for (WebDocument webDocument : webDocuments) {
            for (String aClass : webDocument.getClasses()) {
                distinctClasses.add(aClass);
                record_count++;
            }
        }

        Set<String> distinctTokens=new HashSet<>();
        for (WebDocument webDocument : webDocuments) {
            distinctTokens.addAll(webDocument.getTokens());
        }
        ArrayList<String> distinctTokensArrayList=new ArrayList<>();
        distinctTokensArrayList.addAll(distinctTokens);
        distinctTokensArrayList.sort(String::compareToIgnoreCase);
        for (String s : distinctTokensArrayList) {
            System.out.println(s);
        }

        System.out.println();
        System.out.println("record count is : "+record_count);

        //creating the table and initializing it with 0
        int[][] bayesTable=new int[record_count][distinctTokensArrayList.size()];
        for (int i = 0; i < bayesTable.length ; i++) {
            Arrays.fill(bayesTable[i], 0);
        }

        //creating an array to store the label for each record
        String[] recordClasses=new String[record_count];
        int rowCounter=0;
        for (int i = 0; i < webDocuments.size() ; i++) {
            for (String aClass : webDocuments.get(i).getClasses()) {
                for (String token : webDocuments.get(i).getTokens()) {
                bayesTable[rowCounter][distinctTokensArrayList.indexOf(token)]++;
                }
                recordClasses[rowCounter]=aClass;
            rowCounter++;
            }

        }

//        System.out.println();
//        System.out.println("record classes :");
//        for (String recordClass : recordClasses) {
//            System.out.println(recordClass);
//        }
//        for (int[] ints : bayesTable) {
//            for (int anInt : ints) {
//                System.out.print(anInt+" ");
//            }
//            System.out.println();
//        }

        HashMap<String, BigDecimal> probabilities=new HashMap<>();

        //calculate class probabilities and store them in HashMap -> probabilities.get("class") = p("class")
        for (String distinctClass : distinctClasses) {
            int classCount=0;
            int totalCount=0;
            for (String recordClass : recordClasses) {
                if (recordClass.equals(distinctClass))
                    classCount++;
                totalCount++;
            }
            BigDecimal temp= BigDecimal.valueOf(classCount).divide(BigDecimal.valueOf(totalCount),100,RoundingMode.HALF_EVEN);
            probabilities.putIfAbsent(distinctClass,temp);
        }

        //calculate p(token|class) probabilities and store them in HashMap -> probabilities.get("token,class") = p(token|class)
        for (int i = 0; i < distinctTokensArrayList.size() ; i++) {
            String token=distinctTokensArrayList.get(i);
            for (String distinctClass : distinctClasses) {
                int tokenOccurrenceInClass=0;
                int tokenTotalOccurrence=0;

                for (int j = 0; j < bayesTable.length ; j++) {
                    if (distinctClass.equals(recordClasses[j]))
                    {
                        tokenOccurrenceInClass=tokenOccurrenceInClass+bayesTable[j][i];
                    }
                    tokenTotalOccurrence=tokenTotalOccurrence+bayesTable[j][i];
                }
                if (tokenOccurrenceInClass!=0){
                    BigDecimal temp= BigDecimal.valueOf(tokenOccurrenceInClass).divide(BigDecimal.valueOf(tokenTotalOccurrence),100,RoundingMode.HALF_EVEN);
                    probabilities.put(token+","+distinctClass,temp);
                }
                else
                    probabilities.put(token+","+distinctClass,new BigDecimal(0.01));

            }

        }
        BigDecimal bigResult=new BigDecimal(1);
        for (String s : probabilities.keySet()) {
            bigResult=bigResult.multiply(probabilities.get(s));
        }
        System.out.println(bigResult.toPlainString());


        System.out.println();
        while (true){
            Scanner scanner=new Scanner(System.in);
            String query=scanner.nextLine();
            Set<String> tokens=new HashSet<>();
            tokens.addAll(Tokenizer.getTokens(query.toLowerCase()));
            System.out.println(getMostProbableClass(probabilities,distinctClasses,distinctTokens,tokens));
            if (!probabilities.getOrDefault(query,new BigDecimal(-1)).equals(new BigDecimal(-1)))
            System.out.println(probabilities.get(query).toPlainString());
            else System.out.println("no such entry");
        }
    }
    private static String getMostProbableClass(HashMap<String,BigDecimal> probabilities, Set<String> distinctClasses, Set<String> distinctTokens, Set<String> tokens){
        Set<String>entryTokens=new HashSet<>();
        for (String token : tokens) {
            if (distinctTokens.contains(token))
                entryTokens.add(token);
        }

        String result="";
        BigDecimal maxProbability=new BigDecimal(0);
        for (String distinctClass : distinctClasses) {
        BigDecimal temp=new BigDecimal(1);
            for (String entryToken : entryTokens) {
                if (!probabilities.getOrDefault(entryToken+","+distinctClass,new BigDecimal(-1)).equals(new BigDecimal(-1)))
                    temp=temp.multiply(probabilities.get(entryToken+","+distinctClass));
            }
            temp=temp.multiply(probabilities.get(distinctClass));
            if (temp.compareTo(maxProbability)==1) {
                maxProbability = temp;
                result=distinctClass;
            }
        }

        return result;
    }

    private static ArrayList<String> getLinks(String baseUrl){
        ArrayList<String> exceptions=new ArrayList<>();
        ArrayList<String> links=new ArrayList<>();// Contains the desired links

        if (Files.exists(Path.of("saved_links.txt")))
        {
            FileInputStream fis= null;
            try {
                fis = new FileInputStream("saved_links.txt");
                Scanner scanner=new Scanner(fis);
                while (scanner.hasNextLine())
                    links.add(scanner.nextLine());
                return links;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        exceptions.add(baseUrl+"category/");
        exceptions.add(baseUrl+"projects/");
        exceptions.add(baseUrl+"academic/");
        exceptions.add(baseUrl+"page/");
        exceptions.add(baseUrl+"author/");
        exceptions.add(baseUrl+"contact/");
        exceptions.add(baseUrl+"about-me/");
        exceptions.add(baseUrl+"comments/");
        exceptions.add(baseUrl+"tag/");
        exceptions.add(baseUrl+"wp-statistics");
        WebCrawlerWithDepth crawler= new WebCrawlerWithDepth();
        crawler.getPageLinks(baseUrl, 0);
        for (String link : crawler.getLinks()) {
            boolean isException=false;
            for (String exception : exceptions) {
                if (link.startsWith(exception))
                    isException=true;
            }
            if (!link.endsWith("/"))
                isException=true;
            if (link.equals(baseUrl))
                isException=true;
            if (isException)
                continue;
            else
                links.add(link);
        }
        try {
            PrintWriter printWriter=new PrintWriter("saved_links.txt");
            for (String link : links) {
                printWriter.println(link);
            }
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return links;
    }
}
