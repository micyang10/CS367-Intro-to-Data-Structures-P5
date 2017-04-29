import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class NavigationGraph implements GraphADT<Location, Path> {

	private List<GraphNode<Location, Path>> graph;
	private String[] edgePropertyNames;
	
	
    public NavigationGraph(String[] edgePropertyNames) {
        if (edgePropertyNames == null) {
                throw new IllegalArgumentException();
            }
        graph = new ArrayList<GraphNode<Location, Path>>();
        this.edgePropertyNames = edgePropertyNames;
	}

	
	/**
	 * Returns a Location object given its name
	 * 
	 * @param name
	 *            name of the location
	 * @return Location object
	 */
	public Location getLocationByName(String name) {
		 for (GraphNode<Location, Path> node : graph) {
		     if (node.getVertexData().getName().equals(name)) {
		         return node.getVertexData();
		     } 
		 }
		 return null;
	}


    @Override
    public void addVertex(Location vertex) {
        //Check if vertex already exists and return from method
        for (GraphNode<Location, Path> node : graph) {
            if (node.getVertexData().equals(vertex)) {
                return;
            }
        }
        //Since no verticies are removed, graph.size will be the number of the next vertex (with first vertex being 0)
        graph.add(new GraphNode<Location, Path>(vertex, graph.size())); 
                                                                       
    }


    @Override
    public void addEdge(Location src, Location dest, Path edge) {
        for (GraphNode<Location, Path> node : graph  ) {
            if (node.getVertexData().equals(src)) {
                List<Path> temp = node.getOutEdges();
                temp.add(edge);
                node.setOutEdges(temp);
                return;  // No need to continue method and search once found so return
            }
        }
        throw new IllegalArgumentException();
        
    }


    @Override
    public List<Location> getVertices() {
        List<Location> vertList = new ArrayList<Location>();
        for (GraphNode<Location, Path> node: graph) {
            vertList.add(node.getVertexData());
        }
        return vertList;
    }


    @Override
    public Path getEdgeIfExists(Location src, Location dest) {
        
        List<Path> pathList = getOutEdges(src);       
        
        if (pathList == null) {
            return null;
        }
        
        for (Path path : pathList) {
            if (path.getDestination().equals(dest)) {
                return path;
            }             
        }
        return null;
        
    }


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


    @Override
    public List<Location> getNeighbors(Location vertex) {
        List<Location> neighbors = new ArrayList<Location>();
        List<Path> pathList = getOutEdges(vertex);
        for (Path path : pathList) {
            neighbors.add(path.getDestination());
        }
        return neighbors;
    }


    @Override
    public List<Path> getShortestRoute(Location src, Location dest, String edgePropertyName) {
        
        
        return null;
    }


    @Override
    public String[] getEdgePropertyNames() {
        return edgePropertyNames;
    }

}


class Pair<K , V extends Comparable<V>> implements Comparable<Pair<K,V>>{
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public Pair(K key) {
        this(key, null);
    }
    
    public V getValue() {
        return value;
    } 
    
    public K getKey() {
        return key;
    }
    
    public void setValue(V value) {
        this.value = value; 
    }
    
    @Override
    public int compareTo(Pair<K,V> other) {
        if (other.getValue() == null) {
           return 1; 
        }
        return other.getValue().compareTo(value);
        
    }
    
} 





