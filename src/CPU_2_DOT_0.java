import util.EnumCod;

import java.util.Arrays;

public class CPU_2_DOT_0 {

    private int pi;         //ponteiro de instrucao
    private int tamanho;    //tamanho da instrucao
    private EnumCod comando;
    private byte[] resultado;
    //registradores
//  int regArray [] = new int[5]; // 1 == A 2 == B 3 == C 4 == D

    int regArray[] = {1, 2, 3, 4, 5};
    //vetores
    private short vetorShort[];
    private int vetorInt[];
    private Long vetorLong[];


    //atributos
    private Decoder decoder;
    private Encoder encoder;
    //barramento novo
    private BarramentoControle barramentoControle;
    private BarramentoDados barramentoDados;
    private BarramentoEnderecoMemoria barramentoEnderecoMemoria;
    private Instrucao instrucao;

    //CONSTRUTOR


    public CPU_2_DOT_0(BarramentoControle barramentoControle, BarramentoDados barramentoDados, BarramentoEnderecoMemoria barramentoEnderecoMemoria) {
        this.barramentoControle = barramentoControle;
        this.barramentoDados = barramentoDados;
        this.barramentoEnderecoMemoria = barramentoEnderecoMemoria;
        this.encoder = new Encoder(barramentoDados.getTamanhoBarramento());
        this.decoder = new Decoder();
    }

    public void checagem(int instrucao) {
        if (instrucao == EnumCod.READ.getValue() ||
                instrucao == EnumCod.READ2.getValue() ||
                instrucao == EnumCod.WRITE.getValue() ||
                instrucao == EnumCod.MOV3_2.getValue() ||
                instrucao == EnumCod.ADD3_2.getValue() ||
                instrucao == EnumCod.ADD4_2.getValue() ||
                instrucao == EnumCod.INC2_2.getValue()) {
            send();
        }
    }

    public int excute() {
        int instrucao;
        if (barramentoEnderecoMemoria.havePriority() &&
                barramentoDados.havePriority() &&
                barramentoControle.havePriority()) {
            checagem(receive(true));

        } else if (barramentoControle.possuiInformacaoCpu() &&
                barramentoDados.possuiInformacaoCpu() &&
                barramentoEnderecoMemoria.possuiInformacaoCpu()) {
            checagem(receive(false));


        } else {
            return 1;
        }
        return 0;
    }

