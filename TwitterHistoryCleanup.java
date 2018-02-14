package twitterhistorycleanup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 *
 * @author realityfabric
 */
public class TwitterHistoryCleanup {

    /**
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

    }

    /**
     *
     * @param consumerKey Consumer Key generated by the Twitter API.
     * @param consumerSecret Consumer Secret generated by the Twitter API.
     * @param accessToken Access Token generated by the Twitter API.
     * @param accessTokenSecret Access Token Secret generated by the Twitter API.
     * @return an initialized and authenticated Twitter object
     */
    public static Twitter createTwitter (String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        Twitter twitter = new TwitterFactory().getInstance();

        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

        return twitter;
    }

    /**
     *
     * @param filename The name of a file which contains a serialized ApiKeys object
     * @return an initialized and authenticated Twitter object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Twitter createTwitter (String filename) throws IOException, ClassNotFoundException {
        Twitter twitter = new TwitterFactory().getInstance();

        ApiKeys apiKeys = ApiKeys.readApiKeysFromFile(filename);

        apiKeys.setOAuthConsumer(twitter);
        apiKeys.setOAuthAccessToken(twitter);

        return twitter;
    }

    /**
     *
     * @param apiKeys an ApiKeys object
     * @return an initialized and authenticated Twitter object
     */
    public static Twitter createTwitter (ApiKeys apiKeys) {
        Twitter twitter = new TwitterFactory().getInstance();

        apiKeys.setOAuthConsumer(twitter);
        apiKeys.setOAuthAccessToken(twitter);

        return twitter;
    }

    /**
     *
     * @param twitter an initialized and authenticated Twitter object
     * @return A List of all of the user's tweets.
     * @throws TwitterException
     */
    public static List<Status> getAllTweets (Twitter twitter) throws TwitterException {
        // this list will hold all of the users tweets that can be gathered
        List<Status> tweets;

        // this list will hold the most recently gathered tweets
        List<Status> tweetsPage;

        Paging paging = new Paging();
        paging.count(200);

        tweetsPage = twitter.getUserTimeline(paging);
        tweets = tweetsPage;

        while (tweetsPage.size() > 0) {
            // ID of last tweet in tweetsPage list
            // minus 1 to prevent including the oldest tweet from the last page
            long maxId = tweetsPage.get(tweetsPage.size() - 1).getId() - 1;
            paging.maxId(maxId);

            tweetsPage = twitter.getUserTimeline(paging);
            tweets.addAll(tweetsPage);
        }

        return tweets;
    }

    /**
     *
     * @param tweets a list of tweets
     * @param date a date which all returned tweets will be created before
     * @return a list of tweets which were created before the date used as an argument
     */
    public static List<Status> getTweetsBeforeDate (List<Status> tweets, Date date) {
        List<Status> tweetsBeforeDate = new ArrayList();
        for (int i = 0; i < tweets.size(); i++) {
            if (tweets.get(i).getCreatedAt().before(date)) {
                tweetsBeforeDate.add(tweets.get(i));
            }
        }

        return tweetsBeforeDate;
    }

    /**
     *
     * @param tweets a list of tweets
     * @param date a date which all returned tweets will be created after
     * @return a list of tweets which were created after the date used as an argument
     */
    public static List<Status> getTweetsAfterDate (List<Status> tweets, Date date) {
        List<Status> tweetsAfterDate = new ArrayList();
        for (int i = 0; i < tweets.size(); i++) {
            if (tweets.get(i).getCreatedAt().after(date)) {
                tweetsAfterDate.add(tweets.get(i));
            }
        }

        return tweetsAfterDate;
    }

    public static Date calendarToDate (Calendar calendar) {
        return new Date(calendar.getTimeInMillis());
    }

    public static List<Status> deleteTweets (Twitter twitter, List<Status> tweets) {
        List<Status> errorTweets = new ArrayList<>();

        int i = 0;
        while (i < tweets.size()) {
            try {
                System.out.println(twitter.destroyStatus(tweets.get(i).getId()));
            } catch (TwitterException e) {
                System.out.println(e.getMessage());
                errorTweets.add(tweets.get(i));
            } finally {
                i++;
            }
        }

        return errorTweets;
    }
}