import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class KmeansDriver {

    private static boolean CentroidsChanged = true;
    private static Path file = new Path("hdfs://localhost:9000/points.csv");
    private static Path output = new Path("hdfs://localhost:9000/output");

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, URISyntaxException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        int i = 0; //iterations
        while (true) { // repeat mapReduce Job while iterations < 10 and centroids change after every job
            Job job = Job.getInstance(conf, "K-Means Job");
            job.setJarByClass(KmeansDriver.class);
            job.setMapperClass(KmeansMapper.class);
            job.setReducerClass(KmeansReducer.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            job.setInputFormatClass(TextInputFormat.class);
            job.setOutputFormatClass(TextOutputFormat.class);

            job.addCacheFile(new URI("hdfs://localhost:9000/centroides.csv"));
            FileInputFormat.addInputPath(job, file);

            // delete output if exists
            if (fs.exists(output)) {
                fs.delete(output, true);
            }
            FileOutputFormat.setOutputPath(job, output);

            job.waitForCompletion(true);


            updateCentroids(fs);
            if (i >= 10 || !CentroidsChanged) {
                break;
            }
            i++;
        }
    }

    // replace centroids with new centroids from last output file after the end of every job
    public static void updateCentroids(FileSystem fs) throws IOException {
        FSDataOutputStream out = fs.create(new Path("hdfs://localhost:9000/centroides.csv"), true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

        // get new centroids from last output
        InputStreamReader is = new InputStreamReader(fs.open(new Path("hdfs://localhost:9000/output/part-r-00000")));
        BufferedReader br = new BufferedReader(is);
        String line = null;

        StringBuilder oldCentroid = new StringBuilder();
        StringBuilder newCentroid = new StringBuilder();

        while ((line = br.readLine()) != null) {
            String[] parts = line.replaceAll("\\s+", " ").split(" ");
            String[] centroids = parts[0].split(">");
            //new centroids
            newCentroid.append(centroids[1]);
            newCentroid.append("\n");
            //old centroids
            oldCentroid.append(centroids[0]);
            oldCentroid.append("\n");
        }

        // if old centroids == new centroids -> end of while
        if (newCentroid.toString().equals(oldCentroid.toString())) {
            CentroidsChanged = false;
        }

        // save new centroids to centroides.csv
        bw.write(newCentroid.toString());
        bw.close();
        br.close();
    }

}



