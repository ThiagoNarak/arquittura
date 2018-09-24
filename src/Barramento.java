import util.EnumCod;

import java.util.ArrayList;
import java.util.Arrays;

@Deprecated
public class Barramento implements InterfaceBarramento {
    private int larguraBarramento;
    private ArrayList<EnumCod> filaRamSinal = new ArrayList<>();
    private ArrayList<Instrucao> filaRamPayload = new ArrayList<>();
    private ArrayList<Integer> filaRamEnderecoMemoria = new ArrayList<>();

    private ArrayList<EnumCod> filaCpuSinal = new ArrayList<>();
    private ArrayList<Instrucao> filaCpuPayload = new ArrayList<>();
    private ArrayList<Integer> filaCpuEnderecoMemoria = new ArrayList<>();



    private ArrayList<Instrucao> filaRam = new ArrayList<>();
    private ArrayList<Instrucao> filaCpu = new ArrayList<>();

    public Barramento(int larguraBarramento){
        this.larguraBarramento = larguraBarramento;
    }


    //BARRAMENTO 3 VIAS
    //------== INICIO ==------
    public void sendCpu(EnumCod enumCod,Instrucao instrucao,int enderecoMemoria) {
       filaCpuEnderecoMemoria.add(enderecoMemoria);
       filaCpuSinal.add(enumCod);
       filaCpuPayload.add(instrucao);

    }
    @Override
    public Instrucao receiveCpu(){

        Instrucao aux = new Instrucao(filaCpuEnderecoMemoria.get(0),filaCpuPayload.get(0).getPayload(),filaCpuSinal.get(0));

        return aux;
    }




    public void sendRam(EnumCod enumCod, Instrucao instrucao,int enderecoMemoria){

        filaRamSinal.add(enumCod);
        filaRamPayload.add(instrucao);
        filaRamEnderecoMemoria.add(enderecoMemoria);

    }




    //-------== FIM ==-------
    @Override
    public void sendCpu(Instrucao instrucao) {
        System.out.println("-----====== \033[34;1mBARRAMENTO  \033[19m======-----");
        System.out.println("-----===== Recebendo instrução na fila da CPU ======-----");
        filaCpu.add(instrucao);


    }

//    public Instrucao receiveCpu() {
//        if (filaCpu.size()>0){  //verifica se possui pelo menos 1 informação na fila da ram
//            Instrucao aux = filaCpu.get(0);  //passo a referencia da primeira instrução da fila para aux
//            filaCpu.remove(0); //removo a primeira instrução da fila
//            return aux;
//        }
//
//        return null;
//    }

    @Override
    public void sendRam(Instrucao instrucao) {
        System.out.println("-----====== \033[34;1mBARRAMENTO  \033[19m======-----");
        System.out.println("-----===== Recebendo instrução na fila da Ram ======-----");
        filaRam.add(instrucao);



    }

    @Override
    public Instrucao receiveRam() {

        if (filaRam.size()>0){  //verifica se possui pelo menos 1 informação na fila da ram
            Instrucao aux = filaRam.get(0);  //passo a referencia da primeira instrução da fila para aux
            filaRam.remove(0); //removo a primeira instrução da fila
            return aux;
        }

        return null;
    }

    public int getLarguraBarramento() {
        return larguraBarramento;
    }
}