    public int receive(boolean priority) {
        EnumCod enumCod;
        Instrucao instrucao;
        int endereco;
        Instrucao aux = null;
        if (priority) {
            enumCod = barramentoControle.receivePriority();
            instrucao = barramentoDados.receivePriority();
            this.instrucao = instrucao;
            endereco = barramentoEnderecoMemoria.receivePriority();
        } else {
            enumCod = barramentoControle.receiveCpu();
            instrucao = barramentoDados.receiveCpu();
            this.instrucao = instrucao;
            endereco = barramentoEnderecoMemoria.receiveCpu();
        }

        switch (barramentoDados.getTamanhoBarramento()) {

            case 16: //////////////////////////////////////////////////////////////////////////////////////

                switch (enumCod.getValue()) {

                    case 0: //interrupt

                        pi = endereco;
                        tamanho = instrucao.getTamanho();
                        comando = EnumCod.READ;
                        System.out.println("------======     \033[31;1m CPU \033[19m    =======-----");
                        System.out.println("-====== ALTEROU O PI ======-");
                        System.out.println("PI: " + pi);
                        System.out.println("Tamanho: " + tamanho);
                        System.out.println("comando: " + comando.getValue());
                        break;
                    case 1:
                        vetorShort = decoder.byteToShort(instrucao.getPayload()); //[100] [16] [5]

                        System.out.println("------======     \033[31;1m CPU \033[19m    =======-----");
                        System.out.println("-======    VERIFICANDO  TIPO DE OPERAÇÃO   ======-");
                        switch (vetorShort[0]) {

                            case 100: //MOV
                                //3 casos do mov:
                                System.out.println("-======    OPERAÇÃO: MOV     ======-");
                                System.out.println("-======    VERIFICANDO ++ || +- ||-+     ======-");
                                System.out.println("-======    Encontrado     ======-");

                                if (vetorShort[1] >= 0 && vetorShort[2] >= 0) { //mov 0x00 5
                                    System.out.println("-====== MOV CASO 1 ======-");
                                    System.out.println("-======    Endereço de memoria: " + vetorShort[1]);
                                    System.out.println("-======    Valor: " + vetorShort[2]);

                                    resultado = encoder.encodificar(new String[]{String.valueOf(vetorShort[2])});
                                    pi = vetorShort[1];
                                    tamanho = 2;
                                    comando = EnumCod.WRITE;

                                } else if (vetorShort[1] >= 0 && vetorShort[2] < 0) { //mov 0x0000 B
                                    System.out.println("MOV CASO 2");
                                    System.out.println("-======    Endereço de memoria: " + vetorShort[1]);
                                    System.out.println("-======    Valor: " + vetorShort[2]);
                                    resultado = encoder.encodificar(new String[]{String.valueOf(regArray[Math.abs(vetorShort[2])])});
                                    pi = vetorShort[1];
                                    tamanho = 2;
                                    comando = EnumCod.WRITE;
                                } else if (vetorShort[1] < 0 && vetorShort[2] >= 0) { //mov B, 0x0000
                                    System.out.println("MOV CASO 3");

                                    pi = vetorShort[2];
                                    tamanho = 2;

                                    comando = EnumCod.MOV3_2;
                                }
                                break;
                            case 101: //ADD
                                if (vetorShort[1] < 0 && vetorShort[2] >= 0) { //add B, 5 //[101] [-2] [5]
                                    System.out.println("-====== ADD CASO 1 ======-");
                                    regArray[Math.abs(vetorShort[1])] = vetorShort[2] + regArray[Math.abs(vetorShort[1])];

                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;
                                } else if (vetorShort[1] < 0 && vetorShort[2] < 0) { //ADD B, C
                                    System.out.println("-====== ADD CASO 2 ======-");
                                    regArray[Math.abs(vetorShort[1])] = regArray[Math.abs(vetorShort[1])] + regArray[Math.abs(vetorShort[2])];
                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;

                                } else if (vetorShort[1] >= 0 && vetorShort[2] < 0) { //ADD 0x00, B
                                    pi = vetorShort[1];
                                    tamanho = 2;

                                    comando = EnumCod.ADD3_2;
                                } else if (vetorShort[1] >= 0 && vetorShort[2] >= 0) { //ADD 0x00, 5
                                    pi = vetorShort[1];
                                    tamanho = 2;

                                    comando = EnumCod.ADD4_2;
                                }


                                break;
                            case 102: //IMUL


                                if (vetorShort[1] < 0 && vetorShort[2] < 0 && vetorShort[3] < 0) {
                                    System.out.println("-====== IMUL CASO 1 ======-");
                                    System.out.println(Arrays.toString(vetorShort));

                                    regArray[Math.abs(vetorShort[1])] = regArray[Math.abs(vetorShort[2])] * regArray[Math.abs(vetorShort[3])];
                                    System.out.println(regArray[2] * regArray[3]);
                                    System.out.println("------======     \033[31;1m CPU \033[19m     =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);
                                    comando = EnumCod.FINISH;

                                } else if (vetorShort[1] < 0 && vetorShort[2] < 0 && vetorShort[3] >= 0) {
                                    System.out.println("-====== IMUL CASO 2 ======-");
                                    regArray[Math.abs(vetorShort[1])] = regArray[Math.abs(vetorShort[2])] * vetorShort[3];

                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;

                                } else if (vetorShort[1] >= 0 && vetorShort[2] >= 0 && vetorShort[3] < 0) {
                                    System.out.println("-====== IMUL CASO 3 ======-");
                                    pi = vetorShort[1];
                                    tamanho = 2;
                                    System.out.println("atulizou pi: " + pi);
                                    System.out.println("tamanho: " + tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(vetorShort[2] * regArray[Math.abs(vetorShort[3])])
                                    });
                                    comando = EnumCod.WRITE;
                                } else if (vetorShort[1] >= 0 && vetorShort[2] < 0 && vetorShort[3] < 0) {
                                    System.out.println("-====== IMUL CASO 4 ======-");
                                    pi = vetorShort[1];
                                    tamanho = 2;
                                    System.out.println("atulizou pi: " + pi);
                                    System.out.println("tamanho: " + tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(regArray[Math.abs(vetorShort[2])] * regArray[Math.abs(vetorShort[3])])
                                    });
                                    comando = EnumCod.WRITE;


                                }
                                break;
                            case 103:
                                if (vetorShort[1] < 0) {
                                    System.out.println("-====== inc CASO 1 ======-");
                                    regArray[Math.abs(vetorShort[1])]++;
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;
                                } else if (vetorShort[1] >= 0) {
                                    System.out.println("-====== inc CASO 2 ======-");
                                    pi = vetorShort[1];
                                    tamanho = 2;
                                    comando = EnumCod.INC2_2;
                                }


                                break;
                        }


                        break;
                    case 121:
                        short dadosShort = decoder.byteToShort(instrucao.getDado())[0];


                        regArray[Math.abs(instrucao.getPayload()[3])] = dadosShort;
                        System.out.println("------====== REGISTRADORES 121=======-----");
                        System.out.println("A: " + regArray[1]);
                        System.out.println("B: " + regArray[2]);
                        System.out.println("C: " + regArray[3]);
                        System.out.println("D: " + regArray[4]);
                        comando = EnumCod.FINISH;
                        break;
                    case 122:
                        short dadosShort2 = decoder.byteToShort(instrucao.getDado())[0];
                        dadosShort2 = (short) (dadosShort2 + regArray[Math.abs(vetorShort[2])]);
                        resultado = encoder.encodificar(new String[]{String.valueOf(dadosShort2)});
                        comando = EnumCod.WRITE;
                        break;
                    case 123:
                        short dadosShort3 = decoder.byteToShort(instrucao.getDado())[0];
                        dadosShort3 = (short) (dadosShort3 + vetorShort[2]);
                        resultado = encoder.encodificar(new String[]{
                                String.valueOf(dadosShort3)
                        });
                        comando = EnumCod.WRITE;
                        break;

                    case 124:
                        System.out.println("COD 124 INC");
                        short dadosShort4 = decoder.byteToShort(instrucao.getDado())[0];
                        dadosShort4++;
                        System.out.println(dadosShort4);
                        resultado = encoder.encodificar(new String[]{
                                String.valueOf(dadosShort4)
                        });
                        System.out.println(Arrays.toString(resultado));
                        comando = EnumCod.WRITE;
                        break;

                }

                break;
            case 32:          /////////////////////////////////////////////////////////////////////////
                switch (enumCod.getValue()) {

                    case 0: //interrupt

                        pi = endereco;
                        tamanho = instrucao.getTamanho();
                        comando = EnumCod.READ;
                        System.out.println("------======     \033[31;1m CPU \033[19m    =======-----");
                        System.out.println("-====== ALTEROU O PI ======-");
                        System.out.println("PI: " + pi);
                        System.out.println("Tamanho: " + tamanho);
                        System.out.println("comando: " + comando.getValue());
                        break;
                    case 1:
                        vetorInt = decoder.byteToInt(instrucao.getPayload()); //[100] [16] [5]

                        System.out.println("------======     \033[31;1m CPU \033[19m    =======-----");
                        System.out.println("-======    VERIFICANDO  TIPO DE OPERAÇÃO   ======-");
                        switch (vetorInt[0]) {

                            case 100: //MOV
                                //3 casos do mov:
                                System.out.println("-======    OPERAÇÃO: MOV     ======-");
                                System.out.println("-======    VERIFICANDO ++ || +- ||-+     ======-");
                                System.out.println("-======    Encontrado     ======-");

                                if (vetorInt[1] >= 0 && vetorInt[2] >= 0) { //mov 0x00 5
                                    System.out.println("-====== MOV CASO 1 ======-");
                                    System.out.println("-======    Endereço de memoria: " + vetorInt[1]);
                                    System.out.println("-======    Valor: " + vetorInt[2]);

                                    resultado = encoder.encodificar(new String[]{String.valueOf(vetorInt[2])});
                                    pi = vetorInt[1];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;
                                    comando = EnumCod.WRITE;

                                } else if (vetorInt[1] >= 0 && vetorInt[2] < 0) { //mov 0x0000 B
                                    System.out.println("MOV CASO 2");
                                    System.out.println("-======    Endereço de memoria: " + vetorInt[1]);
                                    System.out.println("-======    Valor: " + vetorInt[2]);
                                    resultado = encoder.encodificar(new String[]{String.valueOf(regArray[Math.abs(vetorInt[2])])});
                                    pi = vetorInt[1];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    comando = EnumCod.WRITE;
                                } else if (vetorInt[1] < 0 && vetorInt[2] >= 0) { //mov B, 0x0000
                                    System.out.println("MOV CASO 3");

                                    pi = vetorInt[2];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;

                                    comando = EnumCod.MOV3_2;
                                }
                                break;
                            case 101: //ADD
                                if (vetorInt[1] < 0 && vetorInt[2] >= 0) { //add B, 5 //[101] [-2] [5]
                                    System.out.println("-====== ADD CASO 1 ======-");
                                    regArray[Math.abs(vetorInt[1])] = vetorInt[2] + regArray[Math.abs(vetorInt[1])];

                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;
                                } else if (vetorInt[1] < 0 && vetorInt[2] < 0) { //ADD B, C
                                    System.out.println("-====== ADD CASO 2 ======-");
                                    regArray[Math.abs(vetorInt[1])] = regArray[Math.abs(vetorInt[1])] + regArray[Math.abs(vetorInt[2])];
                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;

                                } else if (vetorInt[1] >= 0 && vetorInt[2] < 0) { //ADD 0x00, B
                                    pi = vetorInt[1];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;

                                    comando = EnumCod.ADD3_2;
                                } else if (vetorInt[1] >= 0 && vetorInt[2] >= 0) { //ADD 0x00, 5
                                    pi = vetorInt[1];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;

                                    comando = EnumCod.ADD4_2;
                                }


                                break;
                            case 102: //IMUL


                                if (vetorInt[1] < 0 && vetorInt[2] < 0 && vetorInt[3] < 0) {
                                    System.out.println("-====== IMUL CASO 1 ======-");
                                    System.out.println(Arrays.toString(vetorInt));

                                    regArray[Math.abs(vetorInt[1])] = regArray[Math.abs(vetorInt[2])] * regArray[Math.abs(vetorInt[3])];
                                    System.out.println(regArray[2] * regArray[3]);
                                    System.out.println("------======     \033[31;1m CPU \033[19m     =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);
                                    comando = EnumCod.FINISH;

                                } else if (vetorInt[1] < 0 && vetorInt[2] < 0 && vetorInt[3] >= 0) {
                                    System.out.println("-====== IMUL CASO 2 ======-");
                                    regArray[Math.abs(vetorInt[1])] = regArray[Math.abs(vetorInt[2])] * vetorInt[3];

                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;

                                } else if (vetorInt[1] >= 0 && vetorInt[2] >= 0 && vetorInt[3] < 0) {
                                    System.out.println("-====== IMUL CASO 3 ======-");
                                    pi = vetorInt[1];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    System.out.println("atulizou pi: " + pi);
                                    System.out.println("tamanho: " + tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(vetorInt[2] * regArray[Math.abs(vetorInt[3])])
                                    });
                                    comando = EnumCod.WRITE;
                                } else if (vetorInt[1] >= 0 && vetorInt[2] < 0 && vetorInt[3] < 0) {
                                    System.out.println("-====== IMUL CASO 4 ======-");
                                    pi = vetorInt[1];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    System.out.println("atulizou pi: " + pi);
                                    System.out.println("tamanho: " + tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(regArray[Math.abs(vetorInt[2])] * regArray[Math.abs(vetorInt[3])])
                                    });
                                    comando = EnumCod.WRITE;


                                }
                                break;
                            case 103:
                                if (vetorInt[1] < 0) {
                                    System.out.println("-====== inc CASO 1 ======-");
                                    regArray[Math.abs(vetorInt[1])]++;
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;
                                } else if (vetorInt[1] >= 0) {
                                    System.out.println("-====== inc CASO 2 ======-");
                                    pi = vetorInt[1];
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    comando = EnumCod.INC2_2;
                                }


                                break;
                        }


                        break;
                    case 121:
                        int dadosInt = decoder.byteToInt(instrucao.getDado())[0];

                        System.out.println(dadosInt);
                        System.out.println(Arrays.toString(instrucao.getPayload()));
                        regArray[Math.abs(instrucao.getPayload()[7])] = dadosInt;
                        System.out.println("------====== REGISTRADORES 121=======-----");
                        System.out.println("A: " + regArray[1]);
                        System.out.println("B: " + regArray[2]);
                        System.out.println("C: " + regArray[3]);
                        System.out.println("D: " + regArray[4]);
                        comando = EnumCod.FINISH;
                        break;
                    case 122:
                        int dadosInt2 = decoder.byteToInt(instrucao.getDado())[0];
                        dadosInt2 = (short) (dadosInt2 + regArray[Math.abs(vetorInt[2])]);
                        resultado = encoder.encodificar(new String[]{String.valueOf(dadosInt2)});
                        comando = EnumCod.WRITE;
                        break;
                    case 123:
                        int dadosInt3 = decoder.byteToInt(instrucao.getDado())[0];
                        dadosInt3 = (short) (dadosInt3 + vetorInt[2]);
                        resultado = encoder.encodificar(new String[]{
                                String.valueOf(dadosInt3)
                        });
                        comando = EnumCod.WRITE;
                        break;

                    case 124:
                        System.out.println("COD 124 INC");
                        int dadosInt4 = decoder.byteToInt(instrucao.getDado())[0];
                        dadosInt4++;
                        System.out.println(dadosInt4);
                        resultado = encoder.encodificar(new String[]{
                                String.valueOf(dadosInt4)
                        });
                        comando = EnumCod.WRITE;
                        break;

                }
                break;


