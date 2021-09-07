import java.util.*;
public class Twitter {

	private static int timeStamp;
	private Map<Integer, User> userMap;
	
	private class Tweet{
		public int id;
		public int time;
		public Tweet next;
		
		public Tweet(int id) {
			this.id = id;
			time = timeStamp++;
			next = null;
		}
	}
	
	public class User{
		public int id;
		public Set<Integer> followed;
		public Tweet tweetHead;
		
		public User(int id) {
			this.id = id;
			followed = new HashSet<>();
			follow(id);
			tweetHead = null;
		}
		
		public void follow(int id) {
			followed.add(id);
		}
		
		public void unfollow(int id) {
			followed.remove(id);
		}
		
		public void post(int id) {
			Tweet tweet = new Tweet(id);
			tweet.next = tweetHead;
			tweetHead = tweet;
		}
	}
	
	public Twitter() {
		userMap = new HashMap<Integer, User>();
	}
	
	public void postTweet(int userID, int tweetID) {
		if(!userMap.containsKey(userID)) {
			User user = new User(userID);
			userMap.put(userID, user);
		}
		userMap.get(userID).post(tweetID);
	}
	
	public List<Integer> getNewsFeed(int userID){
		List<Integer> result = new LinkedList<>();
		if(!userMap.containsKey(userID)) return result;
		Set<Integer> users = userMap.get(userID).followed;
		PriorityQueue<Tweet> maxHeap = new PriorityQueue<>(users.size(), (a, b) -> (b.time - a.time));
		for(int user : users) {
			Tweet getHead = userMap.get(user).tweetHead;
			if(getHead != null) maxHeap.add(getHead);
		}
		int counter = 0;
		while(!maxHeap.isEmpty() && counter < 10) {
			Tweet latestPost = maxHeap.poll();
			result.add(latestPost.id);
			counter += 1;
			if(latestPost.next != null) maxHeap.add(latestPost.next);
		}
		return result;	
	}
	
	public void follow(int followerID, int followeeID) {
		if(!userMap.containsKey(followerID)) {
			User user = new User(followerID);
			userMap.put(followerID, user);
		}
		if(!userMap.containsKey(followeeID)) {
			User user = new User(followeeID);
			userMap.put(followeeID, user);
		}
		userMap.get(followerID).follow(followeeID);
	}
	
	public void unfollow(int followerID, int followeeID) {
		if(!userMap.containsKey(followerID) || followerID == followeeID) return;
		userMap.get(followerID).unfollow(followeeID);
	}
	
//	public static void main(String[] args) {
//		Twitter twitter = new Twitter();
//		twitter.postTweet(1, 5);
//		System.out.println(twitter.getNewsFeed(1));
//		twitter.follow(1, 2);
//		twitter.postTweet(2, 6);
//		System.out.println(twitter.getNewsFeed(1));
//		twitter.unfollow(1, 2);
//		System.out.println(twitter.getNewsFeed(1));
//	}

}
