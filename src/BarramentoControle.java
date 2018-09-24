import util.EnumCod;

import java.util.ArrayList;

public class BarramentoControle extends Thread implements Barramento3Vias<EnumCod>{
    private ArrayList<EnumCod> filaCpuControle = new ArrayList<>();
    private ArrayList<EnumCod> filaRamControle = new ArrayList<>();
    private ArrayList<EnumCod> filaPrioridade = new ArrayList<>();
    @Override
    public void run() {

    }


    public boolean havePriority(){
        if (filaPrioridade.size()>0){
            return true;
        }
        return false;
    }
    public EnumCod receivePriority(){
        if (filaPrioridade.size() > 0) {
            EnumCod aux = filaPrioridade.get(0);
            filaPrioridade.remove(0);
            return aux;
        }
        return null;

    }
    public void sendPriority(EnumCod enumCod) {
        filaPrioridade.add(enumCod);
    }
    @Override
    public boolean possuiInformacaoRam() {
        if (filaRamControle.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean possuiInformacaoCpu() {
        if (filaCpuControle.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public void sendCpu(EnumCod codigo) {
        filaCpuControle.add(codigo);

    }

    @Override
    public void sendRam(EnumCod codigo) {
    filaRamControle.add(codigo);
    }

    @Override
    public EnumCod receiveRam() {
        if (filaRamControle.size() > 0){
            EnumCod aux = filaRamControle.get(0);
            filaRamControle.remove(0);
            return  aux;
        }
        return null;
    }

    @Override
    public EnumCod receiveCpu() {
        if (filaCpuControle.size() > 0){
            EnumCod aux = filaCpuControle.get(0);
            filaCpuControle.remove(0);

            return aux;
        }
        return null;
    }

}
