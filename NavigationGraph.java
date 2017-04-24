import java.util.ArrayList;
import java.util.List;

public class NavigationGraph implements GraphADT<Location, Path> {

	private List<GraphNode<Location, Path>> graph;
	
	
    public NavigationGraph(String[] edgePropertyNames) {
        if (edgePropertyNames == null) {
                throw new IllegalArgumentException();
            }
        graph = new ArrayList<GraphNode<Location, Path>>();
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
        // TODO Auto-generated method stub
        
    }


    @Override
    public void addEdge(Location src, Location dest, Path edge) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public List<Location> getVertices() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Path getEdgeIfExists(Location src, Location dest) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Path> getOutEdges(Location src) {
        // TODO Auto-generated method stub
        return null;
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
