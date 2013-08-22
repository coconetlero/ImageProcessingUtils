
package graph;

/**
 * Implements the Min Cut - Max Flow Ford-Fulkerson algorithm for graphs. The 
 * algorithm needs a directed weighted graph, one vertex with no predecessor 
 * called the source and exactly one vertex with no successor called the sink or 
 * target.
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez<a/>
 */
public class FordFulkerson {

    private Graph graph;

    private Vertex source;

    private Vertex target;

    /**
     * Basic constructor to obtain the max flow or min cut on a graph
     * 
     * @param graph the directed weighted graph.
     * @param source vertex with no predecessor
     * @param target one vertex with no successor
     */
    public FordFulkerson(Graph graph, Vertex source, Vertex target) {
        this.graph = graph;
        this.source = source;
        this.target = target;
    }   
    
   
}
