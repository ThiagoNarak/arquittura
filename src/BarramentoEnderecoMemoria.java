import java.util.ArrayList;

public class BarramentoEnderecoMemoria extends Thread implements Barramento3Vias<Integer> {
    private ArrayList<Integer> filaCpuEnderecoMemoria = new ArrayList<>();
    private ArrayList<Integer> filaRamEnderecoMemoria = new ArrayList<>();
    private ArrayList<Integer> filaPrioridade = new ArrayList<>();

    public boolean havePriority(){
        if (filaPrioridade.size()>0){
            return true;
        }
        return false;
    }
    public void sendPriority(Integer endereco){
        filaPrioridade.add(endereco);
    }

    public Integer receivePriority(){
        if (filaPrioridade.size() > 0) {
            Integer aux = filaPrioridade.get(0);
            filaPrioridade.remove(0);
            return aux;
        }
        return null;
    }

    @Override
    public void run() {


    }

    @Override
    public void sendCpu(Integer endereco) {
        filaCpuEnderecoMemoria.add(endereco);

    }

    @Override
    public void sendRam(Integer endereco) {
        filaRamEnderecoMemoria.add(endereco);

    }

    @Override
    public boolean possuiInformacaoRam() {
        if (filaRamEnderecoMemoria.size()>0){
            return true;
        }
        return false;
    }
    @Override
    public boolean possuiInformacaoCpu() {
        if (filaCpuEnderecoMemoria.size()>0){
            return true;
        }
        return false;
    }


    @Override
    public Integer receiveRam() {

        if (filaRamEnderecoMemoria.size() > 0){

            Integer aux = filaRamEnderecoMemoria.get(0);
            filaRamEnderecoMemoria.remove(0);
            return  aux;
        }
        return null;
    }

    @Override
    public Integer receiveCpu() {
        if (filaCpuEnderecoMemoria.size() > 0){
            Integer aux = filaCpuEnderecoMemoria.get(0);
            filaCpuEnderecoMemoria.remove(0);
            return aux;
        }
        return null;
    }
}
