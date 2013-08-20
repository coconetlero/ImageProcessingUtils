/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * An ady 
* @author <a ref ="zianfanti@gmail.com"> Zian Fanti<a/>
 * @version 0.0.1-0
 */
public class Graph {

    /**
     * The graph implementation
     */
    private HashMap<Vertex, ArrayList<Edge>> graph;

    /**
     *
     */
    public Graph() {
        this.graph = new HashMap<Vertex, ArrayList<Edge>>();
    }

    /**
     * 
     * @param numOfVertexex 
     */
    public Graph(int numOfVertexex) {
        this.graph = new HashMap<Vertex, ArrayList<Edge>>(numOfVertexex);
    }
    
    /**
     *
     * @param vertexes
     * @param edges
     */
    public Graph(Vertex[] vertexes, Edge[][] edges) {
        this.graph = new HashMap<Vertex, ArrayList<Edge>> (vertexes.length);
        for (int i = 0; i < vertexes.length; i++) {
            ArrayList<Edge> vertexEdges = new ArrayList<Edge>();                        
            vertexEdges.addAll(Arrays.asList(edges[i]));
            graph.put(vertexes[i], vertexEdges);
        }
    }
    
    /**
     * 
     * @param vertex
     * @param edges 
     */
    public void addConnectedVertex(Vertex vertex, ArrayList<Edge> edges) {
        graph.put(vertex, edges);
    }
    
    /**
     * 
     */
    public void addVertex(Vertex vertex) {
        ArrayList<Edge> edges = new ArrayList<Edge>();        
        graph.put(vertex, edges);
    }
    
    /**
     *
     * @param edge
     */
    public void addEdge(Edge edge) {
        Vertex source = edge.getSource();
        ArrayList<Edge> edges = graph.get((Vertex) source);
        edges.add(edge);
        graph.put(source, edges);
    }
    
    /**
     *
     * @return
     */
    public int numOfVertexes() {
        return graph.size();
    }
   
    
    /**
     * Build a string representation of the adjency matrix, corresponding of this
     * graph.
     *
     * @return a string
     */
    @Override
    public String toString() {
        String graphString = "";
        Set vertexes = graph.keySet();
        Iterator vertexIterator = vertexes.iterator();
        
        while(vertexIterator.hasNext()) {
            Vertex v = (Vertex) vertexIterator.next();            
            graphString += (v.getName() + " -> [");
            ArrayList<Edge> edges = graph.get((Vertex) v);
            for (int i = 0; i < edges.size(); i++) {                
                graphString += (i == 0) ? "" : ", ";
                Edge e = edges.get(i);
                graphString += ("(" + e.getTarget().getName() + ", " +
                        e.getWeight() + ")");
            }
            graphString += ("] \n");
        }
        
        return graphString;
    }
}
