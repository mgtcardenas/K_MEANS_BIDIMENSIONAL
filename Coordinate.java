
// Java program to illustrate
// overriding of equals and
// hashcode methods
import java.io.*;
import java.util.*;

class Coordinate
{
	
	int	x;
	int	y;
	
	Coordinate(int x, int y)
	{
		this.x	= x;
		this.y	= y;
	}// end Coordinate - Constructor
	
	@Override
	public boolean equals(Object o)
	{
		
		// if both the object references are
		// referring to the same object.
		if (this == o)
			return true;
			
		// it checks if the argument is of the
		// type Coordinate by comparing the classes
		// of the passed argument and this object.
		// if(!(obj instanceof Coordinate)) return false; ---> avoid.
		if (o == null || o.getClass() != this.getClass())
			return false;
		
		// type casting of the argument.
		Coordinate coordinate = (Coordinate) o;
		
		// comparing the state of argument with
		// the state of 'this' Object.
		return (coordinate.x == this.x && coordinate.y == this.y);
	}// end equals
	
	@Override
	public int hashCode()
	{
		
		/*
		 * We are returning the Coordinate_id as a hashcode value. We can also return
		 * some other calculated value or may be memory address of the Object on which
		 * it is invoked. It depends on how you implement hashCode() method.
		 */
		return 2 * this.x + 3 * this.y;
	} // end hashCode
	
} // end Coordinate
