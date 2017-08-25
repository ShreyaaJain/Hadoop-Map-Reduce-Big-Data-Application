/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp2;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
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
public class FP2 {
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO code application logic here
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "SecondarySort");
        job.setJarByClass(FP2.class);
        
        job.setMapperClass(FP2Mapper.class);
        job.setMapOutputKeyClass(CompositeKeyWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setReducerClass(FP2Reducer.class);
        job.setOutputKeyClass(CompositeKeyWritable.class);
        job.setOutputValueClass(IntWritable.class);
        //job.setPartitionerClass(Lab2Partitioner.class);
        
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //job.setNumReduceTasks(8);
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

   

    public static class FP2Mapper extends Mapper<Object, Text, CompositeKeyWritable, IntWritable> {

        private IntWritable vlt = new IntWritable(1);

        public void map(Object key, Text values, Context context) {
            String h = null;
            if (values.toString().length() > 0) {
                try {
                    String value[] = values.toString().split(",");
                    int i = value[2].indexOf("/");
                    h = value[2].substring(0,i);
                    if (h.charAt(0) == '0')
                    {
                        h = value[2].substring(1,i);
                    }
                    System.out.println();
                    CompositeKeyWritable cw = new CompositeKeyWritable(value[4], h);
                    context.write(cw, vlt);

                } 
                catch (Exception ex1) {
                    System.out.println(h);
                    ex1.printStackTrace();
                }
            }
        }
    }

    public static class FP2Reducer extends Reducer<CompositeKeyWritable, IntWritable, CompositeKeyWritable, IntWritable> {
      private IntWritable res = new IntWritable();
        @Override
        public void reduce(CompositeKeyWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
           int sum = 0;
            for (IntWritable cal : values) {
                     sum += cal.get();
            }
                   
                  res.set(sum);

                 context.write(key, res);
            
        }
    }

}
