import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;


/**
 * @author AlgoDat Team
 * 
 */
public class Network implements INetwork {
	// -- attributes --
	private HashMap<Integer, Node> nodes;

	public INetwork residualGraph;

	// -- constructor --
	public Network() {
		nodes = new HashMap<Integer, Node>();
		residualGraph = null;
	}


	// -- node functions --
	/**
	 * returns the nodes
	 */
	@Override
	public LinkedList<Node> getNodes() {
		return new LinkedList<Node>(nodes.values()); //nodes.values() only returns a Collection, not a List, so we need to create it
	}


	@Override
	public Node addNode() {
		int newId = nodes.size();
		Node newNode = new Node(newId);
		nodes.put(newId, newNode);
		return newNode;
	}

	// -- edge functions --
	public void addEdge(Node startNode, Node endNode, int capacity) {
		if (!(testEdgeNodes(startNode, endNode)))
			inputError();
		startNode.addEdge(startNode, endNode, capacity);
	}

	public void addEdge(int startnode, int endnode, int capacity) {
		addEdge(nodes.get(startnode), nodes.get(endnode), capacity);
	}

	/**
	 * Returns graph edge specified by source and destination indices.
	 * 
	 * @param startNodeInd
	 *            index of start node
	 * @param targetNode
	 *            index of target node
	 */
	public Edge getEdge(int startNodeInd, int targetNodeint) {
		Node n = nodes.get(startNodeInd);
		for (Edge e : n.getIncidentEdges())
			if (e.endNode.id == targetNodeint)
				return e;
		return null;
	}

	public boolean testEdgeNodes(Node startNode, Node endNode) {
		return (startNode != null) && (endNode != null)
				&& nodes.values().contains(startNode)
				&& nodes.values().contains(startNode);
	}

	// -- state reset functions --
	/**
	 * resets the state of all nodes and edges to white
	 */
	public void clearMarksAll() {
		clearMarksNodes();
		for (Node currentNode : nodes.values())
			for (Edge currentEdge : currentNode.getIncidentEdges())
				currentEdge.status = Edge.WHITE;
	}

	/**
	 * help function to reset the state of all nodes to white
	 */
	public void clearMarksNodes() {
		for (Node n : nodes.values())
			n.status = Node.WHITE;
	}

	public boolean isAdjacent(Node startNode, Node endNode) {
		if (!(testEdgeNodes(startNode, endNode)))
			inputError();
		return startNode.hasEdgeTo(endNode);
	}

	/**
	 * Searches for sources in the graph
	 * 
	 * @return All sources found in the graph
	 */
	public Node findSource() {
		LinkedList<Node> sources = new LinkedList<Node>();
		boolean isSource = true;
		// source <-> no incoming edges
		for (Node n : nodes.values()) {
			isSource = true;
			for (Node m : nodes.values()) {
				if (!m.equals(n) && isAdjacent(m, n)) {
					isSource = false;
					break;
				}
			}
			if (isSource)
				sources.add(n);
		}
		// error handling
		testSingle(sources);
		return sources.getFirst();
	}

	/**
	 * Searches the graph for sinks.
	 * 
	 * @return All sinks found in the graph
	 */
	public Node findSink() {
		LinkedList<Node> sinks = new LinkedList<Node>();
		// sink <-> no incoming edges
		for (Node n : nodes.values()) {
			if (n.getIncidentEdges().isEmpty())
				sinks.add(n);
		}
		// error handling
		testSingle(sinks);
		return sinks.getFirst();
	}

	public void testSingle(LinkedList<Node> nodes) {
		if (nodes.size() == 0 || nodes.size() > 1)
			inputError();
	}

	/**
	 * Finds an augmenting path from start to end in the graph A path is
	 * augmenting if all it's edges have residual capacity > 0 (You can choose
	 * from several algorithms to find a path)
	 * 
	 * @param startNodeId
	 *            the id of the start node from where we should start the search
	 * @param endNodeId
	 *            the id of the end node which we want to reach
	 * 
	 * @return the path from start to end or an empty list if there is none
	 */
	public LinkedList<Node> findAugmentingPath(int startNodeId, int endNodeId) {
		Node startNode = nodes.get(startNodeId);
		Node endNode = nodes.get(endNodeId);

		// this list is also the stack for depth first search
		LinkedList<Node> path = new LinkedList<Node>();

		for (Node n : nodes.values())
			n.status = Node.WHITE;

		Node current = startNode;
		// put start node to the stack
		path.addLast(current);

		// depth first search until we found the sink or we couldn't find a path
		while (!current.equals(endNode) && !path.isEmpty()) {
			// get last node on stack
			current.status = Node.GRAY;
			current = path.removeLast();

			Edge e;
			for (Node aNode : current.getSuccessorNodes()) {
				e = getEdge(current.id, aNode.id);
				// search a white neighbor
    			if ((aNode.status == Node.WHITE) && (e.capacity > 0)) {
	    			// if we found a white neighbor and current is not the end node
			    // we add the current node and the neighbor to the path
		    		if (!current.equals(endNode)) {
	    				path.addLast(current);
    					path.addLast(aNode);
					}
					break;
				}
			}
		}
		// we have to add the end node to the path, but only if we have found a
		// path.
		if (!path.isEmpty())
			path.addLast(current);

		// clean up
		clearMarksAll();
		return path;
	}

