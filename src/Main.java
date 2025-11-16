import Documento.*;
import HashTable.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    private static final Set<String> STOP_WORDS = criarSetStopWords();

    public static void main(String[] args) {

        if (args.length < 3) {
            System.err.println("Erro: Argumentos insuficientes.");
            System.err.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }

        String diretorioPath = args[0];
        // double limiar = Double.parseDouble(args[1]);
        // String modo = args[2];

        LeitorArquivo leitor = new LeitorArquivo();
        List<Documento> listaDeDocumentos = new ArrayList<>();

        // Decide qual função hash usar
        HashTable.HashType tipoHashEscolhido = HashTable.HashType.DIVISAO;

        // ✅ 2. CÓDIGO LIMPO (SEM CITAÇÕES)
        System.out.println("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===");
        System.out.println("Função hash utilizada: " + tipoHashEscolhido);

        try {
            List<Path> arquivos = Files.list(Paths.get(diretorioPath))
                    .filter(path -> path.toString().toLowerCase().endsWith(".txt"))
                    .collect(Collectors.toList());

            System.out.println("Total de documentos processados: " + arquivos.size());

            for (Path arquivoPath : arquivos) {
                String nomeArquivo = arquivoPath.getFileName().toString();
                String conteudoOriginal = leitor.lerArquivoComoString(arquivoPath.toString());

                if (conteudoOriginal != null) {

                    Documento doc = new Documento(nomeArquivo, conteudoOriginal, tipoHashEscolhido);
                    doc.processarTexto(STOP_WORDS);
                    listaDeDocumentos.add(doc);

                    // --- Bloco de Teste ---
                    System.out.println("\n--- Processado: " + doc.getNomeArquivo() + " ---");
                    System.out.println("Tokens Limpos: " + Arrays.toString(doc.getTokens()));

                    if (doc.getTokens().length > 0) {
                        String palavraTeste = doc.getTokens()[0];
                        int freq = doc.getTabelaHash().getFrequencia(palavraTeste);
                        System.out.println("-> Teste Hash: Frequência de '" + palavraTeste + "': " + freq);
                    }
                    // --- Fim do Bloco de Teste ---
                }
            }

        } catch (IOException e) {
            System.err.println("Erro fatal ao ler o diretório '" + diretorioPath + "': " + e.getMessage());
        }
    }

    /**
     * Método helper para criar o HashSet de Stop Words.
     */
    private static Set<String> criarSetStopWords() {
        String[] listaDeStopWords = {
                "de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não",
                "uma", "os", "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi",
                "ao", "ele", "das", "tem", "à", "seu", "sua", "ou", "ser", "quando", "muito", "há",
                "nos", "já", "está", "eu", "também", "só", "pelo", "pela", "até", "isso", "ela",
                "entre", "era", "depois", "sem", "mesmo", "aos", "ter", "seus", "quem", "nas",
                "me", "esse", "eles", "estão", "você", "tinha", "foram", "essa", "num", "nem",
                "suas", "meu", "às", "minha", "têm", "numa", "pelos", "elas", "havia", "seja",
                "qual", "será", "nós", "tenho", "lhe", "deles", "essas", "esses", "pelas",
                "este", "fosse", "dele", "tu", "te", "vocês", "vos", "lhes", "meus", "minhas",
                "teu", "tua", "teus", "tuas", "nosso", "nossa", "nossos", "nossas", "dela",
                "delas", "esta", "estes", "estas", "aquele", "aquela", "aqueles", "aquelas",
                "isto", "aquilo", "estou", "está", "estamos", "estão", "estive", "esteve",
                "estivemos", "estiveram", "estava", "estávamos", "estavam", "estivera",
                "estivéramos", "esteja", "estejamos", "estejam", "estivesse", "estivéssemos",
                "estivessem", "estiver", "estivermos", "estiverem", "hei", "há", "havemos",
                "hão", "houve", "houvemos", "houveram", "houvera", "houvéramos", "haja",
                "hajamos", "hajam", "houvesse", "houvéssemos", "houvessem", "houver",
                "houvermos", "houverem", "houverei", "houverá", "houveremos", "houverão",
                "houveria", "houveríamos", "houveriam", "sou", "somos", "são", "era", "éramos",
                "eram", "fui", "foi", "fomos", "foram", "fora", "fôramos", "seja", "sejamos",
                "sejam", "fosse", "fôssemos", "fossem", "for", "formos", "forem", "serei", "será",
                "seremos", "serão", "seria", "seríamos", "seriam", "tenho", "tem", "temos",
                "tém", "tinha", "tínhamos", "tinham", "tive", "teve", "tivemos", "tiveram",
                "tivera", "tivéramos", "tenha", "tenhamos", "tenham", "tivesse", "tivéssemos",
                "tivessem", "tiver", "tivermos", "tiverem", "terei", "terá", "teremos",
                "terão", "teria", "teríamos", "teriam"
        };

        return new HashSet<>(Arrays.asList(listaDeStopWords));
    }
}