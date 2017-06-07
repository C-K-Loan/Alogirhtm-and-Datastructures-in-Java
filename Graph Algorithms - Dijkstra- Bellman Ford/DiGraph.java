import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;

/**
 * A data structure to store a graph consisting of a set of unlabeled nodes and
 * a set of directed, weighted edges connecting the nodes.
 * 
 * The interface provides methods to add nodes and edges. Both methods can be
 * called at all time and the data structure should not assume a limitation on
 * the number of nodes or edges.
 * 
 * 
 * Furthermore, there are methods to access a single edge or to check whether
 * two nodes are connected as well as methods to access all nodes connected with
 * a specific node and thus all outgoing edges.
 * 
 * @author Joerg Schneider, modified by AlgoDat-Tutoren
 */
public class DiGraph implements Graph {

	public HashMap<Integer, Node> nodes;

	// needed for testing, where the execution of methods should not stop
	private boolean showSteps = true;

	public DiGraph() {
		nodes = new HashMap<Integer, Node>();
	}

	/**
	 * Adds an edge to the graph.
	 * 
	 * The edge is specified by a start node and a target node. If there is
	 * already an edge between these two nodes the behavior of the method is
	 * unspecified. The edge is labeled with a positive weight value.
	 * 
	 * @param startNode
	 *            the start node
	 * @param targetNode
	 *            the target node
	 * @param weight
	 *            a positive, non-zero value assigned to this edge
	 */
	@Override
	public void addEdge(Node startnode, Node endnode, int weight) {
		if (startnode != null && endnode != null) {
			
			// only add edges between nodes which are already part of this graph 
			if(nodes.values().contains(startnode) && nodes.values().contains(endnode)){
				startnode.addEdge(endnode, weight);
			}

		}
	}


	@Override
	public void addEdge(int startnode, int endnode, int weight) {
		addEdge(nodes.get(startnode), nodes.get(endnode), weight);
	}


	/**
	 * Adds a node to the graph.
	 * 
	 * As the nodes are unlabeled this method has no parameter. An identifier is
	 * returned which can be used to access this specific node when calling the
	 * other operations.
	 * 
	 * @return the added Node.
	 */
	@Override
	public Node addNode() {
		Node newOne = null;
		int newId = nodes.size();
		newOne = new Node(newId);
		nodes.put(newId, newOne);
		return newOne;
	}


	/**
	 * Returns the weight of the directed edge between the given nodes.
	 * 
	 * @param startNode
	 *            the the start node as provided by addNode()
	 * @param targetNode
	 *            the target node as provided by
	 *            addNode()
	 * @return WEIGHT_NO_EDGE, if there is no directed edge between the start
	 *         node and the target node or the positive, non-zero weight stored
	 *         by addEdge().
	 */
	@Override
	public int getWeight(Node startnode, Node endnode) {
		if (startnode != null && endnode != null) {
			if(nodes.values().contains(startnode) && nodes.values().contains(endnode)){
				return startnode.getWeight(endnode);
			}
		}
		return Graph.WEIGHT_NO_EDGE;
	}

	@Override
	public int getWeight(int startnode, int endnode) {
		return getWeight(nodes.get(startnode), nodes.get(endnode));
	}

	/**
	 * Returns all nodes of the graph.
	 * 
	 * @return list containing all valid node identifiers.
	 */
	@Override
	public List<Node> getNodes() {
		return new ArrayList<Node>(nodes.values());
	}
	
	public List<Edge> getEdges() {
		List<Edge> edges = new LinkedList<Edge>();
		
		for (Node node : nodes.values()) {
			List<Edge> incidentEdges = node.getIncidentEdges();
			edges.addAll(incidentEdges);
		}
		return edges;
	}

	/**
	 * For a node returns all nodes the outgoing edges of the node directly lead to.
	 * 
	 * @return list containing all directly connected nodes.
	 */
	@Override
	public List<Node> getAdjacentNodes(Node startnode) {
		List<Node> nodes = null;
		if (startnode != null && this.nodes.values().contains(startnode)) {
			return startnode.getAdjacentNodes();
		}
		nodes = new LinkedList<Node>();
	    return nodes;
	}

	@Override
	public List<Node> getAdjacentNodes(int startnode) {
		return getAdjacentNodes(nodes.get(startnode));
	}

