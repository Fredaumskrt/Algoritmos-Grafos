import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[] numeroDeArestas = new int[5];
        long inicio, fim, tempoTotal;

        for (int x = 1; x <= 5; x++) {
            numeroDeArestas = new Grafo((int) Math.pow(5, x)).calcularLimites(numeroDeArestas);

            for (int i = 0; i < 5; i++) {
                int numeroDeVertices = (int) Math.pow(5, x);

                System.out.println("Número de vértices: " + numeroDeVertices);
                System.out.println("Número de arestas: " + numeroDeArestas[i]);

                String nomeArquivo = "meu_grafo_v" + x + "_grafo" + i + ".csv";
                Grafo meuGrafo = new Grafo(numeroDeVertices);

                inicio = System.currentTimeMillis(); // Grava o tempo de início

                meuGrafo.criarGrafoConexoAleatorio(numeroDeArestas[i]);
                meuGrafo.salvarGrafoCSV(nomeArquivo);

                // Executa o algoritmo de Dijkstra
                int verticeOrigemDijkstra = 0; // Escolha o vértice de origem desejado
                Map<Integer, Integer> distanciasDijkstra = meuGrafo.dijkstra(verticeOrigemDijkstra);

                // Executa o algoritmo de Bellman-Ford
                int verticeOrigemBellman = 0; // Escolha o vértice de origem desejado
                meuGrafo.bellmanFord(verticeOrigemBellman);

                // Executa o algoritmo de Floyd-Warshall
                meuGrafo.floydWarshall();

                System.out.println("--------------------");
                meuGrafo = new Grafo(numeroDeVertices);

                fim = System.currentTimeMillis(); // Grava o tempo de fim
                tempoTotal = fim - inicio;

                gravarTempoDeExecucao(tempoTotal, "tempo_execucao.txt");
            }
        }
    }

    private static void gravarTempoDeExecucao(long tempo, String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo, true)) {
            writer.write("Tempo de execução total: " + tempo + " milissegundos\n");
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao gravar o tempo de execução: " + e.getMessage());
        }
    }
}