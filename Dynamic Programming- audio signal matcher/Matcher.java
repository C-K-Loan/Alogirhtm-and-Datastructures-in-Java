
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

/**
 * Represents the match of two wave files
 * 
 * Implements the comparison and matching methods according
 * to the optimal dynamic programming presented in the tutorial
 */
public class Matcher implements IMatcher {

	/**
	 * Signal X
	 */
	protected ISignal signalX;
	/**
	 * Signal Y
	 */
	protected ISignal signalY;

	
	/**
	 * The distance/dissimilarity score between
	 * the two wav files. The score is calculated in the
	 * compute method
	 * 
	 *  @see Matcher.compute
	 *  @see Matcher.computeDistance
	 */
	protected double distance = -1.0;

	/**
	 * The path of minimal distance between the two
	 * wave files.
	 * 
	 * The first integer of the pair denotes the
	 * frame number of WavFile w, the second integer
	 * the frame number of WavFile v
	 * 
	 * It will be stored here by the compute method
	 * 
	 * @see Matcher.compute
	 */
	protected List<Pair<Integer, Integer>> matchingPath = null;

	/**
	 * The accumulated distance/dissimilarity matrix
	 * will be stored here by the compute method
	 * 
	 * @see Matcher.compute
	 */
	protected double accumulatedDistance[][] = null;
	
	
		/**
	 * Construct a WavDistance object
	 *
	 * With this constructor, you can use signals defined by a buffer
	 * 
	 * For both passed ISignal buffers we have to check that
	 * they only have one channel and that they do not
	 * exceed the maximum number of frames MAX_FRAMES
	 * 
	 * @param dataX Array with signal values
	 * @param dataY Array with signal values
	 * @param sampleRate in samples per second
	 */
	public Matcher(double[] dataX, double[] dataY, int sampleRate) {
		this.signalX = new SignalFromBuffer(dataX, sampleRate);
		this.signalY = new SignalFromBuffer(dataY, sampleRate);
	}


	/**
	 * Construct a Matcher object
	 *
	 * With this constructor, you can use signals defined by a buffer
	 * 
	 * For both passed ISignal buffers we have to check that
	 * they only have one channel and that they do not
	 * exceed the maximum number of frames MAX_FRAMES
	 * 
	 * @param x 1st Object with ISignal interface
	 * @param y 2nd Object with ISignal interface
	 */
	public Matcher(ISignal x,ISignal y) {
		this.signalX = x;
		this.signalY = y;
	}
	
	/**
	 * Warps each signal to mimic the other one
	 *
	 * @return Pair of warped signals
	 * @throws IOException, WavFileException
	 */
	public Pair<ISignal, ISignal> warpSignals()  {
		
		 ISignal resultX = new SignalFromBuffer(matchingPath.size(), signalX.getSampleRate()); 
		 ISignal resultY = new SignalFromBuffer(matchingPath.size(), signalY.getSampleRate()); 
		
		//Construct signals:
		Iterator<Pair<Integer, Integer>> it = matchingPath.iterator();
		int indexX=0;
		int indexY=0;
		for (int matchcounter = 0; matchcounter < matchingPath.size(); matchcounter++) {
			Pair<Integer, Integer> pair = it.next();
			indexX = pair.getLeft();
			indexY = pair.getRight();
			resultX.setFrame(matchcounter, this.signalX.getFrame(indexX));
			resultY.setFrame(matchcounter, this.signalY.getFrame(indexY));
		}
		//trim the signal to the actual length of the signal:
		resultX.trimTo(indexX);
		resultY.trimTo(indexY);
		
		Pair <ISignal, ISignal> result = new Pair<ISignal, ISignal>(resultX, resultY);
		return result;
	}

	/**
	 * Returns the computed distance
	 * 
	 * @return Calculated distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Returns the computed mapping 
	 * 
	 * @return Calculated mapping
	 */
	public List<Pair<Integer, Integer>> getMappingPath() {
		return matchingPath;
	}

	/**
	 * 
	 * @return the computed mapping between the two signals as an array
	 * 		row: matches
	 * 		column: signals
	 */
	public int[][] getMappingPathAsArray() {
		int[][] array = new int[matchingPath.size()][2];
		int count = 0;
		for (Pair<Integer, Integer> pair : matchingPath) {
			array[count][0] = pair.getLeft();
			array[count][1] = pair.getRight();					
			count++;
		}
		return array;
	}

	
	/**
	 * Compute the distance score and the match between
	 * the two WavFiles
	 * 
	 * The result of this computation is stored in score
	 * 
	 * @throws WavFileException
	 */
	public void compute() throws RuntimeException {
		System.out.println("------");
		
		//Check size of the distance matrix to avoid bombing the memory heap
		Long heapsize = ((long)signalX.getNumFrames()) * ((long)signalX.getNumFrames()) * Double.SIZE / 1048576;
		System.out.println("Allocating " + heapsize.toString() + "MB");
		if (heapsize >  4000) {  // 4000 MB
			throw new RuntimeException("Signals are too long - I would gobble up too much memory when matching them!");
		} 
		
		// initialize the accumulated distance matrix properly
		accumulatedDistance = initializeAccDistanceMatrix(signalX, signalY);
	
		// use dynamic programming to compute accumulated distance matrix 
		computeAccDistanceMatrix(accumulatedDistance, signalX, signalY);
		// compute and store the distance score for the two files 
		distance = computeDistance(accumulatedDistance);
		// compute the mapping between the two files
		matchingPath = computeMatchingPath(accumulatedDistance);
		
		System.out.println("Distance: " + distance);
		System.out.println("------");																																																								
		
	}
	
