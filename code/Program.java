/*

Matthew Erxleben

MSCI 240 Assignment 3

2021-11-2021

This program takes 2 CSV's of U.S. federal election (tweets about Donald Trump and Joe Biden) 
and determines the top 20 twitter users in terms of how many tweets they posted about either/both candidate.
This program does this using 3 different implementations (ArrayList, TreeMap, HashMap) to count the tweets, add it the 
respective implementation and sort and output the top 20 twitter users. This program can be used to 
compare the 3 different implementations by changing the MAX_TWEETS variable to different input sizes and by running the 
3 different implementations to see how long they take and their respective outputs.

The input to this program is the 2 data CSV's of U.S. federal election (tweets about Donald Trump and Joe Biden) 
The output of this program is how long it took to determine the tweet counts with the implementation (either ArrayList, TreeMap, HashMap),
the total number of tweets that were counted over all candidates, and the top 20 users sorted by tweet count in descending order.
*/

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.*;

import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Program {
	public static final String TRUMP_TWEETS_FILENAME = "hashtag_donaldtrump.csv";
	public static final String BIDEN_TWEETS_FILENAME = "hashtag_joebiden.csv";

	/**
	 * This constant will allow you to either read the data into local memory
	 * (_your_ code will be faster, but won't work in Codio for the entire dataset)
	 * or to use persistent storage (_your_ code will be slower, but uses a smaller
	 * amount of memory and is possible to complete entirely in Codio).
	 */
	// TODO: adjust this constant as necessary
	public static final boolean READ_INTO_MEMORY = true;

	/**
	 * This constant will allow you to display the header information for the data
	 * files (i.e., what columns have what names). Set this to false before handing
	 * in the assignment.
	 */
	// TODO: adjust this constant as necessary
	public static final boolean SHOW_HEADERS = false;

	/**
	 * When testing, you may want to choose a smaller portion of the dataset. This
	 * number lets you limit it to only the first MAX_ENTRIES. Setting this over 3.5
	 * million will get all the data. IMPORTANT NOTE: this will ONLY have an effect
	 * when READ_INTO_MEMORAY is true
	 */
	// TODO: adjust this constant as necessary
	private static final int MAX_TWEETS = 1000000;
	// public static final int MAX_TWEETS = Integer.MAX_VALUE;

	/**
	 * This constant represents the folder that contains the data. You should not
	 * have to adjust this.
	 */
	public static final String DATA_DIRECTORY = "data";

	/**
	 * This static field manages timing in your code. You can and should reuse it to
	 * time your code.
	 */
	public static Stopwatch stopwatch = new Stopwatch();

	/**
	 * The main method of your program. The first half of this is written for you
	 * (don't adjust this!). Where it says "Add your code here..." you should put
	 * your main program (remember to use methods where appropriate).
	 * 
	 * @param args the arguments to this main program provided on the command line
	 *             (none)
	 * @throws IOException when the data files cannot be read properly
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * We have already setup all the data to make the rest of your assignment easier
		 * for you.
		 * 
		 * All of the data is available in an Iterable object. While you may not know
		 * what an Iterable object is, you've already used these many times before!
		 * ArrayList, LinkedList, the keySet for a HashMap, etc. are all Iterable
		 * objects. To use them, you just need to use a for each loop.
		 * 
		 * IMPORTANT NOTE: the big difference between what you might be used to and the
		 * Iterable is that YOU CAN ONLY ITERATE OVER IT ONE TIME. Once you've done
		 * that, the Iterable object will be "exhausted". Therefore, make sure in that
		 * first pass that you store the information you need in the appropriate Map
		 * object (as per the assignment instructions).
		 */
		Path dir = Paths.get(DATA_DIRECTORY);

		Iterable<CSVRecord> trumpTweets = readData(dir.resolve(TRUMP_TWEETS_FILENAME), "Trump tweets", READ_INTO_MEMORY,
				false);

		Iterable<CSVRecord> bidenTweets = readData(dir.resolve(BIDEN_TWEETS_FILENAME), "Biden tweets", READ_INTO_MEMORY,
				SHOW_HEADERS);

		Iterable<CSVRecord> allTweets = () -> new IteratorChain<>(trumpTweets.iterator(), bidenTweets.iterator());

		/*
		 ************************ IMPORTANT *********************
		 * 
		 * NOTE: You may NOT change anything about these lists! You should only read
		 * stats from index 0 to index list.size() - 1 Do NOT sort the list or change
		 * them in any way.
		 * 
		 ********************************************************/

		// TODO: you can comment/uncomment to test each map
		// implementation.
//        findTopUsingArrayList(allTweets, 20);
//        findTopUsingTreeMap(allTweets, 20);
		findTopUsingHashMap(allTweets, 20);
	}

	// helper method to print out the top n users
	public static void printer(PriorityQueue<TweetCount> pq, int n) {
		System.out.println("The top " + n + " users by number of tweets are:");
		for (int j = 0; j < n; j++) {
			// poll() and remove() method removes and returns the head of the queue. poll
			// returns null when the queue is empty,
			// remove throws exception when queue is empty. Use poll here to remove and go
			// to next element in queue, to iterate through the entire PQ
			System.out.println(pq.remove().toString());
		}
	}

	public static void findTopUsingArrayList(Iterable<CSVRecord> allTweets, int n) {
		// store TweetCount objects in arraylist (objects hold name and counter of
		// number of times they tweeted)

		int numTweets = 0;
		// construct new arraylist of tweetcount objects
		ArrayList<TweetCount> list = new ArrayList<TweetCount>();

		// for each tweet (record is each tweet, which is a CSVRecord object) in the
		// entire CSV (all tweets)
		for (CSVRecord record : allTweets) {
			// check if the name is there in that row of data for the tweet (some are
			// missing)
			if (record.isSet(8)) {

				// intialize boolean variable to false to see if the name is in the arraylist
				// already or not
				boolean inList = false;

				// for each tweetcount object 'tweet' in the list, check if the user is already
				// in the list or not
				for (TweetCount tweet : list) {
					// if the screen name of the current tweet is already in the arraylist of tweet
					// count objects
					if (record.get(8).equals(tweet.getScreenName())) {
						// increment their tweet count by 1
						tweet.increment();
						// set boolean true as it is in the list so it wont run the next if statement
						inList = true;
					}
				}
				// if name wasnt already in the array list, add it and set their tweet count to
				// 1
				if (inList == false) {
					// if name is not already in the arraylist, then create new tweet count object
					// with the current tweets screen name and a count of 1
					TweetCount user = new TweetCount(record.get(8), 1);
					// add that tweetcount object to the arraylist
					list.add(user);
				}
			}
			numTweets++;
		}
		// this time is NOT including the sorting time, only the time to count tweets
		// and add to arraylist
		System.out.println("To count " + numTweets + " tweets with an ArrayList took " + stopwatch.getElapsedSeconds()
				+ " seconds.");

		// construct new priority queue of tweet count objects
		PriorityQueue<TweetCount> pq = new PriorityQueue<>();

		// for loop that goes through the entire list and adds all to the priority queue
		for (int i = 0; i <= list.size() - 1; i++) {
			pq.add(list.get(i));
		}
		// call helper method to print out top 20
		printer(pq, n);
	}

	// helper method to count tweets, add tweets to map, and find the top 20 users
	// this is because the code for find top tree map and hash map were very similar
	// therefore redudant, and solving using a helper method
	public static void mapFindTop(Iterable<CSVRecord> allTweets, int n, Map<String, TweetCount> map, String mapName) {

		int numTweets = 0;

		// for each tweet (record is each tweet, which is a CSVRecord object) in the
		// entire CSV (all tweets)
		for (CSVRecord record : allTweets) {
			// check if the name is there in that row of data for the tweet (some are
			// missing)
			if (record.isSet(8)) {
				// if the name is already in the tree map
				if (map.containsKey(record.get(8))) {
					// already contains this name, then increase the count of the tweet count object
					// in the value for that specific key
					// Map.get(record.get(8)) will return the tweet count object at that key (at
					// that screenname)
					// then incremement the count by 1 at that object using .incremement()
					map.get(record.get(8)).increment();

				} else {
					// if name is not already in the map, then create new tweet count object with
					// the current tweets screen name and a count of 1
					TweetCount user = new TweetCount(record.get(8), 1);
					// put the screenname as the key and the tweetcount object as the value in the
					// map
					map.put(record.get(8), user);
				}
			}
			numTweets++;
		}

		// this time is NOT including the sorting time, only the time to count tweets
		// and add to map
		System.out.println("To count " + numTweets + " tweets with a " + mapName + " took "
				+ stopwatch.getElapsedSeconds() + " seconds.");

		// construct new priority queue of tweet count objects
		PriorityQueue<TweetCount> pq = new PriorityQueue<>();

		// for each name in the maps set of keys (which are the screen names)
		for (String name : map.keySet()) {
			// add the value to the priority queue (value in map is the tweetcount object)
			// at the key (which is the screen name)
			pq.add(map.get(name));
		}
		// call helper method to print out top 20
		printer(pq, n);
	}

	public static void findTopUsingTreeMap(Iterable<CSVRecord> allTweets, int n) {
		// construct tree map with keys as a string to hold the screen name, and value
		// to hold the tweet count object with that screen name
		TreeMap<String, TweetCount> treeMap = new TreeMap<String, TweetCount>();
		// call helper method to count tweets, add tweets to map, and find the top 20
		// users
		mapFindTop(allTweets, n, treeMap, "TreeMap");

	}

	public static void findTopUsingHashMap(Iterable<CSVRecord> allTweets, int n) {
		/// construct hash map with keys as a string to hold the screen name, and value
		/// to hold the tweet count object with that screen name
		HashMap<String, TweetCount> hashMap = new HashMap<String, TweetCount>();
		// call helper method to count tweets, add tweets to map, and find the top 20
		// users
		mapFindTop(allTweets, n, hashMap, "HashMap");
	}

	/**
	 * YOU SHOULD NOT CHANGE THIS METHOD.
	 * 
	 * This method reads the data into an Iterable object.
	 * 
	 * @param path           the path of the file to read from
	 * @param description    the description to use when reporting the type of data
	 * @param readIntoMemory true if the data should be read into memory (it takes a
	 *                       lot of memory!), false if the Iterable object should
	 *                       just go through the file.
	 * @param printHeader    true if this method should print the header information
	 *                       (i.e., which column has what name).
	 * @return an Iterable object with all of the data in CSVRecord objects
	 * @throws IOException if the file could not be read
	 */
	public static Iterable<CSVRecord> readData(Path path, String description, boolean readIntoMemory,
			boolean printHeader) throws IOException {
		stopwatch.reset();
		stopwatch.start();

		CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader(path.toFile()));

		Map<String, Integer> headerMap = parser.getHeaderMap();
		Iterable<CSVRecord> iterable = () -> parser.iterator();

		if (readIntoMemory) {
			List<CSVRecord> list2 = StreamSupport.stream(iterable.spliterator(), false).limit(MAX_TWEETS)
					.collect(Collectors.toList());

			System.out.printf("Finished reading %,d %s in %f seconds.\n", list2.size(), description,
					stopwatch.getElapsedSeconds());

			iterable = list2;
		}

		if (printHeader) {
			System.out.println("Data available:");
			for (String key : headerMap.keySet()) {
				int value = headerMap.get(key);
				System.out.printf("\t%d = %s\n", value, key);
			}
		}

		return iterable;
	}
}
