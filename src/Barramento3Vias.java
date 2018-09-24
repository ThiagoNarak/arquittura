import java.util.ArrayList;

public interface Barramento3Vias<T> {


    void sendCpu(T endereco);

    void sendRam(T endereco);

    T receiveRam();

    T receiveCpu();

    boolean possuiInformacaoRam();
    boolean possuiInformacaoCpu();
    void sendPriority(T x);
    T receivePriority();
    boolean havePriority();




}
