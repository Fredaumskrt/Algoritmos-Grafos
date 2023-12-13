
public class Aresta {
    private int outroVertice;
    private int pesoDaMinhaAresta;

    public Aresta(int outroVertice, int pesoDaAresta) {
        this.outroVertice = outroVertice;
        this.pesoDaMinhaAresta = pesoDaMinhaAresta;
    }

    public int getOutroVertice() {
        return outroVertice;
    }

    public int getPesoDaMinhaAresta() {
        return pesoDaMinhaAresta;
    }
}
