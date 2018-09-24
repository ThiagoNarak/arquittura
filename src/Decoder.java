import java.nio.ByteBuffer;

public class Decoder {

     public short[] byteToShort(byte payload[]){
        int contador =0;
         short vetor[] = new short[payload.length/2];
        for (int i = 0; i < payload.length ; i= i+ 2) {
            ByteBuffer wrapped = ByteBuffer.wrap(new byte[]{
                    payload[i],
                    payload[i+1]
            });

            short num = wrapped.getShort();
            vetor[contador] = num;

            contador++;
        }

        return vetor;
    }
    public int[] byteToInt(byte payload[]){
        int contador =0;
        int vetor[] = new int[payload.length/4];
        for (int i = 0; i < payload.length ; i= i+ 4) {
            ByteBuffer wrapped = ByteBuffer.wrap(new byte[]{
                    payload[i],
                    payload[i+1],
                    payload[i+2],
                    payload[i+3],

            });
            int num = wrapped.getInt();
            vetor[contador] = num;
            contador++;

        }
       return vetor;
    }
    public Long[] byteToLong(byte payload[]){
        int contador =0;
        Long vetor[] = new Long[payload.length/8];
        for (int i = 0; i < payload.length ; i= i+ 8) {
            ByteBuffer wrapped = ByteBuffer.wrap(new byte[]{
                    payload[i],
                    payload[i+1],
                    payload[i+2],
                    payload[i+3],
                    payload[i+4],
                    payload[i+5],
                    payload[i+6],
                    payload[i+7]

            });
            Long num = wrapped.getLong();
            vetor[contador] = num;
            contador++;


        }
        return vetor;
    }




}
