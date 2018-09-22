import util.EnumCod;

import java.util.Arrays;

public class Ram {

    private Barramento barramento;
    private int tamanho;
    private byte[] vetorBytesRam;



    public void write(){

    Instrucao aux = barramento.receiveRam();
        if (aux!=null) {
            System.out.println("-----====== \033[36;1mRAM  \033[19m======-----");

            System.out.println("-----======= ENTROU NO WRITE =======-------");
            System.out.println("tamanho do vetor de bytes: "+ aux.getTamanho());
            System.out.print("------======Escrevendo Vetor de bytes =======------\n");
            int posicao = aux.getEndereco();
            for (int i = 0; i < aux.getTamanho(); i++) { //percorrendo o vetor do payload

                vetorBytesRam[posicao] = aux.getPayload()[i];// atribuindo o vetor de payload a o vetor da ram

                System.out.print("["+Integer.toBinaryString(vetorBytesRam[posicao])+"] ");
                posicao++;
            }


        }
        System.out.println("\n-------======= IMPRIMINDO VETOR DA RAM =======---------");
        System.out.println(Arrays.toString(vetorBytesRam));

        System.out.println("");
    }
    public void read(){

        System.out.println("-----====== \033[36;1mRAM  \033[19m======-----");
        System.out.println("-----======= ENTROU NO READ =======-------");

        Instrucao aux = barramento.receiveRam();

        byte[] comandoBytes = new byte[aux.getTamanho()];
        System.out.println("tamanho do vetor a ser criado: "+ comandoBytes.length);
        int contador = 0;
        System.out.println("-----======= FAZENDO A BUSCA DA POSICAO: "+ aux.getEndereco() + "  A POSICAO: "+ (aux.getEndereco()+aux.getTamanho())+" =======-------" );

        for (int i = aux.getEndereco(); i < (aux.getEndereco()+aux.getTamanho()) ; i++) {

            comandoBytes[contador] = vetorBytesRam[i];
            contador++;
        }
        System.out.println("-----======= IMPRIMINDO RESULTADO DA BUSCA NA RAM =======-------");
        System.out.println(Arrays.toString(comandoBytes));

        barramento.sendCpu(new Instrucao(comandoBytes,aux.getCodigo()));
        //apagando os dados lidos pela cpu
        if (aux.getCodigo()== EnumCod.READ){
            for (int i = aux.getEndereco(); i <aux.getTamanho() ; i++) {
                vetorBytesRam[i] = 0;
            }
        }


    }
    //CONSTRUTOR
    public Ram(Barramento barramento,int tamanhoRam) {
        this.barramento = barramento;
        this.tamanho = tamanhoRam;
        this.vetorBytesRam = new byte[tamanhoRam*2];
    }

    //GET BARRAMENTO
    public Barramento getBarramento() {
        return barramento;
    }
}
