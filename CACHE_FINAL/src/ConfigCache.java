import java.util.List;

public class ConfigCache {

  private long tamPrincipal;
  private long palavra;
  private long tamCache;
  private long linha;

  private int[] dadosArquivo = new int[4];

  public ConfigCache(String caminhoArquivo) {
    parseArquivo(caminhoArquivo);

  }
  
  private void parseArquivo(String caminhoArquivo){
    List<String> config = FileManager.stringReader(caminhoArquivo);
    
    String[] memoryConfig = config.get(0).split("[ #@_\\/.*;]");
    String[] wordConfig = config.get(1).split("[ #@_\\/.;]");
    String[] cacheConfig = config.get(2).split("[ #@_\\/.;]");
    String[] lineConfig = config.get(3).split("[ #@_\\/.*;]");
    
    tamPrincipal = convertToBits(Long.parseLong(memoryConfig[2]), memoryConfig[3]);
    palavra = convertToBits(Long.parseLong(wordConfig[2]), wordConfig[3]);
    tamCache = convertToBits(Long.parseLong(cacheConfig[2]), cacheConfig[3]);
    linha = convertToBits(Long.parseLong(lineConfig[2]), lineConfig[3]);
    
    // for (int i = 0; i < config.size(); i++) {

    //   String[] configText = config.get(i).split(" ");
    //   String unidadeMedida = configText[3];
    //   int tamanho = Integer.parseInt(configText[2]);
  //     switch (unidadeMedida) {
  //       case "GB;":
  //         dadosArquivo[i] = tamanho * 1024 * 1024;
  //         break;
  //       case "KB;":
  //         dadosArquivo[i] = tamanho * 1024;
  //         break;
  //       case "B;":
  //         dadosArquivo[i] = tamanho;
  //         break;
  //       case "pal;":
  //       dadosArquivo[i] = tamanho;
  //         break;          
  //       default:
  //         break;
  //     }
  //   }
  // }


  }

  protected Long convertToBits(Long bits, String unit) {
    switch (unit) {
        case "KB":
            bits = bits * 1024;
            break;
        case "MB":
            bits = bits * 1024 * 1024;
            break;
        case "GB":
            bits = bits * 1024 * 1024 * 1024;
            break;
    }
    return bits;
}

  public long getTamPrincipal() {
    return tamPrincipal;
  }

  public void setTamPrincipal(long tamPrincipal) {
    this.tamPrincipal = tamPrincipal;
  }

  public long getPalavra() {
    return palavra;
  }

  public void setPalavra(long palavra) {
    this.palavra = palavra;
  }

  public long getTamCache() {
    return tamCache;
  }

  public void setTamCache(long tamCache) {
    this.tamCache = tamCache;
  }

  public long getLinha() {
    return linha;
  }

  public void setLinha(long linha) {
    this.linha = linha;
  }

}
