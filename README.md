# KMeans Points Clustering with HDFS and Hadoop MapReduce

```
This project demonstrates the implementation of KMeans clustering using Hadoop MapReduce and Hadoop HDFS.
KMeans clustering is a popular machine learning algorithm used for clustering similar data points
into groups. In this project, we leverage the power of Hadoop MapReduce to perform KMeans clustering
on a large dataset stored in HDFS.
```

## K-Means clustering Overview
> K-means is a centroid-based clustering algorithm, where we calculate the distance between each data point and 
a centroid to assign it to a cluster. The goal is to identify the K number of groups in the dataset. 

![image](https://github.com/el-moudni-hicham/kmeans-points-clustring-hdfs-map-reduce/assets/85403056/76713809-a37b-4165-b8ce-9f1585a50170)


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

