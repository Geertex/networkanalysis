package cwts.networkanalysis;

import cwts.util.Arrays;
import java.util.Random;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Fast local moving algorithm.
 *
 * <p>
 * The fast local moving algorithm first adds all nodes in a network to a
 * queue. It then removes a node from the queue. The node is moved to the
 * cluster that results in the largest increase in the quality function. If the
 * current cluster assignment of the node is already optimal, the node is not
 * moved. If the node is moved to a different cluster, the neighbors of the
 * node that do not belong to the node's new cluster and that are not yet in
 * the queue are added to the queue. The algorithm continues removing nodes
 * from the queue until the queue is empty.
 * </p>
 *
 * <p>
 * The fast local moving algorithm provides a fast variant of the {@link
 * StandardLocalMovingAlgorithm}.
 * </p>
 *
 * @author Ludo Waltman
 * @author Nees Jan van Eck
 * @author Vincent Traag
 */
public class FastLocalMovingAlgorithmParallel extends IterativeCPMClusteringAlgorithm
{
    /**
     * Random number generator.
     */
    protected Random random;

    /**
     * Constructs a fast local moving algorithm.
     */
    public FastLocalMovingAlgorithmParallel()
    {
        this(new Random());
    }

    /**
     * Constructs a fast local moving algorithm.
     *
     * @param random Random number generator
     */
    public FastLocalMovingAlgorithmParallel(Random random)
    {
        this(DEFAULT_RESOLUTION, DEFAULT_N_ITERATIONS, random);
    }

    /**
     * Constructs a fast local moving algorithm for a specified resolution
     * parameter and number of iterations.
     *
     * @param resolution  Resolution parameter
     * @param nIterations Number of iterations
     * @param random      Random number generator
     */
    public FastLocalMovingAlgorithmParallel(double resolution, int nIterations, Random random)
    {
        super(resolution, nIterations);

        this.random = random;
    }

    /**
     * Improves a clustering by performing one iteration of the fast local
     * moving algorithm.
     *
     * <p>
     * The fast local moving algorithm first adds all nodes in a network to a
     * queue. It then removes a node from the queue. The node is moved to the
     * cluster that results in the largest increase in the quality function. If
     * the current cluster assignment of the node is already optimal, the node
     * is not moved. If the node is moved to a different cluster, the neighbors
     * of the node that do not belong to the node's new cluster and that are
     * not yet in the queue are added to the queue. The algorithm continues
     * removing nodes from the queue until the queue is empty.
     * </p>
     *
     * @param network    Network
     * @param clustering Clustering
     *
     * @return Boolean indicating whether the clustering has been improved
     */
    protected boolean improveClusteringOneIteration(Network network, Clustering clustering)
    {
        if (network.nNodes == 1)
            return false;
        int numberOfWorkers = 7;

        boolean run = true;

        ForkJoinPool executor = new ForkJoinPool(numberOfWorkers);

        ArrayShop arrayShop = new ArrayShop(numberOfWorkers, network.nNodes);

        Set<Integer> taskQueue = new LinkedHashSet<Integer>();

        ClusterDataManager clusterDataManager = new ClusterDataManager(network, clustering, taskQueue);

        TaskFactory taskFactory = new TaskFactory(network, clustering, clusterDataManager, resolution, arrayShop);

        int[] nodeOrder = Arrays.generateRandomPermutation(network.nNodes, random);
        for (int i = 0; i < nodeOrder.length; i++) {
            taskQueue.add(nodeOrder[i]);
        }

        while (run) {
            int node = 0;
            if(!taskQueue.isEmpty()) {
                synchronized (taskQueue) {
                    node = taskQueue.iterator().next();
                    taskQueue.remove(node);
                }
                while(executor.getQueuedSubmissionCount() > 1000){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
                MoveNodeTask task = taskFactory.CreateMoveNodeTask(node);
                executor.execute(task);
            }
            else {
                synchronized (taskQueue) {
                    try {
                        taskQueue.wait(1);
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                }
                if(taskQueue.isEmpty()) {
                    run = false;
                }
            }
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
        }

        boolean update = clusterDataManager.getSomeThingChanged();

        if (update)
            clustering.removeEmptyClusters();

        return update;
    }
}