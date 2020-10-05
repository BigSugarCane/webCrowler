package webCrowler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.agogs.languagelayer.api.APIConsumer;
import com.github.agogs.languagelayer.api.LanguageLayerAPIConsumer;
import com.github.agogs.languagelayer.model.APIResult;
import com.github.agogs.languagelayer.model.Batch;
import com.github.agogs.languagelayer.model.QueryParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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

    public crawlerRun() {
        links = new HashSet<>();
        articles = new ArrayList<>();
    }

    public void getURLs(String URL) {
    	StringBuilder sb = new StringBuilder();
    	int size;
        //4. Check if you have already crawled the URLs
        
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                  //  System.out.println(URL);
                	getHTML(URL);           
                    
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");
                size = linksOnPage.size();
                System.out.println(URL);
                System.out.println("URL Num: "+size);
                writeToCsv(URL+","+size);

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getURLs(page.attr("abs:href"));
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
			
			Document doc = Jsoup.connect(webPage).get();
			doc.getElementsByTag("style").remove();
			doc.removeAttr("style");
			doc.select("img").remove();
			doc.select("[style]").removeAttr("style");
			
			html = doc.html();
			
			System.out.print(html);
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(filename.toString ()), "utf-8"))) {
		   writer.write(html);
		   counter++;
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         
    	
    }


    public static void main(String[] args) {
        //1. Pick a URL from the frontier
    	crawlerRun bwc = new crawlerRun();
    	//links.add("http://www.cpp.edu/");
        bwc.getURLs("http://www.cpp.edu/");
       // bwc.getArticles();
    //	print("http://www.cpp.edu/");
        
        
    }
    public void writeToCsv(String url) throws IOException{
    	FileWriter pw = new FileWriter("report.csv",true); 
    		pw.append(url+",");  
            pw.flush();
            pw.close();
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