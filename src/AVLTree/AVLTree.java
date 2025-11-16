import java.util.*;

public class AVLTree {
    private AVLNode raiz;
    // Variável para contabilizar rotações [cite: 22]
    private int rotacoesSimples; 
    private int rotacoesDuplas;

    public AVLTree() {
        this.raiz = null;
        this.rotacoesSimples = 0;
        this.rotacoesDuplas = 0;
    }

    // --- Métodos Auxiliares ---

    private int getAltura(AVLNode no) {
        return (no == null) ? 0 : no.getAltura();
    }

    private int getFatorBalanceamento(AVLNode no) {
        return (no == null) ? 0 : getAltura(no.getDir()) - getAltura(no.getEsq());
    }

    private void atualizarAltura(AVLNode no) {
        no.setAltura(1 + Math.max(getAltura(no.getEsq()), getAltura(no.getDir())));
    }

    // --- Rotações (Implementação baseada na lógica do vídeo) ---

    private AVLNode rotacaoSimplesDireita(AVLNode desbalanceado) {
        // Implementação da Rotação Simples Direita (LL)
        // 'desbalanceado' é 'p' no vídeo, e 'novaRaiz' é 'q'
        AVLNode novaRaiz = desbalanceado.getEsq();
        desbalanceado.setEsq(novaRaiz.getDir());
        novaRaiz.setDir(desbalanceado);

        // Atualiza as alturas
        atualizarAltura(desbalanceado);
        atualizarAltura(novaRaiz);
        
        // Contagem de Rotação Simples [cite: 22]
        this.rotacoesSimples++;
        
        return novaRaiz;
    }

    private AVLNode rotacaoSimplesEsquerda(AVLNode desbalanceado) {
        // Implementação da Rotação Simples Esquerda (RR)
        // 'desbalanceado' é 'p' no vídeo, e 'novaRaiz' é 'q'
        AVLNode novaRaiz = desbalanceado.getDir();
        desbalanceado.setDir(novaRaiz.getEsq());
        novaRaiz.setEsq(desbalanceado);

        // Atualiza as alturas
        atualizarAltura(desbalanceado);
        atualizarAltura(novaRaiz);

        // Contagem de Rotação Simples [cite: 22]
        this.rotacoesSimples++;
        
        return novaRaiz;
    }

    private AVLNode rotacaoDuplaEsquerdaDireita(AVLNode desbalanceado) {
        // Implementação da Rotação Dupla Esquerda-Direita (LR)
        desbalanceado.setEsq(rotacaoSimplesEsquerda(desbalanceado.getEsq()));
        
        // Contagem de Rotação Dupla (após as duas simples) [cite: 22]
        this.rotacoesDuplas++; 
        
        return rotacaoSimplesDireita(desbalanceado);
    }
    
    private AVLNode rotacaoDuplaDireitaEsquerda(AVLNode desbalanceado) {
        // Implementação da Rotação Dupla Direita-Esquerda (RL)
        desbalanceado.setDir(rotacaoSimplesDireita(desbalanceado.getDir()));
        
        // Contagem de Rotação Dupla (após as duas simples) [cite: 22]
        this.rotacoesDuplas++;
        
        return rotacaoSimplesEsquerda(desbalanceado);
    }


    // --- Método de Inserção Principal ---
    
    /**
     * Insere o resultado na AVL.
     */
    public void inserir(Resultado resultado) {
        this.raiz = inserirRecursivo(this.raiz, resultado);
    }

    /**
     * Lógica de inserção recursiva e balanceamento (Núcleo da AVL).
     */
    private AVLNode inserirRecursivo(AVLNode noAtual, Resultado resultado) {
        if (noAtual == null) {
            return new AVLNode(resultado); // Caso base: insere o novo nó
        }

        // 1. Inserção (com base na similaridade/chave)
        double chaveNova = resultado.getSimilaridade();
        
        if (chaveNova < noAtual.getChave()) {
            noAtual.setEsq(inserirRecursivo(noAtual.getEsq(), resultado));
        } else if (chaveNova > noAtual.getChave()) {
            noAtual.setDir(inserirRecursivo(noAtual.getDir(), resultado));
        } else {
            // Chaves iguais (similaridade igual): adiciona à lista de resultados do nó existente 
            noAtual.addResultado(resultado);
            return noAtual; // Não precisa rebalancear
        }

        // 2. Atualiza a altura do nó atual
        atualizarAltura(noAtual);

        // 3. Verifica o fator de balanceamento e executa rotações
        int balance = getFatorBalanceamento(noAtual);

        // Rotação RR (Direita-Direita)
        if (balance > 1 && chaveNova > noAtual.getDir().getChave()) {
            return rotacaoSimplesEsquerda(noAtual);
        }

        // Rotação LL (Esquerda-Esquerda)
        if (balance < -1 && chaveNova < noAtual.getEsq().getChave()) {
            return rotacaoSimplesDireita(noAtual);
        }

        // Rotação RL (Direita-Esquerda)
        if (balance > 1 && chaveNova < noAtual.getDir().getChave()) {
            return rotacaoDuplaDireitaEsquerda(noAtual);
        }

        // Rotação LR (Esquerda-Direita)
        if (balance < -1 && chaveNova > noAtual.getEsq().getChave()) {
            return rotacaoDuplaEsquerdaDireita(noAtual);
        }

        return noAtual; // Retorna o nó (inalterado ou após a rotação)
    }
    
    // --- Métodos de Consulta (Requisito: lista/topk) ---
    
    /**
     * Retorna a contagem total de rotações.
     */
    public int getTotalRotacoes() {
        return rotacoesSimples + rotacoesDuplas;
    }
    
    /**
     * Retorna a lista de resultados em ordem decrescente de similaridade (In-Order Revertido).
     */
    public List<Resultado> listarDecrescente() {
        List<Resultado> listaResultados = new ArrayList<>();
        listarDecrescenteRecursivo(this.raiz, listaResultados);
        return listaResultados;
    }

    private void listarDecrescenteRecursivo(AVLNode no, List<Resultado> lista) {
        if (no != null) {
            // Visita Sub-Árvore Direita (Maior Similaridade)
            listarDecrescenteRecursivo(no.getDir(), lista); 

            // Adiciona todos os resultados com esta similaridade
            lista.addAll(no.getResultados());

            // Visita Sub-Árvore Esquerda (Menor Similaridade)
            listarDecrescenteRecursivo(no.getEsq(), lista); 
        }
    }
    /**
     * Retorna os K resultados com maior similaridade (Top-K).
     */
    public List<Resultado> topK(int k) {
        List<Resultado> lista = new ArrayList<>();
        topKRecursivo(this.raiz, lista, k);
        return lista;
    }

    private void topKRecursivo(AVLNode no, List<Resultado> lista, int k) {
        if (no == null || lista.size() >= k) {
            return;
        }

        // Visita primeiro a direita (maiores similaridades)
        topKRecursivo(no.getDir(), lista, k);

        // Adiciona os resultados deste nó
        for (Resultado r : no.getResultados()) {
            if (lista.size() < k) {
                lista.add(r);
            } else {
                return; // já coletou k
            }
        }

        // Visita a esquerda (similaridades menores)
        topKRecursivo(no.getEsq(), lista, k);
    }

}