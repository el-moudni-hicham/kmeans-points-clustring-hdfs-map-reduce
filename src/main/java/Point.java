public class Point {
    private double x;
    private double y;

    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public double calculateDistance(Point p) {
        // Use the Euclidean distance formula
        return (int) Math.sqrt((this.getX() - p.getX()) * (this.getX() - p.getX()) + (this.getY() - p.getY()) * (this.getY() - p.getY()));
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
