import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by xigao on 6/22/2016.
 */
public class File2json {
    public static void main(String[] args) throws IOException {
        test();
    }

    private static void test() throws IOException {

        int rowNumberSize = 1;
        String dirPath = "C:\\Users\\xigao\\Desktop\\test\\";
        //String sourceFileName = "20160602T140012235-6924f7995ac846c89779d19ed0df931f";

        Files.walk(Paths.get("C:\\Users\\xigao\\Desktop\\test\\")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                System.out.println(filePath);
                Path p = Paths.get(String.valueOf(filePath));
                String sourceFileName = p.getFileName().toString();

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(String.valueOf(filePath)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int lines = 0;

                try {
                    while (reader.readLine() != null) lines++;
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                for(int i=1;i<=lines;i++){
                    int startRowNumber = i * rowNumberSize;
                    splitFile(startRowNumber,rowNumberSize,String.valueOf(filePath),sourceFileName);
                }
            }
        });


    }

    public static void splitFile(int startRowNumber, int rowNumberSize,
                                 String dirPath, String sourceFileName) {

        File inputFile = new File(dirPath);

        if(inputFile==null || !inputFile.exists()){
            throw new RuntimeException("File not exist, file path: "+inputFile.getAbsolutePath());
        }

        System.out.println("Start Split File  "+sourceFileName);

        String suffix = ".json";
        String realFileName = sourceFileName;
        int index = sourceFileName.lastIndexOf(".");


        int serilizeNumber = 1;
        String outputPath = dirPath+"_"+serilizeNumber + suffix;
        File outputFile = new File(outputPath);

        while(outputFile.exists()){

            serilizeNumber++;

            outputPath = dirPath+"_"+serilizeNumber + suffix;
            outputFile = new File(outputPath);
        }

        System.out.println("The File After Split "+outputPath);

        int currentIndex = 0;
        int writeNumber = 0;

        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader br = null;

        OutputStream out = null;
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;

        try {
            in = new FileInputStream(inputFile);
            reader = new InputStreamReader(in,"utf-8");
            br = new BufferedReader(reader);


            out = new FileOutputStream(outputFile);
            writer = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(writer);

            String line = null;
            String newLine = null;


            while((line=br.readLine())!=null){

                currentIndex++;

                if(writeNumber>=rowNumberSize){
                    break;
                }

                if(currentIndex>=startRowNumber){
                    newLine = line;

                    bw.write(newLine);
                    bw.newLine();

                    writeNumber++;
                }
            }

            bw.flush();

            System.out.println("File Split is Complete...");

        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
