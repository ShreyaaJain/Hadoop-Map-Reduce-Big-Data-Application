/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
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
public class FP1 {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "average");
        job.setJarByClass(FP1.class);
        job.setMapperClass(Map1.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class Map1 extends Mapper<Object, Text, Text, Text> {

        private IntWritable one = new IntWritable(1);

        private Text compositeKey = new Text();
        private Text firstByteLatecny = new Text();
        private Text response = new Text();

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            String input[] = value.toString().split(" ");
            if (input[9].toString().startsWith("2")) {

                firstByteLatecny.set(input[21]);
                String grpIdInString = input[17];
                grpIdInString = grpIdInString.substring(grpIdInString.indexOf("-") + 1, grpIdInString.length());

                Long hour = Long.parseLong(input[14]);
                Date date = new Date(hour);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                String formatted = format.format(date);
                System.out.println(formatted);
                format.setTimeZone(TimeZone.getTimeZone("Boston"));
                formatted = format.format(date);
                System.out.println(formatted);
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(date);   // assigns calendar to given date 
                calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                System.out.println(calendar.get(Calendar.HOUR_OF_DAY));

                String combinedKey = grpIdInString + Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));

                compositeKey.set(combinedKey);
                context.write(compositeKey, firstByteLatecny);
            }
        }
    }

    public static class reducer extends Reducer<Text, Text, Text, IntWritable> {

        private Text cd = new Text();
        private IntWritable mp = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            int count = 0;
            for (Text value : values) {
                String v = value.toString();
                count = count + 1;
                sum = sum + Integer.parseInt(v);

            }
            mp.set(sum / count);
            context.write(key, mp);

        }

    }
}
