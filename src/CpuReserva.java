import util.EnumCod;

import java.util.Arrays;

public class CpuReserva {

    private int pi;  //ponteiro de instrucao
    private int tamanho; //tamanho da instrucao
    private EnumCod comando;
    private byte [] resultado;
    //registradores
//    int regArray [] = new int[5];// 1 ==A 2 ==B 3 == C 4 == D
    int regArray [] = {0,5,22,8,9};
    //vetores
    private short vetorShort[];
    private int vetorInt[];
    private Long vetorLong[];


    //atributos

    private Barramento barramento;
    private Decoder decoder;
    private Encoder encoder;


    //CONSTRUTOR

    public CpuReserva(Barramento barramento){
        this.barramento = barramento;
        this.encoder = new Encoder(barramento.getLarguraBarramento());
        this.decoder = new Decoder();
    }

    //GET BARRRAMENTO
    public Barramento getBarramento() {
        return barramento;
    }

    public void excute(){






    }
    public void receive(){

        Instrucao aux = barramento.receiveCpu();
        switch (barramento.getLarguraBarramento()){

            case 16:
                switch (aux.getCodigo().getValue()) {

                    case 0: //interrupt

                        pi = aux.getEndereco();
                        tamanho = aux.getTamanho();
                        comando = EnumCod.READ;
                        System.out.println("-======    CPU     ======-");
                        System.out.println("-====== ALTEROU O PI ======-");
                        System.out.println("PI: "+pi);
                        System.out.println("Tamanho: "+tamanho);
                        System.out.println("comando: "+comando.getValue());
                        break;
                    case 1:

                        vetorShort = decoder.byteToShort(aux.getPayload()); //[100] [16] [5]

                        System.out.println("-======     CPU     ======-");
                        System.out.println("-======    VERIFICANDO  TIPO DE OPERAÇÃO   ======-");
                        switch (vetorShort[0]){

                            case 100: //MOV
                                //3 casos do mov:
                                System.out.println("-======    OPERAÇÃO: MOV     ======-");
                                System.out.println("-======    VERIFICANDO ++ || +- ||-+     ======-");
                                System.out.println("-======    Encontrado     ======-");

                                if (vetorShort[1]>=0 && vetorShort[2]>=0){ //mov 0x00 5
                                    System.out.println("-====== MOV CASO 1 ======-");
                                    System.out.println("-======    Endereço de memoria: "+vetorShort[1]);
                                    System.out.println("-======    Valor: "+vetorShort[2]);

                                    resultado = encoder.encodificar(new String[]{String.valueOf(vetorShort[2])});
                                    pi = vetorShort[1];
                                    tamanho=2;
                                    comando = EnumCod.WRITE;

                                }else if (vetorShort[1]>=0 && vetorShort[2]<0) { //mov 0x0000 B
                                    System.out.println("MOV CASO 2");
                                    System.out.println("-======    Endereço de memoria: "+vetorShort[1]);
                                    System.out.println("-======    Valor: "+vetorShort[2]);
                                    resultado = encoder.encodificar(new String[]{String.valueOf(regArray[Math.abs(vetorShort[2])])}) ;
                                    pi = vetorShort[1];
                                    tamanho =2;
                                    comando = EnumCod.WRITE;
                                }else{ //mov B, 0x00
                                    System.out.println("MOV CASO 3");

                                    pi = vetorShort[2];
                                    tamanho= 2;

                                    comando = EnumCod.READ2;

                                }

                                break;
                            case 101: //ADD
                                if (vetorShort[1]<0 && vetorShort[2]>=0) { //add B, 5 //[101] [-2] [5]
                                    System.out.println("-====== ADD CASO 1 ======-");
                                    regArray[Math.abs(vetorShort[1])] = vetorShort[2] + regArray[Math.abs(vetorShort[1])];

                                    System.out.println("------======      CPU      =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: "+regArray[1]);
                                    System.out.println("B: "+regArray[2]);
                                    System.out.println("C: "+regArray[3]);
                                    System.out.println("D: "+regArray[4]);

                                }else if (vetorShort[1]<0 && vetorShort[2]<0){ //ADD B, C
                                    System.out.println("-====== ADD CASO 2 ======-");
                                    regArray[Math.abs(vetorShort[1])] = regArray[Math.abs(vetorShort[1])] + regArray[Math.abs(vetorShort[2])];
                                    System.out.println("------======      CPU      =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: "+regArray[1]);
                                    System.out.println("B: "+regArray[2]);
                                    System.out.println("C: "+regArray[3]);
                                    System.out.println("D: "+regArray[4]);
                                }else if (vetorShort[1]>=0 && vetorShort[2]<0){ //ADD 0x00, B
                                    pi= vetorShort[1];
                                    tamanho= 2;

                                    comando= EnumCod.READ2;
                                }else if (vetorShort[1]>=0 && vetorShort[2]>=0){ //ADD 0x00, 5
                                    pi= vetorShort[1];
                                    tamanho= 2;

                                    comando= EnumCod.READ2;
                                }


                                break;
                            case 102: //IMUL


                                if (vetorShort[1]<0&&vetorShort[2]<0&&vetorShort[3]<0){
                                    System.out.println("-====== IMUL CASO 1 ======-");
                                    regArray[Math.abs(vetorShort[1])] = regArray[Math.abs(vetorShort[2])] * regArray[Math.abs(vetorShort[3])];
                                    System.out.println("------======      CPU      =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: "+regArray[1]);
                                    System.out.println("B: "+regArray[2]);
                                    System.out.println("C: "+regArray[3]);
                                    System.out.println("D: "+regArray[4]);
                                }else if (vetorShort[1]<0&&vetorShort[2]<0&&vetorShort[3]>=0) {
                                    System.out.println("-====== IMUL CASO 2 ======-");
                                    regArray[Math.abs(vetorShort[1])] = regArray[Math.abs(vetorShort[2])] * vetorShort[3];
                                    System.out.println("------======      CPU      =======-----");
                                    System.out.println("------====== REGISTRADORES =======-----");
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                }else if (vetorShort[1]>=0&&vetorShort[2]>=0&&vetorShort[3]<0){
                                    System.out.println("-====== IMUL CASO 3 ======-");
                                    pi = vetorShort[1];
                                    tamanho = 2;
                                    System.out.println("atulizou pi: "+ pi);
                                    System.out.println("tamanho: "+ tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(vetorShort[2] * regArray[Math.abs(vetorShort[3])])
                                    });
                                    comando = EnumCod.WRITE;
                                }else if (vetorShort[1]>=0&&vetorShort[2]<0&&vetorShort[3]<0){
                                    System.out.println("-====== IMUL CASO 4 ======-");
                                    pi = vetorShort[1];
                                    tamanho = 2;
                                    System.out.println("atulizou pi: "+ pi);
                                    System.out.println("tamanho: "+ tamanho);

                                    resultado = encoder.encodificar(new String[]{
                                            String.valueOf(regArray[Math.abs(vetorShort[2])] * regArray[Math.abs(vetorShort[3])])
                                    });
                                    comando = EnumCod.WRITE;


                                }
                                break;
                            case 103:
                                if (vetorShort[1]<0){
                                    System.out.println("-====== inc CASO 1 ======-");
                                    regArray[Math.abs(vetorShort[1])]++;
                                    System.out.println("A: " + regArray[1]);
                                    System.out.println("B: " + regArray[2]);
                                    System.out.println("C: " + regArray[3]);
                                    System.out.println("D: " + regArray[4]);

                                }else if (vetorShort[1]>=0){
                                    System.out.println("-====== inc CASO 2 ======-");
                                    pi=vetorShort[1];
                                    tamanho = 2;
                                    comando = EnumCod.READ2;
                                }



                                break;
                        }


                        break;
                    case 2:
                        if (vetorShort[0]==100&&vetorShort[1]<0 && vetorShort[2]>=0){
                            short dadosShort = decoder.byteToShort(aux.getPayload())[0];

                            System.out.println(Arrays.toString(vetorShort));
                            regArray[Math.abs(vetorShort[1])] =  dadosShort;
                            System.out.println("------======      CPU      =======-----");
                            System.out.println("------====== REGISTRADORES =======-----");
                            System.out.println("A: "+regArray[1]);
                            System.out.println("B: "+regArray[2]);
                            System.out.println("C: "+regArray[3]);
                            System.out.println("D: "+regArray[4]);
                        }else if(vetorShort[0]==101 && vetorShort[1]>=0 && vetorShort[2]<0){

                            short dadosShort = decoder.byteToShort(aux.getPayload())[0];
                            dadosShort = (short) (dadosShort +  regArray[Math.abs(vetorShort[2])]);
                            resultado = encoder.encodificar(new String[]{String.valueOf(dadosShort)});
                            comando = EnumCod.WRITE;
                        }else if(vetorShort[0]==101 && vetorShort[1]>=0 && vetorShort[2]>=0){

                            short dadosShort = decoder.byteToShort(aux.getPayload())[0];
                            dadosShort = (short) (dadosShort +  vetorShort[2]);
                            resultado = encoder.encodificar(new String[]{
                                    String.valueOf(dadosShort)
                            });
                            comando = EnumCod.WRITE;


                        }else if (vetorShort[0]==103){

                            short dadosShort = decoder.byteToShort(aux.getPayload())[0];
                            dadosShort++;
                            resultado = encoder.encodificar(new String []{
                               String.valueOf(dadosShort)
                            });
                            comando = EnumCod.WRITE;
                        }




                        break;
                }

                break;
            case 32:
                switch (aux.getCodigo().getValue()) {

                    case 0: //interrupt

                        pi = aux.getEndereco();
                        tamanho = aux.getTamanho();
                        comando = EnumCod.READ;
                        System.out.println("-======    CPU     ======-");
                        System.out.println("-====== ALTEROU O PI ======-");
                        System.out.println("PI: "+pi);
                        System.out.println("Tamanho: "+tamanho);
                        System.out.println("comando: "+comando.getValue());
                        break;
                    case 1:

                        vetorInt = decoder.byteToInt(aux.getPayload()); //[100] [16] [5]

                        System.out.println("-======     CPU     ======-");
                        System.out.println("-======    VERIFICANDO  TIPO DE OPERAÇÃO   ======-");
                        switch (vetorInt[0]){

                            case 100:
                                //3 casos do mov:
                                System.out.println("-======    OPERAÇÃO: MOV     ======-");
                                System.out.println("-======    VERIFICANDO ++ || +- ||-+     ======-");
                                System.out.println("-======    Encontrado     ======-");

                                if (vetorInt[1]>=0 && vetorInt[2]>=0){ //mov 0x00 5
                                    System.out.println("-======    Endereço de memoria: "+vetorInt[1]);
                                    System.out.println("-======    valor: "+vetorInt[2]);

                                    resultado = encoder.encodificar(new String[]{String.valueOf(vetorInt[2])});
                                    pi = vetorInt[1];
                                    tamanho=4;
                                    comando = EnumCod.WRITE;

                                }else if (vetorInt[1]>=0 && vetorInt[2]<0) { //mov 0x0000 B
                                    //TODO: TERMINAR IMPLEMENTAÇÃO

                                }else{ //mov B, 0x00
                                    //TODO: TERMINAR IMPLEMENTAÇÃO
                                }



                                break;

                        }


                        break;

                }


                break;
            case 64:
                switch (aux.getCodigo().getValue()) {

                    case 0: //interrupt

                        pi = aux.getEndereco();
                        tamanho = aux.getTamanho();
                        comando = EnumCod.READ;
                        System.out.println("-======    CPU     ======-");
                        System.out.println("-====== ALTEROU O PI ======-");
                        System.out.println("PI: "+pi);
                        System.out.println("Tamanho: "+tamanho);
                        System.out.println("comando: "+comando.getValue());
                        break;
                    case 1:

                        vetorLong = decoder.byteToLong(aux.getPayload()); //[100] [16] [5]

                        System.out.println("-======     CPU     ======-");
                        System.out.println("-======    VERIFICANDO  TIPO DE OPERAÇÃO   ======-");
                        switch (String.valueOf(vetorLong[0])){

                            case "100" :
                                //3 casos do mov:
                                System.out.println("-======    OPERAÇÃO: MOV     ======-");
                                System.out.println("-======    VERIFICANDO ++ || +- ||-+     ======-");
                                System.out.println("-======    Encontrado     ======-");

                                if (vetorLong[1]>=0 && vetorLong[2]>=0){ //mov 0x00 5
                                    System.out.println("-======    Endereço de memoria: "+vetorLong[1]);
                                    System.out.println("-======    Valor: "+vetorLong[2]);

                                    resultado = encoder.encodificar(new String[]{String.valueOf(vetorLong[2])});
                                    pi = Integer.parseInt(Long.toString(vetorLong[1]));
                                    tamanho=8;
                                    comando = EnumCod.WRITE;

                                }else if (vetorLong[1]>=0 && vetorLong[2]<0) { //mov 0x0000 B
                                    System.out.println("-======    Endereço de memoria: "+vetorLong[1]);
                                    System.out.println("-======    Valor: "+vetorLong[2]);



                                }else{ //mov B, 0x00
                                    //TODO: TERMINAR IMPLEMENTAÇÃO
                                }



                                break;

                        }


                        break;

                }


                break;
        }
//        switch (aux.getCodigo().getValue()) {
//
//            case 0: //interrupt
//
//                pi = aux.getEndereco();
//                tamanho = aux.getTamanho();
//                comando = util.EnumCod.READ;
//                System.out.println("-======    CPU     ======-");
//                System.out.println("-====== ALTEROU O PI ======-");
//                System.out.println("PI: "+pi);
//                System.out.println("Tamanho: "+tamanho);
//                System.out.println("comando: "+comando.getValue());
//                break;
//            case 1:
//
//                vetorShort = decoder.byteToShort(aux.getPayload()); //[100] [16] [5]
//
//                System.out.println("-======     CPU     ======-");
//                System.out.println("-======    VERIFICANDO  TIPO DE OPERAÇÃO   ======-");
//                switch (vetorShort[0]){
//
//                    case 100:
//                        //3 casos do mov:
//                        System.out.println("-======    OPERAÇÃO: MOV     ======-");
//                        System.out.println("-======    VERIFICANDO ++ || +- ||-+     ======-");
//                        System.out.println("-======    Encontrado     ======-");
//
//                        if (vetorShort[1]>=0 && vetorShort[2]>=0){ //mov 0x00 5
//                            System.out.println("-======    Endereço de memoria: "+vetorShort[1]);
//                            System.out.println("-======    Valor: "+vetorShort[2]);
//
//                            resultado = encoder.encodificar(new String[]{String.valueOf(vetorShort[2])});
//                            pi = vetorShort[1];
//                            tamanho=2;
//                            comando = util.EnumCod.WRITE;
//
//                        }else if (vetorShort[1]>=0 && vetorShort[2]<0) { //mov 0x0000 B
//                            //TODO: TERMINAR IMPLEMENTAÇÃO
//
//                        }else{ //mov B, 0x00
//                            //TODO: TERMINAR IMPLEMENTAÇÃO
//                        }
//
//
//
//                            break;
//
//                }
//
//
//                break;
//
//        }


    }
    public void send() {
        if (comando==EnumCod.READ2) {
            System.out.println("------====== MEOTODO SEND DA CPU READ =======------");
            System.out.println("PI: " + pi);
            System.out.println("Tamanho: " + tamanho);
            System.out.println("comando: " + comando.toString());
            barramento.sendRam(new Instrucao(tamanho, pi, comando));
        }

        if (comando==EnumCod.READ) {
            System.out.println("------====== MEOTODO SEND DA CPU READ =======------");
            System.out.println("PI: " + pi);
            System.out.println("Tamanho: " + tamanho);
            System.out.println("comando: " + comando.toString());
            barramento.sendRam(new Instrucao(tamanho, pi, comando));
        }
        if (comando==EnumCod.WRITE){
            System.out.println("------====== MEOTODO SEND DA CPU WRITE =======------");
            System.out.println("PI: " + pi);
            System.out.println("Valor: " + Arrays.toString(resultado));
            System.out.println("Tamanho: " + tamanho);
            System.out.println("comando: " + comando.toString());

            barramento.sendRam(new Instrucao(resultado,pi,resultado.length,comando));
        }

    }




    //
