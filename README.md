# KMeans Points Clustering with HDFS and Hadoop MapReduce

```
This project demonstrates the implementation of KMeans clustering using Hadoop MapReduce and Hadoop HDFS.
KMeans clustering is a popular machine learning algorithm used for clustering similar data points
into groups. In this project, we leverage the power of Hadoop MapReduce to perform KMeans clustering
on a large dataset stored in HDFS.
```

## Table of Contents

   * [K-Means clustering Overview](#k-means-clustering-overview)
   * [Prerequisites](#prerequisites)
   * [Project Structure](#project-structure)
   * [Usage](#usage)


## K-Means clustering Overview
> K-means is a centroid-based clustering algorithm, where we calculate the distance between each data point and 
a centroid to assign it to a cluster. The goal is to identify the K number of groups in the dataset. 

![image](https://github.com/el-moudni-hicham/kmeans-points-clustring-hdfs-map-reduce/assets/85403056/c8f92eec-dc82-41d1-b7e7-f1032e5a09f2)


* K-means algorithm steps :

    1. Choose the number of clusters (k).
    2. Initialize the centroids.
    3. Assign each data point to the closest centroid.
    4. Recompute the centroids.
    5. Repeat steps 3 and 4 until the centroids no longer change.

## Prerequisites

* Java Development Kit (JDK) 8 or higher
* Apache Hadoop installed and configured
* Access to Hadoop Distributed File System (HDFS) cluster

## Project Structure

```
│
├───src
│   ├───main
│   │   ├───java
│   │   │       KmeansDriver.java
│   │   │       KmeansMapper.java
│   │   │       KmeansReducer.java
│   │   │       Point.java
│   │   │
│   │   └───resources
│   │ 
└───target
    │   kmeans-points-clustring-hdfs-map-reduce-1.0-SNAPSHOT.jar

```

## Usage

Copies the local file points.csv to the HDFS filesystem at the specified path. 

```bash 
hadoop fs -copyFromLocal points.csv hdfs://localhost:9000/points.csv
```

![image](https://github.com/el-moudni-hicham/kmeans-points-clustring-hdfs-map-reduce/assets/85403056/017ea59e-dba9-4f8a-90c8-a2ea52a2d230)


Copies the local file centroides.csv to the HDFS filesystem at the specified path.

```bash 
hadoop fs -copyFromLocal centroides.csv hdfs://localhost:9000/centroides.csv
```

![image](https://github.com/el-moudni-hicham/kmeans-points-clustring-hdfs-map-reduce/assets/85403056/9d4e436c-5dde-412a-bf65-a7a906342994)

Adds the centroides.csv file from HDFS to the distributed cache of the MapReduce job.

```java
job.addCacheFile(new URI("hdfs://localhost:9000/centroides.csv"));
```

This allows the Mapper and Reducer tasks to access the centroid data while processing.

```java
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
```

Specifies the input file path (points.csv) and the output directory path (output) on HDFS.

```java
  private static Path file = new Path("hdfs://localhost:9000/points.csv");
  private static Path output = new Path("hdfs://localhost:9000/output");
```

Specifies the input file path (points.csv) and the output directory path (output) on HDFS.

```bash 
hadoop jar kmeans-points-clustring-hdfs-map-reduce-1.0-SNAPSHOT.jar KmeansDriver
```

Displays the content of the output file generated by the MapReduce job.

```bash 
hdfs dfs -cat /output/part-r-00000
```

The output file contains the centroids and their corresponding points after the KMeans clustering.

![image](https://github.com/el-moudni-hicham/kmeans-points-clustring-hdfs-map-reduce/assets/85403056/d40ca348-f276-44b6-8bff-7978f6bb85d1)

Visual representation of the KMeans clustering results, showing the clusters formed by centroids and their corresponding points.

![image](https://github.com/el-moudni-hicham/kmeans-points-clustring-hdfs-map-reduce/assets/85403056/cbedeef5-9987-4245-8387-96e71b18cb4d)

