package elezioni;

public class Elettore implements Cittadino {
	private String nome;
	private String cognome;
	private boolean haVotato;
	public boolean capolista;
	public boolean candidato;

	public Elettore(String nome, String cognome) {
		this.nome = nome;
		this.cognome = cognome;
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public String getCognome() {
		return cognome;
	}

	@Override
	public boolean haVotato() {
		boolean voto = haVotato;
		haVotato = true;
		return voto;
	}

	@Override
	public boolean isCapolista() {
		for (Lista l : Elezione.listeElenco) {
			if (l.getCapolista() == this)
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isCandidato() {
		return Elezione.listeElenco.stream()
				.<Cittadino> flatMap(l -> l.getCandidati().stream())
				.filter(c -> c == this).count() > 0;

	}

	@Override
	public long getNumeroVoti() {
		return Elezione.votazione.get(this);
	}

	public void setCapolista(boolean capolista) {
		this.capolista = capolista;
	}

	public void setCandidato(boolean candidato) {
		this.candidato = candidato;
	}

}
