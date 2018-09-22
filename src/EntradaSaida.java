import util.EnumCod;

import java.util.ArrayList;
import java.util.Arrays;

public class EntradaSaida {
    private ArrayList<Instrucao> instrucaoArrayList;
    private ArrayList<Byte> byteBuffer;
    private ArrayList<Integer> integers;
    private Barramento barramento;
    private int posicao;
    private int posicaoBuffer;
    private boolean sobreescrever;



    //CONSTRUTOR
    public EntradaSaida(Barramento barramento) {
        this.barramento = barramento;
        this.posicao = 0;
        this.instrucaoArrayList = new ArrayList<>(4);
        this.byteBuffer = new ArrayList<>();
        this.integers  = new ArrayList<>();
        this.posicaoBuffer = 0;
    }

    public void adicionarInstrucao1(byte vetor[]){


        for (int i = 0; i <vetor.length ; i++) {

            byteBuffer.add(vetor[i]);
            integers.add(new Integer(posicaoBuffer));
        }

        posicaoBuffer++;

    }

    public boolean adicionarInstrucao(byte vetor[]){

        if (instrucaoArrayList.size()<4){
            if (posicao==16){
                posicao=0;
            }

            System.out.println("------======= ADICIONANDO AO BUFFER E/S ========------ | INSTRUÇÃO P/ RAM");

            Instrucao instrucaoRam = new Instrucao(vetor,posicao,vetor.length,EnumCod.WRITE);   //comando .WRITE nao faz diferença na instrução que vai para memoria RAM

            System.out.println("vetor"+ Arrays.toString(vetor));
            System.out.println("posicao: "+ posicao);
            System.out.println("Tamanho: "+vetor.length);
            System.out.println("codigo: "+EnumCod.WRITE);

            Instrucao instrucaoCpu = new Instrucao(vetor.length,posicao, EnumCod.INTERRUPT);

            System.out.println("------======= ADICIONANDO AO BUFFER E/S ========------ | INSTRUÇÃO P/ CPU");

            System.out.println("posicao: "+ posicao);
            System.out.println("Tamanho: "+vetor.length);
            System.out.println("codigo: "+EnumCod.INTERRUPT);

            instrucaoArrayList.add(instrucaoRam);
            instrucaoArrayList.add(instrucaoCpu);

            posicao += (getBarramento().getLarguraBarramento()/2);
            return true;
        }else {
            return false;
        }
    }

    public Barramento getBarramento() {
        return barramento;
    }

    public Instrucao pegarInstrucao() {
        if (instrucaoArrayList.size()>0){

                Instrucao aux = instrucaoArrayList.get(0);
                instrucaoArrayList.remove(0);

                return aux;

        }
        return null;
    }


}
