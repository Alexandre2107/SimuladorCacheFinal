public enum MetodosSubstituicao {
  ALEATORIO("Aleatório"),
  LRU("LRU"),
  FIFO("FIFO"),
  LFU("LFU");

  private String descricao;

  MetodosSubstituicao(String descricao){
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }

  public static MetodosSubstituicao[] getMetodos(){
      MetodosSubstituicao[] metodosSubstituicao = {
        ALEATORIO, 
        LRU,
        FIFO,
        LFU
    };
    return metodosSubstituicao;
  }
}
