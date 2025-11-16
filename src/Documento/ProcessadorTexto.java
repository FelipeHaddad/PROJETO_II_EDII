package Documento;

public class ProcessadorTexto {

    /**
     * Normaliza um texto: converte para minúsculas e remove pontuação
     * e caracteres não alfanuméricos, preservando acentos e números.
     *
     * @param textoOriginal O texto lido do arquivo.
     * @return O texto limpo e normalizado.
     */
    public String normalizar(String textoOriginal) {
        if (textoOriginal == null) {
            return "";
        }

        // 1. Converte tudo para minúsculas
        String textoMinusculo = textoOriginal.toLowerCase();

        // 2. Remove pontuação e caracteres não alfanuméricos
        // Preserva letras (incluindo acentuadas), números e espaços.
        String textoLimpo = textoMinusculo.replaceAll("[^\\p{L}\\p{N}\\s]", "");

        return textoLimpo;
    }
}