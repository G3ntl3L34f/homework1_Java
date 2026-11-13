import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public Vertex addVertex(String name) {
        Vertex vertex = findVertex(name);
        if (vertex == null) {
            vertex = new Vertex(name);
            vertices.add(vertex);
        }
        return vertex;
    }

    public void addEdge(String from, String to, int weight) {
        Vertex a = addVertex(from);
        Vertex b = addVertex(to);
        edges.add(new Edge(a, b, weight));
        edges.add(new Edge(b, a, weight));
    }

    // Нужен для теста отрицательного цикла.
    public void addDirectedEdge(String from, String to, int weight) {
        Vertex a = addVertex(from);
        Vertex b = addVertex(to);
        edges.add(new Edge(a, b, weight));
    }

    public Vertex findVertex(String name) {
        for (Vertex vertex : vertices) {
            if (vertex.getName().equals(name)) {
                return vertex;
            }
        }
        return null;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}