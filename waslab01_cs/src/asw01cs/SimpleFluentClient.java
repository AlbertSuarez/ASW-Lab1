package asw01cs;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class SimpleFluentClient {

    public final static void main(String[] args) throws Exception {

    	String lastTweetID = Request.Post("http://localhost:8080/waslab01_ss/wot")
    	        .bodyForm(Form.form().add("author", "Cervantes").add("tweet_text", "holiiii").build())
    	        .addHeader("Accept", "text/plain").execute().returnContent().asString().trim();
    	
     
    	System.out.println(lastTweetID);    	
    	System.out.println(Request.Get("http://localhost:8080/waslab01_ss").addHeader("Accept", "text/plain") .execute().returnContent());
    	
    	Request.Post("http://localhost:8080/waslab01_ss/wot")
    	        .bodyForm(Form.form().add("deleteTweetID",lastTweetID).build())
    	        .addHeader("Accept", "text/plain").execute();
     
    	System.out.println(lastTweetID + " was deleted."); 
    }
}

