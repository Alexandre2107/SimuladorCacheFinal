public class FIFO extends Mapeamentos {
	public FIFO(ConfigCache configCache) {
		super(configCache);
	}

	public static int Substituir(ValoresAssociativo[] posicoes, String valorTag, int hit, int id) {
		int op = 0;
		int hitCache = hit;
		
		for (int j = 0; j < posicoes.length; j++) {
			if (posicoes[j].getTag().equals(valorTag)) {
				op = 1;
				hitCache++;
				break;
			} else {
				op = 0;
			}
		}
		if (id >= posicoes.length) {
			id = 0;
		}
		
		if (op == 0) {
			if (posicoes[id].getTag().equals(valorTag)) {
				hitCache++;
			} else {
				posicoes[id].setTag(valorTag);
			}
		}
		return hitCache;
	}
}