import java.util.Arrays;

public class Emulator {
    private int tamanhoBarramento;
    private int tamanhoRam;
    private DAO dao;
    private Ram ram;
    private Cpu cpu;
    private Barramento barramento;
    private EntradaSaida es;
    private Encoder encoder;
    private Parser parser;
    private int contadorES;
    private int instrucao;

    public Emulator(int tamanhoBarramento, int tamanhoRam) {
        this.tamanhoBarramento = tamanhoBarramento;
        this.tamanhoRam = tamanhoRam;

        dao = new DAO();
        barramento = new Barramento(tamanhoBarramento);
        cpu = new Cpu(barramento);
        ram = new Ram(barramento,tamanhoRam);
        es = new EntradaSaida(barramento);
        encoder = new Encoder(tamanhoBarramento);
        parser = new Parser(tamanhoBarramento);
        contadorES = 0;
        instrucao =0;
    }



    public void run() {
        System.out.println(dao.pegarArrayListDAO().size());
        for (int i = 0; i <dao.pegarArrayListDAO().size() ; i++) {

            System.out.println(dao.pegarArrayListDAO().get(i));

            String vetor[] = parser.validando(dao.pegarArrayListDAO().get(i));

            System.out.println(Arrays.toString(vetor));
            //passo 2 encodificar(bytes) o vetor de Strings
            byte vetorBytes[] = encoder.encodificar(vetor);

            //passo 3 adiciona a fila do I/O

            es.adicionarInstrucao(vetorBytes);
            contadorES++;
            if (contadorES > 0){
                while (contadorES!=0){
                    es.getBarramento().sendRam(es.pegarInstrucao());
                    es.getBarramento().sendCpu(es.pegarInstrucao());
                    contadorES--;
                    instrucao=0;
                    while (instrucao !=6){

                        if (instrucao==0){
                            ram.write();
                            instrucao = cpu.receive();
                            System.out.println("\033[31;1m INTERRUPT\033[19m");


                        }
                        if (instrucao==1){
                            cpu.send();
                            ram.read();
                            instrucao = cpu.receive();
                            System.out.println("\033[31;1m READ\033[19m");
                        }
                        if (instrucao == 2) {
                            cpu.send();
                            ram.read();
                            instrucao = cpu.receive();
                            System.out.println("\033[31;1m READ2\033[19m");
                        }
                        if (instrucao == 5){
                            cpu.send();
                            ram.write();
                            instrucao=6;
                            System.out.println("\033[31;1m FINISH\033[19m");
                        }

                    }
                }

            }
        }

    }

}
