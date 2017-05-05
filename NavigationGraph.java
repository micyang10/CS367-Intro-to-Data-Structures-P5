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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * An implementation of the GraphADT interface. This implementation 
 * defines the structure of a Navigation Graph type. It supports displaying 
 * the graph and its edge properties, as it supports all graph functions. 
 * See GraphADT.java for a description of each method. 
 *
 * <p>Bugs: None that we are aware of
 *
 * @author Michael Yang, Rohan Sampat
 */
public class NavigationGraph implements GraphADT<Location, Path> {
	//a graph of nodes
	private List<GraphNode<Location, Path>> graph;
	//name of edge properties of graphs
	private String[] edgePropertyNames;
	//adjacency matrix 
	private HashMap<Integer, List<Integer>> adjMatrix;	
	
	/**
	* Constructs a new navigation graph instance. 
	* 
	* @param edgePropertyNames name of edge properties of graphs
	*/
    public NavigationGraph(String[] edgePropertyNames) {
        if (edgePropertyNames == null) {
                throw new IllegalArgumentException();
            }
        graph = new ArrayList<GraphNode<Location, Path>>();
        this.edgePropertyNames = edgePropertyNames;
        adjMatrix = new HashMap<Integer, List<Integer>>();
	}
	
	/**
	 * Returns a Location object given its name
	 * 
	 * @param name
	 *            name of the location
	 * @return Location object
	 */
	public Location getLocationByName(String name) {
		if (name == null) {
		    throw new IllegalArgumentException();
		}
	    name = name.toLowerCase(); 
	    for (GraphNode<Location, Path> node : graph) {
		     if (node.getVertexData().getName().equals(name)) {
		         return node.getVertexData();
		     } 
		 }
		 return null;
	}

