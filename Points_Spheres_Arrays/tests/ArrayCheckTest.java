import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class ArrayCheckTest {
	// NOTTODO DIESE ZEILE BITTE NICHT VERAENDERN!!
	// TODO Fuehrt eure Tests auf diesem ArrayCheck-Objekt aus!
	// Ansonsten kann eure Abgabe moeglicherweise nicht
	// gewertet werden.
	public ArrayCheck ArrayCheck = new ArrayCheck();
	public ArrayList<Integer> arr = null;


	@Test(timeout = 1000)
	public void testAllDivisibleBy() {
		// TODO Schreibt verschiedene Testfaelle, die unterschiedliche Arrays
		// anlegen und an testAllDivisibleBy uebergeben.
		// Testet auch randfaelle wie z.B. leere arrays.

		//NULL check
		ArrayList<Integer> arr = null;
		assertEquals(" Empty array check",false, ArrayCheck.allDivisibleBy(arr, 8));
		
		//Empty array Check
		arr = new  ArrayList<Integer>();
		assertEquals(" Empty array check",false, ArrayCheck.allDivisibleBy(arr, 8));

		
		//Div by 0 Check
		arr = new  ArrayList<Integer>();
		arr.add(3);
		arr.add(6);
		arr.add(9);
		assertEquals("Divide by 0 Test", false, ArrayCheck.allDivisibleBy(arr, 0));
		

		//Div test fine
		assertEquals("Divide by an Actual Divisor", true,ArrayCheck.allDivisibleBy(arr, 3));
		arr.add(7);
		
		//div test false
		assertEquals("Divide by an non Divisor of all ", false, ArrayCheck.allDivisibleBy(arr, 3));

		//0 div by n
		arr = new  ArrayList<Integer>();
		arr.add(0); 
		arr.add(0); 
		arr.add(0); 

		assertEquals("0 Diveded by Something", true , ArrayCheck.allDivisibleBy(arr, 531));
		
	}

	@Test(timeout = 1000)
	public void testIsFibonacci() {
		
		//NULL check
		assertEquals(" NULL array check",false, ArrayCheck.isFibonacci(null));
		
		//Empty array Check
		ArrayList<Integer> arr = new  ArrayList<Integer>();
		assertEquals(" Empty array check",false, ArrayCheck.isFibonacci(arr));
		//Array with 1 Ele
		arr.add(2);
		assertEquals(" 1 Element in Array Test",false, ArrayCheck.isFibonacci(arr));
		
		//Array with 2 ele
		arr.add(5);
		assertEquals(" 2 Element in Array test",false,ArrayCheck.isFibonacci(arr));
		//array with 3 ele
		arr.add(43);
		
		assertEquals(" 3 element in array test",false, ArrayCheck.isFibonacci(arr));
		
		
		//test basic fib seq
		arr = new  ArrayList<Integer>();
		arr.add(0);
		assertEquals(" First Element only Fib but to short test",false, ArrayCheck.isFibonacci(arr));
		arr.add(1);
		arr.add(1);
		arr.add(2);
		assertEquals(" First 4 fib Numbers test",true, ArrayCheck.isFibonacci(arr));

		//break the fib seq
		arr.add(232);
		assertEquals(" Broken fib folge Test",false, ArrayCheck.isFibonacci(arr));
		
		//fib folge starting in the middle f fib sequence
		arr = new  ArrayList<Integer>();
		arr.add(3);
		arr.add(5);
		arr.add(8);
		arr.add(13);
		assertEquals(" Fib Seq not starting with the first Fib number",true, ArrayCheck.isFibonacci(arr));
		
		//break the sequence above
		arr.add(134123);
		assertEquals(" Broken fib seq",false, ArrayCheck.isFibonacci(arr));
		
		arr=new ArrayList<Integer>();
		arr.add(233);
		arr.add(377);
		arr.add(610);
		arr.add(987);
		arr.add(1597);
		assertEquals(" Fib Seq test",true, ArrayCheck.isFibonacci(arr));

		arr.add(23);
		assertEquals(" Fib Seq test",false, ArrayCheck.isFibonacci(arr));


	}
	
}

