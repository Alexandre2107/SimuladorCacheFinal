
import java.util.ArrayList;

public class Mapeamentos {
	protected long tamanhoCache;
	protected long tamanhoMemoria;
	protected long palavra;
	protected long nLinhas;
	protected int logLinha;
	protected int count;
	protected int missCache;
	protected int hitCache;
	private TiposMapeamento tipoMapeamento;



	public String getInfo() {
		String x = Long.toString(count);
		String y = Double.toString(missCache);
		String z = Long.toString(hitCache);
		String a = Double.toString((hitCache * 100) / count);

		return "Número de Acessos: " + x + '\n' + "MissCache: " + y + '\n' + "HitCache: " + z  + '\n' + "HitRate: " + a + " %";
}

	public Mapeamentos(ConfigCache configCache) {
		this.tamanhoMemoria = configCache.getTamPrincipal();
		this.tamanhoCache = configCache.getTamCache();
		this.palavra = configCache.getPalavra();
		this.nLinhas = configCache.getLinha();
		// this.logLinha = (int) (Math.log(nLinhas) / Math.log(2));
	}

	public void executar(){
		direto();
	}

	public void executarAssociacao(MetodosSubstituicao metodosSubstituicao){
			switch(this.tipoMapeamento){
				case ASSOCIATIVO:	
					Associativo(metodosSubstituicao);			
					break;
				case ASSOCIATIVO_CONJUNTO:				
					AssociativoConjunto(metodosSubstituicao);
					break;						
				default:
					break;
		}
	}

	public TiposMapeamento getTipoMapeamento() {
		return tipoMapeamento;
	}

	public void setTipoMapeamento(TiposMapeamento tipoMapeamento) {
		this.tipoMapeamento = tipoMapeamento;
	}

	private void direto() {
		// Calculo de quantos bits a tag tem
		Long enderecoEmbits =  (long) ((Math.log(tamanhoMemoria / palavra) / Math.log(2)));
		Long linhaEmBits = (long) (Math.log(nLinhas) / Math.log(2));
		Long palavraEmBits = (long) (Math.log(palavra)/Math.log(2));

		Long tag = enderecoEmbits - palavraEmBits  - linhaEmBits;

		String[] posicoes = new String[(int) nLinhas];
		String valorTag;
		String valorLinha;
		long endereco = 0;
		Long lCache;
		count = 0;
		missCache = 0;
		hitCache = 0; 

		ArrayList<String> teste = FileManager.stringReader("D:/CACHE_FINAL/CACHE_FINAL/data/teste_3D.txt");
		for (String linha : teste) {
			long acesso = Long.parseLong(linha);

			endereco = (tag + palavraEmBits + linhaEmBits);

			
			String stringBin = intToBinaryString(acesso, endereco);

			if (stringBin == null) {
				System.out.println("cu");
			}
			valorTag = stringBin.substring(0, tag.intValue());

			valorLinha = stringBin.substring(tag.intValue(),  linhaEmBits.intValue() + tag.intValue());

			// System.out.println(valorTag);
			// System.out.println(valorLinha);

			lCache = Long.parseLong(valorLinha, 2);


			if (posicoes[lCache.intValue()] != null &&  posicoes[lCache.intValue()].equals(valorTag)) {
				hitCache++;
			} else {
				posicoes[lCache.intValue()] = valorTag;
				missCache++;
			}
			count++;
		}
		mostrarInformacoes(count, hitCache, missCache);
	}

	private int chamarSubstituicao(MetodosSubstituicao metodosSubstituicao, ValoresAssociativo[] posicoes, String valorTag, int hitCache, int nLinhas, int id, ValoresAssociativo end){
		switch(metodosSubstituicao){
			case ALEATORIO:
				hitCache = Aleatorio.Substituir(posicoes, valorTag, hitCache, nLinhas);
				break;
			case FIFO:
				hitCache = FIFO.Substituir(posicoes, valorTag, hitCache, id);
				break;
			case LFU:
				hitCache = LFU.Substituir(posicoes, valorTag, end, hitCache);
				break;
			case LRU:
				hitCache = LRU.Substituir(posicoes, valorTag, hitCache);
				break;
			default:
				break;
		}
		return hitCache;
	}

