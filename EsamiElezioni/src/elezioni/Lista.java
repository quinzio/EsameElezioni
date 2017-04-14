package elezioni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Lista {

	private Cittadino capolista;
	private Collection<Cittadino> candidati = new ArrayList<>();
	private Collection<Cittadino> iscritti = new ArrayList<>();
	private String nome;
	private String motto;

	public Lista(String nome, String motto) {
	}

	public String getNome() {
		return nome;
	}

	public String getMotto() {
		return motto;
	}

	public void assegnaCapolista(Cittadino capolista) throws CandidatoNonValido {
		boolean found = false;
		for (Cittadino el : candidati) {
			if (el == capolista) {
				found = true;
				break;
			}
		}
		if (!found)
			throw new CandidatoNonValido();

		if (capolista.isCapolista())
			throw new CandidatoNonValido();
		this.capolista = capolista;
	}

	@SuppressWarnings("unchecked")
	public void aggiungiCandidato(Cittadino capolista)
			throws CandidatoNonValido {
		if (Elezione.listeElenco.stream()
				.<Cittadino> flatMap(l -> l.getCandidati().stream())
				.filter(c -> c == this).count() > 0)
			throw new CandidatoNonValido();
	}

	public Cittadino getCapolista() {
		return capolista;
	}

	/**
	 * Restuisce la collezione dei candidati (NON include il capolista)
	 */
	public Collection getCandidati() {
		return candidati;
	}

	public long getNumeroVoti() {
		return candidati.stream().collect(Collectors.summingLong(c -> Elezione.votazione.get(c)));
	}

	public double getPercentualeVoti() {
		return getNumeroVoti() / Elezione.votanti;
	}
}
