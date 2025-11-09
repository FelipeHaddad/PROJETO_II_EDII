package HashTable;
import java.util.ArrayList;
import java.util.List;

public class HashTable {
    // A lista da HashTable vai conter várias listas com os nós. Isso é o tratamento de Colisão por encadeamento fechado exterior
    private List<List<Node>> array;

    public int MetodoDivisao (String palavra) {
        System.out.println("Palavra recebida: " + palavra);

        // Valor que será utilizado na funcao hash
        int valorTotal = 0;

        // Vai pegar o valor de cada letra da palavra e somar no valorTotal
        for (int i = 0; i < palavra.length(); i++) {
            char letra = palavra.charAt(i);
            int valorLetra = (int) letra;

            System.out.println("Letra: " + letra + " -> Valor: " + valorLetra);

            // 3. Soma o valor da letra ao total
            valorTotal += valorLetra;
        }

        int dispersao = 15;

        int indice = valorTotal % dispersao;
        System.out.println("O indice encontrado para a palavra: "+palavra+" foi: " + indice);
        return indice;
    }

    public int MetodoMultiplicacao (String palavra){
        System.out.println("Palavra recebida: " + palavra);

        // Valor que será utilizado na funcao hash
        int valorTotal = 0;

        // Vai pegar o valor de cada letra da palavra e somar no valorTotal
        for (int i = 0; i < palavra.length(); i++) {
            char letra = palavra.charAt(i);

            // 3. Soma o valor da letra ao total
            valorTotal += (int) letra;
            System.out.println("Letra: " + letra + " -> Valor: " + valorTotal);
        }

        double constante = 0.65;
        int dispersao = 15;
        int indice = (int) ((valorTotal * constante) % 1) * dispersao;

        return indice;
    }

}
