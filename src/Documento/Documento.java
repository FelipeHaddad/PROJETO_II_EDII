package Documento;

// Importa o Set (necessário para o processarTexto)
import java.util.Set;
// Importa as classes da HashTable que você criou
import HashTable.HashTable;
import HashTable.Node;

public class Documento {

    private String nomeArquivo;
    private String textoOriginal;
    private String[] tokensLimpos; // Array de tokens sem stop words

    // ✅ NOVO: Cada documento tem sua própria Tabela Hash interna [cite: 108]
    private HashTable tabelaHash;

    private ProcessadorTexto processador = new ProcessadorTexto();

    /**
     * Construtor modificado para também inicializar a Tabela Hash interna.
     * @param hashType O tipo de hash (DIVISAO ou MULTIPLICACAO) que este
     * documento deve usar para sua tabela interna.
     */
    public documento(String nomeArquivo, String textoOriginal, HashTable.HashType hashType) {
        this.nomeArquivo = nomeArquivo;
        this.textoOriginal = textoOriginal;

        // Inicializa a tabela hash interna deste documento
        // Usei 15 como tamanho, conforme seu código original
        this.tabelaHash = new HashTable(hashType, 15);
    }

    /**
     * Executa todas as etapas de pré-processamento do texto.
     * 1. Normalização
     * 2. Tokenização
     * 3. Remoção de Stop Words
     * 4. Construção da Tabela Hash
     */
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

        // 3. Remover Stop Words
        this.tokensLimpos = filtrarStopWords(tokensNaoFiltrados, stopWords);

        // 4. ✅ NOVO: Construir a Tabela Hash
        construirTabelaHash();
    }

    /**
     * Método privado que itera sobre os tokens limpos e os
     * insere na Tabela Hash.
     */
    private void construirTabelaHash() {
        if (this.tokensLimpos == null) return;

        // Itera sobre cada palavra limpa
        for (String token : this.tokensLimpos) {
            // Usa o método 'put' que acabamos de criar
            this.tabelaHash.put(token);
        }
    }

    // (Método filtrarStopWords que fizemos antes)
    private String[] filtrarStopWords(String[] tokensOriginais, Set<String> stopWords) {
        // ... (código da resposta anterior)
        return null; // Apenas para compilar o exemplo
    }

    // --- Getters ---

    // Retorna os tokens limpos
    public String[] getTokens() {
        return tokensLimpos;
    }

    // ✅ NOVO: Getter para acessar a tabela hash preenchida
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