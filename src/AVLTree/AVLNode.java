import java.util.List;
import java.util.ArrayList;

public class AVLNode {
    // Chave principal: o grau de similaridade (double)
    private double chave; 
    // O nó armazena uma lista de Resultados para chaves iguais 
    private List<Resultado> resultados; 
    
    private int altura;
    private AVLNode esq;
    private AVLNode dir;

    public AVLNode(Resultado resultado) {
        this.chave = resultado.getSimilaridade();
        this.resultados = new ArrayList<>();
        this.resultados.add(resultado);
        this.altura = 1; // Altura inicial é 1 para um novo nó
        this.esq = null;
        this.dir = null;
    }

    // Getters and Setters...
    public double getChave() { return chave; }
    public List<Resultado> getResultados() { return resultados; }
    public int getAltura() { return altura; }
    public AVLNode getEsq() { return esq; }
    public AVLNode getDir() { return dir; }

    public void setAltura(int altura) { this.altura = altura; }
    public void setEsq(AVLNode esq) { this.esq = esq; }
    public void setDir(AVLNode dir) { this.dir = dir; }

    // Método para adicionar resultado, usado em caso de colisão de chave (similaridade)
    public void addResultado(Resultado resultado) {
        this.resultados.add(resultado);
    }
}