import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class KmeansMapper extends Mapper<LongWritable, Text, Text, Text> {

    List<Point> centroides = new ArrayList<>();

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException {
        URI uri[] = context.getCacheFiles();
        FileSystem fs = FileSystem.get(context.getConfiguration());
        InputStreamReader is = new InputStreamReader(fs.open(new Path(uri[0])));
        BufferedReader br = new BufferedReader(is);
        String line = null;

        while ((line = br.readLine()) != null) {
            String[] xy = line.split(",");
            Point c = new Point(Double.valueOf(xy[0]), Double.valueOf(xy[1]));
            centroides.add(c);
        }
    }


    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        // Split the line into the x and y coordinates

        String[] coordinates = value.toString().split(",");
        double x = Double.parseDouble(coordinates[0]);
        double y = Double.parseDouble(coordinates[1]);

        Point point = new Point(x, y);
        // Find the closest centroid
        Point closestCentroid = findClosestCentroid(point);


        context.write(new Text(closestCentroid.toString()), value);
    }

    private Point findClosestCentroid(Point p) {
        // Initialize the minimum distance
        double minDistance = Integer.MAX_VALUE;
        Point closestCentroid = new Point();

        // Iterate over all the centroids
        for (int i = 0; i < centroides.size(); i++) {
            // Calculate the distance between the point and the centroid
            double distance = p.calculateDistance(centroides.get(i));

            // Update the minimum distance if necessary
            if (distance < minDistance) {
                minDistance = distance;
                closestCentroid = centroides.get(i);
            }
        }

        return closestCentroid;
    }

}
