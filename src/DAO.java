import java.io.*;
import java.util.ArrayList;


public class DAO {

    private static ArrayList<String> instrucoesArrayList = new ArrayList<String>();
    private File f = new File("assembly.txt");

    static {

        File f = new File("assembly.txt");

        FileReader fr = null;
        BufferedReader br = null;

        try {

            if (!f.exists())
                f.createNewFile();

            fr = new FileReader(f);
            br = new BufferedReader(fr);



            String linha;

            while ((linha = br.readLine()) != null) {

                    String p = linha;
                    instrucoesArrayList.add(p);




            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ArrayList<String> pegarArrayListDAO() {
        return instrucoesArrayList;
    }




}