package lesson20240903.lists;
import java.util.*;

public class Graph {
    private Map<Integer, List<Integer>> adjList;
    private int firstVertex;
    private int lastVertex;
    // Constructor
    public Graph() {
        adjList = new HashMap<>();
    }

    // Add a vertex to the graph
    public void addVertex(int vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    // Add an undirected edge to the graph
    public void addEdge(int source, int destination) {
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(destination, new ArrayList<>());
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);  // For undirected graph
    }

    // Add a directed edge to the graph
    public void addDirectedEdge(int source, int destination) {
        if (firstVertex == 0) firstVertex = source;
        lastVertex = destination;
        adjList.putIfAbsent(source, new ArrayList<>());
        adjList.putIfAbsent(destination, new ArrayList<>());
        adjList.get(source).add(destination);
    }

    // Remove a vertex and its associated edges
    public void removeVertex(int vertex) {
        adjList.values().forEach(e -> e.remove(Integer.valueOf(vertex)));
        adjList.remove(vertex);
    }

    // Remove an edge
    public void removeEdge(int source, int destination) {
        List<Integer> edgesFromSource = adjList.get(source);
        List<Integer> edgesFromDestination = adjList.get(destination);
        if (edgesFromSource != null) {
            edgesFromSource.remove(Integer.valueOf(destination));
        }
        if (edgesFromDestination != null) {
            edgesFromDestination.remove(Integer.valueOf(source));
        }
    }

    // Get the adjacency list
    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjList;
    }

    // Print the graph
    public void printGraph() {
        for (var entry : adjList.entrySet()) {
            System.out.println("Vertex " + entry.getKey() + " is connected to: " + entry.getValue());
        }
    }

    public Set<Integer> getVertices() {
        return adjList.keySet();
    }

    public int getFirstVertex() {
        return firstVertex;
    }
    public int getLastVertex() {
        return lastVertex;
    }


    public int getOutDegree(int vertex) {
        return adjList.containsKey(vertex) ? adjList.get(vertex).size() : 0;
    }

    public int getInDegree(int vertex) {
        int inDegree = 0;
        for (int key : adjList.keySet()) {
            List<Integer> neighbors = adjList.get(key);
            if (neighbors.contains(vertex)) {
                inDegree++;
            }
        }
        return inDegree;
    }
}