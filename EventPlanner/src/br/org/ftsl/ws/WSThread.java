package br.org.ftsl.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.os.AsyncTask;

public class WSThread extends AsyncTask<WSFunctions ,Void,Void>
{

	
	Document processInfo(String url, Document doc){
		HttpGet httpRequest = new HttpGet(url);
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response; 
	
		try {
			response = httpclient.execute(httpRequest);
			HttpEntity httpEntity = response.getEntity();
			String xml = EntityUtils.toString(httpEntity);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            doc.getDocumentElement().normalize();
            
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	@Override
	protected Void doInBackground(WSFunctions... args) {
		WSFunctions wf = args[0];
		wf.doc = processInfo(wf.url, wf.doc);
		return null;
	}
};
