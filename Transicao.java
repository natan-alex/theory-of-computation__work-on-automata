class Transicao{
  public String origem;
  public String destino;
  public String simbolo;

  public Transicao(){
    
  }

  public Transicao(String ori, String dest, String sim){
     this.origem=ori;
     this.destino=dest;
     this.simbolo=sim;
}

 public String getOrigem(){
   return this.origem;
 }

public String getDestino(){
   return destino;
}
public String getSimbolo(){
   return simbolo;
 }

@Override
    public String toString() {
        return "[ "+this.origem + "|" + this.destino + "|"+ this.simbolo + " ]";
    }
  
}