import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Grafo {
    private final int numVertices;
    private final List<List<Aresta>> listaAdj;

    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        listaAdj = new ArrayList<>();

        for (int i = 0; i < numVertices; ++i) {
            listaAdj.add(new ArrayList<>());
        }
    }

    public void adicionarAresta(int origem, int destino, int peso) {
        Aresta aresta = new Aresta(destino, peso);
        listaAdj.get(origem).add(aresta);
    }

    public void criarGrafoConexoAleatorio(int numArestas) {
        Random random = new Random();

        for (int i = 0; i < numArestas; i++) {
            int origem = random.nextInt(numVertices);
            int destino = random.nextInt(numVertices);
            int peso = random.nextInt(50) + 1;

            while (origem == destino || arestaExiste(origem, destino) || arestaExiste(destino, origem)) {
                origem = random.nextInt(numVertices);
                destino = random.nextInt(numVertices);
            }

            adicionarAresta(origem, destino, peso);
        }
    }

    private boolean arestaExiste(int origem, int destino) {
        for (Aresta aresta : listaAdj.get(origem)) {
            if (aresta.getDestino() == destino) {
                return true;
            }
        }
        return false;
    }
    

    public void salvarGrafoCSV(String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (int i = 0; i < numVertices; ++i) {
                for (Aresta aresta : listaAdj.get(i)) {
                    writer.append(i + "," + aresta.getDestino() + "," + aresta.getPeso() + "\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo CSV: " + e.getMessage());
        }
    }

    public int[] calcularLimites(int[] x) {
        int limiteInferior = numVertices - 1;
        int limiteSuperior = numVertices * (numVertices - 1) / 2;

        for (int i = 0; i < x.length; i++) {
            x[i] = limiteInferior + (i * (limiteSuperior - limiteInferior) / 4);
        }

        return x;
    }

    public void bellmanFord(int verticeOrigem) {
        int[] distancias = new int[numVertices];
        Arrays.fill(distancias, Integer.MAX_VALUE);
        distancias[verticeOrigem] = 0;

        for (int i = 1; i < numVertices; ++i) {
            for (int u = 0; u < numVertices; ++u) {
                for (Aresta aresta : listaAdj.get(u)) {
                    int v = aresta.getDestino();
                    int peso = aresta.getPeso();

                    if (distancias[u] != Integer.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                        distancias[v] = distancias[u] + peso;
                    }
                }
            }
        }

        for (int u = 0; u < numVertices; ++u) {
            for (Aresta aresta : listaAdj.get(u)) {
                int v = aresta.getDestino();
                int peso = aresta.getPeso();

                if (distancias[u] != Integer.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                    System.out.println("O grafo contem um ciclo negativo!");
                    return;
                }
            }
        }

        System.out.println("Distancias minimas apos o Bellman-Ford:");
        for (int i = 0; i < numVertices; ++i) {
            System.out.println("Vertice " + i + ": " + distancias[i]);
        }
    }


    public void salvarTempoDijkstra(int verticeOrigem) {
        long inicio = System.nanoTime();
        dijkstra(verticeOrigem);
        long fim = System.nanoTime();

        salvarTempo("tempos_execucao_dijkstra.txt", inicio, fim);
    }

    public void salvarTempoBellmanFord(int verticeOrigem) {
        long inicio = System.nanoTime();
        bellmanFord(verticeOrigem);
        long fim = System.nanoTime();

        salvarTempo("tempos_execucao_bellmanford.txt", inicio, fim);
    }

    public void salvarTempoFloydWarshall() {
        long inicio = System.nanoTime();
        floydWarshall();
        long fim = System.nanoTime();

        salvarTempo("tempos_execucao_floydwarshall.txt", inicio, fim);
    }

    public void salvarTempoOPF(int verticeOrigem) {
        long inicio = System.nanoTime();
        executarOPF(verticeOrigem);
        long fim = System.nanoTime();

        salvarTempo("tempos_execucao_opf.txt", inicio, fim);
    }

    private void salvarTempo(String nomeArquivo, long inicio, long fim) {
        try (FileWriter writer = new FileWriter(nomeArquivo, true)) {
            long tempoTotal = fim - inicio;

            writer.write("Tempo de execucao: " + tempoTotal + " nanossegundos\n");
            writer.write("-------------------------------------\n");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o tempo de execucao: " + e.getMessage());
        }
    }



    public int[][] floydWarshall() {
        int[][] dist = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = Integer.MAX_VALUE / 2;
                }
            }
        }

        for (int i = 0; i < numVertices; i++) {
            for (Aresta aresta : listaAdj.get(i)) {
                dist[i][aresta.getDestino()] = aresta.getPeso();
            }
        }

        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE / 2 &&
                            dist[k][j] != Integer.MAX_VALUE / 2 &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        System.out.println("Distancias minimas apos o Floyd:");
        for (int i = 0; i < numVertices; ++i) {
            for (int j = 0; j < numVertices; ++j) {
                System.out.print(dist[i][j] + " ");
            }
            System.out.println();
        }

        salvarDistanciasFloydWarshall(dist);

        return dist;
    }

    private void salvarDistanciasFloydWarshall(int[][] distancias) {
        String nomeArquivo = "distancias_floyd_warshall.txt";
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (int i = 0; i < numVertices; ++i) {
                for (int j = 0; j < numVertices; ++j) {
                    writer.append(distancias[i][j] + " ");
                }
                writer.append("\n");
            }
            writer.flush();
            System.out.println("Distancias salvas em " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de distancias: " + e.getMessage());
        }
    }

    public Map<Integer, Integer> dijkstra(int verticeOrigem) {
        Map<Integer, Integer> distancias = new HashMap<>();
        PriorityQueue<VerticeDistancia> filaPrioridade = new PriorityQueue<>(
                Comparator.comparingInt(VerticeDistancia::getDistancia));

        for (int i = 0; i < numVertices; ++i) {
            distancias.put(i, Integer.MAX_VALUE);
        }

        distancias.put(verticeOrigem, 0);
        filaPrioridade.add(new VerticeDistancia(verticeOrigem, 0));

        while (!filaPrioridade.isEmpty()) {
            int verticeAtual = filaPrioridade.poll().getVertice();

            for (Aresta aresta : listaAdj.get(verticeAtual)) {
                int destino = aresta.getDestino();
                int novaDistancia = distancias.get(verticeAtual) + aresta.getPeso();

                if (novaDistancia < distancias.get(destino)) {
                    distancias.put(destino, novaDistancia);
                    filaPrioridade.add(new VerticeDistancia(destino, novaDistancia));
                }
            }
        }

        return distancias;
    }

    public void executarOPF(int verticeOrigem) {
        long inicio = System.nanoTime();

        int numVertices = this.numVertices;
        boolean[] visitado = new boolean[numVertices];
        int[] distancia = new int[numVertices];
        Arrays.fill(distancia, Integer.MAX_VALUE);

        distancia[verticeOrigem] = 0;

        PriorityQueue<Integer> filaPrioridade = new PriorityQueue<>(numVertices,
                Comparator.comparingInt(o -> distancia[o]));
        filaPrioridade.offer(verticeOrigem);

        while (!filaPrioridade.isEmpty()) {
            int u = filaPrioridade.poll();

            if (visitado[u])
                continue;

            visitado[u] = true;

            for (Aresta aresta : listaAdj.get(u)) {
                int v = aresta.getDestino();
                int pesoArestaUV = aresta.getPeso();

                int newDistance = Math.max(distancia[u], pesoArestaUV);

                if (newDistance < distancia[v]) {
                    distancia[v] = newDistance;
                    filaPrioridade.offer(v);
                }
            }
        }

        long fim = System.nanoTime();
        salvarTempo(numVertices, "OPF", inicio, fim);
    }

    private static void salvarTempo(int numVertices, String algoritmo, long inicio, long fim) {
        String nomeArquivo = "tempos_execucao.txt";

        try (FileWriter writer = new FileWriter(nomeArquivo, true)) {
            long tempoTotal = fim - inicio;

            writer.write("Algoritmo: " + algoritmo + "\n");
            writer.write("Numero de Vertices: " + numVertices + "\n");
            writer.write("Tempo de execucao: " + tempoTotal + " nanossegundos\n");
            writer.write("-------------------------------------\n");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o tempo de execucao: " + e.getMessage());
        }


        



    }

    public static class Aresta {
        private final int destino;
        private final int peso;

        public Aresta(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }

        public int getDestino() {
            return destino;
        }

        public int getPeso() {
            return peso;
        }
    }

    private static class VerticeDistancia {
        private final int vertice;
        private final int distancia;

        public VerticeDistancia(int vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }

        public int getVertice() {
            return vertice;
        }

        public int getDistancia() {
            return distancia;
        }
    }
}
