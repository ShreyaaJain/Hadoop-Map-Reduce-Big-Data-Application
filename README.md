# Hadoop-Map-Reduce-Big-Data-Application

The Bay Area Bike Share system allows users to rent bicycles for short journeys between stations throughout the city. Users can be annual members or short term (1 or 3 days). 
This data includes all Bay Area Bike Share trips from 2015-2016.

## Analysis 1 : How much is the Bay Area Bike Share used?

-	This analysis was done using map reduce word count framework.
-	Used TextInputFormat to read the input csv file
-	Used combiner to optimize the time of run
-	By using the combiner, the output took 30 seconds lesser than the original time which was 45 sec.

### Conclusion: Using the word-count program to get that count of the subscription type. From the output it can be seen that there are 11% customers and 89% Subscribers.


## Analysis 2 – Which areas attract more tourists/commuters? (based on the proportation of trips by month getting the frequency of riders)

-	This analysis was done using Secondary Sorting and Join Map reduce framework by getting the composite key and joining them against the secondary key
-	Custome Writable class is used to achieve Combiner Optimization.
-	Inner join is used to join the flight location and the flight number for each ride.
-	Time taken by the analysis was 1 min 15sec. After using combiner optimization the time taken to run analysis was reduced by 40 seconds 


### Conclusion: It can be analyzed that SanFrancisco attracts more number of tourists that the others and also the month trend which shows the lowest for Dec and Jan

