package Documento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LeitorArquivo {

    /**
     * Lê o conteúdo completo de um arquivo de texto e o retorna como uma String.
     *
     * @param caminhoArquivo O caminho para o arquivo (ex: "doc1.txt" ou "documentos/doc1.txt").
     * @return O conteúdo do arquivo como String, ou null se ocorrer um erro.
     */
    public String lerArquivoComoString(String caminhoArquivo) {
        try {
            // Lê todos os bytes do arquivo e os converte para uma String
            // usando a codificação padrão (UTF-8).
            String conteudo = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
            return conteudo;
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo '" + caminhoArquivo + "': " + e.getMessage());
            // Em um sistema real, talvez fosse melhor lançar a exceção
            return null;
        }
    }
}
