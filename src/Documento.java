import java.util.Set;
import java.util.List; // Importe List
import java.util.ArrayList; // Importe ArrayList

public class Documento {

    private String nomeArquivo;
    private String textoOriginal;
    private String[] tokensLimpos; // Array de tokens sem stop words

    // Cada documento tem sua própria Tabela Hash interna
    private HashTable tabelaHash;

    private ProcessadorTexto processador = new ProcessadorTexto();

    /**
     * Construtor modificado para também inicializar a Tabela Hash interna.
     * @param hashType O tipo de hash (DIVISAO ou MULTIPLICACAO) que este
     * documento deve usar para sua tabela interna.
     */
    public Documento(String nomeArquivo, String textoOriginal, HashTable.HashType hashType) {
        this.nomeArquivo = nomeArquivo;
        this.textoOriginal = textoOriginal;

        // Inicializa a tabela hash interna deste documento
        // Usei 15 como tamanho, conforme seu código original
        this.tabelaHash = new HashTable(hashType, 15);
        
        // Inicializa tokensLimpos como um array vazio para evitar NullPointer
        this.tokensLimpos = new String[0]; 
    }

    public void processarTexto(Set<String> stopWords) {

        // 1. Normalizar
        String textoNormalizado = processador.normalizar(this.textoOriginal);

        // 2. Tokenizar
        String[] tokensNaoFiltrados;
        if (textoNormalizado != null && !textoNormalizado.isEmpty()) {
            tokensNaoFiltrados = textoNormalizado.split("\\s+");
        } else {
            tokensNaoFiltrados = new String[0];
        }

        // 3. Remover Stop Words (Agora chama o método corrigido)
        this.tokensLimpos = filtrarStopWords(tokensNaoFiltrados, stopWords);

        // 4. Construir a Tabela Hash
        construirTabelaHash();
    }

    /**
     * Método privado que itera sobre os tokens limpos e os
     * insere na Tabela Hash.
     */
    private void construirTabelaHash() {
        // Esta verificação agora é redundante se getTokens() for seguro, mas mantemos
        if (this.tokensLimpos == null) return;

        // Itera sobre cada palavra limpa
        for (String token : this.tokensLimpos) {
            // Usa o método 'put' (ou 'inserir') da sua HashTable
            this.tabelaHash.put(token); // Assumindo que o método se chama 'put'
        }
    }


    private String[] filtrarStopWords(String[] tokensOriginais, Set<String> stopWords) {
        List<String> tokensFiltrados = new ArrayList<>();
        
        for (String token : tokensOriginais) {
            // Adiciona o token à lista APENAS se ele NÃO estiver no Set de stop words
            if (!stopWords.contains(token) && !token.isEmpty()) {
                tokensFiltrados.add(token);
            }
        }
        
        // Converte a Lista de volta para um array
        return tokensFiltrados.toArray(new String[0]);
    }

    // --- Getters ---

    public String[] getTokens() {
        if (this.tokensLimpos == null) {
            // Se algo falhar, retorna um array vazio em vez de null
            return new String[0];
        }
        return tokensLimpos;
    }

    // Getter para acessar a tabela hash preenchida
    public HashTable getTabelaHash() {
        return this.tabelaHash;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getTextoOriginal() {
        return textoOriginal;
    }
}