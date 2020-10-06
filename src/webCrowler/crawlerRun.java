package webCrowler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.agogs.languagelayer.api.APIConsumer;
import com.github.agogs.languagelayer.api.LanguageLayerAPIConsumer;
import com.github.agogs.languagelayer.model.APIResult;
import com.github.agogs.languagelayer.model.Batch;
import com.github.agogs.languagelayer.model.QueryParams;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.language.LanguageIdentifier;

import java.util.Scanner; 

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import org.jsoup.Jsoup;
public class crawlerRun {

	private static HashSet<String> links;
    private List<List<String>> articles;
    static int counter =1;
    static int limit =0;

    public crawlerRun() {
        links = new HashSet<>();
        articles = new ArrayList<>();
    }

    public void getURLs(String URL,String lan) throws IOException{
    	StringBuilder sb = new StringBuilder();
    	int size;
        // Check if you have already crawled the URLs
        if (!links.contains(URL) && URL != ""  && limit < 130) {
            try {
            Document doc = Jsoup.connect(URL).ignoreContentType(true).userAgent("Mozilla").get();
            	String title = doc.title();
          //  if(languageCheck(title).equals(lan)) {
                // If not add it to the index
                if (links.add(URL)) {
                	//tester
                  //  System.out.println(URL); 
                	limit++;
                	getHTML(URL);           
                }
          //  }
                // Fetch the HTML code
                // extract links to other URLs
                Elements linksOnPage = doc.select("a[href]");
                size = linksOnPage.size();
                System.out.println(URL);
                System.out.println("URL Num: "+size);
                writeToCsv(URL+","+size);
                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getURLs(page.attr("abs:href"),lan);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
        

    } 
    public static void getHTML(String url) {
    	 String webPage = url;
         String html;
         StringBuilder filename = new StringBuilder() ;
         filename.append("repository\\");
         filename.append(counter);
         filename.append (".txt");
		try {
			
			Document doc = Jsoup.connect(webPage).ignoreContentType(true).userAgent("Mozilla").get();
			doc.getElementsByTag("style").remove();
			doc.removeAttr("style");
			doc.select("img").remove();
			doc.select("[style]").removeAttr("style");
			String title = doc.title();
			html = doc.html();	
			//tester
			System.out.println(title);
			
			try { 
				if(doc.toString() != null && !doc.toString().isEmpty()){
					Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename.toString ()), "utf-8")) ;
					writer.write(doc.toString());
					counter++;
				}	
			}catch (IOException ex) {
				ex.printStackTrace();
			}
			//System.out.print(languageCheck(title));
		
		}
		 catch (IOException ex) {
			ex.printStackTrace();
			System.out.print(url+"is wrong");
		 } 
    }
    


    public static void main(String[] args) throws IOException {
//    	 Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//    	    System.out.println("Please Enter seed WebSite: ");
//    	    String url = myObj.nextLine();  // Read user input
//    	    System.out.println("Please Enter Language code(en,cn,lt): ");
//    	    String lan = myObj.nextLine();  // Read user input
    	crawlerRun bwc = new crawlerRun(); 	
     //   bwc.getURLs("http://"+url,lan);
   
        chineseC("abc");
        
        
        
        
        //	languageCheck("Nada hay más surreal que la realidad");
    	
    	bwc.getURLs("https://www.cpp.edu","en");
   
        
    }
    public void writeToCsv(String url) throws IOException{
    	FileWriter pw = new FileWriter("report.csv",true); 
    		pw.append(url+","+"\n");  
            pw.flush();
            pw.close();
    }
    public static String languageCheck(String contain) throws IOException {
    	LanguageIdentifier identifier = new LanguageIdentifier(contain);
        String language = identifier.getLanguage();
        System.out.println("Language of the given content is : " + language);
        return language;
    }
    public static String chineseC(String s) {
    	String language = "";
    	 for (int i = 0; i < s.length(); ) {
    	        int codepoint = s.codePointAt(i);
    	        i += Character.charCount(codepoint);
    	        if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
    	        	language = "cn";
    	            return language;
    	        }
    	    }
    	    return language;
    }
    
    
//    public static String checkLan(String contain,String choice) throws IOException {
//    	APIConsumer con = new LanguageLayerAPIConsumer("http://api.languagelayer.com/", "7ec8de2ed45584cbb1ea0fbf6c6f5ae0");
//        QueryParams params = new QueryParams().query(contain);
//        APIResult result = con.detect(params);
//       // get_lang(query = "I really really love R and that's a good thing, right?", api_key = "your_api_key")
//        String language = new ObjectMapper().writeValueAsString(result.getResults().get(0).getLanguageCode());
//        
//        System.out.println(new ObjectMapper().writeValueAsString(result.getResults()));
//        return language ;
//    }

}