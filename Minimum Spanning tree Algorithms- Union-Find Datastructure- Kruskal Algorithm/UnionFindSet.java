import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A class implementing a Union-Find-data structure with representatives.
 * 
 * @author AlgoDat-Team
 */
public class UnionFindSet<T>{

	//You can use this map to store the representant for each element of the Union find structure
	//First Value is Key Of Node  and Second Value is his representant 
    private HashMap<T,T> element2representative;

	public UnionFindSet() {
		element2representative = new HashMap<>();
	}

	/**
	 * Takes a collection of n elements and adds 
	 * n disjoint partitions each with one element in it.
	 * 
	 * Each Element is stored with Element as  KEy and Element as reperesentn
	 * 
	 * @param set
	 *            The set to be partitioned.
	 */
	public void add(List<T> elements) {
		
		for(T  t : elements){
			add(t);
			
		}
		
	}

	/**
	 * Creates one disjoint partition with the element in it 
	 * and adds it to the Union-Find data structure
	 * 
	 *Element Is Stored With KEy Being THe element itself. The Representant is also the Element, since it is a singular union
	 * 
	 * 
	 * @param element
	 *            The element to put in the partition.
	 */
	public void add(T element) {
		element2representative.put(element, element);
	}

	/**
	 * Retrieves the partition which contains the wanted element.
	 * 
	 * A partition is identified by a single, representative element.
	 * This function returns the representative of the partition
	 * that contains x. 
	 * 
	 * @param x
	 *            The element whose partition we want to know
	 * @return 
	 *            The representative element of the partition
	 */ //TODO FEHLER BEHANDLUNG
 	public T getRepresentative(T x) {
		
	return	element2representative.get(x); 
	}

	/**
	 * Joins two partitions. First it retrieves the partitions containing the
	 * given elements. Removes one of the joined partitions from
	 * <code>partitions</code>.
	 *            x'S  representant  will be the new Representant of the unified Union
	 * 
	 * @param x
	 *            One element whose partition is to be joined.
	 * @param y
	 *            The other element whose partition is to be joined.
	 */
	public void union(T x, T y) {
		
		if (getRepresentative(y)== getRepresentative(x)) return ;  //they are from the same union 
		
		//okay y and x have different representators, so we join them
		
		T repGlobal = getRepresentative(x);
		T repOld= getRepresentative(y);
		T value = x;
		T key = x;
		// we Dont need to Change any of the Edges who HAve X's representant  because they all have global rep as representant
		//we Must find everyone that has the Same representant(repOld) as Y and give him the Representant of Rep global
		
		//element2representative.
		
		for(Entry<T, T> entry : element2representative.entrySet()) {// iterates over Every Entry in the Hashmap
		    key = entry.getKey();
		    value = entry.getValue();
		    if(value==repOld){// if one of the Values(reps) is the same as the Oldrep, remove this Entry and add it again with repGloal
		    	//System.out.println("Setting " );
		    	//if (element2representative.containsKey(key)) element2representative.remove(key);
		    	element2representative.put(key, repGlobal);

		    }
	
		}
	
		
		
	}
}

