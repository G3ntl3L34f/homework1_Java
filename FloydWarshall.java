import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FloydWarshall {
    public static final int INF = 1_000_000_000;

    public static class Result {
        public final int[][] distance;
        public final int[][] next;
        public final List<String> names;
        public final boolean negativeCycle;

        Result(int[][] distance, int[][] next, List<String> names) {
            this.distance = distance;
            this.next = next;
            this.names = names;
            boolean found = false;
            for (int i = 0; i < distance.length; i++) {
                if (distance[i][i] < 0) found = true;
            }
            negativeCycle = found;
        }

        public List<String> getPath(String from, String to) {
            int start = names.indexOf(from);
            int finish = names.indexOf(to);

            if (start == -1 || finish == -1 || next[start][finish] == -1) {
                return Collections.emptyList();
            }

            List<String> path = new ArrayList<>();
            int current = start;
            path.add(names.get(current));

            while (current != finish) {
                current = next[current][finish];
                if (current == -1 || path.size() > names.size() + 1) {
                    return Collections.emptyList();
                }
                path.add(names.get(current));
            }
            return path;
        }
    }

    public Result calculate(Graph graph) {
        List<String> names = new ArrayList<>();
        for (Vertex vertex : graph.getVertices()) {
            names.add(vertex.getName());
        }
        Collections.sort(names);

        int n = names.size();
        int[][] dist = new int[n][n];
        int[][] next = new int[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
            next[i][i] = i;
        }

        for (Edge edge : graph.getEdges()) {
            int from = names.indexOf(edge.getFrom().getName());
            int to = names.indexOf(edge.getTo().getName());
            if (edge.getWeight() < dist[from][to]) {
                dist[from][to] = edge.getWeight();
                next[from][to] = to;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        int candidate = dist[i][k] + dist[k][j];
                        if (candidate < dist[i][j]) {
                            dist[i][j] = candidate;
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }
        }

        return new Result(dist, next, names);
    }
}