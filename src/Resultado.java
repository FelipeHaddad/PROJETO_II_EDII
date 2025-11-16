public class Resultado {
    private String nomeDoc1;
    private String nomeDoc2;
    private double similaridade;

    public Resultado(String doc1, String doc2, double similaridade) {
        this.nomeDoc1 = doc1;
        this.nomeDoc2 = doc2;
        this.similaridade = similaridade;
    }

    // Getters
    public double getSimilaridade() { return similaridade; }
    public String getNomeDoc1() { return nomeDoc1; }
    public String getNomeDoc2() { return nomeDoc2; }
}