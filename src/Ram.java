import util.EnumCod;

import java.util.Arrays;

public class Ram {

    private EnumCod enumCod ;
    private Instrucao instrucao ;
    private int enderecoMemoria ;
    //    private Barramento barramento;
    private int tamanho;
    private byte[] vetorBytesRam;

    //barramento novo
    private BarramentoControle barramentoControle;
    private BarramentoDados barramentoDados;
    private BarramentoEnderecoMemoria barramentoEnderecoMemoria;

    //CONSTRUTOR


    public Ram(BarramentoControle barramentoControle, BarramentoDados barramentoDados, BarramentoEnderecoMemoria barramentoEnderecoMemoria) {
        this.barramentoControle = barramentoControle;
        this.barramentoDados = barramentoDados;
        this.barramentoEnderecoMemoria = barramentoEnderecoMemoria;
        vetorBytesRam = new byte[1024];
    }

    public void write() {

        System.out.println("-----====== \033[36;1mRAM  \033[19m======-----");
        System.out.println("ENDERECO DE MEMORIA:" + enderecoMemoria);
        System.out.println("-----======= ENTROU NO WRITE =======-------");
        System.out.println("tamanho do vetor de bytes: " + instrucao.getTamanho());
        System.out.print("------======Escrevendo Vetor de bytes =======------\n");

        for (int i = 0; i < instrucao.getTamanho(); i++) { //percorrendo o vetor do payload

            vetorBytesRam[enderecoMemoria] = instrucao.getPayload()[i];// atribuindo o vetor de payload a o vetor da ram
            enderecoMemoria++;
        }

        System.out.println("\n-------======= IMPRIMINDO VETOR DA RAM =======---------");
        System.out.println(Arrays.toString(vetorBytesRam));

        System.out.println("");
    }

    public int execute() {
        boolean rodando = true;
            while (rodando){
                enumCod =null;
                instrucao =null;
                enderecoMemoria = -1;
            if (barramentoEnderecoMemoria.possuiInformacaoRam() &&
                    barramentoControle.possuiInformacaoRam() &&
                    barramentoDados.possuiInformacaoRam()) {

                enumCod = barramentoControle.receiveRam();
                instrucao = barramentoDados.receiveRam();
                enderecoMemoria = barramentoEnderecoMemoria.receiveRam();

                if (enumCod == EnumCod.WRITE) {
                    write();
                } else if (enumCod==EnumCod.READ){
                    read();
                }else{
                    read2();
                }

            }else {

                rodando = false;
                return 1;
            }
        }
        return 0;
    }    public void read2() {


        byte[] comandoBytes = new byte[instrucao.getTamanho()];
        System.out.println("tamanho do vetor a ser criado: " + comandoBytes.length);
        int contador = 0;
        System.out.println("-----======= FAZENDO A BUSCA DA POSICAO: " + enderecoMemoria + "  A POSICAO: " + (enderecoMemoria + instrucao.getTamanho()) + " =======-------");

        for (int i = enderecoMemoria; i < (enderecoMemoria + instrucao.getTamanho()); i++) {

            comandoBytes[contador] = vetorBytesRam[i];
            contador++;
        }
        System.out.println("-----======= IMPRIMINDO RESULTADO DA BUSCA NA RAM =======-------");
        System.out.println(Arrays.toString(comandoBytes));

//        barramento.sendCpu(new Instrucao(comandoBytes,aux.getCodigo()));
        System.out.println("-----======= ENVIANDO PARA BARRAMENTO PRIORITARIO =======-------");

        barramentoDados.sendPriority(new Instrucao(comandoBytes, enumCod,instrucao.getPayload()));
        barramentoControle.sendPriority(enumCod);
        barramentoEnderecoMemoria.sendPriority(enderecoMemoria);

    }

    public void read() {

        System.out.println("-----====== \033[36;1mRAM  \033[19m======-----");
        System.out.println("-----======= ENTROU NO READ "+enumCod.getValue()+" =======-------");




        byte[] comandoBytes = new byte[instrucao.getTamanho()];
        System.out.println("tamanho do vetor a ser criado: " + comandoBytes.length);
        int contador = 0;
        System.out.println("-----======= FAZENDO A BUSCA DA POSICAO: " + enderecoMemoria + "  A POSICAO: " + (enderecoMemoria + instrucao.getTamanho()) + " =======-------");

        for (int i = enderecoMemoria; i < (enderecoMemoria + instrucao.getTamanho()); i++) {

            comandoBytes[contador] = vetorBytesRam[i];
            contador++;
        }
        System.out.println("-----======= IMPRIMINDO RESULTADO DA BUSCA NA RAM =======-------");
        System.out.println(Arrays.toString(comandoBytes));

//        barramento.sendCpu(new Instrucao(comandoBytes,aux.getCodigo()));
        barramentoDados.sendCpu(new Instrucao(comandoBytes, enumCod));
        barramentoControle.sendCpu(enumCod);
        barramentoEnderecoMemoria.sendCpu(enderecoMemoria);
        //apagando os dados lidos pela cpu
        if (enumCod == EnumCod.READ) {

            for (int i = enderecoMemoria; i < instrucao.getTamanho() + enderecoMemoria; i++) {
                vetorBytesRam[i] = 0;
            }
        }

    }

    //CONSTRUTOR
    public Ram(int tamanhoRam) {

        this.tamanho = tamanhoRam;
        this.vetorBytesRam = new byte[tamanhoRam];
    }

    //GET BARRAMENTO
    public BarramentoControle getBarramentoControle() {
        return barramentoControle;
    }

    public BarramentoDados getBarramentoDados() {
        return barramentoDados;
    }

    public BarramentoEnderecoMemoria getBarramentoEnderecoMemoria() {
        return barramentoEnderecoMemoria;
    }

}
