# Hadoop-Map-Reduce-Big-Data-Application

The Bay Area Bike Share system allows users to rent bicycles for short journeys between stations throughout the city. Users can be annual members or short term (1 or 3 days). 
This data includes all Bay Area Bike Share trips from 2015-2016.

## Analysis 1 : How much is the Bay Area Bike Share used?

-	This analysis was done using map reduce word count framework.
-	Used TextInputFormat to read the input csv file
-	Used combiner to optimize the time of run
-	By using the combiner, the output took 30 seconds lesser than the original time which was 45 sec.

![alt tag](https://github.com/ShreyaaJain/Hadoop-Map-Reduce-Big-Data-Application/blob/master/Graphs/Analysis1.png)

### Conclusion: 
Using the word-count program to get that count of the subscription type. From the output it can be seen that there are 11% customers and 89% Subscribers.


## Analysis 2 – Which areas attract more tourists/commuters? (based on the proportation of trips by month getting the frequency of riders)

-	This analysis was done using Secondary Sorting and Join Map reduce framework by getting the composite key and joining them against the secondary key
-	Custome Writable class is used to achieve Combiner Optimization.
-	Inner join is used to join the flight location and the flight number for each ride.
-	Time taken by the analysis was 1 min 15sec. After using combiner optimization the time taken to run analysis was reduced by 40 seconds 

![alt tag](https://github.com/ShreyaaJain/Hadoop-Map-Reduce-Big-Data-Application/blob/master/Graphs/Analysis2.png)

### Conclusion: 
It can be analyzed that SanFrancisco attracts more number of tourists that the others and also the month trend which shows the lowest for Dec and Jan


## Analysis 3 – What is ride pattern during office hours?

-	Have used bloom filters for this analysis by passing the office hours as the hot values which is from 7am – 7pm.
-	Tried using DistributedCache for this analysis to read the hot values from the HDFS.
-	False Positivity is kept to 0.1%.
-	Extracted the month from the time field.
-	Only mapper is used.
-	From this ouput used the word count code from analysis 1 to get the count of riders in the particular hour.

![alt tag](https://github.com/ShreyaaJain/Hadoop-Map-Reduce-Big-Data-Application/blob/master/Graphs/Analysis3.png)

### Conclusion:
The analysis shows that the office peak hours are 8am and 5pm for subscribers.

## Analysis 4: How does the bikers ratio varies over the month?

-	Partitioner is used for this analysis by keeping the key as Month number. There will be 12 separate files which will contain flight traffic details for that particular month.
-	CustomWritable class is used to read the origin, destination, Airline company   and month for eacxh trip.
-	No of reducers is set to 12 as there are 12 months
-	12 output files were created for each month.

![alt tag](https://github.com/ShreyaaJain/Hadoop-Map-Reduce-Big-Data-Application/blob/master/Graphs/Analysis4.png)

### Conclusion: 
Details of the month for both subscribers and customers


## Analysis 5: How many bike stations are there in each region?

-	This analysis was done using Counters where the input data was the station data.
-	Done only on a mapper side
-	Array was created to pass on the 4 unique key.
-	If there was a match the count was incremented by one for that particular key

# Caps
## loawe
### ghjjhg