	/**
	 * Create an accumulated distance matrix for the wave file  
	 * and initialize correctly
	 * 
	 * @return Preinitialized accumulated distance matrix with correct dimensions
	 */				
	protected double[][] initializeAccDistanceMatrix(ISignal signalX, ISignal wavfileY) {
		int x = signalX.getNumFrames()+1;
		int y = wavfileY.getNumFrames()+1;
		
		// since all signals shout be same size it should be ok, otherwise fehlerbehandlung
		double matrix [][]= new double [x][y];//wie groß die diemensionen?
		matrix[0][0]	= 0;
		
		
		//set [0][i], first line of the matrix to infinity,  (expet 0,0 )
		for(int i=1; i<y; i++ ){	// break out when i = x,  thats when array error
			matrix[0][i]= Double.POSITIVE_INFINITY;
		
			
		}
		
		//set [i][0], first row of the matrix to infinity,  (expet 0,0 )
		for(int i=1; i<x; i++ ){	// break out when i = x,  thats when array error
			matrix[i][0]= Double.POSITIVE_INFINITY;
		
			
		}
		
		System.out.println("INITITED : ");
		//prettyprintMatrix(matrix);
		return matrix;
	}
	
	
	/*
	 *  Check the surrounding 3 entrys in the matrix for the smallest entry
	 *  whe check these neighbours and return the smallest  [x-1][y], [x][y-1], [x-1],[y-1]
	 *   
	 *  @param accDistance , the matrix we look in for to find the min
	 *  @param x  x index of the Element we look for neighbous 
	 *  @param y  y index of the Element we look for neighbous 
	 */
	public double getSmallestNeighbour (double accDistance[][], int x , int y){
		double min = Double.POSITIVE_INFINITY;
		
		if(accDistance[x-1][y]<min)  min = accDistance[x-1][y];
		if(accDistance[x][y-1]<min)  min = accDistance[x][y-1];
		if(accDistance[x-1][y-1]<min)  min = accDistance[x-1][y-1];

		
		
		return min;
	}
	
	
	/*
	 * Compares the Signal X and Y and position X[x] aand Y[y] and returns the difference 
	 * 
	 * computes Dij, = |Xi- Yj|
	 * 
	 * @param signalX A double array of frames of file wavfileX 
	 * @param signalY A double array of frames of file wavfileY
	 * 
	 * @param x, the index we  take from X for the comparision
	 * @param y, the index of Y we take for the comparision
	 */
	public double getDistanceX (ISignal signalX, ISignal signalY, int x, int y){
		double xVal = signalX.getFrame(x-1);
		double yVal = signalY.getFrame(y-1);
		double value;
		value = xVal-yVal;
		x=x-1;
		y=y-1;
		System.out.println("Calculating Distance for Index i: "+ x + "and J : " + y +" resulting in distance :" +value);
		//System.out.println("Calculating : " +xVal+" - "+ yVal+ "Equals "+ value + "for Indexes X : " +x+ "and Y : "	+y);
		//System.out.println("X Val is : "+ xVal   + "Y");
		if (value<0) return value*-1;
		else return value;
		
	}
	
	/**
	 * Read the frames from the two WavFiles and use them
	 * to compute the accumulated distance matrix
	 * 
	 * @param signalX A double array of frames of file wavfileX 
	 * @param signalY A double array of frames of file wavfileY
	 * @param accDistance Preinitialized accumulated distance matrix to fill/compute
	 */
//shoul be done maybe some fehlerbehandlung 
	protected void computeAccDistanceMatrix(double [][] accDistance, ISignal signalX, ISignal signalY) {
		if (accDistance == null) throw new RuntimeException("Called before initializing the distance matrix!");
		System.out.print("Computing...");
		int x= signalX.getNumFrames();
		int y= signalY.getNumFrames();
		double diff = 0;
		double min= 0;
		double entry=0;
		
		//iteriert die matrix von oben links, nach oben rechts, zeile für zeile (abgesehen  oberste zeile und linkeste zeile wegen init
		for(int i = 1; i <=x ; i++ ){
			for(int j = 1; j<=y; j++){
				diff= getDistanceX(signalX, signalY, i, j);//get diference 
				System.out.println("Difference is : "+ diff);
				System.out.println("min is : "+ min);
				min= getSmallestNeighbour(accDistance, i, j);//find smallest neighbour 
				accDistance[i][j]= diff+min;
				System.out.println("I  is " + i +" and x is " + x +" and Y:"+y+"and j:"+j);
				System.out.println("WE WILL DO THIS FOR I <=" +x+" AND Y<="+y);
				
			}
			
			
			
		}
		
		
		System.out.println("done");		
		return;
	}
	
