
import org.junit.* ;
import static org.junit.Assert.* ;

public class SphereTest {
	
	private double sphereRadius;
	private int sphereX;
	private int sphereY;
	private int sphereZ;
	private double delta = 1e-5;
	
	
	@Before
	public void setUp() {
		sphereRadius = 3.0;
		sphereX = 5;
		sphereY = -2;
		sphereZ = -10;
	}
	
	@Test(timeout=1000)
	public void testConstructorAndGettersX() {
		Sphere dummySphere = new Sphere(sphereX, sphereY, sphereZ, sphereRadius);
		
		assertEquals("getX() does not return the value used during construction", sphereX, dummySphere.getX(), delta);
	}
	
	@Test(timeout=1000)
	public void testConstructorAndGettersY() {
		Sphere dummySphere = new Sphere(new Point(sphereX, sphereY, sphereZ), sphereRadius);
		
		assertEquals("getY() does not return the value used during construction", sphereY, dummySphere.getY(), delta);
	}
	
	@Test(timeout=1000)
	public void testConstructorAndGettersZ() {
		Sphere dummySphere = new Sphere(new Point(sphereX, sphereY, sphereZ), sphereRadius);
		
		assertEquals("getZ() does not return the value used during construction", sphereZ, dummySphere.getZ(), delta);
	}

	@Test(timeout=1000)
	public void testCalculateDiameter() {
		Sphere dummySphere = new Sphere(new Point(sphereX, sphereY, sphereZ), sphereRadius);
		
		assertEquals("calculateDiameter() does not return the twice the radius", 2*sphereRadius, dummySphere.calculateDiameter(), delta);
	}
	
	/* Feel free to implement more tests. */
}