//            case 0: //interrupt
//
//                pi = aux.getEndereco();
//                tamanho = aux.getTamanho();
//                comando = util.EnumCod.READ;
//
//                break;
//            case 1: //READ
//
//
//                if (barramento.getLarguraBarramento()==16){
//                    vetorShort = decoder.byteToShort(aux.getPayload());
//
//                    switch (vetorShort[0]){
//                        case 100: //mov 0X000 5
//
//                            break;
//                        case 101: //add B, C
//
//                            regArray[Math.abs(vetorShort[1])] = regArray[Math.abs(vetorShort[2])];
//
//                            break;
//                        case 102: //imul
//
//                            break;
//                        case 103: //inc
//                            ;
//                            //TODO: PERGUNTAR COMO O VALOR DO INC E RECEBIDO PELA CPU PELA SEGUNDA VEZ
//                            //SEM QUE ENCERRE A EXECUCAO  DO METODO
//
//
//                            break;
//
//
//                    }
//
//
//                }else if (barramento.getLarguraBarramento()==32){
//                    vetorInt = decoder.byteToInt(aux.getPayload());
//
//                }else if(barramento.getLarguraBarramento()==64){
//                    vetorLong = decoder.byteToLong(aux.getPayload());
//
//                }
//
//
//
//
//                break;
//        }
//
//
//    }
    //        switch (aux.getCodigo().getValue()){
    //
    //
    //        Instrucao aux = barramento.receiveCpu();
//    public void receive() {

//





}
