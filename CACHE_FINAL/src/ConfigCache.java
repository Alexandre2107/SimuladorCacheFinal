import java.util.List;

public class ConfigCache {

  private int tamPrincipal;
  private int palavra;
  private int tamCache;
  private int linha;

  private int[] dadosArquivo = new int[4];

  public ConfigCache(String caminhoArquivo) {
    parseArquivo(caminhoArquivo);
    
    tamPrincipal =  dadosArquivo[0];
    palavra =  dadosArquivo[1];  
    tamCache =  dadosArquivo[2];  
    linha = (dadosArquivo[2] / (dadosArquivo[1] * dadosArquivo[3]));
  }

  private void parseArquivo(String caminhoArquivo){
    List<String> config = FileManager.stringReader(caminhoArquivo);
    
    for (int i = 0; i < config.size(); i++) {

      String[] configText = config.get(i).split(" ");
      String unidadeMedida = configText[3];
      int tamanho = Integer.parseInt(configText[2]);

      switch (unidadeMedida) {
        case "GB;":
          dadosArquivo[i] = tamanho * 1024 * 1024 * 8;
          break;
        case "KB;":
          dadosArquivo[i] = tamanho * 1024 * 8;
          break;
        case "B;":
          dadosArquivo[i] = tamanho * 8;
          break;
        case "pal;":
        dadosArquivo[i] = tamanho;
          break;          
        default:
          break;
      }
    }
  }
 
  public int getTamPrincipal() {
    return tamPrincipal;
  }
  public void setTamPrincipal(int tamPrincipal) {
    this.tamPrincipal = tamPrincipal;
  }
  public int getPalavra() {
    return palavra;
  }
  public void setPalavra(int palavra) {
    this.palavra = palavra;
  }
  public int getTamCache() {
    return tamCache;
  }
  public void setTamCache(int tamCache) {
    this.tamCache = tamCache;
  }
  public int getLinha() {
    return linha;
  }
  public void setLinha(int linha) {
    this.linha = linha;
  }

}
