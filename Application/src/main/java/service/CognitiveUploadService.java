package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import utility.UploadUtils;

import java.net.URI;
import java.util.HashMap;

import javax.servlet.http.Part;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@Service
public class CognitiveUploadService {
	
		
		//TODO: add these to application.config
		private final String subscriptionKey;
		private final String endpoint;

	    //private static final String uriBase = endpoint + "vision/v2.1/analyze";
	  
		@Autowired
		public CognitiveUploadService(Environment env){
			subscriptionKey = env.getProperty("azure.computer-vision-subscription-key");
			endpoint = env.getProperty("azure.computer-vision-endpoint");
		}
	    
	    public HashMap<String,String> getMetadata(Part file,String url) {
	        String imageToAnalyze = url;
	        
	        //Detect file type and select endpoint
	        String fileTypeEndpoint = getEndpoint(file);
	        if(fileTypeEndpoint==null) //No type detected
	        	return null; 
	        String uriBase = endpoint + fileTypeEndpoint;
	       
	        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	        try {
	            URIBuilder builder = new URIBuilder(uriBase);

	            // Request parameters. All of them are optional.
	            builder.setParameter("visualFeatures", "Categories,Description,Color");
	            builder.setParameter("language", "en");

	            // Prepare the URI for the REST API method.
	            URI uri = builder.build();
	            HttpPost request = new HttpPost(uri);

	            // Request headers.
	            request.setHeader("Content-Type", "application/json");
	            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

	            // Request body.
	            StringEntity requestEntity =
	                    new StringEntity("{\"url\":\"" + imageToAnalyze + "\"}");
	            request.setEntity(requestEntity);

	            // Call the REST API method and get the response entity.
	            HttpResponse response = httpClient.execute(request);
	            HttpEntity entity = response.getEntity();

	            if (entity != null) {
	                // Format and display the JSON response.
	                String jsonString = EntityUtils.toString(entity);
	                JSONObject json = new JSONObject(jsonString);
	                
	                HashMap<String, String> metadata= new HashMap<String, String>();
	                
	                StringBuilder tags = new StringBuilder();
	                for( Object tag : json.getJSONObject("description")
	                		.getJSONArray("tags").toList())
	                	tags.append(tag+",");
	                tags.deleteCharAt(tags.length()-1); //Remove last comma
	                metadata.put("Tags", tags.toString());
	                
	                
	                System.out.println("REST Response:\n");
	                System.out.println(json.toString(2));
	                
	                return metadata;
	            }
	        } catch (Exception e) {
	            // Display error message.
	            System.out.println(e.getMessage());
	        }
	        
	        return null;
	    }
	    
	    
	    private String getEndpoint(Part part) {
	    	String fileType=part.getSubmittedFileName().substring(part.getSubmittedFileName().lastIndexOf('.')+1); //Get file type
	    	
	    	if(fileType==null) //No file type detected
	    		return null;
	    	if(fileType.equalsIgnoreCase("jpg") || fileType.equalsIgnoreCase("png") || fileType.equalsIgnoreCase("gif"))
	    		return UploadUtils.imageEndpoint;
	    	return null; //If no type are detected
	    }
	    
	}
	

