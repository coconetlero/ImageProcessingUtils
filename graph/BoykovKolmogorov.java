package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

/**
 *
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti<a/>
 *
 * @Article{Boykov and Kolmogorov 2004,
 * author = "Yuri Boykov and Vladimir Kolmogorov",
 * title = "An Experimental Comparison of Min-Cut/Max-Flow Algorithms for
 * Energy Minimization in Vision",
 * journal = "<i>IEEE TRANSACTIONS ON PATTERN ANALYSIS AND MACHINE
 * INTELLIGENCE</i>",
 * year = "2004",
 * pages = "1124-1137",
 * keywords = "Energy minimization, graph algorithms, minimum cut, maximum flow,
 * image restoration, segmentation, stereo, multicamera scene reconstruction",
 * }
 */
public class BoykovKolmogorov {

    /**
     * The given graph to apply this algorithm
     */
    private Graph graph;

    /**
     * the source vertex for the algorithm
     */
    private Vertex source;

    /**
     * the sink vertex for the algorithm
     */
    private Vertex target;

    /**
     * The source tree
     */
    private Graph S;

    /**
     * The target tree
     */
    private Graph T;

    /**
     *
     */
    private LinkedList<Vertex> active;

    /**
     *
     */
    private LinkedList<Vertex> orphans;

    /**
     * Flag indicating the affiliation of each vertex, for vertexes in S tree
     */
    private boolean[] S_tree;

    /**
     *
     */
    private boolean[] T_tree;

    /**
     *
     * @param graph
     * @param source
     * @param target
     */
    public BoykovKolmogorov(Graph graph, Vertex source, Vertex target) {
        this.graph = graph;
        this.source = source;
        this.target = target;

        this.S = new Graph();
        this.T = new Graph();
        S.addVertex(source);
        T.addVertex(target);

        this.active = new LinkedList<Vertex>();
        active.add(source);
        active.add(target);

        this.orphans = new LinkedList<Vertex>();

        this.S_tree = new boolean[graph.size()];
        this.T_tree = new boolean[graph.size()];
        S_tree[source.getName()] = true;
        T_tree[target.getName()] = true;
    }

    /**
     *
     * @return
     */
//    public ArrayList<Vertex> minCut() {
//    }
    /**
     * Active nodes acquire new children from a set of free nodes.
     *
     *
     * @return an <code>ArrayList</code> containing the path between source and
     *         target vertexes as a sequence.
     */
    public Edge[] grow() {
        while (!active.isEmpty()) {
            Vertex p = active.pop();
            ArrayList<Edge> currentEdges = graph.getEdges(p);
            for (Edge edge : currentEdges) {
                // if tree_cap(p->q) > 0
                if (edge.getWeight() > 0) {
                    Vertex q = edge.getTarget();
                    if (this.tree(q) == 0) {
                        q.setParent(p);
                        switch (this.tree(p)) {
                            case 1:
                                S.addVertex(q);
                                S_tree[q.getName()] = true;
                                break;
                            case 2:
                                T.addVertex(q);
                                T_tree[q.getName()] = true;
                                break;
                        }
                        active.add(q);
                    }
                    if ((this.tree(q) != 0) && (this.tree(q) != this.tree(p))) {
                        // return P = PATH_(s->t)                        
                        Stack<Vertex> path = new Stack<Vertex>();
                        path.push(target);
                        while (path.peek().getParent() != null) {
                            path.push(path.peek().getParent());
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @param path
     */
    public void augmentation(Edge[] path) {
        // find the bottleneck capacity delta on path P
        float delta = Float.MAX_VALUE;
        for (int i = 0; i < path.length; i++) {
            float tempWeight = path[i].getWeight();
            if (tempWeight < delta) {
                delta = tempWeight;
            }
        }

        // update the residual graph by pushing flow Delta through P
        for (int i = 0; i < path.length; i++) {
            Edge edge = path[i];
            edge.setWeight(edge.getWeight() - delta);

            Edge residualEdge = graph.getEdge(edge.getTarget(), edge.getSource());
            if (residualEdge == null) {
                graph.addEdge(new Edge(edge.getTarget(), edge.getSource(), delta));
            } else {
                residualEdge.setWeight(residualEdge.getWeight() + delta);
            }

            if (edge.getWeight() == 0) {
                Vertex p = edge.getSource();
                Vertex q = edge.getTarget();
                if (tree(p) == 1 && tree(q) == 1) {
                    p.setParent(null);
                    orphans.add(q);
                }
                if (tree(p) == 2 && tree(q) == 2) {
                    p.setParent(null);
                    orphans.add(p);
                }
            }
        }
    }

    /**
     * All orphan nodes in O are processed until O becomes empty. Each node p
     * being processed tries to find a new valid parent within the same search
     * tree; in case of success, p remains in the tree but with a new parent;
     * otherwise, it becomes a free node and all its children are added to O.
     */
    public void adoption() {
        while(!orphans.isEmpty()) {
            Vertex p = orphans.pop();  
            
        }
    }

    /**
     * Test if the given vertex belongs to S or T trees, or if vertex is
     * orphan.
     *
     * @param v a vertex in question
     *
     * @return 0 if vertex is orphan
     *         1 if vertex belongs to S tree
     *         2 if vertex belongs to T tree
     */
    private int tree(Vertex v) {
        int name = v.getName();
        if (!S_tree[name] && !T_tree[name]) {
            return 0;
        }
        if (S_tree[name]) {
            return 1;
        }
        if (T_tree[name]) {
            return 2;
        } else {
            return -1;
        }
    }
    /**
     *
     * @param path
     *
     * @return
     */
//    private ArrayList<Edge> vertexToEdgePath(Stack<Vertex> inputPath) {
//        // from target retrieve the source
//        LinkedList<Vertex> sPath = new LinkedList<Vertex>();
//        sPath.add(target);
//        while (sPath.getLast().getParent() != null) {
//            sPath.add(sPath.getLast().getParent());
//        }
//
//        Edge[] path = new Edge[sPath.size() - 1];
//        int i = 0;
//        while (!sPath.isEmpty()) {
//            Vertex v = sPath.removeFirst();
//            if (v.getParent() != null) {
//                ArrayList<Edge> E = tree.getEdges(v.getParent());
//                for (Edge e : E) {
//                    if (v.equals(e.getTarget())) {
//                        path[i] = e;
////                            System.out.println(e);
//                        break;
//                    }
//                }
//            }
//            i++;
//        }
//
//
//
//        if (path.length > 0) {
//            return path;
//        } else {
//            return null;
//        }
//    }
}
