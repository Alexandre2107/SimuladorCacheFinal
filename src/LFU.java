public class LFU extends Mapeamentos {
	public LFU(String arquivoConfiguracao, String arquivoDados, TiposMapeamento tipoMapeamento) {
		super(arquivoConfiguracao, arquivoDados, tipoMapeamento);
	}

	public static int Substituir(ValoresAssociativo[] posicoes, String valorTag, ValoresAssociativo end, int hit) {
		int op = 0;
		int hitCache = hit;
		int menor = 0;
		
		for (int j = 0; j < posicoes.length; j++) {
			if (posicoes[j].getTag().equals(valorTag)) {
				op = 1;
				hitCache++;
				break;
			} else {
				op = 0;
			}
		}
		if (op == 0) {
			for (int j = 0; j < posicoes.length; j++) {
				if (posicoes[j].getAcesso() <= menor) {
					menor = j;
					break;
				}
			}
			if (posicoes[menor].getTag().equals(end.getTag())) {
				hitCache++;
				posicoes[menor].setAcesso(1);
			} else {
				posicoes[menor].setTag(end.getTag());
				posicoes[menor].setAcesso(0);
			}
		}
		return hitCache;
	}
}
