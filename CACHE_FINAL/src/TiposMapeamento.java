public enum TiposMapeamento {
      DIRETO("Mapeamento Direto"),
      ASSOCIATIVO("Mapeamento Associativo"),
      ASSOCIATIVO_CONJUNTO("Mapeamento Associativo Conjunto");

      private String descricao;

      TiposMapeamento(String descricao){
            this.descricao = descricao;
      }

      public String getDescricao() {
            return descricao;
      }

      public static TiposMapeamento[] getTiposMapeamento(){
            TiposMapeamento[] metodosSubstituicao = {
                  DIRETO, 
                  ASSOCIATIVO,
                  ASSOCIATIVO_CONJUNTO
          };
          return metodosSubstituicao;
        }
}
