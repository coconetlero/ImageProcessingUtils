package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

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

    /**
     *
     * @return
     */
//    public ArrayList<Vertex> minCut() {
//        Edge[] path = findPath(source, target);
//        
//    }
    /**
     * Find a path between source and target vertexes into this graph, using the
     * Depth-first search algoritm for traversingthe graph. This function works
     * with directed, weighted graphs.
     *
     * @param source vertex
     * @param target vertex
     * @return an <code>ArrayList</code> containing the path between source and
     * target vertexes as a sequence.
     */
    public Edge[] findPath(Vertex source, Vertex target) {
        Graph tree = new Graph();

        boolean pathFound = false;
        Stack<Vertex> S = new Stack<Vertex>();
        S.push(source);
        source.setVisited(true);

        // create a tree until target is reached 
        while (!S.empty() && !pathFound) {
            Vertex v = S.pop();
            ArrayList<Edge> E = new ArrayList<Edge>();

            // if v has an unvisited neighbour w 
            for (Edge edge : graph.getEdges(v)) {
                Vertex w = edge.getTarget();
                if (w.equals(target)) {
                    w.setVisited(true);
                    w.setParent(v);
                    E.add(new Edge(v, w));
                    tree.addConnectedVertex(v, E);
                    tree.addVertex(w);
                    pathFound = true;
                    break;
                }
                else if (!w.isVisited() && (edge.getWeight() > 0)) {
                    w.setVisited(true);
                    w.setParent(v);
                    E.add(new Edge(v, w));
                    S.push(w);
                }
            }
            tree.addConnectedVertex(v, E);
        }

        System.out.println(tree);

        // from target retrieve the source
        LinkedList<Vertex> sPath = new LinkedList<Vertex>();
        sPath.add(target);
        while (sPath.getLast().getParent() != null) {
            sPath.add(sPath.getLast().getParent());
        }

        Edge[] path = new Edge[sPath.size()];
        int i = 0;
        while (!sPath.isEmpty()) {
            Vertex v = sPath.removeFirst();
            if (v.getParent() != null) {
                ArrayList<Edge> E = tree.getEdges(v.getParent());
                for (Edge e : E) {
                    if (v.equals(e.getTarget())) {
                        path[i] = e;
                        System.out.println(e);
                    }
                }
            }
            i++;
        }
        return path;
    }
}
