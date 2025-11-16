
public class Node {
    private String palavra;
    private int frequencia;
    private Node proximo;

    public Node(String palavra) {
        this.palavra = palavra;
        this.frequencia = 1;
        this.proximo = null;
    }

    public void setPalavra(String palavra) { this.palavra = palavra; }

    public void setFrequencia(int frequencia) { this.frequencia = frequencia; }

    public void setProximo(Node proximo) { this.proximo = proximo; }

    public String getPalavra() { return this.palavra; }

    public int getFrequencia() { return this.frequencia; }

    public Node getProximo() { return this.proximo; }

    public void incrementarFrequencia() { this.frequencia++; }
}