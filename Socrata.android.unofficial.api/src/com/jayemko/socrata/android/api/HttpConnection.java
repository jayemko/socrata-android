package com.jayemko.socrata.android.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Base64;
import android.util.Log;

public class HttpConnection implements Connection{

	 	String domain;
	    String user;
	    String password;
	    String apptoken;
	    static String urls="https://opendata.socrata.com";

	    /**
	     *
	     * @param domain The CNAME of the domain
	     * @param user The user name (email) of the user you're authenticating as
	     * @param password The password for the user.
	     * @param apptoken The apptoken for your application...
	     */
	    public HttpConnection(String domain, String user, String password, String apptoken)
	    {
	        this.domain = domain;
	        this.user = user;
	        this.password = password;
	        this.apptoken = apptoken;
	    }
	    
	    
	    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	                return true;
	        }
	    };
	    /**
	     * Trust every server - dont check for any certificate
	     */
	    private static void trustAllHosts() {
	            // Create a trust manager that does not validate certificate chains
	            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                            return new java.security.cert.X509Certificate[] {};
	                    }
	                    
						public void checkClientTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType)
								throws java.security.cert.CertificateException {
							// TODO Auto-generated method stub
							
						}

						public void checkServerTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType)
								throws java.security.cert.CertificateException {
							// TODO Auto-generated method stub
							
						}
	            } };

	            // Install the all-trusting trust manager
	            try {
	                    SSLContext sc = SSLContext.getInstance("TLS");
	                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
	                    HttpsURLConnection
	                                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
	            } catch (Exception e) {
	                    e.printStackTrace();
	            }

	    }
	    
	    public HttpsURLConnection resource(String url){
	        try {
	        	
	                trustAllHosts();
	                    HttpsURLConnection c = (HttpsURLConnection) new URL(url).openConnection();
	                    c.setHostnameVerifier(DO_NOT_VERIFY);
	                    
	           

				
	        	//HttpURLConnection c=(HttpURLConnection) new URL(url).openConnection();
	        	c.setRequestProperty("Content-Type", "application/json");
	        	c.setRequestProperty("X-Socrata-Host", domain);
	        	
	        	String credEndoced=Base64.encodeToString((user + ":" + password).getBytes(), Base64.DEFAULT);
	        	c.setRequestProperty("Authorization", "Basic " + credEndoced);
	        	c.setRequestProperty("X-App-Token", apptoken);
	        
	        	return c;
	
	        } catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
	    
	    
	    
	
	
	public Response post(String url, String body) {
		JSONObject object;
		
		 
		try {
			/**cast da stringa 'json' a oggetto JSONObject*/
			object = (JSONObject) new JSONTokener(body).nextValue();
			/**creo la query in modo 'ben formato' per JSON*/
			String query = object.getString("query");
			
		
		
		
		
		
		
		HttpsURLConnection conn=this.resource(url);
		
			try {
				
				//HttpResponse response;
				
				conn.setRequestMethod("POST");
				//conn.setRequestProperty("Content-length", "0");
				conn.setUseCaches(false);
				conn.setAllowUserInteraction(false);
				conn.setConnectTimeout(15*1000);
				conn.setReadTimeout(15*1000);
				conn.setDoOutput(true);
				try {
					
					//Workaround android.os.NetworkOnMainThreadException
					/* 
					StrictMode.ThreadPolicy policy = new
					StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					*/
					
					conn.connect();
				
				try {
					byte[] outputBytes = query.getBytes("UTF-8");
					OutputStream os;
					
					try {
						os = conn.getOutputStream();
						os.write(outputBytes);
						os.close();
						
						BufferedReader br =new BufferedReader(new InputStreamReader(conn.getInputStream()));
						StringBuilder sb =new StringBuilder();
						String line;
						while((line=br.readLine())!=null){
							sb.append(line+"\n");
						}
						br.close();
						Response res=new Response(conn.getResponseCode(), sb.toString());
						return res;
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return null;
	}


	
	public Response get(String url) {
		
		HttpURLConnection conn=this.resource(url);
		try {
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-length", "0");
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setConnectTimeout(15*1000);
			conn.setReadTimeout(15*1000);
			try {
				conn.connect();
				BufferedReader br =new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb =new StringBuilder();
				String line;
				while((line=br.readLine())!=null){
					sb.append(line+"\n");
				}
				br.close();
				Response res=new Response(conn.getResponseCode(), sb.toString());
				return res;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	public Response post(String url, MultivaluedMap<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response post(String url, MultivaluedMap<String, String> params,
			String body) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response post(String url, MultivaluedMap<String, String> params,
			File file) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response get(String url, MultivaluedMap<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response put(String url, String body) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response put(String url, MultivaluedMap<String, String> params,
			String body) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response delete(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public static JSONObject getLocationInfo(String address) {
	    StringBuilder stringBuilder = new StringBuilder();
	    try {

	    address = address.replaceAll(" ","%20");    

	    HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    stringBuilder = new StringBuilder();


	        response = client.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuilder.toString());
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	    return jsonObject;
	}
	
	public static JSONObject getLocationInfo(Double lat,Double lng) {
	    StringBuilder stringBuilder = new StringBuilder();
	    try {

	    //address = address.replaceAll(" ","%20");    
	    String latlng=Double.toString(lat)+","+Double.toString(lng);
	    HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?latlng=" + latlng + "&sensor=false");
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    stringBuilder = new StringBuilder();


	        response = client.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuilder.toString());
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	    return jsonObject;
	}
	
	public static String getProviciaByCoord(Double lat,Double lng){
		String provincia = "";
		
		try {
        	
        	
            
            JSONObject jsonObject=HttpConnection.getLocationInfo(lat, lng);
            
			JSONArray array = ((JSONArray)((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONArray("address_components"));
			
			for(int i=0;i<array.length();i++){
				if(array.getJSONObject(i).getString("short_name").length()==2&&array.getJSONObject(i).getString("short_name").equals("IT")==false){
					provincia=array.getJSONObject(i).getString("short_name");
				}
			}
			/*
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(provincia);
			stringBuilder.delete(0, 12);
			provincia=stringBuilder.toString();*/
        } catch (Exception e) {
            e.printStackTrace();

        }
		Log.d("Httpconnection",provincia);
		return provincia;
		
	}



}
