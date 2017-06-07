import java.util.Comparator;
import java.lang.Math;

/**
 * This class implements a heuristic based on the "Manhatten" distance metric (L1 Norm)
 * 
 **/
public class HeuristicManhattan implements Comparator<CellNode> {
	
		/**
		 * The goal node which the heuristic operates on:
		 */
		CellNode goal;
		
		/**
		 * 
		 * @param goal
		 * 		the target/goal node the heuristic should be computed with
		 */
		public HeuristicManhattan(CellNode goal) {
			this.goal = goal;
		}
		
		/**
		 * Computes an estimate of the remaining distance from node n to the goal node and
		 * updates the node attribute distanceRemainingEstimate
		 * 
		 *  This class implements the L1 norm ("Manhattan" distance)
		 *  as the permissible heuristic
		 *  
		 * @param n
		 * 		node to estimate the remaining distance from
		 */
		public void estimateDistanceToGoal(CellNode n) {
			double x = n.getRow(); 
			double y = n.getColumn();
			
			x-= this.goal.getRow();
			if(x<0) x *=-1;

			y-= this.goal.getColumn();
			if(y<0) y*=-1;
			
			n.distanceToGoalEstimate= x+y;
			
        	 
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