	/**
	* Adds a vertex to the Graph.
	* 
	* @param vertex 
	* 			vertex to be added           
	*/	
    @Override
    public void addVertex(Location vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException();
        }
        //Check if vertex already exists and return from method
        for (GraphNode<Location, Path> node : graph) {
            if (node.getVertexData().equals(vertex)) {
                return;
            }
        }
        //Since no vertices are removed, graph.size will be the number 
        //of the next vertex (with first vertex being 0)
        GraphNode<Location, Path>temp = new GraphNode<Location, 
        		Path>(vertex, graph.size());
        graph.add(temp);
        adjMatrix.put(temp.getId(), new ArrayList<Integer>());
                                                                       
    }

    /**
	* Creates a directed edge from src to dest
	* 
	* @param src source vertex from where the edge is outgoing
	* @param dest destination vertex where the edge is incoming
	* @param edge 
	* 			edge between src and dest			  
	*/	
    @Override
    public void addEdge(Location src, Location dest, Path edge) {
        GraphNode<Location, Path> srcNode = null;
        GraphNode<Location, Path> destNode = null;
        for (GraphNode<Location, Path> node : graph  ) {           
            if (node.getVertexData().equals(src)) {
                List<Path>temp = node.getOutEdges();
                for (Path tempPath : temp) {
                    if (tempPath.getDestination().equals(src) 
                    		&& tempPath.getDestination().equals(dest)) {
                        return;
                    }
                }
                temp.add(edge);
                node.setOutEdges(temp);
                srcNode = node;
            }
        }        
        for (GraphNode<Location, Path> node : graph) {
            if (node.getVertexData().equals(dest) && srcNode != null) {
                destNode = node;
                List<Integer> entry = adjMatrix.remove(srcNode.getId());
                entry.add(node.getId());
                adjMatrix.put(srcNode.getId(), entry);
            }
        } 
        if (srcNode == null || destNode == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
	* Getter method for the vertices
	* 
	* @return List of vertices of type V	  
	*/	
    @Override
    public List<Location> getVertices() {
        List<Location> vertList = new ArrayList<Location>();
        for (GraphNode<Location, Path> node: graph) {
            vertList.add(node.getVertexData());
        }
        return vertList;
    }

    /**
	* Returns edge if there is one from src to dest vertex else null
	* 
	* @param src source vertex from where the edge is outgoing
	* @param dest destination vertex where the edge is incoming
	* @return Edge of type E from src to dest  
	*/
    @Override
    public Path getEdgeIfExists(Location src, Location dest) {
        List<Path> pathList = getOutEdges(src);       
        if (src == null || dest == null) {
            throw new IllegalArgumentException();
        }
        
        for (Path path : pathList) {
            if (path.getDestination().equals(dest)) {
                return path;
            }             
        }
        return null;
    }

    /**
	* Returns the outgoing edges from a vertex
	* 
	* @param src source vertex from where the edge is outgoing
	* @return List of edge of type E  
	*/
    @Override
    public List<Path> getOutEdges(Location src) {
        List<Path> pathList = null; 
        for (GraphNode<Location, Path> node: graph) {
            if (node.getVertexData().equals(src)) {
                pathList = node.getOutEdges();
                break;
            }
        }
        if (pathList == null) {
            throw new IllegalArgumentException();
        }
        return pathList;
    }

    /**
	* Returns neighbors of a vertex
	* 
	* @param vertex 
	* 			vertex for which the neighbors are required
	* @return List of vertices(neighbors) of type V
	*/
    @Override
    public List<Location> getNeighbors(Location vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException();
        }
        List<Location> neighbors = new ArrayList<Location>();
        List<Path> pathList = getOutEdges(vertex);
        for (Path path : pathList) {
            neighbors.add(path.getDestination());
        }
        return neighbors;
    }

    /**
	* Calculate the shortest route from src to dest vertex 
	* using edgePropertyName
	* 
	* @param src source vertex from where the shortest route is desired
	* @param dest destination vertex to which the shortest route is desired
	* @param edgePropertyName edge property by which shortest route has to 
	* be calculated
	* @return List of edges that denote the shortest route by edgePropertyName  
	*/
    @Override
    public List<Path> getShortestRoute(Location src, Location dest, 
    				String edgePropertyName) {
       
       if (src == null || dest == null || edgePropertyName == null) {
            throw new IllegalArgumentException();
       }
        
       PriorityQueue<Pair<GraphNode<Location, Path>, Double>> pq 
       			= new PriorityQueue<Pair<GraphNode<Location, Path>, Double>>();
       List<Pair<GraphNode<Location, Path>, Double>> pairList 
       			= new ArrayList<Pair<GraphNode<Location, Path>, Double>>();
       HashMap<GraphNode<Location, Path>,GraphNode<Location, Path>> pred 
       			= new HashMap<GraphNode<Location, Path>,GraphNode<Location, 
       				Path>>();      
       int srcID = -1;
       int property = -1;
       GraphNode<Location, Path> destNode = null;
       for (int i = 0; i < edgePropertyNames.length; i++) {
           if (edgePropertyNames[i].equals(edgePropertyName)) {
               property = i;
               break;
           } 
       }
       if (property == -1) {
           throw new IllegalArgumentException();
       }
       
       for (GraphNode<Location, Path> node : graph) {
           if (node.getVertexData().equals(src)) {
               srcID = node.getId();
               pairList.add(new Pair<GraphNode<Location, Path>, 
            		   Double>(node, 0.0));
           } else {
           
               pairList.add(new Pair<GraphNode<Location, Path>, 
            		   Double>(node, Double.MAX_VALUE));
           }
           
           if (node.getVertexData().equals(dest)) {
               destNode = node;
           }
       }
       if (destNode == null || srcID == -1) {
           throw new IllegalArgumentException();
       }
       
       pq.add(pairList.get(srcID));
       while (!pq.isEmpty()) {
           Pair<GraphNode<Location, Path>, Double> u = pq.poll();
           for(GraphNode<Location, Path> v : getNeighboringGraphNodes(
        		   		u.getKey())) {
               Path temp = getEdgeIfExists(u.getKey().getVertexData(), 
            		   		v.getVertexData());
               if (pairList.get(v.getId()).getValue() > u.getValue() 
            		   		+ temp.getProperties().get(property)) {
                   pairList.get(v.getId()).setValue( u.getValue() 
                		   		+ temp.getProperties().get(property));
                   pq.add(pairList.get(v.getId()));
                   pred.put(v, u.getKey());
               }
           }
       }
       
       if (pred.get(destNode) == null) {
           return new LinkedList<Path>();
       }
        
       List<Path> route = new LinkedList<Path>();
       while (pred.get(destNode) !=null) {
            route.add(getEdgeIfExists(pred.get(destNode).getVertexData(), 
            		destNode.getVertexData()));
            destNode = pred.get(destNode);
       }
       Collections.reverse(route);
       return route;
    }
    
    /**
	* Getter method for edge property names
	* 
	* @return array of String that denotes the edge property names 
	*/
    @Override
    public String[] getEdgePropertyNames() {
        return edgePropertyNames;
    }
    
    /**
	* Getter method for neighboring graph nodes
	* 
	* @param node 
	* 			node that will be used to find neighboring graph nodes
	* @return List of neighboring graph nodes  
	*/
    private List<GraphNode<Location, Path>> getNeighboringGraphNodes
    				(GraphNode<Location, Path> node) {
       
        if (node == null) {
            throw new IllegalArgumentException();
        }
        
        List<GraphNode<Location, Path>> neighbors 
        		= new ArrayList<GraphNode<Location, Path>>();
        for (Integer n : adjMatrix.get(node.getId())) {
            neighbors.add(graph.get(n));
        }
        return neighbors;
    } 
    
    /**
	* Return a string representation of the graph
	* 
	* @return String representation of the graph 
	*/
    @Override
    public String toString() {
        String gString = "";
        for (GraphNode<Location, Path> node : graph) {
            for (Path path : node.getOutEdges()) {
                gString+= path.toString() + ", ";
            }
            gString = gString.trim().split("(([,]$))")[0];
            gString+= "\n";
        }
        gString = gString.trim().split("(([,]$))")[0];
        return gString;
    }

}

