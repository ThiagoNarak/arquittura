import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.Arrays;

public class Emulator {
    private int tamanhoBarramento;
    private int tamanhoRam;
    private DAO dao;
    private Ram ram;
    private CPU_2_DOT_0 cpu;
    private Barramento barramento;
    private EntradaSaida es;
    private Encoder encoder;
    private Parser parser;
    private int contadorES;
    private int instrucao;
    private int frequencia;

    public Emulator(int tamanhoBarramento, int tamanhoRam,int frequencia) {
        this.frequencia = frequencia;
        this.tamanhoBarramento = tamanhoBarramento;
        this.tamanhoRam = tamanhoRam;
        BarramentoControle barramentoControle = new BarramentoControle();
        BarramentoDados barramentoDados = new BarramentoDados(tamanhoBarramento);
        BarramentoEnderecoMemoria barramentoEnderecoMemoria = new BarramentoEnderecoMemoria();

        dao = new DAO();
        barramento = new Barramento(tamanhoBarramento);
        cpu = new CPU_2_DOT_0(barramentoControle, barramentoDados, barramentoEnderecoMemoria);
        ram = new Ram(barramentoControle, barramentoDados, barramentoEnderecoMemoria);
        es = new EntradaSaida(barramentoControle, barramentoDados, barramentoEnderecoMemoria,frequencia);
        encoder = new Encoder(tamanhoBarramento);
        parser = new Parser(tamanhoBarramento);
        contadorES = 0;
        instrucao = 0;
    }


    public void run() {

        for (int i = 0; i < dao.pegarArrayListDAO().size(); i++) {

            System.out.println(dao.pegarArrayListDAO().get(i));

            String vetor[] = parser.validando(dao.pegarArrayListDAO().get(i));

            System.out.println(Arrays.toString(vetor));
            //passo 2 encodificar(bytes) o vetor de Strings
            byte vetorBytes[] = encoder.encodificar(vetor);

            //passo 3 adiciona a fila do I/O

            es.adicionarInstrucao(vetorBytes);
        }
        int valor = 0;
        int contador = 0;
        boolean rodando = true;

        while (rodando) {
            valor = 0;

            valor += es.execute();
            valor += ram.execute();
            valor += cpu.excute();

            contador++;
            if (valor == 3) break;
            System.out.println("\033[31;1m Rodou " + contador + " \033[19m ");
        }
        System.out.println("A: " + cpu.regArray[1]);
        System.out.println("B: " + cpu.regArray[2]);
        System.out.println("C: " + cpu.regArray[3]);
        System.out.println("D: " + cpu.regArray[4]);


    }

}





