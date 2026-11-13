public class Tests {
    private static void check(boolean condition, String name) {
        System.out.println((condition ? "[OK] " : "[ОШИБКА] ") + name);
        if (!condition) throw new RuntimeException("Тест не пройден: " + name);
    }

    public static void main(String[] args) {
        FloydWarshall algorithm = new FloydWarshall();

        // 1. Основной граф варианта 14.
        Graph mainGraph = new Graph();
        for (String v : new String[]{"A", "B", "C", "D", "E", "F"}) mainGraph.addVertex(v);
        mainGraph.addEdge("A", "B", 1);
        mainGraph.addEdge("B", "C", 1);
        mainGraph.addEdge("B", "D", 1);
        mainGraph.addEdge("B", "E", 1);
        mainGraph.addEdge("C", "E", 1);
        mainGraph.addEdge("D", "E", 1);
        mainGraph.addEdge("E", "F", 1);
        FloydWarshall.Result main = algorithm.calculate(mainGraph);
        int a = main.names.indexOf("A"), f = main.names.indexOf("F");
        check(main.distance[a][f] == 3, "Основной граф: расстояние A-F равно 3");
        check(main.getPath("A", "F").toString().equals("[A, B, E, F]"),
                "Основной граф: путь A-B-E-F");

        // 2. Одна вершина.
        Graph one = new Graph();
        one.addVertex("A");
        FloydWarshall.Result oneResult = algorithm.calculate(one);
        check(oneResult.distance[0][0] == 0 &&
                oneResult.getPath("A", "A").toString().equals("[A]"),
                "Одна вершина: путь A-A имеет длину 0");

        // 3. A и F существуют, но не соединены.
        Graph disconnected = new Graph();
        disconnected.addVertex("A");
        disconnected.addVertex("F");
        FloydWarshall.Result disconnectedResult = algorithm.calculate(disconnected);
        check(disconnectedResult.distance[0][1] == FloydWarshall.INF &&
                disconnectedResult.getPath("A", "F").isEmpty(),
                "Нет пути между A и F");

        // 4. Граф с обычным циклом.
        Graph cycle = new Graph();
        cycle.addEdge("A", "B", 1);
        cycle.addEdge("B", "C", 1);
        cycle.addEdge("C", "A", 1);
        FloydWarshall.Result cycleResult = algorithm.calculate(cycle);
        check(cycleResult.distance[0][2] == 1 || cycleResult.distance[2][0] == 1,
                "Граф с циклом: кратчайшее расстояние рассчитано");

        // 5. Граф без цикла (цепочка).
        Graph noCycle = new Graph();
        noCycle.addEdge("A", "B", 2);
        noCycle.addEdge("B", "C", 3);
        FloydWarshall.Result noCycleResult = algorithm.calculate(noCycle);
        check(noCycleResult.distance[0][2] == 5,
                "Граф без цикла: расстояние по цепочке равно 5");

        // 6. Отрицательный цикл в ориентированном графе.
        Graph negative = new Graph();
        negative.addDirectedEdge("A", "B", 1);
        negative.addDirectedEdge("B", "C", -3);
        negative.addDirectedEdge("C", "A", 1);
        FloydWarshall.Result negativeResult = algorithm.calculate(negative);
        check(negativeResult.negativeCycle, "Отрицательный цикл обнаружен");

        // 7. Повторный запуск на том же объекте.
        FloydWarshall.Result repeated = algorithm.calculate(mainGraph);
        check(repeated.distance[a][f] == 3, "Повторный запуск даёт тот же результат");

        System.out.println("Все проверки пройдены.");
    }
}