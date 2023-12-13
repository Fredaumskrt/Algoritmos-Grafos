import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Grafo {
    private final int numeroDeVertices;
    private final List<List<Aresta>> listaDeAdjacencia;

    public Grafo(int numeroDeVertices) {
        this.numeroDeVertices = numeroDeVertices;
        listaDeAdjacencia = new ArrayList<>();

        for (int i = 0; i < numeroDeVertices; ++i) {
            listaDeAdjacencia.add(new ArrayList<>());
        }
    }

    public void adicionarAresta(int origem, int destino, int peso) {
        Aresta aresta = new Aresta(destino, peso);
        listaDeAdjacencia.get(origem).add(aresta);
    }

    public void criarGrafoConexoAleatorio(int numeroDeArestas) {
        Random random = new Random();

        for (int i = 0; i < numeroDeArestas; i++) {
            int origem = random.nextInt(numeroDeVertices);
            int destino = random.nextInt(numeroDeVertices);
            int peso = random.nextInt(50) + 1;

            while (origem == destino || arestaExiste(origem, destino) || arestaExiste(destino, origem)) {
                origem = random.nextInt(numeroDeVertices);
                destino = random.nextInt(numeroDeVertices);
            }

            adicionarAresta(origem, destino, peso);
        }
    }

    private boolean arestaExiste(int origem, int destino) {
        for (Aresta aresta : listaDeAdjacencia.get(origem)) {
            if (aresta.getDestino() == destino) {
                return true;
            }
        }
        return false;
    }

    public void salvarGrafoCSV(String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (int i = 0; i < numeroDeVertices; ++i) {
                for (Aresta aresta : listaDeAdjacencia.get(i)) {
                    writer.append(i + "," + aresta.getDestino() + "," + aresta.getPeso() + "\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo CSV: " + e.getMessage());
        }
    }

    public int[] calcularLimites(int[] x) {
        int limiteInferior = numeroDeVertices - 1;
        int limiteSuperior = numeroDeVertices * (numeroDeVertices - 1) / 2;

        for (int i = 0; i < x.length; i++) {
            x[i] = limiteInferior + (i * (limiteSuperior - limiteInferior) / 4);
        }

        return x;
    }

    // Algoritmo Bellman Ford
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void bellmanFord(int verticeOrigem) {
        int[] distancias = new int[numeroDeVertices];
        Arrays.fill(distancias, Integer.MAX_VALUE);
        distancias[verticeOrigem] = 0;

        // Relaxamento das arestas repetido |V| - 1 vezes
        for (int i = 1; i < numeroDeVertices; ++i) {
            for (int u = 0; u < numeroDeVertices; ++u) {
                for (Aresta aresta : listaDeAdjacencia.get(u)) {
                    int v = aresta.getDestino();
                    int peso = aresta.getPeso();

                    if (distancias[u] != Integer.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                        distancias[v] = distancias[u] + peso;
                    }
                }
            }
        }

        // Verifica se há ciclo negativo
        for (int u = 0; u < numeroDeVertices; ++u) {
            for (Aresta aresta : listaDeAdjacencia.get(u)) {
                int v = aresta.getDestino();
                int peso = aresta.getPeso();

                if (distancias[u] != Integer.MAX_VALUE && distancias[u] + peso < distancias[v]) {
                    System.out.println("O grafo contém um ciclo negativo!");
                    return;
                }
            }
        }

        // Imprime as distâncias mínimas após o Bellman-Ford
        System.out.println("Distancias minimas apo1s o Bellman-Ford:");
        for (int i = 0; i < numeroDeVertices; ++i) {
            System.out.println("Vertice " + i + ": " + distancias[i]);
        }
    }

 public int[][] floydWarshall() {
        int[][] dist = new int[numeroDeVertices][numeroDeVertices];

        // Inicializa a matriz de distâncias
        for (int i = 0; i < numeroDeVertices; i++) {
            for (int j = 0; j < numeroDeVertices; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = Integer.MAX_VALUE / 2;
                }
            }
        }

        // Preenche a matriz com as distâncias conhecidas do grafo
        for (int i = 0; i < numeroDeVertices; i++) {
            for (Aresta aresta : listaDeAdjacencia.get(i)) {
                dist[i][aresta.getDestino()] = aresta.getPeso();
            }
        }

        // Algoritmo Floyd-Warshall
        for (int k = 0; k < numeroDeVertices; k++) {
            for (int i = 0; i < numeroDeVertices; i++) {
                for (int j = 0; j < numeroDeVertices; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE / 2 &&
                            dist[k][j] != Integer.MAX_VALUE / 2 &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        // Imprime as distâncias mínimas após o Floyd-Warshall
        System.out.println("Distâncias mínimas após o Floyd-Warshall:");
        for (int i = 0; i < numeroDeVertices; ++i) {
            for (int j = 0; j < numeroDeVertices; ++j) {
                System.out.print(dist[i][j] + " ");
            }
            System.out.println();
        }

        // Salva as distâncias do algoritmo de Floyd-Warshall em um arquivo
        salvarDistanciasFloydWarshall(dist);

        return dist;
    }

    private void salvarDistanciasFloydWarshall(int[][] distancias) {
        String nomeArquivo = "distancias_floyd_warshall.txt";
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (int i = 0; i < numeroDeVertices; ++i) {
                for (int j = 0; j < numeroDeVertices; ++j) {
                    writer.append(distancias[i][j] + " ");
                }
                writer.append("\n");
            }
            writer.flush();
            System.out.println("Distâncias salvas em " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo de distâncias: " + e.getMessage());
        }
    }








    // Algoritmo Dijkstra
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public Map<Integer, Integer> dijkstra(int verticeOrigem) {
        Map<Integer, Integer> distancias = new HashMap<>();
        PriorityQueue<VerticeDistancia> filaPrioridade = new PriorityQueue<>(
                Comparator.comparingInt(VerticeDistancia::getDistancia));

        for (int i = 0; i < numeroDeVertices; ++i) {
            distancias.put(i, Integer.MAX_VALUE);
        }

        distancias.put(verticeOrigem, 0);
        filaPrioridade.add(new VerticeDistancia(verticeOrigem, 0));

        while (!filaPrioridade.isEmpty()) {
            int verticeAtual = filaPrioridade.poll().getVertice();

            for (Aresta aresta : listaDeAdjacencia.get(verticeAtual)) {
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
}
