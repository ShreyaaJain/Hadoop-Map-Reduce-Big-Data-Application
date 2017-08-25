/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fp3;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 *
 * @author sjain
 */
public class FP3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "ReduceSideJoin");
        job.setJarByClass(FP3.class);

        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, JoinMapper1.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, JoinMapper2.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(JoinReducer.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(args[2]));
        job.setOutputKeyClass(org.apache.hadoop.io.Text.class);
        job.setOutputValueClass(org.apache.hadoop.io.Text.class);
        job.waitForCompletion(true);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class JoinMapper1 extends Mapper<Object, Text, Text, Text> {

        private Text outkey = new Text();

        private Text outvalue = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            int index1 = value.toString().indexOf(":");
            int index2 = value.toString().indexOf(",");
            String val = value.toString().trim().substring(index1 + 1, index2);

            Text outkey = new Text(val);
            outvalue.set("A" + value);
            context.write(outkey, outvalue);
        }
    }

    public static class JoinMapper2 extends Mapper<Object, Text, Text, Text> {

        private Text outkey = new Text();
        private Text outvalue = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] separatedInput = value.toString().split(",");
            if (separatedInput.length > 5) {
                String stationId = separatedInput[0].trim();
                if (stationId == null) {
                    return;

                }
                outkey.set(stationId);

                outvalue.set("B" + separatedInput[5].toString().trim());
                context.write(outkey, outvalue);
            }
        }
    }

    public static class JoinReducer extends Reducer<Text, Text, Text, Text> {

        private static final Text EMPTY_TEXT = new Text();
        private Text tmp = new Text();

        private ArrayList<Text> listA = new ArrayList<Text>();
        private ArrayList<Text> listB = new ArrayList<Text>();
        private String joinType = null;

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            listA.clear();
            listB.clear();

            while (values.iterator().hasNext()) {
                System.out.println(key);
                tmp = values.iterator().next();
                System.out.println(tmp);
                if (tmp.charAt(0) == 'A') {
                    listA.add(new Text(tmp.toString().substring(1)));
                } else if (tmp.charAt(0) == 'B') {
                    listB.add(new Text(tmp.toString().substring(1)));
                }
            }

            {
                if (!listB.isEmpty() && !listA.isEmpty()) {
                    for (Text A : listA) {
                        for (Text B : listB) {
                            context.write(A, B);
                        }
                    }

                }

            }
        }
    }
}
