import java.util.ArrayList;
import java.util.ListIterator;


/**
 * This class implements three methods. These test an array on a few
 * characteristics.
 *
 * @author AlgoDat-Tutoren
 *
 */
public class ArrayCheck {
	/**
	 * Tests all elements of the given array,
     * if they are divisible by the given divisor.
     *
     * @param arr
     *            array to be tested
     * @param divisor
     * 				number by which all elements of the given array should be divisible
     * @return true if all elements are divisible by divisor
     */
	
	
	
	public boolean allDivisibleBy(ArrayList<Integer> arr, int divisor) {
    	
    	if (arr== null)return false;
   		if(arr.isEmpty()|| divisor ==0 )return false ;//fehler Behandlung
    	
   		
        ListIterator<Integer> iterator = arr.listIterator();
        while (iterator.hasNext()){
        	if (iterator.next() % divisor !=0){return false; }    	
        }
    	

        	
        return true;
        
        
    }
    
    public void arrayPrint(ArrayList<Integer>arr){
    	ListIterator<Integer> iterator = arr.listIterator();
    	System.out.print("Array: ");
    	while(iterator.hasNext()){
        	int element = iterator.next();
    		System.out.print(element+",");
    	}
    	System.out.println("newline");
    }

    /**
     * Tests if the given array is a part of
     * the fibonacci sequence.
     *
     * @param arr
     *            array to be tested
     * 
     * @param fibSeq, array to store fib Numbers and Compare to
     * @param uppebound how many fib numbers to calcuate
     * @param n runindex
     * @param f2,fnew ints for calculating fib
     * @param fArr int to store array element
     * @param iterator iterator to walk array 
     * @return true if the elements are part of
     *         the fibonacci sequence
     */
    public boolean isFibonacci(ArrayList<Integer> arr) {
    	int upperbound,n,f2,fnew,fArr=0;
    	upperbound=0;

    	n=3;

        if ( arr==null)return false ; //fehler Behandlung
        if(arr.isEmpty()==true|| arr.size() <=3 ) return false;
        
        ListIterator<Integer> iterator = arr.listIterator();

        
    	//find biggest number in array for upperbound
    	while(iterator.hasNext()){
    		fArr=iterator.next();
    		if(fArr>upperbound) upperbound=fArr;
    	}
    	upperbound+=10;
    	fArr=0;
        int[] fibSeq =  new int[upperbound] ;
    	fibSeq[0]=0;
    	fibSeq[1]=1;
    	fibSeq[2]=1;
        
		//fülle array mit allen Fib zahlen bis zu einer grenze;
        while(n <upperbound){
        	fibSeq[n]=fibSeq[n-1]+fibSeq[n-2]; //fib algo
         	n++;
        }
        
        n=0; 
        //resett iterator for comparison
        iterator=arr.listIterator();

        	while(iterator.hasNext()){
        	System.out.println("starting comparison...");
        	fArr=iterator.next();
        	
        	
        	while(fArr!= fibSeq[n]){
        		System.out.println("Checking if first number is a fib");
        		n++;
        		if (n >= fibSeq.length) return false; //not single number matched fib, return false

        	}
        	
        	n++;//THE MAGICAL N++ that fixes everything
        	while(iterator.hasNext()){
        		System.out.println("Checking if the rest is matching");
            	fArr=iterator.next();
            	System.out.println("FArr is " +fArr+" and fib num is " +fibSeq[n] + "und arr size ist " + arr.size());

        		if (n>=fibSeq.length || fArr != fibSeq[n]) return false; //First number matched but one of the following broke the pattern, return false
        		n++;
        	}
        	
        	return true;
        }
        	
        	return true;
        
        
        
        
        /**
    	if (calculateFib(fArr) == true){//finde out if the first number is part of FIb seq
    		
    		while(fnew!= fArr) { //get to the first fib number matching
    		fnew=f1+f2;
    		f1=f2;
    		f2=fnew;
    		}
    		
    		while(iterator.hasNext()){
    			fArr=iterator.next();
        		fnew=f1+f2;
        		f1=f2;
        		f2=fnew;
    			if(fnew!= iterator.next()){ System.out.println("Fnew is" + fnew +" and fArr is " +fArr); returnVal = false;}

    		}
    		
    	}
    	
    	
    	
    	
      	else {
    		arrayPrint(arr); 
    		System.out.println("Is Fib NL");
      		return false;
      	}

    	
    	
    	
    	
    	
    	
    	if (returnVal == true){ 
    		arrayPrint(arr); 
    		System.out.println("Is Fib NL");
    		}
    	else {
    		arrayPrint(arr); 
    		System.out.println("Is not FIB NL");}
    	return returnVal;
    	
    	*/
    }
    
    
    /**
	 * Check if upperbound number is part of the Fib Seq. 
     * @param upperbound highest fib number to calculate and to find out if it is part of fib seq
     * @return true: upperbound is a fib number, so return true
     * 		   false: upperbound is no fib number, return false;
     */
    public boolean calculateFib(int upperbound){
    	int f1,f2,fnew;
    	f1=0;
    	f2=1;
    	fnew=0;
    	
    	while(fnew <= upperbound){
    		if(fnew== upperbound){return true;}//the number is part of fib
    	
    		fnew=f1+f2;
    		f1=f2;
    		f2=fnew;
    	}
    	
    	return false;
    }

}

