@Deprecated
public interface InterfaceBarramento {

    public void sendCpu(Instrucao instrucao);
    public void sendRam(Instrucao instrucao);
    public Instrucao receiveRam();
    public Instrucao receiveCpu();

}