	/**
	 * Determines if the given nodes are directly connected, that is whether
	 * there is some directed edge that links startNode to targetNode.
	 * For all existing nodes: isConnected(n1, n2) <=> getAdjacentNodes(n1).contains(n2)
	 * 
	 * @param startNode
	 *            the start node as provided by addNode()
	 * @param targetNode
	 *            the target node as provided by
	 *            addNode()
	 * @return true iff there is a directed edge between the given start node
	 *         and target node.
	 */
	@Override
	public boolean isConnected(Node startnode, Node endnode) {
		if (startnode != null && endnode != null) {
			if(nodes.values().contains(startnode) && nodes.values().contains(endnode)){
				return startnode.hasEdgeTo(endnode);
			}
		}
		return false;
	}

	
	@Override
	public boolean isConnected(int startNodeID, int endNodeID) {
		return isConnected(nodes.get(startNodeID), nodes.get(endNodeID));
	}

	/**
	 * resets all colors in visual representation of the graph to white
	 */
	@Override
	public void clearMarks() {
		for (Node nextNode : nodes.values()) {
			nextNode.status = Node.WHITE;
			for (Edge nextEdge : nextNode.getIncidentEdges()) {
				nextEdge.status = Edge.WHITE;
			}
		}
	}

	/**
	 * help function for reset the state of all nodes to white
	 */
	private void resetState() {
		for (Node n : nodes.values()) {
			n.status = Node.WHITE;
		}
	}
	
	
	/**
	 * travels randomly in the graph.
	 * used to demonstrate visualization class
	 * @param index of startnode
	 */
	@Override
	public void showGraph(Node startNode){
		
		resetState();
		if(startNode == null || !nodes.values().contains(startNode)){
			System.out.println("Error! this node does not exist!");
			return;
		}
		startNode.status = Node.GRAY;
		
		this.stopExecutionUntilSignal();
		startNode.status = Node.BLACK;
		
		List<Edge> edges = startNode.getIncidentEdges();
		Random ran = new Random();
		
		while(edges.size() != 0){
			List<Node> nodes = new ArrayList<Node>();
			//filter black neighbor node
			//white and gray neighbor nodes will add to nodes-list
			for(Edge e: edges){
				if(e.endnode.status != Node.BLACK){
					e.endnode.status = Node.GRAY;
					nodes.add(e.endnode);
				}
			}
			this.stopExecutionUntilSignal();
			if(nodes.size() == 0) return;
			
			startNode= nodes.get(ran.nextInt(nodes.size()));
			edges = startNode.getIncidentEdges();
			startNode.status = Node.BLACK;
		}
	}
	
	public void showGraph(int nodeID) {
		showGraph(nodes.get(nodeID));
	}
	
	
	/**
	 * Traverses the Graph using breadth-first search
	 * @param startNode the node to start the search with
	 * @return a list containing the reachable nodes, ordered as visited during the search
	 */
	@Override
	public List<Node> breadthFirstSearch(Node startNode){
		LinkedList<Node> nodeList = null;
		resetState();
		// See Blatt 05
		return nodeList;
		
	}
	
	@Override
	public List<Node> breadthFirstSearch(int nodeID) {
		return breadthFirstSearch(nodes.get(nodeID));
	}
	
	
	/**
	 * Traverses the Graph using depth-first search
	 * @param startNode the node to start the search with
	 * @return a list containing the reachable nodes, ordered as visited during the search
	 */
	@Override
	public List<Node> depthFirstSearch(Node startNode){
		LinkedList<Node> nodeList = null;
		resetState();
		// See Blatt 05
		return nodeList;
		
	}
	
	@Override
	public List<Node> depthFirstSearch(int nodeID) {
		return depthFirstSearch(nodes.get(nodeID));
	}
	

	// checks whether the given node has a white neighbor or not
	private boolean hasWhiteNeighbor(Node node) {

		for (Node neighbour : node.getAdjacentNodes()) {
			if (neighbour.status == Node.WHITE) {
				return true;
			}
		}
		return false;
	}

	// checks whether the given node has a gray neighbor or not
	private boolean hasGrayNeighbor(Node node) {

		for (Node neighbour : node.getAdjacentNodes()) {

			if (neighbour.status == Node.GRAY) {
				return true;
			}
		}

		return false;
	}

