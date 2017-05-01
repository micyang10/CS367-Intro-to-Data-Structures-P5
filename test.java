import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class test {
    static Scanner in = new Scanner(System.in); 
    static int locationCount = 0;
    static NavigationGraph graph = new NavigationGraph(new String[]{"test", "test2", "test3"});
    public static void main(String[] args) {
           
    
    
    
    
    while (true) {
    switch (testIN()) {
        case 1:
            System.out.println("GIVE name");            
            System.out.println(graph.getLocationByName(in.nextLine()));
            in.nextLine();
            break;
        case 2:
            Location temp = createNewLocation();
            graph.addVertex(temp);
            System.out.println("added location: " + temp.getName());
            break;
        case 3:
           in.nextLine();
            System.out.println("SOURCE?");
            String src = in.nextLine();
            System.out.println("DEST?");
            String dest = in.nextLine();
            in.nextLine();
            Path path = createNewPath(src, dest);
            graph.addEdge(path.getSource(), path.getDestination(), path);
            break;
        case 4:
            for(Location l: graph.getVertices()) {
               System.out.println(l.getName()); 
            }
            break;
        case 5:
            List<Location> temp1 = graph.getVertices();
            int temp2 = 0;
            for(Location l: temp1) {
                System.out.println("" + temp2 + " "+l.getName()); 
             }
             System.out.println("SelectSrc");
             Location start = temp1.get(in.nextInt());
             System.out.println("SelectDest");
             Location end = temp1.get(in.nextInt());
             Path p = graph.getEdgeIfExists(start, end);
             System.out.println(p.getSource().getName() + "-->" + p.getDestination().getName());
             for (Double i : p.getProperties()) {
                 System.out.print(i + " ");
             }
             System.out.println();
             break;
        case 6:
            List<Location> temp11 = graph.getVertices();
            int temp22 = 0;
            for(Location l: temp11) {
                System.out.println("" + temp22 + " "+l.getName());
              
             }
            System.out.println("SelectSrc");
            Location startt = temp11.get(in.nextInt());
            List<Path> pl = graph.getOutEdges(startt);
            for (Path i : pl) {
                System.out.println(i.getSource().getName() + "-->" + i.getDestination().getName());
            }
            break;
        case 7:
            System.out.println("NOT YET IMPLEMENTED");
            break;
        case 8:
            List<Location> temp111 = graph.getVertices();
            int temp222 = 0;
            for(Location l: temp111) {
                System.out.println("" + temp222 + " "+l.getName());
              
             }
            System.out.println("SelectSrc");
            Location starttt = temp111.get(in.nextInt());
            for (Location l: graph.getNeighbors(starttt)) {
                System.out.println(l.getName());
            }
            break;
        case 9:   
            List<Location> tempp = graph.getVertices();
            int tempp1 = 0;
            for(Location l: tempp) {
                System.out.println("" + tempp1 + " "+l.getName()); 
             }
             System.out.println("SelectSrc");
             Location startT = tempp.get(in.nextInt());
             System.out.println("SelectDest");
             Location endD = tempp.get(in.nextInt());
             System.out.println("select property test1, test2, test3");
             in.nextLine();
            List<Path> shortList =  graph.getShortestRoute(startT, endD, in.nextLine());
             for (Path i : shortList) {
                 System.out.println(i.getSource().getName() + "-->" + i.getDestination().getName());
             }
        default:
            break;
        }
    }
    
    
    
    
    

    }
    
    public static int testIN() {
        System.out.println("CHOOOSE OPTION\n" + 
        "1. getLocationByName\n" +
         "2. ADDvert\n" +
           "3.ADDedge\n" +
            "4. GETALLVert\n" +
             "5. TestForEdge\n" +
              " 6. GetEdgesOnVertex\n"+
               "7. getALLedges\n" +
                "8. getNeighbors\n" +
               "9. Dajikstra");
        
        return in.nextInt();
    }
    
    public static Location createNewLocation() {
        locationCount++;
        return new Location("l" + locationCount);
    }
    
    public static Path createNewPath(String src, String dest) {
        Random r = new Random();
        List<Double> properties = new ArrayList<Double>();
        for (int i =0; i < 3; i++) {
            properties.add( (double)(r.nextInt(100)));
        }
        return new Path(graph.getLocationByName(src), graph.getLocationByName(dest), properties);
        
       
    }

}
