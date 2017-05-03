import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class NavigationGraph implements GraphADT<Location, Path> {

	private List<GraphNode<Location, Path>> graph;
	private String[] edgePropertyNames;
	private HashMap<Integer, List<Integer>> adjMatrix;
	 
	
	
	
	
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
        //Since no verticies are removed, graph.size will be the number of the next vertex (with first vertex being 0)
        GraphNode<Location, Path>temp = new GraphNode<Location, Path>(vertex, graph.size());
        graph.add(temp);
        adjMatrix.put(temp.getId(), new ArrayList<Integer>());
                                                                       
    }


    @Override
    public void addEdge(Location src, Location dest, Path edge) {
        GraphNode<Location, Path> srcNode = null;
        GraphNode<Location, Path> destNode = null;
        for (GraphNode<Location, Path> node : graph  ) {           
            if (node.getVertexData().equals(src)) {
                List<Path>temp = node.getOutEdges();
                for (Path tempPath : temp) {
                    if (tempPath.getDestination().equals(src) && tempPath.getDestination().equals(dest)) {
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


    @Override
    public List<Path> getShortestRoute(Location src, Location dest, String edgePropertyName) {
       
        if (src == null || dest == null || edgePropertyName == null) {
            throw new IllegalArgumentException();
        }
        
        
        PriorityQueue<Pair<GraphNode<Location, Path>, Double>> pq = new PriorityQueue<Pair<GraphNode<Location, Path>, Double>>();
       List<Pair<GraphNode<Location, Path>, Double>> pairList = new ArrayList<Pair<GraphNode<Location, Path>, Double>>();
       HashMap<GraphNode<Location, Path>,GraphNode<Location, Path>> pred = new HashMap<GraphNode<Location, Path>,GraphNode<Location, Path>>();      
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
               pairList.add(new Pair<GraphNode<Location, Path>, Double>(node, 0.0));
           } else {
           
               pairList.add(new Pair<GraphNode<Location, Path>, Double>(node, Double.MAX_VALUE));
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
           for(GraphNode<Location, Path> v : getNeighboringGraphNodes(u.getKey())) {
               Path temp = getEdgeIfExists(u.getKey().getVertexData(), v.getVertexData());
               if (pairList.get(v.getId()).getValue() > u.getValue() + temp.getProperties().get(property)) {
                   pairList.get(v.getId()).setValue( u.getValue() + temp.getProperties().get(property));
                   pq.add(pairList.get(v.getId()));
                   pred.put(v, u.getKey());
               }
           }
       }
       
       
       
     
  
       
       if (pred.get(destNode) ==null) {
           return new LinkedList<Path>();
       }
        
       List<Path> route = new LinkedList<Path>();
       while (pred.get(destNode) !=null) {
            route.add(getEdgeIfExists(pred.get(destNode).getVertexData(), destNode.getVertexData()));
            destNode = pred.get(destNode);
       }
       Collections.reverse(route);
       return route;
       
       
       
        
    }
    
    
    
 
    


    @Override
    public String[] getEdgePropertyNames() {
        return edgePropertyNames;
    }
    
    private List<GraphNode<Location, Path>> getNeighboringGraphNodes(GraphNode<Location, Path> node) {
       
        if (node == null) {
            throw new IllegalArgumentException();
        }
        
        List<GraphNode<Location, Path>> neighbors = new ArrayList<GraphNode<Location, Path>>();
        for (Integer n : adjMatrix.get(node.getId())) {
            neighbors.add(graph.get(n));
        }
        return neighbors;
    } 
    
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





