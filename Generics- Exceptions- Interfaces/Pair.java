import org.hamcrest.core.IsInstanceOf;


public class Pair<T> {
	
	
	/**
	 * The first Component of the PAir
	 */
	private T first;
	
	/**
	 * The second Component of the PAir
	 */
	private T second;
	
	/**
	 * 
	 * @param first first component
	 * @param second second component 
	 * 
	 * if first classtype != second classtype we dont have a valid pair
	 */
	Pair(T first, T second) {
		if(first.getClass().equals(second.getClass() )){
		this.first=first;
		this.second=second;
		}
		else return;
	}

	/**
	 * 
	 * @return first element
	 */
	public T getFirst() {
		return this.first;
	}
	
	/**
	 * 
	 * @return second element
	 */

	public T getSecond() {
		return this.second;
	}

	
	/**
	 * 
	 * @param dt new first element
	 */
	public void setFirst(T dt) {
		if(this.first.getClass().equals(dt.getClass()))
		this.first=dt;
		else return;
	}

	/**
	 * 
	 * @param dt new second element
	 */
	public void setSecond(T dt) {
		if(this.second.getClass().equals(dt.getClass()))
		this.second=dt;
		else return;	}

	/**
	 * Swaps element in Pos1 with Element in pos 2
	 */
	public void swap() {
		T tmp = this.first;
		this.first=this.second;
		this.second=tmp;
	}
	
	/**
	 *  @return Returns a String  of the form "<first, <second>" without the <>
	 */
	@Override
	public String toString() {
		String text;
		try{
			text =this.first.toString()+", "+this.second.toString();
		}
		catch (Exception e){
			return "<not implemented>, <not implemented>"; 
		}

		
		return text;
	}
}

