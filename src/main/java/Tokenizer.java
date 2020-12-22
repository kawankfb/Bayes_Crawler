import opennlp.tools.stemmer.PorterStemmer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static boolean isWhite(char character){
        return     (character >= 64 &&   character <= 90)//English Capital letters
                || (character >= 97 &&   character <= 122);//English lowerCase letters
    }

    public static List<String> tokenize(String string){
        StringBuilder temp=new StringBuilder();
        List<String> distinctTokens =new ArrayList<>();
        for (int i = 0; i <string.length() ; i++) {
            char tempChar=string.charAt(i);
            if (isWhite(tempChar))
                temp.append(tempChar);
            else if (temp.length()>0){
                distinctTokens.add(temp.toString());
                temp=new StringBuilder();
            }
        }
        if (temp.length()>0)
            distinctTokens.add(temp.toString());
        return distinctTokens;
    }


    public static ArrayList<String> getTokens(String body){
        ArrayList<String> tokens=new ArrayList<>();
        List<String> tokenizedString=Tokenizer.tokenize(body.toLowerCase());
        PorterStemmer porterStemmer=new PorterStemmer();
        for (String s : tokenizedString) {
            if (!isStopWord(s)) {

                tokens.add(porterStemmer.stem(s));
                porterStemmer.reset();
            }
            }


        return tokens;
    }


    public static boolean isStopWord(String token){
        String[] stopWordsArray=new String[]{"i", "me", "my", "myself", "we", "our",
                "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves",
                "he", "him", "his", "himself", "she", "her", "hers", "herself", "it",
                "its", "itself", "they", "them", "their", "theirs", "themselves",
                "what", "which", "who", "whom", "this", "that", "these", "those",
                "am", "is", "are", "was", "were", "be", "been", "being", "have", "has",
                "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and",
                "but", "if", "or", "because", "as", "until", "while", "of", "at", "by",
                "for", "with", "about", "against", "between", "into", "through", "during",
                "before", "after", "above", "below", "to", "from", "up", "down", "in", "out",
                "on", "off", "over", "under", "again", "further", "then", "once", "here",
                "there", "when", "where", "why", "how", "all", "any", "both", "each", "few",
                "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own",
                "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don",
                "should", "now","ve","zero","one","two","three","four","five","six","seven",
                "eight","nine","ten","first","second","third"};
        for (String s : stopWordsArray) {
            if (s.equals(token))
                return true;
        }
        return false;
    }
}