/**
 * This class is a wrapper class that will be used for the priority queue
 * and for the Navigation Graph class
 *
 * <p>Bugs: None that we are aware of
 *
 * @author Michael Yang, Rohan Sampat
 */
class Pair<K , V extends Comparable<V>> implements Comparable<Pair<K,V>>{
	private K key;
    private V value;
    
    /**
	* Constructs new Pair instance, initializing key and value from parameters.
	* 
	* @param key
	* 			key to be initialized
	* @param value
	* 			value to be initialized
	*/
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    /**
	* Constructs new Pair instance, initializing key from parameters.
	* 
	* @param key
	* 			key to be initialized
	*/
    public Pair(K key) {
        this(key, null);
    }
    
    /**
	* Accesses value of a Pair object
	* 
	* @return value of Pair object
	*/
    public V getValue() {
        return value;
    } 
    
    /**
	* Accesses key of a Pair object
	* 
	* @return key of Pair object
	*/
    public K getKey() {
        return key;
    }
    
    /**
	* Sets value of a Pair object
	* 
	* @param value of Pair object
	*/
    public void setValue(V value) {
        this.value = value; 
    }
    
    /**
	 * Compares two values and assesses which occurs first chronologically.
	 * 
	 * @param other the second value to which compare this value with
	 * @return -1 if this value comes before the other value, 
	 * 1 if this value comes after the other value,
	 * and 0 if the value are the same.
	 */
    @Override
    public int compareTo(Pair<K,V> other) {
        if (other.getValue() == null) {
           return 1; 
        }
        return other.getValue().compareTo(value);
    }
    
} 





