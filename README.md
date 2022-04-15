# Tweet-Analyzer
MSCI 240 – Data Structures &amp; Algorithms: Tweet Analyzer Assignment

This program takes 2 CSV's of U.S. federal election data (tweets about Donald Trump and Joe Biden) and determines the top 20 twitter users in terms of how many tweets they posted about either/both candidate. This program does this using 3 different implementations (ArrayList, TreeMap, HashMap) to count the tweets, add it the respective implementation and sort and output the top 20 twitter users. The program obtains the top 20 twitter users by using a Priority Queue with a Max Heap. This program can be used to compare the 3 different implementations by changing the MAX_TWEETS variable to different input sizes (up to 1000000 tweets each) and by running the 3 different implementations to see how long they take and their respective outputs.


## 1.	Introduction:
The purpose of this report is to breakdown the 3 implementations (ArrayList, TreeMap, HashMap) and how they count the tweets and add it to the respective list or map. These implementations will be compared by analyzing the code to see the worst-case time complexity of each method by looking at operations, loops, and using the manual. Empirical data with a set of different n values and multiple trails is used to test the run time of the code, which will show what implementation is the fastest. The output of the program is consistent amongst all 3 implementations and will be displayed in this report. This will also breakdown the sorting and output of the top 20 twitter users. The sorting uses a priority queue with a max heap; therefore, the root of the queue will be the TweetCount object with the highest count of tweets. This method is used over other sorting methods based on the application of max heap towards this problem, as we want access to the top number of tweets. Then the choice for what the best implementation is a HashMap for this problem, based on the speed, time complexity, and an analysis of the Tree Map vs Hash Map vs ArrayList behaviour. The issues that were encountered will also be discussed, explaining how they were solved when completing this project.

## 2.	Growth rate analysis (analytical and empirical): 
### a. Worst-case growth rate:
In the TweetCount class, a comparable was implemented as well as Override the compareTo method to compare tweet count objects based on integer comparison. This allows the priority queue to compare tweetcount objects based on priority. Furthermore, the toString method was overridden print out “’name’ had ‘# of tweets’ tweets” whenever a TweetCount object is printed to a string, such as when it is removed from the Priority Queue and outputted like in my implementation. 
The priority queue uses a max heap and therefore its insertion cost will be O(log n), which was obtained from the manual. This is a lower time complexity than all implementations’ worst-cases, therefore it will not affect the worst case of any implementations. 
#### ArrayList:
Worst-case: O(n^2) or quadratic
This method uses an ArrayList, and includes the operation .get, .contains, etc is all constant O(1) with an ArrayList, however these are in a double for loop. Therefore, this makes the worst case time complexity O(n^2). This was obtained through my own analysis of my code.
#### HashMap:
Worst-case: O(n log n) or linearithmic
This method uses a HashMap, and is using a self-balancing binary search tree which will use a worst case time complexity of O(log n), this was obtained using the manual. Furthermore, we have this in a for loop, therefore the time complexity will be multiplied by n making the worst case time complexity O(n log n). This was obtained through my own analysis of my code.
#### TreeMap: 
Worst-case: O(n log n) or linearithmic
This method uses a TreeMap and includes the operation containsKey which is the highest time complexity of operations in this method, which is O(log n). Furthermore, we have that operation in a for loop, therefore the time complexity will be multiplied by n making the worst case time complexity O(n log n). This was obtained through my own analysis of my code.

