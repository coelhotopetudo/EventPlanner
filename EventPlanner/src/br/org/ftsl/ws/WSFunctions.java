package br.org.ftsl.ws;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WSFunctions {
	
	public Document doc;
	public String url;
	
	public Document getDocFromUrl(String url) {
		this.doc = null;
		this.url = url;
		
		new WSThread().execute(this);
		
		int countMaxWaiting = 0;
		
		try {
			while(this.doc == null){
				countMaxWaiting++;
				if(countMaxWaiting > 100){
					return null;
				}
				Thread.sleep(100);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this.doc;
    }
	
	
	public String getElementValue(Element nameElement){
		return "";
	}
	
	public String getElementPropert(Element nameElement){
		return "";
	}
	
}
