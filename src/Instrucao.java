import util.EnumCod;

public class Instrucao {


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
}

