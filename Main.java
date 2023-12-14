import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[] numArestasArray = new int[5];
        long startTime, endTime, totalTime;

        for (int power = 1; power <= 5; power++) {
            numArestasArray = new Grafo((int) Math.pow(5, power)).calcularLimites(numArestasArray);

            for (int i = 0; i < 5; i++) {
                int numVertices = (int) Math.pow(5, power);

                System.out.println("Número de vértices: " + numVertices);
                System.out.println("Número de arestas: " + numArestasArray[i]);

                String fileName = "meu_grafo_v" + power + "_grafo" + i + ".csv";
                Grafo myGraph = new Grafo(numVertices);

                startTime = System.currentTimeMillis(); // Grava o tempo de início

                myGraph.criarGrafoConexoAleatorio(numArestasArray[i]);
                myGraph.salvarGrafoCSV(fileName);

                // Executa o algoritmo de Dijkstra
                int sourceVertexDijkstra = 0; // Escolha o vértice de origem desejado
                Map<Integer, Integer> distancesDijkstra = myGraph.dijkstra(sourceVertexDijkstra);

                // Executa o algoritmo de Bellman-Ford
                int sourceVertexBellman = 0; // Escolha o vértice de origem desejado
                myGraph.bellmanFord(sourceVertexBellman);

                // Executa o algoritmo de Floyd-Warshall
                myGraph.floydWarshall();
                
                int sourceVertexOPF = 0; // Escolha o vértice de origem desejado
                myGraph.executarOPF(sourceVertexOPF);

                System.out.println("--------------------");
                myGraph = new Grafo(numVertices);

                endTime = System.currentTimeMillis(); // Grava o tempo de fim
                totalTime = endTime - startTime;

                gravarTempoDeExecucao(totalTime, "tempo_execucao.txt");
            }
        }
    }

    private static void gravarTempoDeExecucao(long tempo, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("Tempo de execução total: " + tempo + " milissegundos\n");
        } catch (IOException e) {
            System.out.println("Erro ao gravar o tempo de execução: " + e.getMessage());
        }
    }
}
