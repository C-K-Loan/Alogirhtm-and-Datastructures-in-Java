import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import javax.swing.text.StyleContext.SmallAttributeSet;

import org.junit.internal.ExactComparisonCriteria;
import org.omg.CORBA.OMGVMCID;

/**
 * @author Uwe + Damien
 * 
 */
public class Network implements INetwork {
	// -- attributes --
	private HashMap<Integer, Node> nodes;
	public int maxFlowInNetwork;

	// -- constructor --
	public Network() {
			nodes = new HashMap<Integer, Node>();
			//nodes.
	}

	// -- node functions --
	/**
	 * returns the nodes
	 */
	@Override
	public LinkedList<Node> getNodes() {
		
		return (LinkedList<Node>) nodes.values();
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
	
	
	/*
	 * coneverts a collection of nodes into a LinkedList of nodes 
	 */
	public LinkedList <Node> makeCollectionToList ( ){
		Collection<Node> nodeCollection = 		nodes.values();
		LinkedList <Node>nodeList = new LinkedList<Node>();
		for(Node n : nodeCollection	){
			nodeList.add(n);
		}
		
		return nodeList;
		
	}

	
	public int getAmountOfVistitableNodes(Node node){
		int amount=0;
		LinkedList<Edge> currAdj = node.getIncidentEdges();
		
		for(Edge e: currAdj){
			if (e.capacity >0  ) amount++;
		}
		return amount;
	}
	
	/**
	 * Finds an augmenting path from start to end in the graph A path is
	 * augmenting if all it's edges have residual capacity > 0 (You can choose
	 * from several algorithms to find a path)
	 * finden path für den es in allen edges  eine capacity min  >0 gibt 
	 * @param startNodeId
	 *            the id of the start node from where we should start the search
	 * @param endNodeId
	 *            the id of the end node which we want to reach
	 * 
	 * @return the path from start to end or an empty list if there is none
	 */
	
	
	
	/*TODO
	 *testing
	 *sollte eigentlich stimmen, ich bin mir ein bisschen unsicher ob das mit der abbruchedingungn bei  dem Stack und so klappt aber sollte eigentich richtig sein
	 *
	 * Fehlerbehandlign implementieren, einfach paar expections e throwen
	 * 
	 */ // ees gibt einen enedlosslooop aus righeneineinem grund in diieser schleife, ka warum 
	
	
	
	public LinkedList<Node> findAugmentingPath(int startNodeId, int endNodeId) {
		/*
		 * Init
		 * set all nodes to white
		 * set all edge to white and their curflow to 0
		 */

		//fehlerbehandlung
//		Exception  x = new Exception();
//		if (nodes.get(startNodeId)== null) throw x;
//		if (nodes.get(endNodeId)== null) throw x;


		LinkedList<Node> nodeList = makeCollectionToList();
		Node endNode = nodes.get(endNodeId);
		Node curNode = nodes.get(startNodeId);
		LinkedList<Node> augPath = new LinkedList<Node>();//where we save the nodes in the order we visited them
		Stack<Node> stack= new Stack<Node>();//stack der abzuarbeitenen Elemente 
		augPath.add(curNode);
		stack.add(curNode);
		Edge firstEdge;
		int i = 0;
		firstEdge = curNode.edges.getFirst();
		Node oldCurNode = new Node(3231);
		this.clearMarksAll();
		while( stack.empty()==false && curNode != endNode  && oldCurNode!= curNode){
		curNode=stack.peek();
		
		System.out.println("Visitable Nodes are : " + getAmountOfVistitableNodes(curNode));
			while (getAmountOfVistitableNodes(curNode)==0) { //check if there are any usable Edges Left, if not Return EMpty List since We are at an Dead end 	
				stack.pop();
				curNode.status=2;
				System.out.println("popping "+ curNode.id);
				if(stack.size()==0) break;//if stack empty work is done we can break ehehehe	
				curNode=stack.peek();
		}
		
		for (Edge e : curNode.edges){
			System.out.println( "CURNODE IS "+ curNode.id + "and Edge e" + e +" THIS HAS CAPACITY " + e.capacity + " AND STATUS " + e.status);
			if(e.endNode== curNode)i++;
			System.out.println("i is " + i+ " and old Curnode is " + oldCurNode.id);
			if(i==2 ) return new LinkedList<Node>();
				
			if(e.capacity > 0  ){
				System.out.println("IN IF " );
				oldCurNode = curNode;
				curNode=e.getEndnode();
				e.status=1;
				stack.push(curNode);
				augPath.add(e.endNode);
				
				break;
			}

		}
		
		}		
		
		if (getAmountOfVistitableNodes(curNode)==0 && curNode!=nodes.get(endNodeId)) return new LinkedList<Node>();//We arrived at a node where there was no edge with capacit left and we werent at the endnode, so no more aug paths left
		
//		while (stack.isEmpty()==false && curNode!= endNode ){//traverses graph in depthsearch style, we stop when stack is empty  (meaning no path left) or when we found the endnode 
//		
//			for(Edge e : curNode.edges){
//				System.out.println("WE ARE CHECKING EDGES FROM NODE  : " + curNode.id );
//				System.out.println("EDGE HAS CAPACITY: " +e.capacity + " and CURRENT FLOW "+ e.currentFlow);
//				if (e.startNode.id == endNodeId) break;
//		
//				if (e.startNode == nodes.get(endNode))
//				if (e== firstEdge){	i++; if (i ==2 ) {System.out.println("No Augmentet Path Found! " );return new LinkedList<Node>();}} // We have iteratetet over the same edge Multiple Times, this means there are no edges From the startnode Left which have flow, Se we return an empty list 
//				if (e.currentFlow <e.capacity){ //this edge has some flow left, so we take this edge and stop looking at the others
//				//	System.out.println("We found a suitable Edge for the Path" + );
//					augPath.add(e.endNode);
//					stack.remove(curNode);
//					curNode=e.endNode;
//					stack.add(curNode);
//					break;
//				}
//				
//			}
//			
//		}
		
	//	if (stack.isEmpty()==true) return new LinkedList<Node>(); // no path found
		System.out.print("AUGMENTET PATH IS : " );

		for (Node n : augPath){
			
			System.out.print(" : "+n.id);
		}
		System.out.println("");
		
		return augPath; //we must  have found the endnode sind the stack isnt empty
	}


	/**
	 * Computes the maximum flow over the network with the Ford-Fulkerson
	 * Algorithm
	 * 
	 * @returns Value of maximal flow
	 */
	public int fordFulkerson() {

		
		Network residualGraph = initializeResidualGraph();
		residualGraph.maxFlowInNetwork= 0;
		Node source = findSource();
		Node sink = findSink();
		
		LinkedList<Edge> edgeList= new LinkedList<Edge>();
		LinkedList<Node> nodeList =	makeCollectionToList();//residualGraph.getNodes();// this.getNodes();
		int newCurFlow = 0;

		//System.out.println(nodeList);
		System.out.println("SOURCE IS : " + source.id + "SINK IS :"+ sink.id);

		LinkedList<Node> augPath	= findAugmentingPath(source.id, sink.id);
		/*
		 * Init
		 * set all nodes to white
		 * set all edge to white and their curflow to 0
		 */
		for(Node n : nodeList){
			edgeList= n.getIncidentEdges();	
			for(Edge e : edgeList){
				e.currentFlow=0;
				e.status=0;
			}	
		}
		
 		while (augPath.isEmpty() == false ) {  // while there is stil an Aug Path, we continue adding flow
 			augPath=findAugmentingPath(source.id, sink.id);
 			newCurFlow= findMinCapacity(augPath);
 			//residualGraph.updateResidualCapacity(newCurFlow, augPath); // update the capacity and  edges 
 			System.out.println("CUR FLOW RECIEVED IS"+  newCurFlow );
 			this.maxFlowInNetwork+=newCurFlow;
			updateResidualCapacity(newCurFlow, augPath);
			
			augPath=findAugmentingPath(source.id, sink.id);
			
 		}
 		System.out.println("MAXFLOW IS IN NETWORK IS : " + this.maxFlowInNetwork);
				return maxFlowInNetwork;
	}


	/**
	 * Builds the residual graph to a flow graph
	 * 
	 * @return the residual graph to this flow graph
	 */
	public Network initializeResidualGraph() {

		Network residualGraph = new Network();

		// adding nodes
		for (Node n : nodes.values())
			residualGraph.addNode();
		// adding edges
		for (Node n : nodes.values()) {
			for (Edge e : n.getIncidentEdges()) {
				// Add forward edges with same capacity
				residualGraph.addEdge(n.id, e.endNode.id, e.capacity);
				// Add backwards edges
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
	
	//TODO
	/*
	 * FEHLERBEHANDLUNG
	 * 
	 */
	public int findMinCapacity(LinkedList<Node> path) {
		System.out.println("Searching for SMALLESTSHARED FLOW");

		if (path == null){ System.out.println(" PATH WAS NULL IN FIND MIN CAP"); return 0;}
		if (path.isEmpty()) return 0 ;
		
		int i = 0;
		int restFlow=0;
		int smallestRestflow=0;
		//wierd init but it works
		for (Node n : path ){
			smallestRestflow =this.getEdge(n.id, n.id+1).capacity - this.getEdge(n.id, n.id+1).currentFlow;
		break;
		}
		int[] idArray = new int[path.size()];//save all the ids  of the path in an array so we can acsess them easier later on 
		for(Node n : path){
			idArray[i]=n.id;
		i++;
		}

		i=0;

		for (Node n : path ){
				if(i+1>=idArray.length) break;	//we have arrived at last Node, there is no Edge left to chedck and Checking next index would give us a crash
				System.out.println("lbub");
				System.out.println("Edge From : " + n.id + "to "+idArray[i+1]);
			//	System.out.print("The Edge has Capacity : " + n.getEdgeTo(idArray[i]).capacity + "and curFlow ");
		
				
				restFlow=getEdge(n.id, idArray[i+1]).capacity -getEdge(n.id, idArray[i+1]).currentFlow;
				System.out.println("lb22ub");

				//	System.out.println(", with Restflow " + restFlow);
				if (restFlow<smallestRestflow) smallestRestflow=restFlow;// if CurRestflow >maxFlow we have a new MAxflow 
				i++;
		}
		
		
		System.out.println("Smallest SHared Flow FOR PATH IS : "+ smallestRestflow);
		
		return smallestRestflow;
	}

	/**
	 * Update capacity on given path, to be executed on residual graph
	 */
	
	/*
	 * TODO
	 * sosllte eigneltich richtig sein, vielleicht noch fehlerbehandlung checken  
	 */
	public void updateResidualCapacity(int minCapacity, LinkedList<Node> path) {
	//	System.out.println("UPDATING STUFF" );
		int i = 0;
		
		for (Node n : path ){
			if (n==path.getLast()) break;// we are at last node, so there are no edges to check left 
	
			this.getEdge(n.id, path.get(i+1).id).capacity -= minCapacity; // the  outgoing edge gets  min Cap added on
			//this.getEdge(path.get(i+1).id, n.id).capacity=-minCapacity ; //the ingoing edge 	

			
			
			i++;
		}
	//	System.out.println("DONE UPDATING STUFF" );
	
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

