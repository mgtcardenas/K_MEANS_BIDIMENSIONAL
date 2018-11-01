
// #region imports
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
// #endregion imports

public class StaticBidimensional extends Application
{
	
	// K-Means
	private static final int					CIRCLE_SIZE				= 5;
	private static final int					NUMBER_OF_CIRCLES		= 12;
	private static final int					NUMBER_OF_CLUSTERS		= 3;
	private static final int					WINDOW_WIDTH			= 800;
	private static final int					WINDOW_HEIGHT			= 500;
	private static Random						dice					= new Random();
	private static Hashtable<Coordinate, Paint>	meansTable				= new Hashtable<>();
	private static Hashtable<Coordinate, Paint>	bestMeansTable			= new Hashtable<>();
	private static boolean						thereWasANewMean		= false;
	private static double						totalVariation;
	private static double						smallestTotalVariation	= Double.MAX_VALUE;
	
	// GUI
	private static Group	canvas	= new Group();
	private static Scene	scene	= new Scene(canvas, WINDOW_WIDTH, WINDOW_HEIGHT);
	
	public static void main(String[] args)
	{
		launch(args);
	}// end main
	
	public static void setCircles()
	{
		
		HashSet<Coordinate>	coordinates	= new HashSet<Coordinate>();
		Coordinate			coordinate;
		Circle				circle;
		
		while (coordinates.size() != NUMBER_OF_CIRCLES)
		{
			coordinate = new Coordinate(dice.nextInt(WINDOW_WIDTH + 1), dice.nextInt(WINDOW_HEIGHT + 1));
			coordinates.add(coordinate);
		} // end while
		
		for (Coordinate c : coordinates)
		{
			circle = new Circle(c.x, c.y, CIRCLE_SIZE);
			canvas.getChildren().add(circle);
		} // end for
	}// end setCircles
	
	public static void assignRandomClusters()
	{
		meansTable.clear();
		Circle		circle;
		Coordinate	coordinate;
		while (meansTable.keySet().size() != NUMBER_OF_CLUSTERS)
		{
			circle		= (Circle) canvas.getChildren().get(dice.nextInt(NUMBER_OF_CIRCLES));
			coordinate	= new Coordinate((int) circle.getCenterX(), (int) circle.getCenterY());
			
			meansTable.put(coordinate, Color.color(Math.random(), Math.random(), Math.random()));
		} // end while
	}// end assignRandomClusters
	
	public static void paintClusters(Hashtable<Coordinate, Paint> table)
	{
		for (int i = 0; i < NUMBER_OF_CIRCLES; i++)
		{
			
			Circle	circle				= (Circle) canvas.getChildren().get(i);
			double	smallestDistance	= Double.MAX_VALUE;
			double	distance			= 0.0;
			double	dX					= 0.0;
			double	dY					= 0.0;
			
			for (Coordinate coordinate : table.keySet())
			{
				dX			= Math.abs(circle.getCenterX() - (double) coordinate.x);
				dY			= Math.abs(circle.getCenterY() - (double) coordinate.y);
				distance	= Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
				
				if (distance < smallestDistance)
				{
					smallestDistance = distance;
					circle.setFill(table.get(coordinate));
				} // end if
			} // end for
		} // end for
	}// end paintClusters
	
	public static void calculateMeans(Hashtable<Coordinate, Paint> table)
	{
		Enumeration clusters = table.keys();
		while (clusters.hasMoreElements())
		{
			
			int		circlesInCluster	= 0;
			double	xMean				= 0.0;
			double	yMean				= 0.0;
			
			Coordinate	currentMean		= (Coordinate) clusters.nextElement();
			Paint		clusterColor	= table.get(currentMean);
			
			for (int i = 0; i < NUMBER_OF_CIRCLES; i++)
			{
				Circle circle = (Circle) canvas.getChildren().get(i);
				// If circle belongs to current cluster
				if (circle.getFill() == clusterColor)
				{
					circlesInCluster++;
					xMean	+= circle.getCenterX();
					yMean	+= circle.getCenterY();
				} // end if
			} /// end for
			
			xMean	= xMean / (double) circlesInCluster;
			yMean	= yMean / (double) circlesInCluster;
			
			Coordinate newMean = new Coordinate((int) xMean, (int) yMean);
			
			if (!table.containsKey(newMean))
			{
				table.remove(currentMean);
				table.put(newMean, clusterColor);
				thereWasANewMean = true;
			} // end if
			
			System.out.println("Center : " + newMean.x + "," + newMean.y);
			
		} // end while
		
		System.out.println("-----------------------");
		
	}// end calculateMeans
	
	public static void cluster()
	{
		assignRandomClusters();
		do
		{
			thereWasANewMean = false;
			paintClusters(meansTable);
			calculateMeans(meansTable);
		}
		while (thereWasANewMean);
	}// end cluster
	
	public static double getVariation()
	{
		
		totalVariation = 0.0;
		
		for (Coordinate coordinate : meansTable.keySet())
		{
			
			double	clusterVariation	= 0.0;
			double	dX;
			double	dY;
			double	smallestDx			= Double.MAX_VALUE;
			double	smallestDy			= Double.MAX_VALUE;
			double	largestDx			= Double.MIN_VALUE;
			double	largestDy			= Double.MIN_VALUE;
			
			Paint clusterColor = meansTable.get(coordinate);
			
			for (int i = 0; i < NUMBER_OF_CIRCLES; i++)
			{
				Circle circle = (Circle) canvas.getChildren().get(i);
				
				if (circle.getFill() == clusterColor)
				{
					dX	= coordinate.x - circle.getCenterX();
					dY	= coordinate.y - circle.getCenterY();
					
					if (dX < smallestDx)
						smallestDx = dX;
					
					if (dY < smallestDy)
						smallestDy = dY;
					
					if (dX > largestDx)
						largestDx = dX;
					
					if (dY > largestDy)
						largestDy = dY;
				}// end if
				
			} // end for
			
			clusterVariation	= (largestDx - smallestDx) * (largestDy - smallestDy);
			totalVariation		+= clusterVariation;
			
		} // end for
		
		System.out.println("The Variation is : " + totalVariation);
		return totalVariation;
		
	}// end getVariation
	
	public static void kMeans(int attempts)
	{
		for (int i = 0; i < attempts; i++)
		{
			System.out.println("---");
			System.out.println(i);
			System.out.println("---");
			cluster();
			double variation = getVariation();
			if (variation < smallestTotalVariation)
			{
				bestMeansTable			= meansTable;
				smallestTotalVariation	= variation;
			} // end if
		} // end for
	}// end kMeans
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		
		primaryStage.setTitle("Bidimensional K-Means");
		
		primaryStage.setScene(scene);
		
		setCircles();
		
		kMeans(5);
		System.out.println("Smalles Variation was: " + smallestTotalVariation);
		paintClusters(bestMeansTable);
		
		/* Uncomment if you wish to see the means of the points */
		// for (Coordinate coordinate : bestMeansTable.keySet()) {
		// Circle circle = new Circle(coordinate.x, coordinate.y, CIRCLE_SIZE);
		// canvas.getChildren().add(circle);
		// } // end for
		
		primaryStage.show();
	}// end start
	
}// end StaticBidimensional
