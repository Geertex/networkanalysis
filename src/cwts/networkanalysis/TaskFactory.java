package cwts.networkanalysis;

public class TaskFactory {
	Network network;
	Clustering clustering;
	ClusterDataManager clusterDataManager;
	double[] clusterWeights;
	double resolution;

	public TaskFactory (Network network, Clustering clustering, ClusterDataManager clusterDataManager, double resolution) {
		this.network = network;
		this.clustering = clustering;
		this.clusterDataManager = clusterDataManager;
		this.resolution = resolution;
		clusterWeights = clusterDataManager.getClusterWeights();
	}

	public MoveNodeTask CreateMoveNodeTask (int node) {
		return new MoveNodeTask(network, clustering, clusterDataManager, clusterWeights, resolution, node);
	}
}