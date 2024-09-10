package lesson20240903.lists;

import lesson20240903.GraphConverter;
import lesson20240903.GraphWithMatrix;

import java.util.*;

public class DeBruijnGraphMatrix {

    public static void main(String[] args) {
        String sequence = "GATCAC";
        int k = 4;


        List<String> kMinusOneMers = generateKMers(sequence, k);
        Map<String, Integer> indexMap = buildIndexMap(kMinusOneMers);
        GraphWithMatrix graph = buildDeBruijnGraph(sequence, k, indexMap);
        Map<Integer, String> indexMapReversed = reverseIndexMap(sequence, k, indexMap, graph);

        printGraph(graph);
        printIndexMap(indexMap, indexMapReversed);

    }

    // Extracts k-1-mers from the sequence
    private static List<String> generateKMers(String sequence, int k) {
        List<String> kMinusOneMers = new ArrayList<>();
        for (int i = 0; i <= sequence.length() - k; i++) {
            String kmer = sequence.substring(i, i + k);
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            kMinusOneMers.add(prefix);
            kMinusOneMers.add(suffix);
        }
        return kMinusOneMers;
    }


    private static Map<String, Integer> buildIndexMap(List<String> kMinusOneMers) {
        Map<String, Integer> indexMap = new HashMap<>();
        int uniqueCount = 0;
        for (String mer : kMinusOneMers) {
            if (!indexMap.containsKey(mer)) {
                indexMap.put(mer, uniqueCount++);
            }
        }
        return indexMap;
    }


    private static GraphWithMatrix buildDeBruijnGraph(String sequence, int k, Map<String, Integer> indexMap) {
        GraphWithMatrix graph = new GraphWithMatrix(indexMap.size());
        for (int i = 0; i <= sequence.length() - k; i++) {
            String kmer = sequence.substring(i, i + k);
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            int prefixIndex = indexMap.get(prefix);
            int suffixIndex = indexMap.get(suffix);
            if (i == 0) {
                graph.addEdge(prefixIndex, suffixIndex);
            } else {
                graph.addDirectedEdge(prefixIndex, suffixIndex);
            }
        }
        return graph;
    }


    private static Map<Integer, String> reverseIndexMap(String sequence, int k, Map<String, Integer> indexMap, GraphWithMatrix graph) {
        Map<Integer, String> indexMapReversed = new HashMap<>();
        for (int i = 0; i <= sequence.length() - k; i++) {
            String kmer = sequence.substring(i, i + k);
            String prefix = kmer.substring(0, k - 1);
            String suffix = kmer.substring(1);
            int prefixIndex = indexMap.get(prefix);
            int suffixIndex = indexMap.get(suffix);
            indexMapReversed.put(prefixIndex, suffix);
            indexMapReversed.put(suffixIndex, prefix);
        }
        return indexMapReversed;
    }


    private static void printGraph(GraphWithMatrix graph) {
        System.out.println("De Bruijn Graph (Adjacency Matrix):");
        graph.printGraph();
    }


    private static void printIndexMap(Map<String, Integer> indexMap, Map<Integer, String> indexMapReversed) {
        System.out.println("\nMapping of k-1-mers to indices:");
        for (Map.Entry<String, Integer> entry : indexMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + indexMapReversed.get(entry.getValue()));
        }
    }





//    private static String reconstructGenome(GraphWithMatrix graph, Map<String, Integer> indexMap, int k) {
//        int[][] adjMatrix = graph.getAdjMatrix();
//        int numVertices = adjMatrix.length;
//
//        int startNode = -1, endNode = -1;
//        int[] inDegrees = new int[numVertices];
//        int[] outDegrees = new int[numVertices];
//

//        calculateDegrees(adjMatrix, numVertices, inDegrees, outDegrees);
//

//        startNode = findStartNode(outDegrees, inDegrees);
//        endNode = findEndNode(outDegrees, inDegrees);
//
//        if (startNode == -1 || endNode == -1) {
//            System.out.println("No Eulerian path found.");
//            return "";
//        }
//
//        List<Integer> eulerianPath = findEulerianPath(adjMatrix, startNode);
//        return buildGenomeFromPath(eulerianPath, indexMap, k);
//    }
//

//    private static void calculateDegrees(int[][] adjMatrix, int numVertices, int[] inDegrees, int[] outDegrees) {
//        for (int i = 0; i < numVertices; i++) {
//            for (int j = 0; j < numVertices; j++) {
//                if (adjMatrix[i][j] == 1) {
//                    outDegrees[i]++;
//                    inDegrees[j]++;
//                }
//            }
//        }
//    }
//

//    private static int findStartNode(int[] outDegrees, int[] inDegrees) {
//        for (int i = 0; i < outDegrees.length; i++) {
//            if (outDegrees[i] - inDegrees[i] == 1) {
//                return i;
//            }
//        }
//        return -1;
//    }
//

//    private static int findEndNode(int[] outDegrees, int[] inDegrees) {
//        for (int i = 0; i < inDegrees.length; i++) {
//            if (inDegrees[i] - outDegrees[i] == 1) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private static String buildGenomeFromPath(List<Integer> eulerianPath, Map<String, Integer> indexMap, int k) {
//        StringBuilder genome = new StringBuilder();
//        if (!eulerianPath.isEmpty()) {
//            genome.append(indexMap.entrySet().stream()
//                    .filter(entry -> entry.getValue() == eulerianPath.get(0))
//                    .map(Map.Entry::getKey)
//                    .findFirst()
//                    .orElse(""));
//
//            for (int i = 1; i < eulerianPath.size(); i++) {
//                int vertex = eulerianPath.get(i);
//                String kmer = indexMap.entrySet().stream()
//                        .filter(entry -> entry.getValue() == vertex)
//                        .map(Map.Entry::getKey)
//                        .findFirst()
//                        .orElse("");
//                genome.append(kmer.charAt(k - 1));
//            }
//        }
//        return genome.toString();
//    }
//

//    private static List<Integer> findEulerianPath(int[][] adjMatrix, int startNode) {
//        List<Integer> path = new ArrayList<>();
//        Stack<Integer> stack = new Stack<>();
//        stack.push(startNode);
//
//        while (!stack.isEmpty()) {
//            int u = stack.peek();
//            boolean hasOutgoingEdge = false;
//            for (int v = 0; v < adjMatrix.length; v++) {
//                if (adjMatrix[u][v] == 1) {
//                    stack.push(v);
//                    adjMatrix[u][v] = 0;
//                    hasOutgoingEdge = true;
//                    break;
//                }
//            }
//            if (!hasOutgoingEdge) {
//                path.add(stack.pop());
//            }
//        }
//
//        Collections.reverse(path);
//        return path;
//    }
}
