import java.util.ArrayList;

public class WebDocument {
    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private ArrayList<String> classes;

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    private ArrayList<String> tokens;
    private String body;
    public WebDocument(ArrayList<String> classes,String body){
    this.classes=classes;
    this.body=body;
    setTokens(Tokenizer.getTokens(body));
    }
    public WebDocument(){
        setClasses(new ArrayList<>());
        setBody("");
        setTokens(new ArrayList<>());
    }

}
