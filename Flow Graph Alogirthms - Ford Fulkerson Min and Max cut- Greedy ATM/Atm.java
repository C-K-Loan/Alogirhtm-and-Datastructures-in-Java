import java.util.LinkedList;
import java.util.List;
import java.lang.RuntimeException;

/**
 * The class <code>Node</code> implements a node in a network.
 * 
 * @author AlgoDat team
 */
public class Atm {

	public LinkedList<Integer> denominations;

    /**
	 * Initializes the banknote denominations available to the ATM
	 *
	 * @param name
	 *            the drawn value in visualization
	 **/
	//sollte done sein vielleicht noch randfälle checken oder überlegen
	public Atm() {
		// initialize list of available denominations
		denominations = new LinkedList<Integer>();
		//Add denominations in a sorted order, highest value first:
		denominations.add(200);
		denominations.add(100);
		denominations.add(50);
		denominations.add(20);
		denominations.add(10);
		denominations.add(5);
	}

	/**
	 * Computes the the number of banknotes for each denomination
	 * 
	 * @param total
	 *            Amount of money requested
	 *            End point of this edge.
	 * @return List<int> 
	 *            Amount of banknotes for each denomination, 
	 *            as a list in the same order as the list denominations
	 *            Example: [0,1,0,0,0,0]: one 100EUR banknote
	 */
	public LinkedList<Integer> getDivision(int total) {
		
		//fehler behandlung
		//if(total ==null) return null;
		if(total <0) return null;
		

		LinkedList<Integer> division = new LinkedList<Integer>();
		int div = 0;
		
		for (int e : denominations){// start with 200 note, then 100 etc..
		division.add(total/e);
		div = total/e;
	//	System.out.println(e + " fits " + div);
		total -=div*e;
		}
		
		System.out.println("You can pay " +total + " with "+ division);
		return division; 
	}
}

