package cwts.networkanalysis;

import java.util.LinkedHashSet;
import java.util.Set;

public class MoveNodeTask implements Runnable {
	Network network;
	Clustering clustering;
	ClusterDataManager clusterDataManager;
	double[] clusterWeights, edgeWeightPerCluster;
	double resolution, maxQualityValueIncrement, qualityValueIncrement;
    int[] neighboringClusters;
    int bestCluster, currentCluster, k, l, nNeighboringClusters, node;
    ArrayShop arrayShop;

	public MoveNodeTask (Network network, Clustering clustering, ClusterDataManager clusterDataManager, double[] clusterWeights, double resolution, int node, ArrayShop arrayShop) {
		this.network = network;
		this.clustering = clustering;
		this.clusterDataManager = clusterDataManager;
		this.clusterWeights = clusterWeights;
		this.resolution = resolution;
		this.node = node;
		this.arrayShop = arrayShop;
	}

	public void run() {
	    Object[] arrays = arrayShop.getArrays();
        edgeWeightPerCluster = (double[])arrays[0];
        neighboringClusters = (int[])arrays[1];
        int arraysNumber = (int)arrays[2];

		currentCluster = clustering.clusters[node];

        identifyNeighbours();

        findBestCluster();

        if (bestCluster != currentCluster)
        {
            clusterDataManager.moveNode(currentCluster, bestCluster, node);
        }
        arrayShop.returnArrays(arraysNumber);
	}

	private void findBestCluster() {
		bestCluster = currentCluster;
        maxQualityValueIncrement = edgeWeightPerCluster[currentCluster] - network.nodeWeights[node] * (clusterWeights[currentCluster]-network.nodeWeights[node]) * resolution;
        for (k = 0; k < nNeighboringClusters; k++)
        {
            l = neighboringClusters[k];
            qualityValueIncrement = edgeWeightPerCluster[l] - network.nodeWeights[node] * clusterWeights[l] * resolution;
            if (qualityValueIncrement > maxQualityValueIncrement)
            {
                bestCluster = l;
                maxQualityValueIncrement = qualityValueIncrement;
            }

            edgeWeightPerCluster[l] = 0;
        }
	}

	private void identifyNeighbours() {
		neighboringClusters[0] = clusterDataManager.getNextUnusedCluster();
        nNeighboringClusters = 1;
        
        for (k = network.firstNeighborIndices[node]; k < network.firstNeighborIndices[node + 1]; k++)
        {
            l = clustering.clusters[network.neighbors[k]];

            if (edgeWeightPerCluster[l] == 0)
            {
                neighboringClusters[nNeighboringClusters] = l;
                nNeighboringClusters++;
            }
            edgeWeightPerCluster[l] += network.edgeWeights[k];
        }
	}
}