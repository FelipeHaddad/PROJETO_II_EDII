/*-Joao Pedro Sinzato Bocchini - 10440034

-Felipe Bertacco Haddad - 10437372

-Ana Julia Yaguti Matilha - 10436655 */



// Importações do Java
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

        // --- 1. Leitura e Validação Completa dos Argumentos ---
        if (args.length < 3) {
            System.err.println("Erro: Argumentos insuficientes.");
            System.err.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }

        String diretorioPath = args[0];
        double limiar = 0.0;
        try {
            limiar = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Erro: Limiar '" + args[1] + "' não é um número válido.");
            return;
        }

        String modo = args[2].toLowerCase();

        // Validação de argumentos para topk e busca
        if (modo.equals("topk") && args.length < 4) {
            System.err.println("Erro: Modo 'topk' requer o argumento <K>.");
            return;
        }
        if (modo.equals("busca") && args.length < 5) {
            System.err.println("Erro: Modo 'busca' requer <doc1> e <doc2>.");
            return;
        }

        // --- 2. Instanciação de Estruturas ---
        LeitorArquivo leitor = new LeitorArquivo();
        List<Documento> listaDeDocumentos = new ArrayList<>();
        ComparadorDeDocumentos comparador = new ComparadorDeDocumentos(); // ✅ Usa o comparador
        AVLTree arvoreResultados = new AVLTree(); // ✅ Usa a AVL

        // Escolha da função hash
        HashTable.HashType tipoHashEscolhido = HashTable.HashType.DIVISAO;

        // --- 3. Impressão de Cabeçalho (Conforme PDF) ---
        System.out.println("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ==="); // [cite: 92]
        System.out.println("Função hash utilizada: " + tipoHashEscolhido); // [cite: 95]
        System.out.println("Métrica de similaridade: Cosseno"); // [cite: 87, 95]

        // --- 4. Processamento de Arquivos (Leitura e Tabela Hash) ---
        try {
            List<Path> arquivos = Files.list(Paths.get(diretorioPath))
                    .filter(path -> path.toString().toLowerCase().endsWith(".txt"))
                    .collect(Collectors.toList());

            System.out.println("Total de documentos processados: " + arquivos.size()); // [cite: 93]

            for (Path arquivoPath : arquivos) {
                String nomeArquivo = arquivoPath.getFileName().toString();
                String conteudoOriginal = leitor.lerArquivoComoString(arquivoPath.toString());

                if (conteudoOriginal != null) {
                    Documento doc = new Documento(nomeArquivo, conteudoOriginal, tipoHashEscolhido);
                    doc.processarTexto(STOP_WORDS);
                    listaDeDocumentos.add(doc);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro fatal ao ler o diretório '" + diretorioPath + "': " + e.getMessage());
            return;
        }

        // --- 5. Lógica de Comparação (baseada no 'modo') ---

        if (modo.equals("busca")) {
            // --- MODO BUSCA (Compara 2 arquivos específicos) ---
            String nomeDoc1 = args[3];
            String nomeDoc2 = args[4];

            Documento doc1 = encontrarDoc(listaDeDocumentos, nomeDoc1);
            Documento doc2 = encontrarDoc(listaDeDocumentos, nomeDoc2);

            if (doc1 != null && doc2 != null) {
                double similaridade = comparador.calcularSimilaridadeCosseno(doc1, doc2);
                System.out.println("Comparando: " + nomeDoc1 + " <-> " + nomeDoc2); // [cite: 85]
                System.out.printf("Similaridade calculada: %.2f%n", similaridade); // [cite: 86]
            } else {
                System.err.println("Erro: Um ou ambos os arquivos do modo 'busca' não foram encontrados.");
            }

        } else if (modo.equals("lista") || modo.equals("topk")) {
            // --- MODO LISTA ou TOPK (Compara todos os pares) ---
            int totalPares = 0;

            for (int i = 0; i < listaDeDocumentos.size(); i++) {
                for (int j = i + 1; j < listaDeDocumentos.size(); j++) {
                    Documento docA = listaDeDocumentos.get(i);
                    Documento docB = listaDeDocumentos.get(j);

                    double similaridade = comparador.calcularSimilaridadeCosseno(docA, docB);
                    totalPares++;

                    // Cria o objeto Resultado e insere na AVL
                    Resultado res = new Resultado(docA.getNomeArquivo(), docB.getNomeArquivo(), similaridade);
                    arvoreResultados.inserir(res);
                }
            }
            
            System.out.println("Total de pares comparados: " + totalPares); 
            System.out.println("Total de rotações na AVL: " + arvoreResultados.getTotalRotacoes());

            // --- 6. Exibição de Resultados (Lista ou TopK) ---

            // Pega a lista ordenada (maior para menor)
            List<Resultado> resultadosOrdenados = arvoreResultados.listarDecrescente();

            if (modo.equals("lista")) {
                System.out.printf("Pares com similaridade >= %.2f:%n", limiar); // [cite: 96]
                for (Resultado res : resultadosOrdenados) {
                    if (res.getSimilaridade() >= limiar) {
                        System.out.printf("%s <-> %s = %.2f%n",
                                res.getNomeDoc1(), res.getNomeDoc2(), res.getSimilaridade());
                    }
                }
            } else { // modo "topk"
                int k = Integer.parseInt(args[3]);
                System.out.printf("Top %d pares mais semelhantes:%n", k);
                for (int i = 0; i < k && i < resultadosOrdenados.size(); i++) {
                    Resultado res = resultadosOrdenados.get(i);
                    System.out.printf("%s <-> %s = %.2f%n",
                            res.getNomeDoc1(), res.getNomeDoc2(), res.getSimilaridade());
                }
            }
        } else {
            System.err.println("Erro: Modo '" + modo + "' desconhecido. Use 'lista', 'topk' ou 'busca'.");
        }
    }

    /**
     * Método auxiliar para encontrar um documento na lista pelo nome.
     */
    private static Documento encontrarDoc(List<Documento> lista, String nomeArquivo) {
        for (Documento doc : lista) {
            if (doc.getNomeArquivo().equals(nomeArquivo)) {
                return doc;
            }
        }
        return null; // Não encontrou
    }

    /**
     * Método helper para criar o HashSet de Stop Words.
     */
    private static Set<String> criarSetStopWords() {
        // (O seu código de stop words... está perfeito)
        String[] listaDeStopWords = {"de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "é", "com", "não", "uma", "os", "no", "se", "na", "por", "mais", "as", "dos", "como", "mas", "foi", "ao", "ele", "das", "tem", "à", "seu", "sua", "ou", "ser", "quando", "muito", "há", "nos", "já", "está", "eu", "também", "só", "pelo", "pela", "até", "isso", "ela", "entre", "era", "depois", "sem", "mesmo", "aos", "ter", "seus", "quem", "nas", "me", "esse", "eles", "estão", "você", "tinha", "foram", "essa", "num", "nem", "suas", "meu", "às", "minha", "têm", "numa", "pelos", "elas", "havia", "seja", "qual", "será", "nós", "tenho", "lhe", "deles", "essas", "esses", "pelas", "este", "fosse", "dele", "tu", "te", "vocês", "vos", "lhes", "meus", "minhas", "teu", "tua", "teus", "tuas", "nosso", "nossa", "nossos", "nossas", "dela", "delas", "esta", "estes", "estas", "aquele", "aquela", "aqueles", "aquelas", "isto", "aquilo", "estou", "está", "estamos", "estão", "estive", "esteve", "estivemos", "estiveram", "estava", "estávamos", "estavam", "estivera", "estivéramos", "esteja", "estejamos", "estejam", "estivesse", "estivéssemos", "estivessem", "estiver", "estivermos", "estiverem", "hei", "há", "havemos", "hão", "houve", "houvemos", "houveram", "houvera", "houvéramos", "haja", "hajamos", "hajam", "houvesse", "houvéssemos", "houvessem", "houver", "houvermos", "houverem", "houverei", "houverá", "houveremos", "houverão", "houveria", "houveríamos", "houveriam", "sou", "somos", "são", "era", "éramos", "eram", "fui", "foi", "fomos", "foram", "fora", "fôramos", "seja", "sejamos", "sejam", "fosse", "fôssemos", "fossem", "for", "formos", "forem", "serei", "será", "seremos", "serão", "seria", "seríamos", "seriam", "tenho", "tem", "temos", "tém", "tinha", "tínhamos", "tinham", "tive", "teve", "tivemos", "tiveram", "tivera", "tivéramos", "tenha", "tenhamos", "tenham", "tivesse", "tivéssemos", "tivessem", "tiver", "tivermos", "tiverem", "terei", "terá", "teremos", "terão", "teria", "teríamos", "teriam"};
        return new HashSet<>(Arrays.asList(listaDeStopWords));
    }
}