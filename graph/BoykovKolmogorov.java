package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti<a/>
 *
 * @Article{Boykov and Kolmogorov 2004, author = "Yuri Boykov and Vladimir
 * Kolmogorov", title = "An Experimental Comparison of Min-Cut/Max-Flow
 * Algorithms for Energy Minimization in Vision", journal = "<i>IEEE
 * TRANSACTIONS ON PATTERN ANALYSIS AND MACHINE INTELLIGENCE</i>", year =
 * "2004", pages = "1124-1137", keywords = "Energy minimization, graph
 * algorithms, minimum cut, maximum flow, image restoration, segmentation,
 * stereo, multicamera scene reconstruction", }
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
    int idxxx = 0;

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
    public ArrayList<Vertex> minCut(int width) {
        while (true) {
            Edge[] path = grow();
            if (path.length == 0) {
                return findFinalTreeVertexes();
            }
            augment(path);
            adopt(width);
        }
    }

    /**
     * Active nodes acquire new children from a set of free nodes.
     *
     *
     * @return an <code>ArrayList</code> containing the path between source and
     * target vertexes as a sequence.
     */
    public Edge[] grow() {
        while (!active.isEmpty()) {
            Vertex p = active.getFirst();
            ArrayList<Edge> currentEdges;
            try {
                currentEdges = graph.getEdges(p);

                for (Edge edge : currentEdges) {
                    // if tree_cap(p->q) > 0
                    if (edge.getWeight() > 0) {
                        Vertex q = edge.getTarget();
                        if (this.tree(q) == 0) {
                            q.setParent(p);
                            switch (this.tree(p)) {
                                case 1:
                                    S_tree[q.getName()] = true;
                                    break;
                                case 2:
                                    T_tree[q.getName()] = true;
                                    break;
                            }
                            active.add(q);
                        }
                        if ((this.tree(q) != 0) && (this.tree(q) != this.tree(p))) {
                            // return P = PATH_(s->t)                                                       
//                            Stack<Edge> sPath = new Stack<Edge>();
                            Deque<Edge> sPath = new ArrayDeque<Edge>();

                            // find path from p to s
                            Vertex current = p;
                            Vertex parent = current.getParent();
                            while (parent != null) {
                                Edge e = graph.getEdge(parent, current);
                                sPath.push(e);
                                current = parent;
                                parent = current.getParent();
                            }

                            // find path from q to t 
                            ArrayList<Edge> tPath = new ArrayList<Edge>();

                            current = q;
                            parent = current.getParent();
                            while (parent != null) {
                                Edge e = graph.getEdge(parent, current);
                                tPath.add(e);
                                current = parent;
                                parent = current.getParent();
                            }

                            // concatenate the the paths s->p and q->t
                            Edge[] path = new Edge[sPath.size() + tPath.size() + 1];
                            int idx = 0;
                            while (!sPath.isEmpty()){
                                path[idx] = sPath.pop();
                                idx++;
                            }
                            path[idx] = graph.getEdge(p, q);
                            idx++;
                            for (Edge e : tPath) {
                                path[idx] = e;
                                idx++;
                            }

                            // print path
//                        String s = "(";
//                        for (int i = 0; i < path.length; i++) {
//                            s += path[i];
//                            s += ", ";
//                        }
//                        s += ")";
//                        System.out.println(s);

                            return path;
                        }
                    }
                }
            }
            catch (Exception ex) {
                Logger.getLogger(BoykovKolmogorov.class.getName()).log(Level.SEVERE, null, ex);
            }
            active.removeFirst();
        }
        return new Edge[0];
    }

    /**
     *
     * @param path
     */
    public void augment(Edge[] path) {
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
                residualEdge = new Edge(edge.getTarget(), edge.getSource(), delta);
                graph.addEdge(residualEdge);
            }
            else {
                residualEdge.setWeight(residualEdge.getWeight() + delta);
            }

            if (edge.getWeight() <= 0) {
                Vertex p = edge.getSource();
                Vertex q = edge.getTarget();

                if (tree(p) == 1 && tree(q) == 1) {
                    q.setParent(null);
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
    private void adopt(int width) {
        while (!orphans.isEmpty()) {
//            System.out.println(orphans.size());
            Vertex p = orphans.pop();

            // process p
            // find a new valid parent for p
            boolean findValidParent = false;

            // fisrt check if source or sink can be a valid parent
            switch (this.tree(p)) {
                case 1:
                    Edge es = graph.getEdge(source, p);
                    if (es.getWeight() > 0) {
                        p.setParent(source);
                        findValidParent = true;
                    }
                    break;
                case 2:
                    Edge et = graph.getEdge(target, p);
                    if (et.getWeight() > 0) {
                        p.setParent(target);
                        findValidParent = true;
                    }
                    break;
            }

            // find a valid parent with vertex neighbours 
            int[] neighbours = this.getNeighbours(p.getName(), width);
            for (int i = 0; i < neighbours.length; i++) {
                if (neighbours[i] > 0) {
                    Edge e = graph.getEdge(new Vertex(neighbours[i]), p);
                    if (e.getWeight() > 0) {
                        Vertex q = e.getSource();
                        if ((tree(q) == tree(p)) && validOrigin(q)) {
                            p.setParent(q);
                            findValidParent = true;
                            break;
                        }
                    }
                }
            }

            // If p does not find a valid parent, then p becomes a free node
            if (!findValidParent) {
                for (int i = 0; i < neighbours.length; i++) {
                    if (neighbours[i] > 0) {

                        Edge e = graph.getEdge(new Vertex(neighbours[i]), p);
                        Vertex q = e.getSource();
                        if (tree(q) == tree(p)) {
                            if (e.getWeight() > 0) {
                                active.add(q);
                            }
                            Vertex qParent = q.getParent();
                            if (qParent != null) {
                                if (qParent.equals(p)) {
                                    orphans.add(q);
                                    q.setParent(null);
                                }
                            }
                        }
                    }
                }
                S_tree[p.getName()] = false;
                T_tree[p.getName()] = false;

                int idx = active.indexOf(p);
                while (idx >= 0) {
                    active.remove(idx);
                    idx = active.indexOf(p);
                }
            }
        }
    }

    /**
     * Test if the given vertex belongs to S or T trees, or if vertex is orphan.
     *
     * @param v a vertex in question
     *
     * @return 0 if vertex is orphan. 1 if vertex belongs to S tree. 2 if vertex
     * belongs to T tree.
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
        }
        else {
            return -1;
        }
    }

    /**
     * Verify if a given
     * <code>Vertex</code> has a conected path to the source or sink vertex;
     *
     * @param q current Vertex
     *
     * @return true if valid path to the source or sink vertex was found, or
     * false in other case
     */
    private boolean validOrigin(Vertex q) {
        Vertex parent = q.getParent();
        while (parent != null) {
            if (parent == source || parent == target) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    /**
     * Find the four neighbours of the given pixel index.
     *
     * @param idx
     *
     * @return an array of 4 neighbours of the given index pixel.
     */
    private int[] getNeighbours(int idx, int width) {
        idx--;
        int x = idx % width;
        int y = idx / width;

        int[] neighbours = new int[4];

        if ((x - 1) < 0) {
            neighbours[0] = -1;
        }
        else {
            neighbours[0] = ((y * width) + (x - 1)) + 1;
        }

        if ((y - 1) < 0) {
            neighbours[1] = -1;
        }
        else {
            neighbours[1] = (((y - 1) * width) + x) + 1;
        }

        if ((x + 1) == width) {
            neighbours[2] = -1;
        }
        else {
            neighbours[2] = ((y * width) + (x + 1)) + 1;
        }

        int n_idx = ((y + 1) * width) + x;
        if (n_idx >= (graph.size() - 2)) {
            neighbours[3] = - 1;
        }
        else {
            neighbours[3] = ++n_idx;
        }

        return neighbours;
    }

    private ArrayList<Vertex> findFinalTreeVertexes() {
        ArrayList<Vertex> treeVertexex = new ArrayList<Vertex>();
        treeVertexex.add(source);

        Stack<Vertex> S = new Stack<Vertex>();
        S.push(source);
        source.setVisited(true);

        // create a tree until target is reached 
        while (!S.empty()) {
            try {
                Vertex v = S.pop();                
                ArrayList<Edge> E = graph.getEdges(v);
                // if v has an unvisited neighbour w                
                for (Edge edge : E) {
                    if (edge.getWeight() > 0) {
                        Vertex w = edge.getTarget();
                        if (!w.isVisited()) {
                            treeVertexex.add(w);
                            w.setVisited(true);
                            w.setParent(v);
                            S.push(w);
                        }
                    }
                }
            }
            catch (Exception ex) {
                Logger.getLogger(BoykovKolmogorov.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return treeVertexex;
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