	// -- code to complete --

	/**
	 * Computes the maximum flow over the network with the Ford-Fulkerson
	 * Algorithm
	 * 
	 * @returns Value of maximal flow
	 */
	public int fordFulkerson() {
		int totalFlow = 0;

		Node source = findSource();
		Node sink = findSink();
		// create a new residual graph:
		residualGraph = initializeResidualGraph();
		
		// search for a path from source to sink in the residual graph
		LinkedList<Node> path = residualGraph.findAugmentingPath(source.id,
				sink.id);
		// find path source -> sink -> keep enlarging flow
		while (!path.isEmpty()) {
			// search min capacity in path found
			int minResidualCapacity = residualGraph.findMinCapacity(path);
			totalFlow += minResidualCapacity;
			// enlarge flow along path by min capacity
			residualGraph.updateResidualCapacity(minResidualCapacity, path);
			// again search path from source to sink in residual graph
			path = residualGraph.findAugmentingPath(source.id, sink.id);
		}
		// set flows on real graph
		Node nReal;
		int endNodeInd;
		Edge eResGr;
		for (Integer nodeInd : nodes.keySet()) {
			nReal = nodes.get(nodeInd);
			for (Edge eReal : nReal.getIncidentEdges()) {
				endNodeInd = eReal.endNode.id;
				eResGr = residualGraph.getEdge(nodeInd, endNodeInd);
				eReal.currentFlow = eReal.capacity - eResGr.capacity;
			}
		}
		
		
		return totalFlow;
	}


	/**
	 * Finds min cut in this graph
	 * 
	 * 
	 * @return a list of edges which have to be cut for 
	 * severing the flow between source and sink node
	 */
	public List<Edge> findMinCut() {
		
		int maxFlow = fordFulkerson();
		List<Edge> cutEdges = new LinkedList<Edge>();
		Node source = findSource();
		LinkedList<Node> S= residualGraph.findReachableNodes(source.id); //nodes reachable from sink in res graph
		LinkedList<Node> T=  new LinkedList<Node>(); //all nodes not reachable from sink in res graph
		LinkedList<Edge> currAdj = null;
		this.clearMarksAll();
		
		
		//we add all the nodes which are not reachable from sink to no Reach list
		for(Node n : nodes.values()){
			if (S.contains(n)== false){
				T.add(n);
			}
		}
		
		// if there is no Edge between 2 Nodes return will be null
		//probire jede kombination zwischen allen
		// nimm jedes node und von jedem node alle inc Edges
		for(Node n : nodes.values()){
			currAdj = n.getIncidentEdges();
			for(Edge e : currAdj){
				// If the Edge E is connecting a Vertex from S and T  its a Edge for Min cut  
				if(e.status==0 &&(S.contains(e.startNode) && T.contains(e.endNode))|| (S.contains(e.endNode)&& T.contains(e.startNode))){
					cutEdges.add(e);	
				}
				e.status=1;

		}
		
		}	
		return cutEdges;
	}


	public LinkedList<Node> findReachableNodes(int startNode) {
		return findReachableNodes(nodes.get(startNode));
	}

