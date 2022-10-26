import java.io.*;
import java.util.List;
import java.util.ArrayList;




class Main {
  public static void main(String[] args) {
    ArrayList <String> status = new ArrayList<>();
    ArrayList <Transicao> trans = new ArrayList<>();
    try{

     BufferedReader br = new BufferedReader(new FileReader("ex1.1a.jff"));

     while(br.ready()){
        String linha = br.readLine();
        String id;
        if(linha.contains("state id")){
          id= linha.substring(linha.indexOf("\"")+1,linha.indexOf("\"")+2);
          status.add(id);
          //System.out.println(linha);
        }
       if(linha.contains("<transition>")){
         String linha1 = br.readLine();
         String origem;
origem= linha1.substring(linha1.indexOf(">")+1,linha1.indexOf(">")+2);

         String linha2 = br.readLine();
String destino;
destino= linha2.substring(linha2.indexOf(">")+1,linha2.indexOf(">")+2);

         String linha3 = br.readLine();
         String simbolo;
simbolo= linha3.substring(linha3.indexOf(">")+1,linha3.indexOf(">")+2);
        //System.out.println(linha);
        //System.out.println(linha1);
        //System.out.println(linha2);
        //System.out.println(linha3);
        //System.out.println(origem);
        //System.out.println(destino);
        //System.out.println(simbolo);
        Transicao tran = new Transicao(origem, destino, simbolo);
         trans.add(tran);
       }
     }
    System.out.println(status);



      
    
 System.out.println(trans);



     br.close();
  }catch(IOException ioe){
     ioe.printStackTrace();
  }
       }
}
