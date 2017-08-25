/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp6top10;

import java.io.IOException;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author sjain
 */
public class FP6Top10 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Configuration conf = new Configuration();

        Job job;
        try {
            job = Job.getInstance(conf, "Topten");
            job.setJarByClass(FP6Top10.class);
            job.setMapperClass(TopMapper.class);
            job.setMapOutputKeyClass(NullWritable.class);
            job.setMapOutputValueClass(Text.class);
            job.setReducerClass(Reduce1.class);
            job.setOutputKeyClass(NullWritable.class);
            job.setOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            boolean success = job.waitForCompletion(true);

        } catch (IOException | InterruptedException | ClassNotFoundException ex) {

        }

    }

    public static class TopMapper extends Mapper<Object, Text, NullWritable, Text> {

        private TreeMap<Integer, Text> reputation
                = new TreeMap<Integer, Text>();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String row[] = value.toString().split(",");

            if (NumberUtils.isNumber(row[2].trim())) {

                reputation.put(Integer.parseInt(row[2].toString()),
                        new Text(value.toString()));
                if (reputation.size() > 10) {
                    reputation.remove(reputation.firstKey());
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {

            for (Text t : reputation.descendingMap().values()) {
                context.write(NullWritable.get(), t);
            }

        }

    }

    public static class Reduce1 extends Reducer<NullWritable, Text, NullWritable, Text> {

        private TreeMap<Integer, Text> repToRecordMap = new TreeMap<Integer, Text>();

        public void reduce(NullWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            {
                for (Text value : values) {

                    repToRecordMap.put(Integer.parseInt(value.
                            toString().split(",")[2]), new Text(value));
                }

                if (repToRecordMap.size() > 10) {
                    repToRecordMap.remove(repToRecordMap.firstKey());
                }

                for (Text t : repToRecordMap.descendingMap().values()) {
                    context.write(NullWritable.get(), t);
                }
            }
        }

    }

}
