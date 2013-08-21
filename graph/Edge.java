package graph;

/**
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti<a/>
 * @version 0.0.1-0
 */
public class Edge {
    
    /**
     * The source of this edge
     */
    private Vertex source;

    /**
     * The target of this edge
     */
    private Vertex target;

    /**
     * The value of this edge
     */
    private float weight;

    /**
     * Basic constructor
     */
    public Edge() {        
    }

    /**
     * An no weigth <code>Edge</code> constructor
     * 
     * @param source <code>Vertex</code>
     * @param target <code>Vertex</code>
     */
    public Edge(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
        this.weight = 0;
    }

    /**
     * A weighted Edge constructor
     * @param source <code>Vertex</code>
     * @param target <code>Vertex</code>
     * @param weight of this Edge
     */
    public Edge(Vertex source, Vertex target, float weight) {
        this(source, target);
        this.weight = weight;
    }

    /**
     * Construct a Edge from Edge data
     * @param edge
     */
    public Edge(Edge edge) {
            this.source = edge.getSource();
            this.target = edge.getTarget();
            this.weight = edge.getWeight();        
    }

    /**
     * Return a new copy of this Edge with direction inverted
     * @return
     */
    public Edge invert() {
        return new Edge(target, source, weight);
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            if (((Edge) obj).getSource() == source && ((Edge) obj).getTarget() == target) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 71 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
            return "(" + source.getName() + 
                    " - " + this.weight + " - " +
                    target.getName() + ")";
    }

    /**
     * @return the source
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Vertex source) {
        this.source = source;
    }

    /**
     * @return the target
     */
    public Vertex getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(Vertex target) {
        this.target = target;
    }

    /**
     * @return the value
     */
    public float getWeight() {
        return weight;
    }

    /**
     * @param weight the value to set
     */
    public void setWeight(float weight) {        
        this.weight = weight;
    }   
}