	// Disjkstra algorithm
	// ----------------------------------------------------------------------------------------------------

	// entry point for our test cases. don't change this function.
	@Override
	public void populateDijkstraFrom(int startNodeID) {
		populateDijkstraFrom(nodes.get(startNodeID));
	}


	/*
	 * TODO
	 * paar bugs in Djkstra, checken ob das bisher richtig geht
	 * es  sollte alles richtig implementiert sein
	 * 
	 * 
	 * Der Bisherige Testbaum hat alles Richtig wiedergegeben, allerdings kann ich das iwie nicht printen, sonst stimmts
	 * Randfälle betrachten für BUggy Parameter oder Null und unzusammenhängender Graph, etc 
	 * 
	 * 
	 * 
	 * 
	 */
	
	@Override
	public void populateDijkstraFrom(Node startNode) {
		

		this.resetState();
		// TODO: Your populateDijkstraFrom implementation here
		
		// You can use this implementation of a priority queue.
		// Nodes that are added to the queue are automatically sorted by distance 
		// because we overwrote Node.compareTo()
		 PriorityQueue<Node> distanceQueue = new PriorityQueue<Node>();
		 List<Node> nodeList = this.getNodes();
		 ArrayList<Node> currAdj = startNode.getAdjacentNodes();

		 Node curN = startNode;
		 
	//Init
		 //Give Everynode Status 0= unprocessed and Distance Infinity for Starting Values , except S with distance =
		 //add them to prioque after setting values 
		 for(Node n : nodeList){ 
			 n.distance=Integer.MAX_VALUE;
			 n.status=0;
			 n.predecessor=null;
			 if (n.equals(startNode)){ n.distance=0; n.status=0;} //special case for setting values for Starting node 
			 distanceQueue.add(n);//adding to que
		 }
		 
		 
		 

		while(distanceQueue.isEmpty()==false){
			curN=distanceQueue.peek();
			currAdj=curN.getAdjacentNodes();
			curN.status=Node.GRAY;
		//	System.out.println("Curnode is :" + curN.id + "with status "+ curN.status + "with distance" + curN.distance);
			stopExecutionUntilSignal();
			for(Node n :currAdj){// check if current Node Provides a shorter path to any unprocessed node 
				if(n.status==0){
				//	System.out.println("and adjacent to"+ n.id);
					if(n.status !=2 || n.distance==Integer.MAX_VALUE){n.predecessor= curN; n.distance = curN.distance + getWeight(curN, n);} //Special Case to Avoid MAxvalue+N, if we have Node with dist MAX value we can just give it the path of curn + the edge  
	
					if(n.status!= 2|| n.distance> curN.distance+ getWeight(curN, n)){n.predecessor =curN; n.distance=curN.distance + getWeight(curN, n);} // the Path from  startN to curN to n shorter than any other previous discovered path, so update it 
				
				//	System.out.println("Shortest Path to "+ n.id + " is length :" + n.distance + "and with Predecesor " + n.predecessor.id);

				}
				
			}
			curN.status=2;
			System.out.println("Popping" + curN.id);
			distanceQueue.remove(curN);//curn done Processing
				
		}
		
		System.out.println("Setting up Distances Done");
		
		nodeList=this.getNodes();
		
//		for(Node n: nodeList){
//			if(n!= startNode){
//			System.out.println(distanceQueue);
//			System.out.println("in Loop" + nodeList.size());
//			System.out.println("Shortest Path to "+ n.id + " is length :" + n.distance + "and with Predecesor " + n.predecessor.id);
//			}
//			}
		 
		 
		 
		// clean up
		this.clearMarks();

	}


	@Override
	public List<Node> getShortestPathDijkstra(int startNodeID, int targetNodeID) {
	
		return getShortestPathDijkstra(nodes.get(startNodeID), nodes.get(targetNodeID));
	}

