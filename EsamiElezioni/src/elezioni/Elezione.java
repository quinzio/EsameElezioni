package elezioni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Elezione {

	static public Collection<Cittadino> elettori = new ArrayList<>();
	static public Collection<Lista> listeElenco = new ArrayList<>();
	static public Map<Cittadino, Integer> votazione = new HashMap<>();
	static public int votanti;

	public Elezione() {

	}

	public Cittadino aggiungiElettore(String nome, String cognome) {
		Elettore el;
		elettori.add(el = new Elettore(nome, cognome));
		return el;
	}

	@SuppressWarnings("rawtypes")
	public Collection getElettori() {
		return elettori;
	}

	public Cittadino getElettore(String nome, String cognome) {
		for (Cittadino el : elettori) {
			if ((el.getNome().equals(nome))
					&& (el.getCognome().equals(cognome))) {
				return el;
			}
		}
		return null;
	}

	public void registraLista(Lista lista) {
		listeElenco.add(lista);
	}

	/**
	 * Il cittadino votante esprime un voto per la lista ed un voto di
	 * preferenza per il candidato identificato da nome e cognome
	 * 
	 * @throws TentatoDoppioVoto
	 *             se il cittadino ha già votato
	 * @throws TaglioNonPermesso
	 *             se il candidato per cui si esprime la preferenza non
	 *             appartiene alla lista
	 */
	public void vota(Cittadino votante, String lista, String nome,
			String cognome) throws TentatoDoppioVoto, TaglioNonPermesso {

		boolean giaVotato = votante.haVotato();
		if (giaVotato)
			throw new TentatoDoppioVoto();

		Cittadino target = null;
		for (Cittadino c : elettori) {
			if (c.getNome().equals(nome))
				if (c.getCognome().equals(cognome)) {
					target = c;
				}
		}
		if (target == null)
			throw new TentatoDoppioVoto();

		votanti++;
		for (Lista l : Elezione.listeElenco) {
			if (l.getNome().equals(lista)) {
				Cittadino c = l.getCapolista();
				@SuppressWarnings("unchecked")
				Collection<Cittadino> cand = l.getCandidati();

				boolean found = false;
				for (Cittadino c1 : cand) {
					if (c1 == target) {
						if (votazione.containsKey(c)) {
							Integer voti = votazione.get(c);
							voti++;
							votazione.put(c, voti);
						} else {
							votazione.put(c, 1);
						}
						found = true;
						break;
					}
				}
				if (!found)
					throw new TaglioNonPermesso();
				break;
			}
		}
	}

	/**
	 * Il cittadino votante esprime un voto per la lista il voto di preferenza
	 * va automaticamente al capolista
	 * 
	 * @throws TentatoDoppioVoto
	 *             se il cittadino ha già votato
	 */
	public void vota(Cittadino votante, String lista) throws TentatoDoppioVoto {
		boolean giaVotato = votante.haVotato();
		if (giaVotato)
			throw new TentatoDoppioVoto();

		votanti++;
		for (Lista l : Elezione.listeElenco) {
			if (l.getNome().equals(lista)) {
				Cittadino c = l.getCapolista();

				if (votazione.containsKey(c)) {
					Integer voti = votazione.get(c);
					voti++;
					votazione.put(c, voti);
				} else {
					votazione.put(c, 1);
				}
				break;
			}
		}
	}

	public long getNumeroVotanti() {
		return votanti;
	}

	@SuppressWarnings("rawtypes")
	public Collection getRisultatiListe() {
		return listeElenco
				.stream()
				.sorted(Comparator.comparing(Lista::getNumeroVoti,
						Comparator.reverseOrder()))
				.collect(Collectors.toList());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getRisultatiCandidati() {
		return listeElenco
				.stream()
				.<Cittadino>flatMap(l -> l.getCandidati().stream())
				.sorted(Comparator.<Cittadino, Integer>comparing(c -> Elezione.votazione.get(c)))
				.collect(Collectors.toList());
	}
}
