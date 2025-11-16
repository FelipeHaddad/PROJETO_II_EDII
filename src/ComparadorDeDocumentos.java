import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Esta classe implementa os métodos para calcular a similaridade
 * entre dois objetos Documento.
 */
public class ComparadorDeDocumentos {

    /**
     * Calcula a Similaridade de Cosseno entre dois documentos.
     * A métrica de Cosseno é uma boa escolha, conforme sugerido pelo PDF.
     *
     * Fórmula: Similaridade = (A · B) / (||A|| * ||B||)
     *
     * @param doc1 O primeiro documento (com sua HashTable).
     * @param doc2 O segundo documento (com sua HashTable).
     * @return O score de similaridade (entre 0.0 e 1.0).
     */
    public double calcularSimilaridadeCosseno(Documento doc1, Documento doc2) {

        // Pega as tabelas hash de cada documento
        HashTable hash1 = doc1.getTabelaHash();
        HashTable hash2 = doc2.getTabelaHash();

        // 1. Criar o vocabulário combinado (todas as palavras únicas de ambos)
        // Usamos os tokens limpos (sem stop words) que já temos nos documentos.
    
        // Inicializa o vocabulário como um HashSet vazio
        Set<String> vocabulario = new HashSet<>();

        // Pega os tokens de forma segura
        String[] tokens1 = doc1.getTokens();
        String[] tokens2 = doc2.getTokens();

        // Adiciona os tokens do doc1 apenas se não forem nulos
        if (tokens1 != null) {
            vocabulario.addAll(Arrays.asList(tokens1));
        }

        // Adiciona os tokens do doc2 apenas se não forem nulos
        if (tokens2 != null) {
            vocabulario.addAll(Arrays.asList(tokens2));
        }
 


        // 2. Calcular o Produto Escalar (A · B) e as Magnitudes (||A|| e ||B||)
        // Fazemos tudo em um loop só para eficiência.

        double dotProduct = 0.0; // O numerador (A · B)
        double magnitude1 = 0.0; // Quadrado da magnitude de A (||A||^2)
        double magnitude2 = 0.0; // Quadrado da magnitude de B (||B||^2)

        for (String palavra : vocabulario) {
            // Pega a frequência de cada palavra em cada hash
            // O método getFrequencia() da sua hash retorna 0 se não encontrar
            int freq1 = hash1.getFrequencia(palavra);
            int freq2 = hash2.getFrequencia(palavra);

            // Soma para o produto escalar (numerador)
            dotProduct += (freq1 * freq2);

            // Soma para as magnitudes (denominador)
            magnitude1 += Math.pow(freq1, 2);
            magnitude2 += Math.pow(freq2, 2);
        }

        // 3. Calcular o resultado final
        // Tira a raiz quadrada das magnitudes
        double magnitudeFinal1 = Math.sqrt(magnitude1);
        double magnitudeFinal2 = Math.sqrt(magnitude2);

        // Evitar divisão por zero se um documento for vazio
        if (magnitudeFinal1 == 0.0 || magnitudeFinal2 == 0.0) {
            return 0.0;
        } else {
            // Retorna o resultado da fórmula
            return dotProduct / (magnitudeFinal1 * magnitudeFinal2);
        }
    }
}