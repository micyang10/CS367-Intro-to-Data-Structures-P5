import java.util.ArrayList;
import java.util.List;

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
        return pathList;
    }


    @Override
    public List<Location> getNeighbors(Location vertex) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Path> getShortestRoute(Location src, Location dest, String edgePropertyName) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public String[] getEdgePropertyNames() {
        // TODO Auto-generated method stub
        return null;
    }

}
