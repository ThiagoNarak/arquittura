import util.EnumCod;

import java.util.ArrayList;
import java.util.Arrays;

public class EntradaSaida {
    private ArrayList<Instrucao> instrucaoArrayList;
    private Barramento barramento;
    private int posicao;
    private boolean sobreescrever;

    public int getFrequencia() {
        return frequencia;
    }

    private int frequencia;

    //barramento novo
    private BarramentoControle barramentoControle;
    private BarramentoDados barramentoDados;
    private BarramentoEnderecoMemoria barramentoEnderecoMemoria;

    //CONSTRUTOR


    public EntradaSaida(BarramentoControle barramentoControle, BarramentoDados barramentoDados, BarramentoEnderecoMemoria barramentoEnderecoMemoria, int frequencia) {
        this.frequencia = frequencia;
        this.barramentoControle = barramentoControle;
        this.barramentoDados = barramentoDados;
        this.barramentoEnderecoMemoria = barramentoEnderecoMemoria;
        this.posicao = 0;
        this.instrucaoArrayList = new ArrayList<>(4);

    }


    //CONSTRUTOR
    public EntradaSaida(Barramento barramento) {
        this.barramento = barramento;
        this.posicao = 0;
        this.instrucaoArrayList = new ArrayList<>(4);

    }

    public int execute() {
        int contagemBytes = 0;
        boolean rodando = true;

        if (instrucaoArrayList.size() != 0) {
            if (!getBarramentoDados().possuiInformacaoCpu() &&
                    !getBarramentoDados().possuiInformacaoRam() &&
                    !getBarramentoDados().havePriority()) {


                System.out.println("------======= ADICIONANDO DO BUFFER E/S AO BARRAMENTO ========------ | INSTRUÇÃO P/ RAM e CPU");
                int clock = (frequencia * getBarramentoDados().getTamanhoBarramento()) / 8;
                while (rodando) {
                    if (pegarInstrucao(false) != null) {
                        contagemBytes += pegarInstrucao(false).getPayload().length;
                        contagemBytes += getBarramentoDados().getTamanhoBarramento() / 8;
                        System.out.println("CONTAGEM BYTES " + contagemBytes);
                    } else {
                        break;
                    }
                    //CLOCK
                    if (contagemBytes <= clock) {

                        getBarramentoControle().sendRam(pegarInstrucao(false).getCodigo());
                        getBarramentoDados().sendRam(pegarInstrucao(false));
                        getBarramentoEnderecoMemoria().sendRam(pegarInstrucao(true).getEndereco());

                        getBarramentoControle().sendCpu(pegarInstrucao(false).getCodigo());
                        getBarramentoDados().sendCpu(pegarInstrucao(false));
                        getBarramentoEnderecoMemoria().sendCpu(pegarInstrucao(true).getEndereco());

                    } else rodando = false;
                }
            } else {
                rodando = false;
                return 1;
            }

        } else return 1;
        return 0;

    }

    public boolean adicionarInstrucao(byte vetor[]) {
        if (posicao >= getBarramentoDados().getTamanhoBarramento() * 2) {
            posicao = 0;
        }
        System.out.println("------======= ADICIONANDO AO BUFFER E/S ========------ | INSTRUÇÃO P/ RAM");
        Instrucao instrucaoRam = new Instrucao(vetor, posicao, vetor.length, EnumCod.WRITE);   //comando .WRITE nao faz diferença na instrução que vai para memoria RAM
        System.out.println("vetor" + Arrays.toString(vetor));
        System.out.println("posicao: " + posicao);
        System.out.println("Tamanho: " + vetor.length);
        System.out.println("codigo: " + EnumCod.WRITE);

        Instrucao instrucaoCpu = new Instrucao(vetor.length, posicao, EnumCod.INTERRUPT);
        System.out.println("------======= ADICIONANDO AO BUFFER E/S ========------ | INSTRUÇÃO P/ CPU");

        System.out.println("posicao: " + posicao);
        System.out.println("Tamanho: " + vetor.length);
        System.out.println("codigo: " + EnumCod.INTERRUPT);

        instrucaoArrayList.add(instrucaoRam);
        instrucaoArrayList.add(instrucaoCpu);

        posicao += barramentoDados.getTamanhoBarramento() / 2;
        return true;


    }

    public Barramento getBarramento() {
        return barramento;
    }

    public BarramentoControle getBarramentoControle() {
        return barramentoControle;
    }

    public BarramentoDados getBarramentoDados() {
        return barramentoDados;
    }

    public BarramentoEnderecoMemoria getBarramentoEnderecoMemoria() {
        return barramentoEnderecoMemoria;
    }

    public Instrucao pegarInstrucao(boolean apagar) {
        if (instrucaoArrayList.size() > 0) {
            if (apagar == true) {
                Instrucao aux = instrucaoArrayList.get(0);
                instrucaoArrayList.remove(0);

                return aux;
            }
            return instrucaoArrayList.get(0);
        }
        return null;
    }


}