	/**
	 * Compute the distance of the final accumulated distance
	 * of the two input WavFiles and return it
	 * 
	 * @see distance
	 */
	protected double computeDistance(double[][] accDistance) throws RuntimeException {
		if(accDistance == null) {throw new RuntimeException("Called before computing the distance matrix!");}

		int x = accDistance.length;
		int y = accDistance[0].length;
		
		return  accDistance[x-1][y-1];
		
	}
	
	
	
	
	
	
	/*
	 * finds the smallest neighbour  and returns it Index. Diagonal neighbhour B has a higher prio than the others, when  B<=Other Neigbhours 
	 * 
	 * @param Acc distance the matrix we search in
	 * @param x the x index of the entry we want to check for
	 * @param y the y index of the entry we want to check for
	 */
	public int[] getMatchingPathNeighbourIndex (double[][] accDistance, int x, int y){
		
		int [] data = new int[2];
		double min = Double.POSITIVE_INFINITY;
		
		
		//addd every -1 , so we take into account for  represeting the index of the acutal signals
		if(accDistance[x-1][y]<min){
			min = accDistance[x-1][y];
			data[0]=x-2;
			data[1]=y-1;
		}
		if(accDistance[x][y-1]<min)  {
			min = accDistance[x][y-1];
			data[0]=x-1;
			data[1]=y-2;
		}
		
		
		//the Min is in the Diagonal,  so return its Index
		if(accDistance[x-1][y-1]<=min){ 
			data[0]=x-2;
			data[1]=y-2;
			min = accDistance[x-1][y-1];
			
		}

	
		
		
		System.out.println("Smallest has Index X:"+data[0]+"Y:"+data[1]+" and Value:"+min);
		return data;
	}
	
	
	/*
	 * returnsa flipped version of the list, where the first element is the last and the last is the first etc
	 * 
	 * @param list the list we flip
	 * @return the flipped list 
	 */
	public LinkedList<Pair<Integer,Integer>> flipLinkedList(LinkedList<Pair<Integer,Integer>> list){
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>(0, 0);
		LinkedList<Pair<Integer,Integer>>result = new LinkedList<Pair<Integer,Integer>>();
	
		while(list.isEmpty()!=true){
			result.add(list.getLast());
			list.removeLast();
			
		}
		

		
		
		return result;
		
		
	}
	
	/**
	 * Compute the path that matches the two Signals
	 * and store it in the class variable "match".
	 * 
	 * We have to take care that the path is as short as
	 * possible: If we have the opportunity to go diagonally,
	 * we should do this 
	 * (i.e. take the lexicographically smallest pair) 
	 * 
	 * Also make sure that the indexes contained in the pair
	 * correspond to the indexes of the frames, i.e. that they
	 * range from 0 ... fileSize-1 (of the respective file)
	 * 
	 * Finally make sure that the path is in the correct order,
	 * from start to end!
	 * 
	 * @return pairs of frame indexes [ (x_i1, y_i1), ..., (x_in, y_in) ]  
	 */
	protected LinkedList<Pair<Integer,Integer>> computeMatchingPath(double[][] accDistance) throws RuntimeException {
		if(accDistance == null) {throw new RuntimeException("Called before computing the distance matrix!");}		
		System.out.print("Computing mapping path...");		
		int x = accDistance.length-1;
		int y = accDistance[0].length-1;
		Pair<Integer, Integer> pair = new Pair<Integer, Integer>(x-1, y-1);
		int i=0;
		int j =0;
		int [] data = new int[2];// where we save indexes temporarly
		LinkedList<Pair<Integer,Integer>>result = new LinkedList<Pair<Integer,Integer>>();
		result.add(pair);
		System.out.println("Reading out the path...");
		while (x!= 0 && y != 0){
			System.out.println("X is : "+x + "and Y is:"+y);
			data = getMatchingPathNeighbourIndex(accDistance, x, y);
			pair = new Pair<Integer, Integer>(data[0],data[1]);
			x=data[0]+1;
			y=data[1]+1;
			result.add(pair);
		//	System.out.println("PICKED);
		}
		result.removeLast();
		result=flipLinkedList(result);
		printList(result);
		
		System.out.println("done");
		return result;
		
		
	}


	/**
	 * Helper function to print matrices in a nice way
	 * @param m 2D matrix of double values
	 */
	public static void prettyprintMatrix(double[][] m){
		for (double[] row: m) {
			for (double element : row) {
				if (element == Double.POSITIVE_INFINITY) {					
					System.out.print(" inf, ");
				} else {					
					System.out.print(String.format("%.2f, ", element));
				}
			}
			System.out.println();				
		}
	}
	
	/*
	 * prints the given pair list 
	 */
	public void printList (LinkedList<Pair<Integer,Integer>> list ){
		System.out.println("THE RESULT IS :");
		for(Pair p :list ){
			System.out.println("X IS : "+ p.getLeft()+"AND Y IS :"+ p.getRight());
		}
		
		
	}

	
}