	/** Does a Breadth-First-Search to find the set of nodes which 
	 * are connected to the startNode with nonzero flow capacity
	 * 
	 * @argument startNode: start/source node
	 * 
	 * @return List of nodes that are reachable from source 
	 */	
	public LinkedList<Node> findReachableNodes(Node startNode) {
		
		LinkedList<Edge> currAdj = startNode.getIncidentEdges();
		Queue<Node> queueN= new LinkedList<Node>();//Our que
		this.clearMarksAll();
		
		queueN.add(startNode);
		Node currNode=null;
		LinkedList<Node> nodeList = new LinkedList<Node>();//where we save the nodes in the order we visited them
		nodeList.add(startNode);
		
		while (queueN.isEmpty()!= true){
			currNode=queueN.poll();
			System.out.println(queueN);
			System.out.println("Curr Node is : "+ currNode.id);
			currNode.status=2;//we will add all the surrounding nodes for curr
			currAdj=currNode.getIncidentEdges();
			for(Edge e : currAdj ){//get all the surrounding nodes for curr noodes we havent visited and add them to que 
				if(e.capacity>0 && e.status==0){
					System.out.println("Visiting Edge With Endnonde "+e.endNode.id + "and capacity " + e.capacity );
					e.status=1; 
					if (e.endNode.status==0){
						queueN.add(e.endNode);
						e.endNode.status=1;
						nodeList.add(e.endNode);
					}
					}
				//else {}// the que capacity is 0 or below, so its unimporant for us and we can remove it 
				}
			}	
		
		
		return nodeList;
	}	
	
	/**
	 * Builds a residual graph from a flow graph
	 * 
	 * @return the residual graph of this flow graph
	 */
	public INetwork initializeResidualGraph() {

		Network residualGraph = new Network();
		Edge reverseEdge;
		// adding nodes
		for (int i = 0; i < nodes.values().size(); i++)
			residualGraph.addNode();
		// adding edges
		for (Node n : nodes.values()) {
			for (Edge e : n.getIncidentEdges()) {
				// Add forward edges with same capacity
				residualGraph.addEdge(n.id, e.endNode.id, e.capacity);
				// Add backwards edges
				reverseEdge = getEdge(e.endNode.id, n.id);
				if (reverseEdge != null)
					residualGraph.addEdge(e.endNode.id, n.id, reverseEdge.capacity);
				else
					residualGraph.addEdge(e.endNode.id, n.id, 0);
			}
		}
		return residualGraph;
	}

	
	
	/**
	 * Finds the minimal residual capacity over the given path
	 * 
	 * @return the minimal capacity
	 */
	public int findMinCapacity(LinkedList<Node> path) {
		int minCapacity = Integer.MAX_VALUE;
		Node curr, currNext;
		for (int i = 0; i < path.size() - 1; i++) {
			curr = path.get(i);
			currNext = path.get(i + 1);
			for (Edge e : curr.getIncidentEdges()) {
				if ((e.endNode.equals(currNext)) && (minCapacity > e.capacity)) {
					minCapacity = e.capacity;
					break;
				}
			}
		}
		return minCapacity;
	}

	/**
	 * Update capacity on given path, to be executed on residual graph
	 */
	public void updateResidualCapacity(int minCapacity, LinkedList<Node> path) {
		Node current, next;
		// take the nodes out of the path and search for the right edge
		// if we found it, we increase the flow on this edge
		for (int i = 0; i < path.size() - 1; i++) {
			current = nodes.get(path.get(i).id);
			next = nodes.get(path.get(i + 1).id);
			current.getEdgeTo(next).capacity -= minCapacity;
			next.getEdgeTo(current).capacity += minCapacity;
		}
	}

	/**
	 * Calculates the current flow in this graph.
	 * 
	 * @param source
	 *            the source of the flow
	 * 
	 * @return the value of the flow
	 */
	public int getFlow(Node source) {
		int flow = 0;
		for (Edge e : source.getIncidentEdges()) {
			if (e.currentFlow > 0)
				flow += e.currentFlow;
		}
		return flow;
	}

	public LinkedList<Node> breadthFirstSearch(int startNode) {
		return breadthFirstSearch(nodes.get(startNode));
	}

	public LinkedList<Node> breadthFirstSearch(Node startNode) {
		LinkedList<Node> nodeList = null;
		clearMarksNodes();

		if (startNode == null || !nodes.values().contains(startNode)) {
			nodeList = new LinkedList<Node>();
		} else {
			nodeList = new LinkedList<Node>();
			LinkedList<Node> queue = new LinkedList<Node>();

			startNode.status = Node.GRAY;
			queue.addLast(startNode);

			while (!queue.isEmpty()) {
				Node current = queue.removeFirst();
				current.status = Node.BLACK;
				nodeList.addLast(current);

				for (Node neighbor : current.getSuccessorNodes()) {

					if (neighbor.status == Node.WHITE) {
						neighbor.status = Node.GRAY;
						queue.addLast(neighbor);
					}
				}
			}
		}
		return nodeList;
	}

	// -- utils --
	public void inputError() {
		System.out.println("Incorrect input.");
		System.exit(1);
	}
}

