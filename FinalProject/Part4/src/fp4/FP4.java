/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp4;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author sjain
 */
public class FP4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "median");
        job.setJarByClass(FP4.class);
        job.setMapperClass(Map1.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        // job.setCombinerClass(comb.class);
        job.setReducerClass(reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class Map1 extends Mapper<Object, Text, Text, IntWritable> {
        //private DoubleWritable rating = new DoubleWritable();

        private IntWritable one = new IntWritable(1);
        private Text word = new Text();

        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            String input[] = value.toString().split("\t");
            //double ratingValue  = Double.parseDouble(input[2]);
            // ratingValue = ratingValue;

            if (input.length > 1) {
                word.set(input[1]);

            }
            context.write(word, one);
        }

    }

    public static class reducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        private Text cd = new Text();
        private IntWritable mp = new IntWritable();

        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum = sum + value.get();

            }
            mp.set(sum);
            context.write(key, mp);

        }

    }
}
