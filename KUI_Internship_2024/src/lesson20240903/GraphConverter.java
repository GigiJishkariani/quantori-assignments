package lesson20240903;

import lesson20240903.lists.Graph;

public class GraphConverter {

    public static Graph convertGraphWithMatrixToGraph(GraphWithMatrix graphWithMatrix) {
        Graph graph = new Graph();
        int[][] adjMatrix = graphWithMatrix.getAdjMatrix();
        int numVertices = adjMatrix.length;

        // Add vertices
        for (int i = 0; i < numVertices; i++) {
            graph.addVertex(i);
        }

        // Add edges
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] == 1) {
                    // Add undirected edge
                    graph.addEdge(i, j);
                }
            }
        }

        return graph;
    }

    public static void main(String[] args) {
        // Example usage
        GraphWithMatrix graphWithMatrix = new GraphWithMatrix(4);
        graphWithMatrix.addEdge(0, 1);
        graphWithMatrix.addEdge(0, 2);
        graphWithMatrix.addEdge(1, 2);
        graphWithMatrix.addEdge(2, 3);

        Graph graph = convertGraphWithMatrixToGraph(graphWithMatrix);

        System.out.println("Converted Graph:");
        graph.printGraph();
    }
}
