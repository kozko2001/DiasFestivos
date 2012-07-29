package net.coscolla.android.modules;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Http {

	
	public static Errorable<String> Get(String url)
	{
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response;
		try {
			URI uri = URI.create(url);
			response = httpclient.execute(new HttpGet(uri));
		
		    StatusLine statusLine = response.getStatusLine();
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();
		        
		        return new Errorable<String>(responseString);
		    } else{
		    	response.getEntity().getContent().close();
		    	throw new IOException(statusLine.getReasonPhrase());
		    }
		} catch (ClientProtocolException e) {
			return new Errorable<String>(e);
		} catch (IOException e) {
			return new Errorable<String>(e);
		}
	}
}
