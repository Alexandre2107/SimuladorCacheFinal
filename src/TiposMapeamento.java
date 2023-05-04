public enum TiposMapeamento {
      DIRETO("Direto"),
      ASSOCIATIVO("Associativo"),
      ASSOCIATIVO_CONJUNTO("Associativo Conjunto");

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
