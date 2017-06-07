/**
 * 
 * @author Algorithm and Datastructures Team SS2016
 * @version 1.0  
 * 
 */
import java.lang.RuntimeException;
public class MyHashMap {
	
	/**
	 * Use this array to store the values
	 * DO NOT MODIFY OR REMOVE THIS Attribute. Our tests rely on it.
	 */
	Student[] array;
	
	
	/*
	 * generates Hash value for student S (no linear sonding)
	 * uses  |(s.hash mod array.length| for generating value.
	 */
	public int getHash(Student s){
		int hash = s.hashCode()%this.array.length;
		
		if(hash<0) return hash*-1;
		else return hash;
	}
	
	/**
	 * Tries inserting a Student into the hash map.
	 * Throws RunTimeException, if the student is already in the table or the table is full.
	 *uses the getHash(s) function to get hash and then it takes care of linear probing
	 * @throws Exception 
	 */
	public void add(Student s) throws Exception{
		int hash = getHash(s);
		int arrayRuns=0;
		Exception e= new Exception();
		System.out.println("Trying to Add Student:"+ s.getName());

		if (contains(s)==true) throw e;
		
		//search for place to insert
		//We will check max 2 times the array with linear probing
		while(this.array[hash]!= null && arrayRuns<2){
			hash++;
			if (hash>=this.array.length){hash=0; arrayRuns++;}
			
		}
		
		if(arrayRuns >=2) throw e; //array full
		
		array[hash]=s;
	}
	
	/**
	 * Try removing a Student from the hash table.
	 * You should use the same implementation for remove discussed in the tutorial.
	 * You should NOT use the lazy deletion strategy (adding a special flag key indicating a deleted key)
	 * See https://en.wikipedia.org/wiki/Linear_probing#Deletion for more details about the algorithm!
	 * Throw RunTimeException if the hash table contained the Student
	 * @throws Exception 
	 */
	public void remove(Student s) throws Exception{
	int hash = getHash(s);
	int sondingCount =0;
	int arrayRuns=0;
	int originalHash=hash;
	int hashOfDeletetS=0;
	Exception e = new RuntimeException();
	System.out.println("Trying to Remove Student:"+ s.name);
	if(contains(s)!=true) throw e;//trying to remove non contained student
	
		//find out where we hashed the value to
	while(this.array[hash].equals(s)!=true && arrayRuns <2){
		sondingCount++;
		hash++;
		if (hash>=this.array.length){hash=0; arrayRuns++;}
	}//the student to be deletet is storend in hash now
	hashOfDeletetS=hash;
	this.array[hash]=null;
	//2 almost analog cases either hash = array.size-1 or not

	arrayRuns=0;
	hash++;
	if(hash<this.array.length){
		//when array[hash]== null, this means there is no element  we need to move 
		while(this.array[hash]!= null && arrayRuns <2){
			if (getHash(this.array[hash])==originalHash){//we need to move an element
				this.array[hashOfDeletetS]=this.array[hash];//move element to pos of deletet ele
				this.array[hash]=null;//delete moved ele
				hashOfDeletetS=hash;
				hash++;
				if (hash>=this.array.length){hash=0; arrayRuns++;}//set var for next loop
				}else return;//nothing to delete anymore, because the element is not from the same orignial hash
				}	
	}else{//analog case
		hash=0;
		//when array[hash]== null, this means there is no element  we need to move 
		while(this.array[hash]!= null && arrayRuns <2){
			if (getHash(this.array[hash])==originalHash){//we need to move an element
				this.array[hashOfDeletetS]=this.array[hash];//move element to pos of deletet ele
				this.array[hash]=null;//delete moved ele
				hashOfDeletetS=hash;
				hash++;
				if (hash>=this.array.length){hash=0; arrayRuns++;}//set var for next loop
				}else return;//nothing to delete anymore, because the element is not from the same orignial hash
				}
	
	
	
	
	
	
	
	
	}
	}
	
	
	
	
	/**
	 * Returns true, if the hash table contains the given Student
	 * @param hash, int used to cycle the array 
	 * @param arrayruns, int to remember if we Jumped from the end of the array to the beginning
	 * 
	 */
	public boolean contains(Student s){
		int hash = getHash(s);
		int arrayRuns=0;
		Exception e= new Exception();
		
		if (array[hash]==null) return false;
		
		System.out.println("Checking if Array Contains Student:" + s.getName());
		
		while(this.array[hash]!=null && this.array[hash].equals(s)==false && arrayRuns<2){
			hash++;
			if (hash>=this.array.length){hash=0; arrayRuns++;}
			
		
		}
		
		
		if(this.array[hash]==null) return false ;
		if(this.array[hash].equals(s)){System.out.println("Student "+s.name+" is contained");return true;}
		
		return false;
	}
	
	/**
	 * @param h Hashvalue to search for
	 * @return Number of Student in the hash table that have the hashvalue h
	 */
	public int getNumberStudentsWithHashvalue(int h){
		int n = 0;
		int i=0;
		
		while (i<this.array.length){//checks every element in array and calculates their hash. If their hash=h  increase students with hash value h count
			if(this.array[i]!=null && getHash(this.array[i])==h) n++;
			i++;
		}
		
		return n;
	}
	
	/**
	 * Doubles the size of the hash table. Recomputes the position of all elements using the
	 * new function.
	 * @throws Exception 
	 */
	public void resize() throws Exception{
		Student[] tmpArray= this.array;
		this.array= new Student[2*this.array.length];
		int i =0;
		//take each element from TMP array and insert it into resized array
		
		while(i<tmpArray.length){
			
			if(tmpArray[i]!=null) add(tmpArray[i]);
			i++;
			
		}
		
		
	
	}

	/**
	 * This method return the array in which the strings are stored.
	 * DO NOT MODIFY OR REMOVE THIS METHOD. Our tests rely on it.
	 */
	public Student[] getArray(){
		return array;
	}
	
	/**
	 * DO NOT MODIFY OR REMOVE THIS METHOD. Our tests rely on it.
	 */
	public void setArray(Student[] array){
		this.array = array;
	}

	/**
	 * Runs the hash function for Student s (dependent on the size of the hash table)
	 * DO NOT MODIFY OR REMOVE THIS METHOD. Our tests rely on it.
	 * @param s Student
	 * @return the hash value for a student. (The position where it would be saved given no collisions)
	 */
	public int hashFunction(Student s){
		int hashvalue = Math.abs(s.hashCode()) % array.length;
		return hashvalue;
	}
	
	/**
	 * Constructor to initialize the hash with a given capacity
	 * DO NOT MODIFY OR REMOVE THIS METHOD. Our tests rely on it.
	 */
	public MyHashMap(int capacity){
		array = new Student[capacity];
	}

}