### b. Data:
Collecting and reporting on data based on empirical time to count and the input size (# of tweets) is essential to analyze each implementation. To obtain accurate results, I used the stopwatch.java class, using the getElapsedSeconds method to obtain the time the implementation takes to count the tweets and put them into the ArrayList/TreeMap/HashMap given a certain # of tweets. The time it takes for the Priority Queue to sort the ArrayList/TreeMap/HashMap and print out the top 20 is not included in the trial times. This is because this will not change the comparison between each implementation, and this is because the ArrayList, TreeMap, and HashMap all are unordered based on the priority of the priority queue (which is by highest number of tweet count). This is why I did not include Priority Queue sorting and printing out the top 20 in the times for data points to compare the ArrayList, TreeMap, and HashMap and instead just compared time to count tweets and add to ArrayList, TreeMap, or HashMap.
During the collection of data, I took 3 trails of each input and took average of the 3 to use as the data points to obtain accurate results. I also used increasing size of number of tweets (n) to show a trend the results. 

#### ArrayList:
![image](https://user-images.githubusercontent.com/77474191/163632076-480ce72e-cb82-4e3e-9899-3a8f7bbb1ce0.png)

#### TreeMap:
![image](https://user-images.githubusercontent.com/77474191/163632092-023f0877-fe62-4826-8ee1-6e0d28e59514.png)


#### HashMap:
![image](https://user-images.githubusercontent.com/77474191/163632109-c5dc5ebc-858b-415e-af0c-163790fd0fd8.png)


### c. Output:
The output is the same tweet counts and top 20 users for the ArrayList, TreeMap, and HashMap. When running the program with a HashMap and 2000 total tweets (MAX_TWEETS = 1000) the output of the program is:
 
## 3.	Discussion of performance + choice:
Looking at the worst-case time complexity and the empirical results from timing the outputs, the ArrayList implementation is the worst as it has O(n^2) worst case time complexity and it takes the longest time from the empirical results, measured in seconds. Looking at the TreeMap and HashMap, they both have the same worst case time complexity of O(n^2). However, HashMap has a faster time via the empirical data and this is because the TreeMap automatically sorts due to natural ordering of the keys, which in this case is alphabetically of the user ID’s. TreeMap has to do extra sorting which isn’t necessary in this case and makes its runtime longer. Therefore, the HashMap is the best choice and is the fastest.

## 4.	Top 20 Discussion:
The method to determine the top 20 users is using a Priority Queue with a max heap. This means that the root of the Priority Queue is the max priority of the entire queue (e.g. 20, 5, 10 in a priority queue with max heap, the root will be 20). This choice was due to the fact that because we want to sort the tweets by the priority of the number of tweets that user has sent, we can create a priority queue that compares based on the count of number of tweets that user has sent. This is through implementing a comparable to the TweetCount class and overriding the compareTo method to compare tweet count objects based on integer comparison and by swapping the two parameters in the compareTo method. This allows the priority queue to compare TweetCount objects based on the priority of max number of tweets at its root. We only have access to the top of the queue, therefore we can remove the root of the queue (which will be the max) and print it out, iterating through this 20 times to print out the top of queue, therefore printing out the top 20 users. This is better than a min heap as min heap will have access to the minimum at its root, which wont be useful as we want access to the max at the root. Chose a priority queue over a Binary Search Tree as it uses less space. A naive priority queue would be inefficient in this case as we only need access to the top elements, and with max being the priority in the queue we can just remove elements at the top of the queue rather than doing a linear scan to find an items position and then removing it. 

## 5.	Discussion of Issues:
There were some issues that I encountered when completing this project. The main issue that was encountered was the issue of missing data in the CSV’s. This was causing index out of bounds exception errors on for loops, which would not let my program compile. To solve this problem, I added an if statement (record.isSet(8)) to the start of my methods to check if there is a screenName for the tweet or not, which solves the issue as it wont bring in rows with empty data from the CSV. The other issue I encountered while making this code was making redundant code. I saw this when printing out the top 20 users, as I had written the same code 3 times for the 3 different methods for the implementations. I made a helper method printer to solve this problem. There was also redundancy with TreeMap and HashMap methods, as the code for both these methods was the same but with either a TreeMap or a HashMap. To solve this problem, I implemented a helper method called mapFindTop that brings in 2 extra parameters, 1 being the map that is constructed in the method (TreeMap or HashMap) and the other being a String name of the respective map (“TreeMap” or “HashMap”). This reduced redundancy of the code and reduced total lines of code.
