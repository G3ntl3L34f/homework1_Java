import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Вершины графа варианта 14.
        for (String name : new String[]{"A", "B", "C", "D", "E", "F"}) {
            graph.addVertex(name);
        }

        // Неориентированные рёбра, вес каждого равен 1.
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 1);
        graph.addEdge("B", "D", 1);
        graph.addEdge("B", "E", 1);
        graph.addEdge("C", "E", 1);
        graph.addEdge("D", "E", 1);
        graph.addEdge("E", "F", 1);

        FloydWarshall algorithm = new FloydWarshall();
        FloydWarshall.Result result = algorithm.calculate(graph);

        System.out.println("Исходная матрица расстояний:");
        printInitialMatrix(graph, result.names);

        System.out.println("\\nИтоговая матрица кратчайших расстояний:");
        printMatrix(result);

        List<String> path = result.getPath("A", "F");
        System.out.println("\\nКратчайший путь A -> F: " + String.join(" -> ", path));
        System.out.println("Длина пути: " + result.distance[
                result.names.indexOf("A")][result.names.indexOf("F")]);

        System.out.println("Отрицательный цикл: " +
                (result.negativeCycle ? "обнаружен" : "не обнаружен"));
    }

    private static void printInitialMatrix(Graph graph, List<String> names) {
        int n = names.size();
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (i == j) ? 0 : FloydWarshall.INF;
            }
        }
        for (Edge edge : graph.getEdges()) {
            int i = names.indexOf(edge.getFrom().getName());
            int j = names.indexOf(edge.getTo().getName());
            matrix[i][j] = Math.min(matrix[i][j], edge.getWeight());
        }
        printArray(matrix, names);
    }

    private static void printMatrix(FloydWarshall.Result result) {
        printArray(result.distance, result.names);
    }

    private static void printArray(int[][] matrix, List<String> names) {
        System.out.print("    ");
        for (String name : names) System.out.printf("%5s", name);
        System.out.println();
        for (int i = 0; i < names.size(); i++) {
            System.out.printf("%-4s", names.get(i));
            for (int j = 0; j < names.size(); j++) {
                String value = matrix[i][j] == FloydWarshall.INF
                        ? "INF" : String.valueOf(matrix[i][j]);
                System.out.printf("%5s", value);
            }
            System.out.println();
        }
    }
}