import util.EnumRegex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private int larguraBarramento;

    public Parser(int larguraBarramento) {
        this.larguraBarramento = larguraBarramento;
    }

    public String[] validando(String entrada) {  //separando a string

        for (EnumRegex s : EnumRegex.values()) {
            Pattern r = Pattern.compile(s.getRegex());
            Matcher m = r.matcher(entrada);

            if (m.find()) {
                String quebraNome[] = m.group(0).split("[,\\s]+");
                return validarPasso2(quebraNome);

            }
        }
    return null;
    }
    private String[] validarPasso2(String quebraNome[]){
        switch (quebraNome[0]){

            case "mov":
                if (quebraNome[1].length()==1||quebraNome[1].length()==(larguraBarramento/4)+2){
                    if (quebraNome[2].length()==1||quebraNome[2].length()==(larguraBarramento/4)+2||Integer.valueOf(quebraNome[2])<=Math.pow(2,larguraBarramento-1)){
                        return quebraNome;
                    }else {
                        System.out.println("Não achou");

                    }
                }
                break;
            case  "add":
                if (quebraNome[1].length()==1||quebraNome[1].length()==(larguraBarramento/4)+2){
                    if (quebraNome[2].length()==1||Integer.valueOf(quebraNome[2])<=Math.pow(2,larguraBarramento-1)){
                        return quebraNome;
                    }else {
                        System.out.println("Não achou");

                    }
                }
                break;
            case "imul":
                if (quebraNome[1].length()==1||quebraNome[1].length()==(larguraBarramento/4)+2){
                    if (quebraNome[2].length()==1||Integer.valueOf(quebraNome[2])<=Math.pow(2,larguraBarramento-1) && quebraNome[3].length()==1||Integer.valueOf(quebraNome[3])<=Math.pow(2,larguraBarramento-1)){
                        return quebraNome;
                    }else {
                        System.out.println("Não achou");

                    }
                }
                break;
            case "inc":
                if (quebraNome[1].length()==1||quebraNome[1].length()==(larguraBarramento/4)+2){
                    return quebraNome;
                }else {
                    System.out.println("Não achou");

                }
                break;

            default:
                return null;

        }
        return null;
    }
}