	/**
	 * Calculates a List of Node indices that describe the shortet path from
	 * start node to target node. The Dijkstra-Algorithmn is used for
	 * calculation.
	 * 
	 * @param targetNodeIndex
	 *            the index of the target node, as returned by addNode()
	 * @return the list of nodes, or null if no path exists
	 */
	@Override
	public List<Node> getShortestPathDijkstra(Node startNode, Node targetNode) {
		
		//fehlerbehandlung
		if(startNode== null) return null;
		if(targetNode== null) return null;
		
		List<Node> nodeList = this.getNodes();
		
		if(nodeList.contains(startNode)==false) return null;
		if(nodeList.contains(targetNode)==false) return null;
		if(nodeList.isEmpty()) return null;

		

		populateDijkstraFrom(startNode);
		ArrayList<Node> currAdj = startNode.getAdjacentNodes();
		LinkedList<Node> path = new LinkedList<Node>()		;
		LinkedList<Node> l = new LinkedList<Node>();

		
		
		// To retrive the Path we start from the targetNode and follow the predecessors until arriving at startNode. THe Resuslting path is the shortest path 
		Node currN = targetNode.predecessor;
		l.add(targetNode);
		l.add(currN);

		
		
		while(currN!= null && currN.predecessor !=null &&currN.predecessor!= startNode){
			l.add(currN.predecessor);
			currN=currN.predecessor;
		}
		l.add(startNode);
		
		
		//This line stops program execution until a key is pressed
		stopExecutionUntilSignal();

		
		
		System.out.println("Shortest path is: " + l);
		//Return an empty list. Replace this line by the shortest path!
		return l;
	}



	// Bellman-Ford algorithm
	// ----------------------------------------------------------------------------------------------------

	// entry point for our test cases. Do not change this function.
	@Override
	public void populateBellmanFordFrom(int startNodeID) {
		populateBellmanFordFrom(nodes.get(startNodeID));
	}

	public void populateBellmanFordFrom(Node startNode) {

		this.resetState();
		 PriorityQueue<Node> distanceQueue = new PriorityQueue<Node>();
		 List<Edge> edgeList = this.getEdges();
		 int i = 0;
		 ArrayList<Node> currAdj = startNode.getAdjacentNodes();
		 List<Node> nodeList = this.getNodes();
		 Node curN = startNode;
		 
	//Init
		 //Give Everynode Status 0= unprocessed and Distance Infinity for Starting Values , except S with distance =
		 //add them to prioque after setting values 
		 for(Node n : nodeList){ 
			 n.distance=Integer.MAX_VALUE;
			 n.status=0;
			 n.predecessor=null;
			 if (n.equals(startNode)){ n.distance=0; n.status=0;} //special case for setting values for Starting node 
			 distanceQueue.add(n);//adding to que
		 }
		 
		 
		 
		 while (i < this.getEdges().size()-1){			// |Edges|-1 Iterationen
				 
		 for(Node n : nodeList){ //get first Node 
			 System.out.println("curnode is " + n.id);
			 edgeList=n.getIncidentEdges();
			 for(Edge e : edgeList){			//get all the Edges from the Curr node and check for everyone if we can update the distances for the nodes they connect to
				 //TODO SPECIAL CASES FOR ALL THE INFITNTIYSSS
				if(n.distance==Integer.MAX_VALUE) break; //dont check any Edges of n Since we cant get any info yet
				
				if(e.endnode.distance==Integer.MAX_VALUE){e.endnode.predecessor = n; e.endnode.distance= n.distance + e.weight;}  //we can asign the Values since  n dist cant be Infinity because IF test above 
				
				if (n.distance+e.weight<e.endnode.distance){e.endnode.distance=n.distance+e.weight; e.endnode.predecessor=n;} //update distance from Node E is adjacent to, since we found a shorter path
			 }
			 
			 
		 	}
		 	i++;
		 	}	
		// clean up
		this.clearMarks();

	}

	@Override
	public List<Node> getShortestPathBellmanFord(int startNodeID, int targetNodeID) {
		return getShortestPathBellmanFord(nodes.get(startNodeID), nodes.get(targetNodeID));
	}

