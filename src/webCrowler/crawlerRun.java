package webCrowler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.agogs.languagelayer.api.APIConsumer;
import com.github.agogs.languagelayer.api.LanguageLayerAPIConsumer;
import com.github.agogs.languagelayer.model.APIResult;
import com.github.agogs.languagelayer.model.Batch;
import com.github.agogs.languagelayer.model.QueryParams;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.language.LanguageIdentifier;

import java.util.Scanner;
import java.util.Set;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import org.jsoup.Jsoup;
public class crawlerRun {

	private static HashSet<String> links;
	private static HashMap<String,Integer> outPut;
	private static Map<String, List<String>> inCome;
    private List<List<String>> articles;
    static int counter =1;
    static int limit =0;

    public crawlerRun() {
        links = new HashSet<>();
        articles = new ArrayList<>();
        outPut = new HashMap<String, Integer>();
        inCome = new HashMap<String, List<String>>();
        
    }

    public void getURLs(String URL) throws IOException{
    	StringBuilder sb = new StringBuilder();
    	int size;
    	
//    	if(inCome.containsKey(URL)&& limit < 20) {
//	    	Integer count = inCome.get(URL);
//	    	if (count == null) {
//	    	    inCome.put(URL, 1);
//	    	}
//	    	else {
//	    	    inCome.put(URL, count + 1);
//	    	}
//    	}else if( limit < 20)
//    	{
//    		inCome.put(URL, 1);
//    	}
    	
        // Check if you have already crawled the URLs
        if (!links.contains(URL) && URL != ""  && limit < 20) {
            try {
            Document doc = Jsoup.connect(URL).ignoreContentType(true).userAgent("Mozilla").get();
            	String title = doc.title();
            	Elements e=doc.select("p");
            	String text = e.text();
            	
//            	for (Element el : e.subList(0, Math.min(1, e.size()))) {
//            		if(el.text()  != null && !el.text().isEmpty()){
//            	    //System.out.println(el.text());
//            			if(!lan.equals("cn"))
//            				eng = checkLan(el.text());
//            	   
//            		}
//           	}
            	
            	
               	 // If not add it to the index
           		links.add(URL);
               	//tester
                   
               	limit++;
             //  	getHTML(URL);           
               
         //  }
               // Fetch the HTML code
               // extract links to other URLs
               Elements linksOnPage = doc.select("a[href]");
               size = linksOnPage.size();
              // System.out.println(URL+" OutLink: "+size); 
//               System.out.println(URL);
//               System.out.println("URL Num: "+size);
               writeToCsv(URL+","+size);
               outPut.put(URL, size);
               //5. For each extracted URL... go back to Step 4.
               for (Element page : linksOnPage) {
                   getURLs(page.attr("abs:href"));
                   if(!inCome.containsKey(page.attr("abs:href"))) {
						
                	   inCome.put(page.attr("abs:href"), new ArrayList<String>());
					}
				
                   inCome.get(page.attr("abs:href")).add(URL);
               }
               	
               	
               	
               
            	
//            	
//            if(lan.equals("es") && checkLan(title).equals(lan)&& title != null && !title.isEmpty()) {
//                // If not add it to the index
//            		links.add(URL);
//                	//tester
//                    System.out.println(URL); 
//                	limit++;
//                	getHTML(URL);           
//                
//          //  }
//                // Fetch the HTML code
//                // extract links to other URLs
//                Elements linksOnPage = doc.select("a[href]");
//                size = linksOnPage.size();
////                System.out.println(URL);
////                System.out.println("URL Num: "+size);
//                writeToCsv(URL+","+size);
//                //5. For each extracted URL... go back to Step 4.
//                for (Element page : linksOnPage) {
//                    getURLs(page.attr("abs:href"),lan);
//                }
//            }
//            else if(lan.equals("cn") && chineseC(title)=="cn") {
//            	 // If not add it to the index
//        		links.add(URL);
//            	//tester
//                System.out.println(URL); 
//            	limit++;
//            	getHTML(URL);           
//            
//      //  }
//            // Fetch the HTML code
//            // extract links to other URLs
//            Elements linksOnPage = doc.select("a[href]");
//            size = linksOnPage.size();
////            System.out.println(URL);
////            System.out.println("URL Num: "+size);
//            writeToCsv(URL+","+size);
//            //5. For each extracted URL... go back to Step 4.
//            for (Element page : linksOnPage) {
//                getURLs(page.attr("abs:href"),lan);
//            }
//            	
//            	
//            }
            
            	
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
    }
        
       
        

    } 
//    public static void getHTML(String url) {
//    	 String webPage = url;
//         String html;
//         StringBuilder filename = new StringBuilder() ;
//         filename.append("repository\\");
//         filename.append(counter);
//         filename.append (".txt");
//		try {
//			
//			Document doc = Jsoup.connect(webPage).ignoreContentType(true).userAgent("Mozilla").get();
//			doc.getElementsByTag("style").remove();
//			doc.removeAttr("style");
//			doc.select("img").remove();
//			doc.select("[style]").removeAttr("style");
//			String title = doc.title();
//			html = doc.html();	
//			//tester
//			//chineseC(title);
//			
//			try { 
//				if(doc.toString() != null && !doc.toString().isEmpty()){
//					Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename.toString ()), "utf-8")) ;
//					writer.write(doc.toString());
//					counter++;
//				}	
//			}catch (IOException ex) {
//				ex.printStackTrace();
//			}
//			//System.out.print(languageCheck(title));
//		
//		}
//		 catch (IOException ex) {
//			ex.printStackTrace();
//			System.out.print(url+"is wrong");
//		 } 
//    }
    


    public static void main(String[] args) throws IOException {
//    	 Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//    	    System.out.println("Please Enter seed WebSite: ");
//    	    String url = myObj.nextLine();  // Read user input
//    	    System.out.println("Please Enter Language code(en,cn,es): ");
//    	    String lan = myObj.nextLine();  // Read user input
    	crawlerRun bwc = new crawlerRun(); 	
 //       bwc.getURLs("http://"+url,lan);
   
    	
    	bwc.getURLs("https://www.cpp.edu");
//    	System.out.println("OutPut: ");
//    	for (Map.Entry<String, Integer> entry : outPut.entrySet()) {
//    	    System.out.println(entry.getKey()+" : "+entry.getValue());
//    	}
    	//removeNoneOut(inCome,outPut);
    	printHash(inCome);
    	PRcalculator(inCome,outPut);
     //    printHash(inCome);
        
    }
    public static void PRcalculator(Map<String, List<String>> inCome,HashMap<String,  Integer> outPut) {
    	HashMap<String,Double> pr = new HashMap<String, Double>();
    	double size = outPut.size();
    	
    	double iPR = 1/size;
    	for(HashMap.Entry<String, Integer> me :outPut.entrySet()) {
    		String key = me.getKey();
    		pr.put(key, iPR);
    	}
    	
    
    	
//    		for(HashMap.Entry<String, Double> me :pr.entrySet()) {
//        		String key = me.getKey();
//        		double temSum = 0;
//        		List<String> tem = inCome.get(key);
//        	//	tem.removeAll(Collections.singleton(null));
////        		int index = tem.size() - 1; 
////        		  
////                // Delete last element by passing index 
////                tem.remove(index); 
//        		
//        		System.out.println(tem);
//        		System.out.println();
////        		for (String s : tem) { 
////        	           temSum = temSum+pr.get(s)/ outPut.get(s);	
////        	      }
////        		pr.put(key,temSum);
//      
//        		
//        		
//        		
//        	}
//    	
    	//tester
    	for(HashMap.Entry<String, Double> me :pr.entrySet()) {
    		String key = me.getKey();
    		System.out.println(key);
    	}
    	
    	
    	
    	
    	
    }
    
    public static void printHash(Map<String, List<String>> web_record) throws IOException{
    	
    	
    	for(Map.Entry<String, List<String>> me : web_record.entrySet()) {
    		
    		String key = me.getKey();
    		
    		
    		List<String> valueList = me.getValue();
    		Set<String> set = new HashSet<>(valueList);
    		valueList.clear();
    		valueList.addAll(set);
    		me.setValue(valueList);
    	//	System.out.println("WebKey is: "+key);
    		
    		for(String s : valueList) {
    			
    		//	System.out.println(s);
    		}
    		
    		
    		
    		
    	}

           
    }
    
    public static void removeNoneOut(Map<String, List<String>> inCome,HashMap<String,  Integer> outPut) {
    	
    	for(HashMap.Entry<String, Integer> me :outPut.entrySet()) {
    		String key = me.getKey();
    		if(outPut.get(key) == 0) {
    			inCome.remove(key);
    			System.out.println(key+" has been removed.");
    		}
    		
    		
    		
    		
    	}
    	
    	
    }
    
    
    public void writeToCsv(String url) throws IOException{
    	FileWriter pw = new FileWriter("report.csv",true); 
    		pw.append(url+","+"\n");  
            pw.flush();
            pw.close();
    }
//    public static String languageCheck(String contain) throws IOException {
//    	LanguageIdentifier identifier = new LanguageIdentifier(contain);
//        String language = identifier.getLanguage();
//        System.out.println("Language of the given content is : " + language);
//        return language;
//    }
//    public static String chineseC(String s) {
//    	String language = "";
//    	 for (int i = 0; i < s.length(); ) {
//    	        int codepoint = s.codePointAt(i);
//    	        i += Character.charCount(codepoint);
//    	        if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
//    	        	language = "cn";
//    	        	//System.out.println(language);
//    	            return language;
//    	        }
//    	    }
//    	    return language;
//    }
//    
//    
//    public static String checkLan(String contain) throws IOException {
//    	APIConsumer con = new LanguageLayerAPIConsumer("http://api.languagelayer.com/", "7ec8de2ed45584cbb1ea0fbf6c6f5ae0");
//        QueryParams params = new QueryParams().query(contain);
//        APIResult result = con.detect(params);
//        String language="no";
//        try{
//         language = new ObjectMapper().writeValueAsString(result.getResults().get(0).getLanguageCode());
////        String language = new ObjectMapper().writeValueAsString(result.getResults());
////        System.out.println(language);
//        }
//        catch ( Exception e) {
//        	
//        }
//        language = language.replaceAll("^\"+|\"+$", "");
//       // System.out.println(language.equals("en"));
//        return language ;
//    }
    
    

}