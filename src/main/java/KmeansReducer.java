import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.Iterator;

public class KmeansReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuilder points = new StringBuilder();
        int nb_points = 0;
        double a = 0;
        double b = 0;
        Iterator<Text> it = values.iterator();
        while (it.hasNext()) {
            String iterationVal = it.next().toString();
            points.append(iterationVal + "/"); //

            String[] coordinates = iterationVal.toString().split(",");
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);

            Point point = new Point(x, y);

            a += point.getX();
            b += point.getY();

            nb_points++;

        }
        double xc = a / nb_points;
        double yc = b / nb_points;
        Point newCentroid = new Point(xc, yc);
        context.write(new Text(key + ">" + newCentroid), new Text(points.toString()));
    }
}


