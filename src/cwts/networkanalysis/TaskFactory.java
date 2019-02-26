package cwts.networkanalysis;

public class TaskFactory {
	Network network;
	Clustering clustering;
	ClusterDataManager clusterDataManager;
	double[] clusterWeights;
	double resolution;
	ArrayShop arrayShop;

	public TaskFactory (Network network, Clustering clustering, ClusterDataManager clusterDataManager, double resolution, ArrayShop arrayShop) {
		this.network = network;
		this.clustering = clustering;
		this.clusterDataManager = clusterDataManager;
		this.resolution = resolution;
		clusterWeights = clusterDataManager.getClusterWeights();
		this.arrayShop = arrayShop;
	}

	public MoveNodeTask CreateMoveNodeTask (int node) {
		return new MoveNodeTask(network, clustering, clusterDataManager, clusterWeights, resolution, node, arrayShop);
	}
}