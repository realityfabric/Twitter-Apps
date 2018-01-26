package twitterhistorycleanup;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ApiKeys implements Serializable {
    final private String consumerKey;
    final private String consumerSecret;
    final private String accessToken;
    final private String accessTokenSecret;
    
    public ApiKeys (String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }
    
    public Twitter setOAuthConsumer (Twitter twitter) {
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        
        return twitter;
    }
    
    public Twitter setOAuthAccessToken (Twitter twitter) {
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
        
        return twitter;
    }
    
    public ApiKeys readApiKeysFromFile (String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename));
        
        ApiKeys apikeys = (ApiKeys) objectInputStream.readObject();
        
        objectInputStream.close();
        
        return apikeys;
    }
    
    public void writeApiKeysToFile (String filename) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename));
        
        objectOutputStream.writeObject(this);
        
        objectOutputStream.close();
    }
}
