
/**
 * Main
 * Jonathan Filbert | 2019
 * Beberapa method hasil diskusi dengan Alvin Hariman , Rony A. Vian, Christopher Samuel, dan Adrian Wijaya
 */

import java.io.*;
import java.util.*;

class Edge implements Comparable<Edge> {
    private int from;
    private int to;
    private int weight;

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    int getWeight() {
        return this.weight;
    }

    int getFrom() {
        return this.from;
    }

    int getTo() {
        return this.to;
    }

    // CompareTo
    public int compareTo(Edge otherEdge) {
        if (this.weight > otherEdge.getWeight()) {
            return 1;
        } else if (this.weight == otherEdge.getWeight()) {
            return 0;
        } else {
            return -1;
        }
    }
}

class Graph {
    private Integer numberOfVertices = 0;
    private HashMap<Integer, ArrayList<Edge>> edgeList;

    public Graph(int number) {
        this.numberOfVertices = number;
        this.edgeList = new HashMap<Integer, ArrayList<Edge>>();
    }

    int[] djikstra(int from) {
        // initialization
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        int[] distanceArray = new int[this.numberOfVertices + 1];
        int initialNode = from;
        // set isi distance array jadi infinity
        Arrays.fill(distanceArray, Integer.MAX_VALUE);
        // jarak ke diri sendiri = 0
        distanceArray[0] = 0;
        distanceArray[from] = 0;
        Edge initialEdge = new Edge(0, initialNode, 0);
        pq.add(initialEdge);
        // START BFS
        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            // forloop semua tetangga dari current Edge
            if (edgeList.get(current.getTo()) == null) {
                continue;
            }
            for (int i = 0; i < edgeList.get(current.getTo()).size(); i++) {
                // Masukin edge neighbor ke pq
                Edge targetEdge = edgeList.get(current.getTo()).get(i);
                if (distanceArray[targetEdge.getTo()] > targetEdge.getWeight() + distanceArray[current.getTo()]) {
                    // Update value
                    distanceArray[targetEdge.getTo()] = targetEdge.getWeight() + distanceArray[current.getTo()];
                    pq.add(targetEdge);
                }
            }
        }
        return distanceArray;
    }

    void print() {
        String result = "";
        for (Integer key : edgeList.keySet()) {
            result += Integer.toString(key) + "\n";
            result += "- - - \n";
            for (int i = 0; i < edgeList.get(key).size(); i++) {
                result += String.format("%s - %s Weight: %s \n", Integer.toString(edgeList.get(key).get(i).getFrom()),
                        Integer.toString(edgeList.get(key).get(i).getTo()),
                        Integer.toString(edgeList.get(key).get(i).getWeight()));
            }
            result += "\n";
        }
        System.out.println(result);
    }

    void addKota(int from, int to, int weight) {
        // Kalo dia belom ada di edgeList, buat baru, trus masukin
        Edge edge = new Edge(from, to, weight);
        if (this.edgeList.get(from) == null) {
            ArrayList<Edge> tempEdgeList = new ArrayList<Edge>();
            tempEdgeList.add(edge);
            this.edgeList.put(from, tempEdgeList);
            // Kalo dia udah ada, append
        } else {
            this.edgeList.get(from).add(edge);
        }
        Edge edgeReverse = new Edge(to, from, weight);
        if (this.edgeList.get(to) == null) {
            ArrayList<Edge> tempEdgeList = new ArrayList<Edge>();
            tempEdgeList.add(edgeReverse);
            this.edgeList.put(to, tempEdgeList);
            // Kalo dia udah ada, append
        } else {
            this.edgeList.get(to).add(edgeReverse);
        }
    }

    int openStore(String[] openStores) {
        // Forloop semua toko yang buka
        boolean[] visitedCities = new boolean[this.numberOfVertices + 1];
        // Initialize menjadi false semua
        for (int j = 0; j < visitedCities.length; j++) {
            visitedCities[j] = false;
        }
        // BFS START DARI TIAP KOTA YANG OPEN
        for (int i = 0; i < openStores.length; i++) {
            int initial = Integer.parseInt(openStores[i]);
            Queue<Integer> edgeQueue = new LinkedList<Integer>();
            // Bikin node pertama jadi true
            visitedCities[initial] = true;
            // Add ke queue
            edgeQueue.add(initial);
            // Mulai BFS
            while (!edgeQueue.isEmpty()) {
                // Poll a city from queue
                int current = edgeQueue.poll();
                // Set the current jadi visited
                visitedCities[current] = true;
                // KAlo current nya itu gapunya anak return 1
                if (edgeList.get(current) == null) {
                    visitedCities[current] = true;
                } else {
                    // Cari adjacent edges dari kota skrg
                    ArrayList<Edge> adjacentCities = this.edgeList.get(current);
                    // Visit semua yang ada disana
                    for (int k = 0; k < adjacentCities.size(); k++) {
                        // Kalo dia belom pernah divisit
                        if (!visitedCities[adjacentCities.get(k).getTo()]) {
                            visitedCities[adjacentCities.get(k).getTo()] = true;
                            // Masukin ke queue
                            edgeQueue.add(adjacentCities.get(k).getTo());
                        }
                    }
                }
            }
        }
        int result = 0;
        for (int i = 0; i < visitedCities.length; i++) {
            if (visitedCities[i] == true) {
                result += 1;
            }
        }
        // return
        return result;
    }

    int countCityWithGivenDistance(int initial, int distance) {
        boolean[] visitedCities = new boolean[this.numberOfVertices + 1];
        // Initialize jadi false semua
        for (int i = 0; i < visitedCities.length; i++) {
            visitedCities[i] = false;
        }
        Queue<Integer> edgeQueue = new LinkedList<Integer>();
        // Bikin node pertama jadi true
        visitedCities[initial] = true;
        // Add ke queue
        edgeQueue.add(initial);
        while (!edgeQueue.isEmpty()) {
            // Poll a city from queue
            int current = edgeQueue.poll();
            // System.out.println(current);
            // Set the current jadi visited
            visitedCities[current] = true;
            if (edgeList.get(current) == null) {
                return 0;
            } else {
                // Cari adjacent edges dari kota skrg
                ArrayList<Edge> adjacentCities = this.edgeList.get(current);
                // Visit semua yang ada disana
                // Visit semua yang ada disana
                for (int k = 0; k < adjacentCities.size(); k++) {
                    // Kalo dia belom pernah divisit
                    // System.out.printf("Check (%d)\n", adjacentCities.get(k).getTo());
                    if (adjacentCities.get(k).getWeight() <= distance) {
                        if (!visitedCities[adjacentCities.get(k).getTo()]) {
                            visitedCities[adjacentCities.get(k).getTo()] = true;
                            // Masukin ke queue
                            edgeQueue.add(adjacentCities.get(k).getTo());
                        }
                    }
                }
            }
        }

        int result = 0;
        for (int i = 0; i < visitedCities.length; i++) {
            if (visitedCities[i] == true) {
                result += 1;
            }
        }
        return result - 1;
    }

    int leastAmountOfRoad(int from, int to) {
        boolean[] visitedCities = new boolean[this.numberOfVertices + 1];
        int[] distanceArray = new int[this.numberOfVertices + 1];
        // Initialize array visitation
        for (int i = 0; i < visitedCities.length; i++) {
            visitedCities[i] = false;
        }
        // Initialize array distance
        for (int i = 0; i < distanceArray.length; i++) {
            distanceArray[i] = 0;
        }
        Queue<Integer> edgQueue = new LinkedList<Integer>();
        visitedCities[from] = true;
        edgQueue.add(from);
        if (this.edgeList.get(from) == null) {
            return -1;
        } else {
            while (!edgQueue.isEmpty()) {
                int current = edgQueue.poll();
                // System.out.println(current);
                visitedCities[current] = true;
                // Kalo gaada jalan return -1
                // get all the adjacent cities
                ArrayList<Edge> adjacentCities = this.edgeList.get(current);
                // Visit all the adjacent cities
                for (int i = 0; i < adjacentCities.size(); i++) {
                    // TODO
                    // Kalo tetangganya belom pernah dikunjungin, distance +=1
                    if (!visitedCities[adjacentCities.get(i).getTo()]) {
                        // Kunjungin dia
                        visitedCities[adjacentCities.get(i).getTo()] = true;
                        // Masukin ke queue
                        edgQueue.add(adjacentCities.get(i).getTo());
                        distanceArray[adjacentCities.get(i).getTo()] = distanceArray[current] + 1;
                        // Tambahin jumlah edgeCount
                    }
                }
            }
            return (distanceArray[to] == 0 ? -1 : distanceArray[to]);
        }
    }

    int leastAmountOfRoadCombination(int from, int to) {
        boolean[] visitedCities = new boolean[this.numberOfVertices + 1];
        int[] distanceArray = new int[this.numberOfVertices + 1];
        int[] countArray = new int[this.numberOfVertices + 1];
        // Initialize
        for (int i = 0; i < visitedCities.length; i++) {
            visitedCities[i] = false;
            distanceArray[i] = 0;
            countArray[i] = 0;
        }
        Queue<Integer> edgeQueue = new LinkedList<Integer>();
        // Bikin node pertama jadi true
        visitedCities[from] = true;
        // Add ke queue
        edgeQueue.add(from);
        // distanceArray[from] = 1;
        countArray[from] = 1;
        while (!edgeQueue.isEmpty()) {
            int current = edgeQueue.poll();
            visitedCities[current] = true;
            ArrayList<Edge> adjacentCities = this.edgeList.get(current);
            if (this.edgeList.get(current) == null) {
                return 0;
            }
            for (int i = 0; i < adjacentCities.size(); i++) {
                // Kalo dia belom pernah divisiit atau distancenya sama kek current+1
                if ((distanceArray[adjacentCities.get(i).getTo()] == distanceArray[current] + 1)
                        || !visitedCities[adjacentCities.get(i).getTo()]) {
                    // Set distance nya
                    if (!visitedCities[adjacentCities.get(i).getTo()]) {
                        // Jadiin true
                        visitedCities[adjacentCities.get(i).getTo()] = true;
                        edgeQueue.add(adjacentCities.get(i).getTo());
                    }
                    // Masukin ke queue
                    // countArray[adjacentCities.get(i).getTo()] += countArray[current] % 10001;
                    countArray[adjacentCities.get(i).getTo()] = (countArray[adjacentCities.get(i).getTo()] % 10001
                            + countArray[current] % 10001) % 10001;
                    distanceArray[adjacentCities.get(i).getTo()] = distanceArray[current] + 1;
                }
            }
        }
        return countArray[to];
    }
}

