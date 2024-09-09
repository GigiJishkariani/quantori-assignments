package lesson20240903.lists;

import lesson20240903.GraphWithMatrix;

import java.util.*;

public class DeBruijnGraphMatrix {

    public static void main(String[] args) {
        String sequence = "GATCACA";
        int k = 4;
        List<String> kMinusOneMers = new ArrayList<>();

        for (int i = 0; i <= sequence.length() - k; i++) {
            String kmer = sequence.substring(i, i + k);
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            kMinusOneMers.add(prefix);
            kMinusOneMers.add(suffix);
        }


        Map<String, Integer> indexMap = new HashMap<>();
        int uniqueCount = 0;
        for (String mer : kMinusOneMers) {
            if (!indexMap.containsKey(mer)) {
                indexMap.put(mer, uniqueCount++);
            }
        }


        GraphWithMatrix graph = new GraphWithMatrix(uniqueCount);

        Map<Integer, String> indexMapReversed = new HashMap<>();

        for (int i = 0; i <= sequence.length() - k; i++) {
            String kmer = sequence.substring(i, i + k);
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            int prefixIndex = indexMap.get(prefix);
            int suffixIndex = indexMap.get(suffix);
            graph.addEdge(prefixIndex, suffixIndex);
            indexMapReversed.put(prefixIndex, suffix);
            indexMapReversed.put(suffixIndex, prefix);
        }



        System.out.println("De Bruijn Graph (Adjacency Matrix):");
        graph.printGraph();


        System.out.println("\nMapping of k-1-mers to indices:");
        for (Map.Entry<String, Integer> entry : indexMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + indexMapReversed.get(entry.getValue()));
        }

        System.out.println(reconstructGenome(graph, indexMap, k));
    }

    private static String reconstructGenome(GraphWithMatrix graph, Map<String, Integer> indexMap, int k) {

        int[][] adjMatrix = graph.getAdjMatrix();
        int numVertices = adjMatrix.length;


        int startNode = -1, endNode = -1;
        int[] inDegrees = new int[numVertices];
        int[] outDegrees = new int[numVertices];


        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] == 1) {
                    outDegrees[i]++;
                    inDegrees[j]++;
                }
            }
        }


        for (int i = 0; i < numVertices; i++) {
            if (outDegrees[i] - inDegrees[i] == 1) {
                startNode = i;
            }
            if (inDegrees[i] - outDegrees[i] == 1) {
                endNode = i;
            }
        }

        if (startNode == -1 || endNode == -1) {
            System.out.println("No Eulerian path found.");
            return "";
        }


        List<Integer> eulerianPath = findEulerianPath(adjMatrix, startNode);


        StringBuilder genome = new StringBuilder();
        if (!eulerianPath.isEmpty()) {

            genome.append(indexMap.entrySet().stream()
                    .filter(entry -> entry.getValue() == eulerianPath.get(0))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(""));

            for (int i = 1; i < eulerianPath.size(); i++) {
                int vertex = eulerianPath.get(i);
                String kmer = indexMap.entrySet().stream()
                        .filter(entry -> entry.getValue() == vertex)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse("");
                genome.append(kmer.charAt(k - 1));
            }
        }

        return genome.toString();
    }


    private static List<Integer> findEulerianPath(int[][] adjMatrix, int startNode) {
        List<Integer> path = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            int u = stack.peek();
            boolean hasOutgoingEdge = false;
            for (int v = 0; v < adjMatrix.length; v++) {
                if (adjMatrix[u][v] == 1) {
                    stack.push(v);
                    adjMatrix[u][v] = 0;
                    hasOutgoingEdge = true;
                    break;
                }
            }
            if (!hasOutgoingEdge) {
                path.add(stack.pop());
            }
        }

        Collections.reverse(path);
        return path;
    }
}
