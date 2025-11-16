package HashTable;

import java.util.ArrayList;
import java.util.List;

public class HashTable {

    // Enum para escolher a função hash (requisito do projeto [cite: 20])
    public enum HashType {
        DIVISAO,
        MULTIPLICACAO
    }

    private int tamanho;
    private HashType hashType;

    // MUDANÇA: Usamos uma List<Node>.
    // Cada índice da 'array' armazena o *primeiro Node* (head) da lista ligada.
    private List<Node> array;

    /**
     * Construtor da HashTable.
     * @param type A função hash que esta tabela deve usar.
     * @param tamanho O tamanho da tabela (o valor 'dispersao' do seu código).
     */
    public HashTable(HashType type, int tamanho) {
        this.hashType = type;
        this.tamanho = tamanho;

        // Inicializa o array principal
        this.array = new ArrayList<>(tamanho);
        // Preenche o array com 'null', indicando que todas as listas estão vazias
        for (int i = 0; i < tamanho; i++) {
            this.array.add(null);
        }
    }

    // --- Métodos de Hash (Limpos) ---

    public int MetodoDivisao(String palavra) {
        int valorTotal = 0;
        for (int i = 0; i < palavra.length(); i++) {
            valorTotal += (int) palavra.charAt(i);
        }
        return valorTotal % this.tamanho;
    }

    public int MetodoMultiplicacao(String palavra) {
        int valorTotal = 0;
        for (int i = 0; i < palavra.length(); i++) {
            valorTotal += (int) palavra.charAt(i);
        }

        double constante = 0.6180339887; // (Ex: A M=15, A=0.6, (15*0.6)%1 = 9%1 = 0)
        double valorHash = (valorTotal * constante) % 1;
        return (int) (this.tamanho * valorHash);
    }

    /**
     * Método privado que chama a função hash selecionada.
     */
    private int hash(String palavra) {
        if (this.hashType == HashType.MULTIPLICACAO) {
            return MetodoMultiplicacao(palavra);
        }
        return MetodoDivisao(palavra);
    }

    // --- Métodos Principais (put/get) ---

    /**
     * Insere uma palavra na tabela.
     * Se a palavra já existir, incrementa sua frequência.
     * Se não, adiciona como um novo nó no início da lista.
     * [Requisito "put" - cite: 109]
     */
    public void put(String palavra) {
        int indice = hash(palavra);

        // Pega o primeiro nó (head) da lista naquele índice
        Node head = this.array.get(indice);
        Node current = head;

        // 1. Percorre a lista (se houver) para ver se a palavra já existe
        while (current != null) {
            if (current.getPalavra().equals(palavra)) {
                // PALAVRA ENCONTRADA: Incrementa a frequência e termina
                current.incrementarFrequencia();
                return;
            }
            current = current.getProximo();
        }

        // 2. PALAVRA NÃO ENCONTRADA: Cria um novo nó
        Node novoNode = new Node(palavra);

        // O novo nó aponta para o antigo "head"
        novoNode.setProximo(head);

        // O novo nó se torna o "head" da lista neste índice
        this.array.set(indice, novoNode);
    }

    /**
     * Busca um nó na tabela pela palavra.
     * [Requisito "get" - cite: 109]
     * @return o Node se encontrado, ou null se não encontrado.
     */
    public Node get(String palavra) {
        int indice = hash(palavra);
        Node current = this.array.get(indice);

        // Percorre a lista do índice
        while (current != null) {
            if (current.getPalavra().equals(palavra)) {
                // Encontrou
                return current;
            }
            current = current.getProximo();
        }

        // Não encontrou
        return null;
    }

    /**
     * Método auxiliar para obter a frequência de uma palavra.
     * @return A frequência (int) da palavra, ou 0 se ela não existir.
     */
    public int getFrequencia(String palavra) {
        Node node = get(palavra);
        if (node != null) {
            return node.getFrequencia();
        }
        return 0;
    }
}