public class Main {
    static BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        String[] programDetails = Main.br.readLine().split(" ");
        // Buat graph nya
        Graph graph = new Graph(Integer.parseInt(programDetails[0]));

        // Forloop sesuai jumlah jalan raya / edge
        for (int i = 0; i < Integer.parseInt(programDetails[1]); i++) {
            String[] jalanrayaArray = Main.br.readLine().split(" ");
            // Tambah edge nya ke graph
            graph.addKota(Integer.parseInt(jalanrayaArray[0]), Integer.parseInt(jalanrayaArray[1]),
                    Integer.parseInt(jalanrayaArray[2]));
        }

        // Forloop perintah
        for (int i = 0; i < Integer.parseInt(programDetails[2]); i++) {
            String[] perintahDetail = Main.br.readLine().split(" ");
            switch (perintahDetail[0]) {
            case "OS":
                String[] openStores = Arrays.copyOfRange(perintahDetail, 1, perintahDetail.length);
                Main.bw.write(Integer.toString(graph.openStore(openStores)) + "\n");
                break;
            case "CCWGD":
                Main.bw.write(Integer.toString(graph.countCityWithGivenDistance(Integer.parseInt(perintahDetail[1]),
                        Integer.parseInt(perintahDetail[2]))) + "\n");
                break;
            case "LAOR":
                Main.bw.write(Integer.toString(graph.leastAmountOfRoad(Integer.parseInt(perintahDetail[1]),
                        Integer.parseInt(perintahDetail[2]))) + "\n");
                break;
            case "LAORC":
                Main.bw.write(Integer.toString(graph.leastAmountOfRoadCombination(Integer.parseInt(perintahDetail[1]),
                        Integer.parseInt(perintahDetail[2]))) + "\n");
                break;
            case "SR":
                int result = graph.djikstra(Integer.parseInt(perintahDetail[1]))[Integer.parseInt(perintahDetail[2])];
                if (result == Integer.MAX_VALUE) {
                    Main.bw.write(Integer.toString(-1) + "\n");
                } else {
                    Main.bw.write(Integer.toString(result) + "\n");
                }
                break;
            case "SM":
                int[] fromFrom = graph.djikstra(Integer.parseInt(perintahDetail[1]));
                int[] fromTo = graph.djikstra(Integer.parseInt(perintahDetail[2]));
                int[] resArray = new int[fromFrom.length];
                int resultFinal = Integer.MAX_VALUE;

                for (int j = 1; j < fromFrom.length; j++) {
                    if (fromFrom[j] > fromTo[j]) {
                        resArray[j] = fromFrom[j];
                    } else {
                        resArray[j] = fromTo[j];
                    }
                    if (resultFinal > resArray[j]) {
                        resultFinal = resArray[j];
                    }
                }
                Main.bw.write((resultFinal == Integer.MAX_VALUE || resultFinal == 0) ? Integer.toString(-1)
                        : Integer.toString(resultFinal));
                Main.bw.write("\n");
                break;
            }
        }
        Main.bw.flush();
    }
}