package com.atos.coap;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ResourceAttributes;
import org.json.*;


public class Server extends CoapServer {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }

    public Server() throws SocketException {
    	add(new TemperatureResource());
    	add(new HumidityResource());
    	add(new PaSoSResource());
    	
    }
    String userpass = "adrian.carpintero@atos.net" + ":" + "thingworx-atos";
	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
	String payload = "";
	

//TEMPERATURE RESOURCE
    class TemperatureResource extends CoapResource {
    	String payload = "";
    	
    	public void sendTemperatureResource(String dato) throws IOException {
    		String userpass = "adrian.carpintero@atos.net" + ":" + "thingworx-atos";
    		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
    		JSONObject json = new JSONObject();
    		json.put("data", dato);
    		
    		//conection
    		URL url = new URL ("http://35.216.198.132:8080/Thingworx/Things/CoapTest/Services/UpdateValue");
    		HttpURLConnection con = (HttpURLConnection)url.openConnection();
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Authorization", basicAuth);
    		con.setRequestProperty("Content-Type", "application/json; utf-8");
    		con.setDoOutput(true); 		
    		//System.out.println(con.getResponseCode() + " " + con.getResponseMessage());
    		
    		//POST DATA
    		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    		try {
    		    HttpPost request = new HttpPost("http://35.216.198.132:8080/Thingworx/Things/CoapTest/Services/UpdateValue");
    		    StringEntity params = new StringEntity(json.toString(),ContentType.APPLICATION_JSON);
    		    request.addHeader("content-type", "application/json");
    		    request.addHeader("authorization", basicAuth);
    		    request.setEntity(params);
    		    
    		    httpClient.execute(request);
    		// handle response here...
    		} catch (Exception ex) {
    		    // handle exception here
    		} finally {
    		    httpClient.close();
    		}
    		
    		//DISCONNECT
    		con.disconnect();
    		
    	}
    	
        public TemperatureResource() {
            super("DeviceTemperature");
            System.out.println(payload);
        }
        public void handlePOST(CoapExchange exchange) {
            System.out.println(exchange.getRequestText());
            payload = exchange.getRequestText();
            try {
            	sendTemperatureResource(payload);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            exchange.respond("POST_REQUEST_SUCCESS");
        }
        public void handleGET(CoapExchange exchange) {
            exchange.respond("GET_REQUEST_SUCCESS");
        }
    }
//HUMIDITY RESOURCE
    class HumidityResource extends CoapResource {
    	String payload = "";
        public HumidityResource() {
            super("DeviceHumidity");
            getAttributes().setTitle("Publish DeviceHumidity");
        }
        public void handlePOST(CoapExchange exchange) {
        	System.out.println(exchange.getRequestText());
            payload = exchange.getRequestText();
            try {
            	sendHumidityResource(payload);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(exchange.getRequestText());
            exchange.respond("POST_REQUEST_SUCCESS");
        }
        public void handleGET(CoapExchange exchange) {
            exchange.respond("GET_REQUEST_SUCCESS");
        }
        public void sendHumidityResource(String dato) throws IOException {
    		String userpass = "adrian.carpintero@atos.net" + ":" + "thingworx-atos";
    		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
    	
    		JSONObject json = new JSONObject();
    		json.put("data", dato);
    		//conection
    		URL url = new URL ("http://35.216.198.132:8080/Thingworx/Things/CoapTest/Services/UpdateValue");
    		HttpURLConnection con = (HttpURLConnection)url.openConnection();
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Authorization", basicAuth);
    		con.setRequestProperty("Accept", "application/json");
    		con.setRequestProperty("Content-Type", "application/json; utf-8");
    		con.setDoOutput(true); 		
    		//System.out.println(con.getResponseCode() + " " + con.getResponseMessage());
    		
    		//POST DATA
    		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    		try {
    		    HttpPost request = new HttpPost("http://35.216.198.132:8080/Thingworx/Things/CoapTest/Services/UpdateValue");
    		    StringEntity params = new StringEntity(json.toString());
    		    request.addHeader("content-type", "application/json");
    		    request.addHeader("accept", "application/json");
    		    request.addHeader("authorization", basicAuth);
    		    request.setEntity(params);
    		    
    		    httpClient.execute(request);
    		// handle response here...
    		} catch (Exception ex) {
    		    // handle exception here
    		} finally {
    		    httpClient.close();
    		}
    		
    		//DISCONNECT
    		con.disconnect();
    		
    	}     
    }
//PaSoS RESOURCE
    class PaSoSResource extends CoapResource {
    	String payload = "";
        public PaSoSResource() {
            super("CruzRoja_ProtocoloPaSoS");
            getAttributes().setTitle("Publish Protocolo_PaSoS");
        }
        public void handlePOST(CoapExchange exchange) {
        	System.out.println(exchange.getRequestText());
            payload = exchange.getRequestText();
            try {
            	postProtocoloPaSoSResource(payload);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(exchange.getRequestText());
            exchange.respond("POST_REQUEST_SUCCESS");
        }
        public void handleGET(CoapExchange exchange) {
            exchange.respond("GET_REQUEST_SUCCESS");
        }
        public void postProtocoloPaSoSResource(String dato) throws IOException {
    		String userpass = "adrian.carpintero@atos.net" + ":" + "thingworx-atos";
    		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
    	
    		JSONObject json = new JSONObject();
    		json.put("PaSoS", dato);
    		//conection
    		URL url = new URL ("http://35.216.198.132:8080/Thingworx/Things/CoapTest/Services/UpdatePaSoS");
    		HttpURLConnection con = (HttpURLConnection)url.openConnection();
    		con.setRequestMethod("POST");
    		con.setRequestProperty("Authorization", basicAuth);
    		con.setRequestProperty("Accept", "application/json");
    		con.setRequestProperty("Content-Type", "application/json; utf-8");
    		con.setDoOutput(true); 		
    		//System.out.println(con.getResponseCode() + " " + con.getResponseMessage());
    		
    		//POST DATA
    		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    		try {
    		    HttpPost request = new HttpPost("http://35.216.198.132:8080/Thingworx/Things/CoapTest/Services/UpdatePaSoS");
    		    StringEntity params = new StringEntity(json.toString());
    		    request.addHeader("content-type", "application/json");
    		    request.addHeader("accept", "application/json");
    		    request.addHeader("authorization", basicAuth);
    		    request.setEntity(params);
    		    
    		    httpClient.execute(request);
    		// handle response here...
    		} catch (Exception ex) {
    		    // handle exception here
    		} finally {
    		    httpClient.close();
    		}
    		
    		//DISCONNECT
    		con.disconnect();
    		
    	}
   
    }
    
}