	/**
	 * Calculates a List of Node indices that describe the shortet path from
	 * start node to target node. The Bellman-Ford-Algorithmn is used for
	 * calculation.
	 * 
	 * @param targetNodeIndex
	 *            the index of the target node, as returned by addNode()
	 * @return the list of nodes, or null if no path exists
	 */
	@Override
	public List<Node> getShortestPathBellmanFord(Node startNode, Node targetNode) {
		// TODO: Your implementation here
		// NOTE: you have to run populateBellmanFordFrom first before you can read
		// out the shortest path

		//fehlerbehandlung
		if(startNode== null) return null;
		if(targetNode== null) return null;
		
		List<Node> nodeList = this.getNodes();
		
		if(nodeList.contains(startNode)==false) return null;
		if(nodeList.contains(targetNode)==false) return null;
		if(nodeList.isEmpty()) return null;

		populateBellmanFordFrom(startNode);

		LinkedList<Node> l = new LinkedList<Node>();
		List<Node> nodes= this.getNodes();
		
		
		// To retrive the Path we start from the targetNode and follow the predecessors until arriving at startNode. THe Resuslting path is the shortest path 
		Node currN = targetNode.predecessor;
		l.add(targetNode);
		l.add(currN);

		
		
		while(currN.predecessor!= startNode){
			l.add(currN.predecessor);
			currN=currN.predecessor;
		}
		l.add(startNode);		
		
		System.out.println("BELLMAN PATH IS :" + l);
		
		for (Node n : nodes ){
			System.out.println("Shortest path to " + n.id + " is " + n.distance + "with predecessor "+n.predecessor);
		}

		return l;
	}


	// ---- Methods to visualize the operations of a graph algorithm -----

	/**
	 * Returns an index, representing the color of the given node.
	 * 0:black, 1:blue, 2:red, 3:green, 4:orange, 5:gray
	 * You may want to paint nodes that don't exist in gray.
	 * 
	 * @param nodeNumber the node for which the color
	 * 		is requested, as returned by addNode()
	 * 
	 * @return an index between 0 and 5, representing the color of the given node
	 */
	@Override
	public int getColorOfNode(Node node) {
		if (null == node) {
			return 5;
		} else {
			return node.status;
		}
	}
	
	@Override
	public int getColorOfNode(int nodeID){
		return getColorOfNode(nodes.get(nodeID));
	}

	/**
	 * Returns an index, representing the color of the given edge.
	 * 0:black, 1:blue, 2:red, 3:green, 4:orange, 5:gray
	 * You may want to paint edges that don't exist in gray.
	 * 
	 * @param sourceNodeNumber the index of the node where the edge starts
	 * 	, as returned by addNode()
	 * @param targetNodeNumber the index of the node where the edge ends
	 * 	, as returned by addNode()
	 * 
	 * @return an index between 0 and 5, representing the color of the given edge
	 */
	@Override
	public int getColorOfEdge(Node startNode, Node targetNode) {
		if (null == startNode || null == targetNode) {
			return 5;
		}
		LinkedList<Edge> incidentEdges = nodes.get(startNode.id)
				.getIncidentEdges();
		// search edge
		Edge searchedEdge = null;
		for (Edge nextEdge : incidentEdges) {
			if (nextEdge.endnode.id == targetNode.id) {
				searchedEdge = nextEdge;
			}
		}
		if (null == searchedEdge) {
			return 5;
		} else {
			return searchedEdge.status;
		}
	}

	// ---- Methods to stop and resume the execution of a graph algorithm -----

	// synchronization variable used to stop and resume processing
	private boolean isStopped = false;

	/**
	 * Tests if the current execution of a graph-algorithm was stopped
	 * @return true if the processing is stopped, false otherwise. 
	 */
	@Override
	synchronized public boolean isStopped() {
		return this.isStopped;
	}

	/**
	 * Stops or resumes the current executed graph algorithm
	 * @param status true to stop, false to resume execution
	 */
	@Override
	synchronized public void setStopped(boolean status) {
		this.isStopped = status;
	}

	/**
	 * Stops the execution of an algorithm until the visualization signals that
	 * he can be resumed. This feature can be used to visualize the steps of the
	 * algorithms.
	 */
	private void stopExecutionUntilSignal() {
		if (showSteps) {
			System.out.print(".");
			this.setStopped(true);
			while (this.isStopped()) {
				this.sleep(100);
			}
		}
	}

	/**
	 * Stops the execution for a given time
	 * 
	 * @param milliseconds
	 *            time the execution will stop in milliseconds
	 */
	private void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// the VM doesn't want us to sleep anymore,
			// so get back to work
		}
	}

	/**
	 * Sets the showSteps flag.
	 * @param show the flag
	 */
	@Override
	public void setShowSteps(boolean show) {
		this.showSteps = show;
	}
}

