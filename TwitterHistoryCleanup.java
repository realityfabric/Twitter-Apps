package twitterhistorycleanup;

import java.util.List;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterHistoryCleanup {

    public static void main(String[] args) {

    }

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
}