import java.util.Comparator;

/**
 * This class implements a heuristic based on the "Euclidean" distance metric (L2 Norm)
 * 
 **/
 public class HeuristicEuclidean implements Comparator<CellNode> {
	
	    /*
	    This is the goal node the heuristic uses to estimate the remaining distance
	    */
		CellNode goal;
		
		public HeuristicEuclidean(CellNode goal) {
			this.goal = goal;
		}
		
		/**
		 * Computes an estimate of the remaining distance from node n to the goal node and
		 * updates the node attribute distanceRemainingEstimate
		 * 
		 *  This class implements the L2 norm (euclidean distance)
		 *  as the permissible heuristic
		 *  
		 * @param n
		 * 		node to estimate the remaining distance from
		 */
		public void estimateDistanceToGoal(CellNode n) {
        	//Your implementation here
			n.distanceToGoalEstimate = 0.0; //Setting this to 0.0 effectively turns A* into Dijkstra
			double x = n.getRow(); 
			double y = n.getColumn();
			
			x-= this.goal.getRow();
			x= x*x;
			y-= this.goal.getColumn();
			y= y*y;
			
			y = Math.sqrt(x+y);
			
			n.distanceToGoalEstimate=y;
			
        	return;
		}
		
		
		
		/*
		 * compares two nodes based on the distance heuristic
		 *  
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */		
        public int compare(CellNode n1, CellNode n2) {
        	estimateDistanceToGoal(n1);
        	estimateDistanceToGoal(n2);
        	double d1 = n1.distanceToGoalEstimate + n1.distance;
        	double d2 = n2.distanceToGoalEstimate+ n2.distance;
        //	System.out.println("N1 Dist is"+n1.distance +"and N2 " +n2.distance+" and res is " +result);
        	
        	if(d1 <d2){
        		System.out.println("returning -1");
        		return -1;}
        	
        	if(d1 >d2){
        		System.out.println("returning 1");
        		return 1;} else 
        	
        	return 0;        	
        }	
}

