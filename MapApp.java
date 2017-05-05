/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2017 
// PROJECT:          Program 5
// FILE:             NavigationGraph.java
//
// TEAM:    P5 Team 102
// Authors: Michael Yang, Rohan Sampat
// Author1: Michael Yang, yang363@wisc.edu, yang363, LEC 001
// Author2: Rohan Sampat, rsampat@wisc.edu, rsampat, LEC 001
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Driver class that reads/parses the input file and creates NavigationGraph
 * object.
 * 
 * * <p>Bugs: None that we are aware of
 * 
 * @author CS367, Michael Yang, Rohan Sampat
 */
public class MapApp {
	//a graph object 
	private NavigationGraph graphObject;

	/**
	 * Constructs a MapApp object
	 * 
	 * @param graph
	 *            NaviagtionGraph object
	 */
	public MapApp(NavigationGraph graph) {
		this.graphObject = graph;
	}

	/**
	 * Main function which takes the command line arguments and 
	 * instantiate the MapApp class.
	 * The main function terminates depending on user input
	 * Use the startService() method to read inputs from console
	 * 
	 * @param args Command line arguments of <directory> 
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java MapApp <pathToGraphFile>");
			System.exit(1);
		}

		// read the filename from command line argument
		String locationFileName = args[0];
		try {
			NavigationGraph graph = createNavigationGraphFromMapFile
					(locationFileName);
			MapApp appInstance = new MapApp(graph);
			appInstance.startService();

		} catch (FileNotFoundException e) {
			System.out.println("GRAPH FILE: " + locationFileName 
					+ " was not found.");
			System.exit(1);
		} catch (InvalidFileException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Displays options to user about the various operations on loaded graph
	 */
	public void startService() {
		System.out.println("Navigation App");
		Scanner sc = new Scanner(System.in);

		int choice = 0;
		do {
			System.out.println();
			System.out.println("1. List all locations");
			System.out.println("2. Display Graph");
			System.out.println("3. Display Outgoing Edges");
			System.out.println("4. Display Shortest Route");
			System.out.println("5. Quit");
			System.out.print("Enter your choice: ");

			while (!sc.hasNextInt()) {
				sc.next();
				System.out.println("Please select a valid option: ");
			}
			choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println(graphObject.getVertices());
				break;
			case 2:
				System.out.println(graphObject.toString());
				break;
			case 3: {
				System.out.println("Enter source location name: ");
				String srcName = sc.next();
				Location src = graphObject.getLocationByName(srcName);

				if (src == null) {
					System.out.println(srcName + " is not a valid Location");
					break;
				}

				List<Path> outEdges = graphObject.getOutEdges(src);
				System.out.println("Outgoing edges for " + src + ": ");
				for (Path path : outEdges) {
					System.out.println(path);
				}
			}
				break;

			case 4:
				System.out.println("Enter source location name: ");
				String srcName = sc.next();
				Location src = graphObject.getLocationByName(srcName);

				System.out.println("Enter destination location name: ");
				String destName = sc.next();
				Location dest = graphObject.getLocationByName(destName);

				if (src == null || dest == null) {
					System.out.println(srcName + " and/or " + destName 
							+ " are not valid Locations in the graph");
					break;
				}

				if (src == dest) {
					System.out.println(srcName + " and " + destName 
							+ " correspond to the same Location");
					break;
				}

				System.out.println("Edge properties: ");
				// List Edge Property Names
				String[] propertyNames = graphObject.getEdgePropertyNames();
				for (int i = 0; i < propertyNames.length; i++) {
					System.out.println("\t" + (i + 1) + ": " 
							+ propertyNames[i]);
				}
				System.out.println("Select property to compute" 
							+ " shortest route on: ");
				int selectedPropertyIndex = sc.nextInt() - 1;

				if (selectedPropertyIndex >= propertyNames.length) {
					System.out.println("Invalid option chosen: " 
								+ (selectedPropertyIndex + 1));
					break;
				}

				String selectedPropertyName 
						= propertyNames[selectedPropertyIndex];
				List<Path> shortestRoute = graphObject.getShortestRoute
						(src, dest, selectedPropertyName);
				for(Path path : shortestRoute) {
					System.out.print(path.displayPathWithProperty
							(selectedPropertyIndex)+", ");
				}
				if(shortestRoute.size()==0) {
					System.out.print("No route exists");
				}
				System.out.println();

				break;

			case 5:
				break;

			default:
				System.out.println("Please select a valid option: ");
				break;
			}
		} while (choice != 5);
		sc.close();
	}

	/**
	 * Reads and parses the input file passed as argument create a
	 * NavigationGraph object. The edge property names required for
	 * the constructor can be got from the first line of the file
	 * by ignoring the first 2 columns - source, destination. 
	 * Use the graph object to add vertices and edges as
	 * you read the input file.
	 * 
	 * @param graphFilepath
	 *            path to the input file
	 * @return NavigationGraph object
	 * @throws FileNotFoundException
	 *             if graphFilepath is not found
	 * @throws InvalidFileException
	 *             if header line in the file has < 3 columns or 
	 *             if any line that describes an edge has different 
	 *             number of properties than as described in the header or 
	 *             if any property value is not numeric 
	 */
	public static NavigationGraph createNavigationGraphFromMapFile
			(String graphFilepath) throws FileNotFoundException, 
			InvalidFileException {
			File file = new File(graphFilepath);
	        Scanner fstream = new Scanner(file);
			String header = fstream.nextLine();
			header =header.replace("Source Destination ", "");
			header =header.replace("source destination ", "");
			String[] arr = header.split(" ");
			//System.out.println(arr.length);
			if (arr.length == 0) {
			    throw new InvalidFileException("Invalid File: " 
			    			+ "Missing Headers");
			}
			for (int i = 0; i < arr.length; i++) {
			   arr[i]= parse(arr[i]);
			}
	        NavigationGraph graph = new NavigationGraph(arr);
	        int test = 0;
	        while (fstream.hasNextLine()) {
	           String fileLine = fstream.nextLine();
	           String[] lineArr = fileLine.split(" ");
	           if (lineArr.length != arr.length+2) {
	               throw new InvalidFileException("Invalid File: Row_" 
	            		   		+ test + " Incomplete Entry");
	           }
	           
	           List<Double> list = new ArrayList<Double>();
	           for(int i = 0; i < lineArr.length; i++) {
	               if (i == 1 || i == 0) {
	                   graph.addVertex(new Location(parse(lineArr[i])));	                   
	               } else {
	                   try {
	                       double temp = Double.parseDouble(lineArr[i]);
	                       if (temp < 0) {
	                           list.add(1.0);
	                       } else {
	                           list.add(temp);
	                       }
	                   } catch (NumberFormatException e) {
	                       throw new InvalidFileException
	                       				("Invalid File: Row_" + test 
	                       				+ " " + lineArr[i] 
	                       				+ " is not a Double!");
	                   }	                   
	               }    
	           }
	           test++;
	           Location srcL = graph.getLocationByName(parse(lineArr[0]));
               Location destL = graph.getLocationByName(parse(lineArr[1]));
	           graph.addEdge(srcL, destL, new Path(srcL, destL, list));
	        }			
			return graph;
	}
	
	/**
	 * Method that parse and replaces with a certain string
	 * 
	 * @param text string to be parsed and replaced
	 */
	private static String parse(String text) {
	    return text.replaceAll("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"
	    			, " ").toLowerCase().trim();
	}
}
