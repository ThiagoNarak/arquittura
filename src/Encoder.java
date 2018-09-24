import util.EnumCod;

import java.nio.ByteBuffer;

public class Encoder {


private int larguraBarramento;

    public Encoder(int larguraBarramento) {
        this.larguraBarramento = larguraBarramento;
    }




    public byte[] encodificar(String []comando){    //separar entre mov,inc,imul e add e
                                                                //e dentro disso separar conforme o tamanho de bits
        switch (comando[0]){
            case "mov":

                int vetorIntMov[] = new int[3];
                vetorIntMov[0] = EnumCod.MOV.getValue();//100

                vetorIntMov[1] = converterCmd2_3(comando[1]);
                vetorIntMov[2] = converterCmd2_3(comando[2]);

                if (larguraBarramento==16){

                    return shortToByte(vetorIntMov);
                }else if (larguraBarramento==32){

                   return intToByte(vetorIntMov);
//                   return shortToByte(vetorIntMov);

                }else {

                    return longToByte(new Long[]{Long.valueOf(vetorIntMov[0]), Long.valueOf(vetorIntMov[1]), Long.valueOf(vetorIntMov[2])});
//                   return shortToByte(vetorIntMov);
                }




            case "inc":

                int vetorIntInc[] = new int[2];

                vetorIntInc[0] = EnumCod.INC.getValue(); //103
                vetorIntInc[1] = converterCmd2_3(comando[1]);

                if (larguraBarramento==16){

                    return  shortToByte(vetorIntInc);

                }else if (larguraBarramento==32){

                    return intToByte(vetorIntInc);

                }else {

                    return longToByte(new Long[]{Long.valueOf(vetorIntInc[0]), Long.valueOf(vetorIntInc[1])});
                }


            case "imul":

                int vetorIntImul []= new int[4];
                vetorIntImul[0] = EnumCod.IMUL.getValue(); //102
                vetorIntImul[1] = converterCmd2_3(comando[1]);
                vetorIntImul[2] = converterCmd2_3(comando[2]);
                vetorIntImul[3] = converterCmd2_3(comando[3]);


                if (larguraBarramento==16){

                    return shortToByte(vetorIntImul);

                }else if (larguraBarramento==32){
                    return intToByte(vetorIntImul);

                }else {

                     return longToByte(new Long[]{

                            Long.valueOf(vetorIntImul[0]),
                            Long.valueOf(vetorIntImul[1]),
                            Long.valueOf(vetorIntImul[2]),
                            Long.valueOf(vetorIntImul[3])});
                }


            case "add":

                int vetorIntAdd[] = new int[3];
                vetorIntAdd[0] = EnumCod.ADD.getValue();//100

                vetorIntAdd[1] = converterCmd2_3(comando[1]);
                vetorIntAdd[2] = converterCmd2_3(comando[2]);

                if (larguraBarramento==16){

                    return shortToByte(vetorIntAdd);

                }else if (larguraBarramento==32){

                    return intToByte(vetorIntAdd);


                }else {
                    return longToByte(new Long[]{Long.valueOf(vetorIntAdd[0]), Long.valueOf(vetorIntAdd[1]), Long.valueOf(vetorIntAdd[2])});
                }



            default:
                if (larguraBarramento==16)return shortToByte(new int[]{Integer.parseInt(comando[0])});
                else if (larguraBarramento==32) return intToByte(new int[]{Integer.parseInt(comando[0])});
                else if (larguraBarramento==64) return longToByte(new Long[]{Long.valueOf(comando[0])});
                return null;

        }

    }


// metodos de conversao para byte:
    private  byte[] shortToByte(int vetor[]){

        int cont =0;
        byte bytes[] = new byte[vetor.length*2];
        for (int i = 0; i < vetor.length; i++) {
            for (int j = 0; j < 2; j++) {

                    bytes [cont] = ByteBuffer.allocate(2).putShort((short) vetor[i]).array()[j];


                cont++;

            }


        }
        return bytes;

    }
    private  byte[] intToByte(int vetor[]){

        byte bytes[] = new byte[vetor.length*4];

        int cont =0;
        for (int i = 0; i <vetor.length ; i++) {
            for (int j = 0; j < 4; j++) {

                bytes[cont] = ByteBuffer.allocate(4).putInt(vetor[i]).array()[j];


                cont++;
            }
        }
       return bytes;



    }
    private  byte[] longToByte(Long[] vetor){

        byte bytes[] = new byte[vetor.length*8];

        int cont =0;
        for (int i = 0; i <vetor.length ; i++) {
            for (int j = 0; j < 8; j++) {

                bytes[cont] = ByteBuffer.allocate(8).putLong(vetor[i]).array()[j];


                cont++;
            }
        }
        return bytes;

    }


    private int converterCmd2_3(String cmd){

        char [] offsets = {'A','B','C','D','P'};

        //checa se é um numero caso seja retornara ja convertido

        //verifica se possui 0x
        if (cmd.contains("0x")) {
            return hexToInt(cmd);

        }else {
            //devolve o codigo do offset A-D  com negativo
            for (int i = 0; i < offsets.length; i++) {
                if (cmd.charAt(0)==offsets[i]){
                    return 0-(i+1);
                }

            }
        }
        return Integer.parseInt(cmd);



    }




