package service;



public class TextAnalyticsService {
	
	static String subscriptionKey = System.getenv("COMPUTER_VISION_SUBSCRIPTION_KEY");
    static String endpoint = System.getenv("COMPUTER_VISION_ENDPOINT");
    static String path = endpoint + "/text/analytics/v2.1/languages"; //Request endpoint
	
	public void analyzeText(String url) {
		
//		OkHttpClient httpClient= new OkHttpClient.Builder().build();
		
		
		
	}

}