	public void Associativo(MetodosSubstituicao opcao) {
		int tag = (int) (Math.log(tamanhoMemoria / palavra) / Math.log(2)) - 2;

		ValoresAssociativo[] posicoes = new ValoresAssociativo[(int) nLinhas];
		String valorTag;
		int i = 0, valor = 0, id = 0;
		long endereco = 0;
		count = 0;
		hitCache = 0;
		missCache = 0;

		ArrayList<String> teste = FileManager.stringReader("D:/CACHE_FINAL/CACHE_FINAL/data/teste_3D.txt");
		for (String linha : teste) {
			long acesso = Integer.parseInt(linha);

			endereco = tag + 2;

			// int bin[] = intToBinary(acesso, endereco);

			String stringBin = intToBinaryString(acesso, endereco);

			valorTag = stringBin.substring(0, tag);
			ValoresAssociativo end = new ValoresAssociativo(valorTag, valor);
			if (i < nLinhas) {
				posicoes[i] = end;
			} else {
				if (opcao == MetodosSubstituicao.LRU) {
					hitCache = LRU.Substituir(posicoes, valorTag, hitCache);
				} else if (opcao == MetodosSubstituicao.FIFO) {
					hitCache = FIFO.Substituir(posicoes, valorTag, hitCache, id);
					id++;
				} else if (opcao == MetodosSubstituicao.LFU) {
					hitCache = LFU.Substituir(posicoes, valorTag, end, hitCache);
				} else if (opcao == MetodosSubstituicao.ALEATORIO) {
					hitCache = Aleatorio.Substituir(posicoes, valorTag, hitCache, nLinhas);
				}
			}
			count++;
			i++;
		}
		missCache = (int) (count - hitCache);
		mostrarInformacoes(count, hitCache, missCache);
	}

	public void AssociativoConjunto(MetodosSubstituicao opcao) {
		long endereco = 0;
		int i = 0, j = 0, nBloco = 0, blocos = 1, tag = 0, logBlocos = 0, valor = 0, id = 0;
		count = 0;
		missCache = 0;
		hitCache = 0;
		blocos = (int) (tamanhoCache / nLinhas);
		tag = (int) ((int) (Math.log(tamanhoMemoria / palavra) / Math.log(2)) + 1 - (Math.log(blocos) / Math.log(2))
		- 2);
		logBlocos = (int) (Math.log(blocos) / Math.log(2));

		ValoresAssociativo[][] posicoes = new ValoresAssociativo[blocos][(int) tamanhoCache];
		String valorTag;
		String valorBloco;

		ArrayList<String> teste = FileManager.stringReader("D:/SimuladorCacheOAC/SimuladorCache_AlexandreRodrigues/data/teste_1.txt");
		for (String linha : teste) {
			long acesso = Integer.parseInt(linha);

			endereco = tag + logBlocos + 2;
			// int bin[] = intToBinary(acesso, endereco);

			String stringBin = intToBinaryString(acesso, endereco);

			valorTag = stringBin.substring(0, tag);
			valorBloco = stringBin.substring(tag, logBlocos + tag);
			nBloco = Integer.parseInt(valorBloco, 2);

			ValoresAssociativo end = new ValoresAssociativo(valorTag, valor);
			for (j = 0; j < tamanhoCache; j++) {
				if (posicoes[nBloco][j] == null) {
					posicoes[nBloco][j] = end;
				}else {
					i++;
				}
			}
			if (i != 0) {
				if (opcao == MetodosSubstituicao.LRU) {
					hitCache = LRU.Substituir(posicoes[nBloco], valorTag, hitCache);
				} else if (opcao == MetodosSubstituicao.FIFO) {
					hitCache = FIFO.Substituir(posicoes[nBloco], valorTag, hitCache, id);
					id++;
				} else if (opcao == MetodosSubstituicao.LFU) {
					hitCache = LFU.Substituir(posicoes[nBloco], valorTag, end, hitCache);
				} else if (opcao == MetodosSubstituicao.ALEATORIO) {
					hitCache = Aleatorio.Substituir(posicoes[nBloco], valorTag, hitCache, tamanhoCache);
				}
			}

			count++;
		}

		missCache = (int) (count - hitCache);
		mostrarInformacoes(count, hitCache, missCache);
	}

	public void mostrarInformacoes(long count2, float hitCache, long missCache2) {
		System.out.println("Acessos: " + count2);
		System.out.println("MissCache: " + missCache2);
		System.out.println("HitCache: " + hitCache);
		System.out.println("Hit Rate: " +  (hitCache * 100) / count2 + "%");
	}

	public static int[] intToBinary(int value, int size) {
		if (value > Math.pow(2, size) - 1) {
			return null;
		}
		int bin[] = new int[size];
		int i = 0;
		while (value > 0 && i < size) {
			int num = value % 2;
			value = value / 2;
			bin[i] = num;
			i++;
		}
		for (int j = 0; j <= size / 2; j++) {
			int temp = bin[j];
			bin[j] = bin[size - j - 1];
			bin[size - j - 1] = temp;
		}
		return bin;
	}

	public static String intToBinaryString(Long value, Long size) {
		if (value > Math.pow(2, size) - 1) {
				return null;
		}
		char bin[] = new char[size.intValue()];
		for (int i = 0; i < size; i++) {
				bin[i] = '0';
		}
		int i = 0;
		while (value > 0 && i < size) {
				Long num = value % 2;
				value = value / 2;
				bin[i] = (num + "").charAt(0);
				i++;
		}
		for (int j = 0; j <= size / 2; j++) {
				char temp = bin[j];
				bin[j] = bin[size.intValue() - j - 1];
				bin[size.intValue() - j - 1] = temp;
		}
		String nova = new String(bin);
		return nova;
}
}