            ////////////////////////////////////////////////////////
            case 64:
                switch (enumCod.getValue()) {

                    case 0: //interrupt

                        pi = endereco;
                        tamanho = instrucao.getTamanho();
                        comando = EnumCod.READ;
                        System.out.println("------======     \033[31;1m CPU \033[19m    =======-----");
                        System.out.println("-====== ALTEROU O PI ======-");
                        System.out.println("PI: " + pi);
                        System.out.println("Tamanho: " + tamanho);
                        System.out.println("comando: " + comando.getValue());
                        break;
                    case 1:
                        vetorLong = decoder.byteToLong(instrucao.getPayload()); //[100] [16] [5]

                        System.out.println("------======     \033[31;1m CPU \033[19m    =======-----");
                        System.out.println("-======    VERIFICANDO  TIPO DE OPERAÇÃO   ======-");
                        switch (String.valueOf(vetorLong[0])) {
                            case "100": //MOV
                                //3 casos do mov:
                                System.out.println("-======    OPERAÇÃO: MOV     ======-");
                                System.out.println("-======    VERIFICANDO ++ || +- ||-+     ======-");
                                System.out.println("-======    Encontrado     ======-");

                                if (vetorLong[1] >= 0 && vetorLong[2] >= 0) { //mov 0x00 5
                                    System.out.println("-====== MOV CASO 1 ======-");
                                    System.out.println("-======    Endereço de memoria: " + vetorLong[1]);
                                    System.out.println("-======    Valor: " + vetorLong[2]);

                                    resultado = encoder.encodificar(new String[]{String.valueOf(vetorLong[2])});
                                    pi =Integer.parseInt(String.valueOf(vetorLong[1]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;
                                    comando = EnumCod.WRITE;

                                } else if (vetorLong[1] >= 0 && vetorLong[2] < 0) { //mov 0x0000 B
                                    System.out.println("MOV CASO 2");
                                    System.out.println("-======    Endereço de memoria: " + vetorLong[1]);
                                    System.out.println("-======    Valor: " + vetorLong[2]);
                                    resultado = encoder.encodificar(new String[]{String.valueOf(regArray[Integer.parseInt(String.valueOf(Math.abs(vetorLong[2])))])});
                                    pi = Integer.parseInt(String.valueOf(vetorLong[1]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    comando = EnumCod.WRITE;
                                } else if (vetorLong[1] < 0 && vetorLong[2] >= 0) { //mov B, 0x0000
                                    System.out.println("MOV CASO 3");

                                    pi = Integer.parseInt(String.valueOf(vetorLong[2]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;

                                    comando = EnumCod.MOV3_2;
                                }
                                break;
                            case "101": //ADD
                                if (vetorLong[1] < 0 && vetorLong[2] >= 0) { //add B, 5 //[101] [-2] [5]
                                    System.out.println("-====== ADD CASO 1 ======-");
                                    regArray[(int) Math.abs(vetorLong[1])] = (int) (vetorLong[2] + regArray[(int)Math.abs(vetorLong[1])]);
                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;
                                } else if (vetorLong[1] < 0 && vetorLong[2] < 0) { //ADD B, C
                                    System.out.println("-====== ADD CASO 2 ======-");
                                    regArray[(int) Math.abs(vetorLong[1])] = regArray[(int)Math.abs(vetorLong[1])] + regArray[(int) Math.abs(vetorLong[2])];
                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;

                                } else if (vetorLong[1] >= 0 && vetorLong[2] < 0) { //ADD 0x00, B
                                    pi = Integer.parseInt(String.valueOf(vetorLong[1]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;

                                    comando = EnumCod.ADD3_2;
                                } else if (vetorLong[1] >= 0 && vetorLong[2] >= 0) { //ADD 0x00, 5
                                    pi = Integer.parseInt(String.valueOf(vetorLong[1]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;

                                    comando = EnumCod.ADD4_2;
                                }


                                break;
                            case "102": //IMUL


                                if (vetorLong[1] < 0 && vetorLong[2] < 0 && vetorLong[3] < 0) {
                                    System.out.println("-====== IMUL CASO 1 ======-");
                                    System.out.println(Arrays.toString(vetorLong));
                                    regArray[(int) Math.abs(vetorLong[1])] = regArray[(int) Math.abs(vetorLong[2])] * regArray[(int) Math.abs(vetorLong[3])];
                                    System.out.println(regArray[2] * regArray[3]);
                                    System.out.println("------======     \033[31;1m CPU \033[19m     =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);
                                    comando = EnumCod.FINISH;

                                } else if (vetorLong[1] < 0 && vetorLong[2] < 0 && vetorLong[3] >= 0) {
                                    System.out.println("-====== IMUL CASO 2 ======-");
                                    regArray[(int) Math.abs(vetorLong[1])] = regArray[(int) Math.abs(vetorLong[2])] * regArray[(int) Math.abs(vetorLong[3])];
                                    System.out.println("------======     \033[31;1m CPU 033[19m    =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;

                                } else if (vetorLong[1] >= 0 && vetorLong[2] >= 0 && vetorLong[3] < 0) {
                                    System.out.println("-====== IMUL CASO 3 ======-");
                                    pi =Integer.parseInt(String.valueOf(vetorLong[1]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    System.out.println("atulizou pi: " + pi);
                                    System.out.println("tamanho: " + tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(vetorLong[2] * regArray[(int) Math.abs(vetorLong[3])])
                                    });
                                    comando = EnumCod.WRITE;
                                } else if (vetorLong[1] >= 0 && vetorLong[2] < 0 && vetorLong[3] < 0) {
                                    System.out.println("-====== IMUL CASO 4 ======-");
                                    pi = Integer.parseInt(String.valueOf(vetorLong[1]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    System.out.println("atulizou pi: " + pi);
                                    System.out.println("tamanho: " + tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(regArray[(int) Math.abs(vetorLong[2])] * regArray[(int) Math.abs(vetorLong[3])])
                                    });
                                    comando = EnumCod.WRITE;


                                }
                                break;
                            case "103":
                                if (vetorLong[1] < 0) {
                                    System.out.println("-====== inc CASO 1 ======-");
                                    regArray[(int)Math.abs(vetorLong[1])]++;
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                    comando = EnumCod.FINISH;
                                } else if (vetorLong[1] >= 0) {
                                    System.out.println("-====== inc CASO 2 ======-");
                                    pi = Integer.parseInt(String.valueOf(vetorLong[1]));
                                    tamanho = barramentoDados.getTamanhoBarramento()/8;;
                                    comando = EnumCod.INC2_2;
                                }


                                break;
                        }


                        break;
                    case 121:
                        Long dadosLong = decoder.byteToLong(instrucao.getDado())[0];

                        System.out.println(Arrays.toString(vetorLong));
                        regArray[Math.toIntExact(Math.abs(vetorLong[1]))] = Math.toIntExact(dadosLong);
                        System.out.println("------====== REGISTRADORES 121=======-----");
                        System.out.println("A: " + regArray[1]);
                        System.out.println("B: " + regArray[2]);
                        System.out.println("C: " + regArray[3]);
                        System.out.println("D: " + regArray[4]);
                        comando = EnumCod.FINISH;
                        break;
                    case 122:
                        Long dadosLong2 = decoder.byteToLong(instrucao.getDado())[0];
                        System.out.println(dadosLong2);
                        dadosLong2 =(dadosLong2 + regArray[(int) Math.abs(vetorLong[2])]);
                        resultado = encoder.encodificar(new String[]{String.valueOf(dadosLong2)});
                        comando = EnumCod.WRITE;
                        break;
                    case 123:
                        Long dadosLong3 = decoder.byteToLong(instrucao.getDado())[0];
                        dadosLong3 = (long) (dadosLong3 + vetorLong[2]);
                        resultado = encoder.encodificar(new String[]{
                                String.valueOf(dadosLong3)
                        });
                        comando = EnumCod.WRITE;
                        break;

                    case 124:
                        System.out.println("COD 124 INC");
                        Long dadosLong4 = decoder.byteToLong(instrucao.getDado())[0];
                        dadosLong4++;
                        System.out.println(dadosLong4);
                        resultado = encoder.encodificar(new String[]{
                                String.valueOf(dadosLong4)
                        });
                        comando = EnumCod.WRITE;
                        break;

                }

                break;
        }   ////////////////////////////////////////////////////////
        return comando.getValue();
    }

    public void send() {
        if (comando == EnumCod.READ || comando == EnumCod.MOV3_2 ||
                comando == EnumCod.ADD3_2 || comando == EnumCod.ADD4_2 ||
                comando == EnumCod.INC2_2) {
            System.out.println("------====== MEOTODO SEND DA CPU READ =======------");
            System.out.println("PI: " + pi);
            System.out.println("Tamanho: " + tamanho);
            System.out.println("comando: " + comando.toString());

            barramentoControle.sendRam(comando);
            barramentoDados.sendRam(new Instrucao(instrucao.getPayload(), tamanho));
            barramentoEnderecoMemoria.sendRam(pi);
        }


        if (comando == EnumCod.WRITE) {
            System.out.println("------====== MEOTODO SEND DA CPU WRITE =======------");
            System.out.println("PI: " + pi);
            System.out.println("Valor: " + Arrays.toString(resultado));
            System.out.println("Tamanho: " + tamanho);
            System.out.println("comando: " + comando.toString());
            barramentoControle.sendRam(comando);
            barramentoDados.sendRam(new Instrucao(resultado, tamanho));
            barramentoEnderecoMemoria.sendRam(pi);
//            barramento.sendRam(new Instrucao(resultado,pi,resultado.length,comando));
        }

    }

}
