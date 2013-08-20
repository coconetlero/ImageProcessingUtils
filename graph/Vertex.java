
package graph;

/**
 * Imaplements a basic Vertex object. 
 * 
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti<a/>
 * @version 0.0.1-0
 */
public class Vertex {

    /** The name of this vertex */
    private Object name;

    /** Vertex parent. If it had */
    private Vertex parent;

    /**
     * Name of the vertex
     * @param name
     */
    public Vertex(int name) {
        this.name = name;
    }

    /**
     * Return the name of this vertex
     * @return
     */
    public Object getName() {
        return name;
    }

    /**
     * Return the parent vertex, if any.
     * @return the parent vertex, if any. Else return null;
     */
    public Vertex getParent() {
       return (parent != null) ? parent : null;
    }

    /**
     * Set parent of this <code>Vertex<c/code>
     * @param v
     */
    public void setParent(Vertex v) {
        this.parent = v;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            return ((Vertex)obj).getName() == name;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "" + name;
    }
}