    private int hexToInt(String cmd){


        return Integer.parseInt(cmd.substring(2, cmd.length()), 16)+larguraBarramento*2;
    }
}
//0x0001 == 65

//    private static byte[] intToByte64(long[] valor) {
//        int mask0_8 = 0xFF;
//        int mask8_16 = 0xFF00;
//        int mask16_24 = 0xFF0000;
//        int mask24_32 = 0xFF000000;
//        long mask32_40 = 0xFF00000000;
//
//
//        System.out.println(Integer.toBinaryString(mask24_32));
//        System.out.println(mask32_40);
//        return null;
//
//    }
//    private  byte[] intToByte32(int[] valor) {
//        int mask0_8 = 0xFF;
//        int mask8_16 = 0xFF00;
//        int mask16_24 = 0xFF0000;
//        int mask24_32 = 0xFF000000;
//
//        byte bytes[] = new byte[12];
//        int contador=0;
//        for (int j = 0; j < 3; j++) {
//
//            contador= contador+3;
//            for (int i = 3; i!=0 && i != 4 && i != 8; i--) {
//
//
//            }
//        }
//        for (int j = 0; j < 3; j++) {
//            int contador=0;
//            int v=0;
//            if(j==0){
//                v=3;
//            }else if (j==1){
//                v=7;
//            }else if (j==2){
//                v=11;
//            }
//            for ( int i = v; i!=-1; i--) {
//                System.out.println("i: "+i);
//                System.out.println("j: "+j);
//
//                if (contador==0){
//                    bytes[i] = (byte) (valor[j] & mask0_8);
//
//                }else if (contador==1){
//                    bytes[i] = (byte) ((valor[j]& mask8_16) >> 8);
//
//                }else if (contador==2){
//                    bytes[i] = (byte) ((valor[j]& mask16_24) >> 16);
//
//                }else if (contador==3){
//                    bytes[i] = (byte) ((valor[j]& mask24_32) >> 24);
//
//                    break;
//                }
//
//                contador++;
//            }
//        }
//
//        bytes[3] = (byte) (valor[0] & mask0_8);
//        bytes[2] = (byte) ((valor[0]& mask8_16) >> 8);
//        bytes[1] = (byte) ((valor[0]& mask16_24) >> 16);
//        bytes[0] = (byte) ((valor[0]& mask24_32) >> 24);
//
//        bytes[7] = (byte) (valor[1] & mask0_8);
//        bytes[6] = (byte) ((valor[1]& mask8_16) >> 8);
//        bytes[5] = (byte) ((valor[1]& mask16_24) >> 16);
//        bytes[4] = (byte) ((valor[1]& mask24_32) >> 24);
//
//        bytes[11] = (byte) (valor[2] & mask0_8);
//        bytes[10] = (byte) ((valor[2]& mask8_16) >> 8);
//        bytes[9] = (byte) ((valor[2]& mask16_24) >> 16);
//        bytes[8] = (byte) ((valor[2]& mask24_32) >> 24);
//
//
//
////        System.out.println(Integer.toBinaryString(bytes[0]));
////        System.out.println(Integer.toBinaryString(bytes[1]));
////        System.out.println(Integer.toBinaryString(bytes[2]));
////        System.out.println(Integer.toBinaryString(bytes[3]));
////        System.out.println(Integer.toBinaryString(bytes[4]));
////        System.out.println(Integer.toBinaryString(bytes[5]));
////        System.out.println(Integer.toBinaryString(bytes[6]));
////        System.out.println(Integer.toBinaryString(bytes[7]));
////        System.out.println(Integer.toBinaryString(bytes[8]));
////        System.out.println(Integer.toBinaryString(bytes[9]));
////        System.out.println(Integer.toBinaryString(bytes[10]));
////        System.out.println(Integer.toBinaryString(bytes[11]));
//        return null;
//    }

//    public byte[] intToByte16(int valor[]){
//
//
//        int mask0_8 = 0xFF;
//        int mask8_16 = 0xFF00;
//        byte bytes[] = new byte[6];
//
//        int j=0;
//        for (int i = 1; i != 7 ; i--) {
//            System.out.println("i: "+i);
//            System.out.println("j: "+j);
//
//            if (i%2==0){
//                bytes[i] = (byte) ((valor[j]& mask8_16) >> 8);
//                i=i+4;
//                j++;
//
//            }else{
//                bytes[i] = (byte) (valor[j] & mask0_8);
//
//            }
//        }
//        bytes[1] = (byte) (valor[0] & mask0_8);
//        bytes[0] = (byte) ((valor[0]& mask8_16) >> 8);
//
//        bytes[3] = (byte) (valor[1] & mask0_8);
//        bytes[2] = (byte) ((valor[1] & mask8_16) >> 8) ;
//
//        bytes[5] = (byte) (valor[2] & mask0_8);
//        bytes[4] = (byte) ((valor[2] & mask8_16) >> 8) ;
//
//
//    }
