package twitterapps;

import java.util.ArrayList;
import java.util.Collections;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterInteractions {	
	public static ArrayList<Status> getThread (Twitter twitter, long endId, long startId)
			throws TwitterException {
		ArrayList<Status> thread = new ArrayList<>();
		Status s = twitter.showStatus(endId);
		
		thread.add(s);
		
		do {
			s = twitter.showStatus(s.getInReplyToStatusId());
			thread.add(s);
		} while (s.getId() != startId && s.getInReplyToStatusId() != -1);
		
		Collections.reverse(thread);
		return thread;
	}
}
