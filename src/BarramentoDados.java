import java.util.ArrayList;
import java.util.Arrays;

public class BarramentoDados  extends Thread implements Barramento3Vias<Instrucao>{
    private ArrayList<Instrucao> filaCpuDados = new ArrayList<>();
    private ArrayList<Instrucao> filaRamDados = new ArrayList<>();
    private ArrayList<Instrucao> filaPrioridade = new ArrayList<>();
    private int tamanhoBarramento;
    public BarramentoDados(int tamanho){
        this.tamanhoBarramento = tamanho;
    }
    @Override
    public void run() {
    }



    public Instrucao receivePriority(){
        if (filaPrioridade.size() > 0) {
            Instrucao aux = filaPrioridade.get(0);
            filaPrioridade.remove(0);
            return aux;
        }
        return null;

    }
    public void sendPriority(Instrucao instrucao){
        filaPrioridade.add(instrucao);
    }
    public boolean havePriority(){
        if (filaPrioridade.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public void sendCpu(Instrucao payload) {
        filaCpuDados.add(payload);

    }

    @Override
    public void sendRam(Instrucao payload) {
        filaRamDados.add(payload);

    }

    @Override

    public Instrucao receiveRam() {
        if (filaRamDados.size() > 0){
            Instrucao aux = filaRamDados.get(0);
            filaRamDados.remove(0);
            return  aux;

        }
        return null;
    }

    @Override
    public Instrucao receiveCpu() {
        if (filaCpuDados.size() > 0){
            Instrucao aux = filaCpuDados.get(0);
            filaCpuDados.remove(0);
            return aux;
        }
        return null;
    }

    @Override
    public boolean possuiInformacaoRam() {
        if (filaRamDados.size()>0){
            return true;
        }
        return false;
    }
    @Override
    public boolean possuiInformacaoCpu() {
        if (filaCpuDados.size()>0){
            return true;
        }
        return false;
    }


    public int getTamanhoBarramento() {
        return tamanhoBarramento;
    }
}
