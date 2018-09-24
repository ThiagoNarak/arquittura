import util.EnumCod;

public class Instrucao {

    private byte[]dado;
    private byte[] payload; //vetor de bytes mov b, 5
    private int endereco;  //inicio de onde ira escrever na ram
    private EnumCod codigo; //tipo de operacao read write ou interrupt
    private int tamanho;

    //CPU
    public Instrucao(int tamanho, int endereco, EnumCod codigo){
        this.tamanho = tamanho;
        this.endereco = endereco;
        this.codigo = codigo;
    }

    public Instrucao( int endereco,byte[] payload, EnumCod codigo) {
        this.payload = payload;
        this.endereco = endereco;
        this.codigo = codigo;
    }

    //RAM
    public Instrucao(byte[] payload, int endereco,int tamanho, EnumCod codigo) {
        this.payload = payload;
        this.endereco = endereco;
        this.codigo = codigo;
        this.tamanho = tamanho;
    }

    public Instrucao(byte[] payload, EnumCod codigo) {
        this.payload = payload;
        this.codigo = codigo;

    }
    public Instrucao (byte[] payload,int tamanho){
        this.tamanho = tamanho;
        this.payload = payload;
    }

    public Instrucao(EnumCod codigo, int tamanho) {
        this.codigo = codigo;
        this.tamanho = tamanho;

    }
    public Instrucao(EnumCod codigo, int tamanho,byte payload[]) {
        this.payload = payload;
        this.codigo = codigo;
        this.tamanho = tamanho;

    }

    public Instrucao(byte[] dados, EnumCod enumCod, byte[] payload) {
        this.dado = dados;
        this.codigo = enumCod;
        this.payload=payload;
    }

    public byte[] getPayload() {
        return payload;
    }

    public int getEndereco() {
        return endereco;
    }

    public EnumCod getCodigo() {
        return codigo;
    }

    public int getTamanho() {
        return tamanho;
    }

    public byte[] getDado() {
        return dado;
    }

    public void setDado(byte[] dado) {
        this.dado = dado;
    }
}

