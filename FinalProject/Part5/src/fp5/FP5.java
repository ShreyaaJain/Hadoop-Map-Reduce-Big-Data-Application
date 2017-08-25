/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp5;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author sjain
 */
public class FP5 {

    /**
     * @param args the command line arguments
     */
    public static class CountMapper extends Mapper<Object, Text, NullWritable, NullWritable> {

        public static final String STATE_COUNTER_GROUP = "state";
        public static final String UNKNOWN_COUNTER = "";
        public String[] sArray = new String[]{"San Jose","San Francisco","Palo Alto","Mountain View"};
        //public String[] sArray = new String[]{"Palo Alto"};
        
        
        private HashSet<String> states = new HashSet<String>(Arrays.asList(sArray));

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            String[] row = value.toString().split(",");
            String airline =  row[5].trim();

            if (airline != null
                    && !airline.isEmpty()) {

                if (states.contains(airline)) {
                    context.getCounter(STATE_COUNTER_GROUP, airline).increment(1);
                } else {
                    context.getCounter(STATE_COUNTER_GROUP, UNKNOWN_COUNTER).increment(1);
                }
            }

        }

    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job1 = Job.getInstance(conf, "Counter");
        job1.setJarByClass(FP5.class);
        job1.setMapperClass(CountMapper.class);
        job1.setMapOutputKeyClass(NullWritable.class);
        job1.setMapOutputValueClass(NullWritable.class);
        FileInputFormat.addInputPath(job1, new Path(args[0]));
        FileOutputFormat.setOutputPath(job1, new Path(args[1]));
        int code = job1.waitForCompletion(true) ? 0 : 1;

        if (code == 0) {
            for (Counter counter : job1.getCounters().getGroup(CountMapper.STATE_COUNTER_GROUP)) {
                System.out.println(counter.getDisplayName() + "\t" + counter.getValue());
            }
        }

    }

}
