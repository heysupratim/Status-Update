package techturning.devs;

//import the twitter api java library



import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusUpdateActivity extends Activity implements OnClickListener {
	
	//our variables that are global in nature ie they are accesible to all the methods
	EditText editStatus;//text field 
	Button buttonUpdate;//the button to update the tweet
	public RequestToken rToken;//to get the rtoken back
	public String oauthVerifier;
	String str= new String();
	public boolean test = true;//this tests whether the authorizations has been done or not
    public  String token;
    public  String secret;

	/** Called when the activity or the app is first started */
	
	@Override //this means that the android application we are creating overrides the oncreate method of the super class
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);//set the basic layout to the screen

		editStatus = (EditText) findViewById(R.id.editStatus);//this loads up the textbox in the memory,links up to the text box -id 
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);//loads up the button in the memory ,links up to the button-id of the button

		buttonUpdate.setOnClickListener(this);//it associates a click action to the button,this is used to tell which button is actually calling this action
		
		
	}

	@Override//this overrides the default onclick method for android ,ie the action to perform when it hears a click 
	public void onClick(View v) {
		
		String status = editStatus.getText().toString();//get the string entered in the text box,additionally convert it to string type to be on a safer side
		str=status;
		
		Log.d("StatusUpdateActivity", "Onclicked with status :" +status);//add the string to the log file for debugging purposes
		
        Twitter twitter = new TwitterFactory().getInstance();//create a twitter instance variable using the class from the imported library
		
		//a try function basically executes the method after the checking that any error or exception has occured or not 
       
        if (test == true) {
        try
		{
        	
        twitter.setOAuthConsumer("c2ogYBGP1bN5LeeaZr3ig","7sTNH7a5i4gejudPI0S9BfiDcPgBS4Oc3uDiUqh3fQ");//it sets the oAuth protocol for the twitter instance we created ,and it connects using the two variables-consumer key and consumer secret 
		String callbackURL = "tt-StatUpdate:///";//its a basic callback value that tells the twitter site ,where to return,this value is declared there in the intent called view in the manifest.xml
		rToken = twitter.getOAuthRequestToken(callbackURL);//this requests the token key -specific to every application 
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(rToken.getAuthenticationURL())));//this starts the intent and sends to the browser called ,the request token and the callback url
		test = false;
		
		 
		}
		catch(IllegalStateException e)
		{
		// access token is already available, or consumer key/secret is not set.
		if(!twitter.getAuthorization().isEnabled()){
		System.out.println("OAuth consumer key/secret is not set.");
		System.exit(-1);
		}
		}

		catch(TwitterException e) 
		{
	     Toast.makeText(StatusUpdateActivity.this, "Network Host not responding",Toast.LENGTH_SHORT).show();//a pop up text box shows up with this message
	     Log.d("StatusUpdateActivity", "Onclicked with status :");
		}
        }//end of if
        else
        {
       
        	try {
    			Twitter tt = new TwitterFactory ().getInstance(); 
    			tt.setOAuthConsumer("c2ogYBGP1bN5LeeaZr3ig","7sTNH7a5i4gejudPI0S9BfiDcPgBS4Oc3uDiUqh3fQ");

    			AccessToken at = tt.getOAuthAccessToken(rToken, oauthVerifier); 
    			token = at.getToken(); 
    			secret = at.getTokenSecret();
    			Log.d("StatusUpdateActivity", "The value of the token recieved :" +token);
    			// Post to twitter.
    			
    			//the following code sets the configuration set for the new twitter instance
    			ConfigurationBuilder confbuilder  = new ConfigurationBuilder();

    			//acc to the token ,secret and the consumerkey and the consumer secret
    			confbuilder.setOAuthAccessToken(token)
    					.setOAuthAccessTokenSecret(secret)
    					.setOAuthConsumerKey("c2ogYBGP1bN5LeeaZr3ig")
    					.setOAuthConsumerSecret("7sTNH7a5i4gejudPI0S9BfiDcPgBS4Oc3uDiUqh3fQ");
    			
    			Twitter t = new TwitterFactory(confbuilder.build()).getInstance();//make the new instance acc to the configuration mentioned above
    			t.updateStatus(str);
    			
    		} catch(Exception e) {
    			try{
    				ConfigurationBuilder confbuilder  = new ConfigurationBuilder();

        			//acc to the token ,secret and the consumerkey and the consumer secret
        			confbuilder.setOAuthAccessToken(token)
        					.setOAuthAccessTokenSecret(secret)
        					.setOAuthConsumerKey("c2ogYBGP1bN5LeeaZr3ig")
        					.setOAuthConsumerSecret("7sTNH7a5i4gejudPI0S9BfiDcPgBS4Oc3uDiUqh3fQ");
        			
        			Twitter t = new TwitterFactory(confbuilder.build()).getInstance();//make the new instance acc to the configuration mentioned above
        			t.updateStatus(str);
    			}
    			catch(Exception e1){
    				Toast.makeText(StatusUpdateActivity.this, "Network Host not responding",Toast.LENGTH_SHORT).show();
    				Log.d("StatusUpdateActivity", "this is the catch call for exception e1");
    			}
    			
    		}
        	
        }
       
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putBoolean("test", false);
	  savedInstanceState.putString("token",token);
	  savedInstanceState.putString("secret", secret);
	  // etc.
	  Log.d("StatusUpdateActivity", "the onSave instance is loaded ");
	  super.onSaveInstanceState(savedInstanceState);
	  
	}
	
        
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d("StatusUpdateActivity", "the onRestore instance stage is loaded");
		super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  token = savedInstanceState.getString("token");
	  secret = savedInstanceState.getString("secret");
	  test = savedInstanceState.getBoolean("test");
	  Log.d("StatusUpdateActivity", "value of the variable test is :"+test);
	  
	}    

	
	@Override//it overrides the on resume method ie what to do when the application screen is returned from  the browser
 	public void onResume() { 
		
		super.onResume(); //ie the StatusUpdateActivity class resumes working
	    Uri uri = getIntent().getData();//gets the intent data

		//if the recieved data is not null then 
	    if (uri != null) { 
		  oauthVerifier = uri.getQueryParameter("oauth_verifier");  //the returned value is stored in the oauthverifier string
		}
	    Log.d("StatusUpdateActivity", "the onResume stage is loaded");
		
		/*try {
			Twitter tt = new TwitterFactory ().getInstance(); 
			tt.setOAuthConsumer("c2ogYBGP1bN5LeeaZr3ig","7sTNH7a5i4gejudPI0S9BfiDcPgBS4Oc3uDiUqh3fQ");

			AccessToken at = tt.getOAuthAccessToken(rToken, oauthVerifier); 
			String token = at.getToken(); 
			String secret = at.getTokenSecret();
			Log.d("StatusUpdateActivity", "Onclicked with status :" +token);
			// Post to twitter.
			
			//the following code sets the configuration set for the new twitter instance
			ConfigurationBuilder confbuilder  = new ConfigurationBuilder();

			//acc to the token ,secret and the consumerkey and the consumer secret
			confbuilder.setOAuthAccessToken(token)
					.setOAuthAccessTokenSecret(secret)
					.setOAuthConsumerKey("c2ogYBGP1bN5LeeaZr3ig")
					.setOAuthConsumerSecret("7sTNH7a5i4gejudPI0S9BfiDcPgBS4Oc3uDiUqh3fQ");
			
			Twitter t = new TwitterFactory(confbuilder.build()).getInstance();//make the new instance acc to the configuration mentioned above
			t.updateStatus("yay!!my first android app ");
			
		} catch(Exception e) {
			Toast.makeText(StatusUpdateActivity.this, "Network Host not responding",Toast.LENGTH_SHORT).show();
		}*/
 	}
	
}