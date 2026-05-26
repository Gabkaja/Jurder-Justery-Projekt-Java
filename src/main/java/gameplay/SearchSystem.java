package gameplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.GameEngine;
import engine.SceneManager;

// odpowiada za przeszukiwanie aktualnego pomieszczenia
public class SearchSystem extends SceneManager {

	private final Map<String, Integer> searchCountByRoom;
	private final Map<String, Boolean> clueFoundByRoom;
	private String lastActionMessage;

	public SearchSystem(GameEngine engine) {
		super(engine);
		this.searchCountByRoom = new HashMap<>();
		this.clueFoundByRoom = new HashMap<>();
		this.lastActionMessage = "Rozejrzyj się i wybierz, jak chcesz przeszukać pomieszczenie.";
	}

	@Override
	public String getTitle() {
		return "Przeszukiwanie pomieszczenia";
	}

	@Override
	public String getNarration() {
		String roomName = engine.getCurrentLocation() == null
			? "Nieznane pomieszczenie"
			: engine.getCurrentLocation().toString();
		int attempts = searchCountByRoom.getOrDefault(roomName, 0);

		return "Aktualna lokacja: " + roomName + "\n"
			+ "Liczba prób przeszukania: " + attempts + "\n\n"
			+ lastActionMessage;
	}

	@Override
	public List<String> getOptions() {
		return List.of(
			"Przeszukaj pomieszczenie",
			"Uruchom minigre sledcza",
			"Wroc do menu glownego"
		);
	}

	@Override
	public SceneManager onChoice(int choice) {
		return switch (choice) {
			case 1 -> {
				handleRoomSearch();
				yield this;
			}
			case 2 -> {
				handleMinigame();
				yield this;
			}
			case 3 -> new SceneManager.MainMenuScene(engine);
			default -> this;
		};
	}

	private void handleRoomSearch() {
        // TODO: zaimplementowane przed obiektem Location, nie wiem jaki getter zwraca nazwe pokoju
		String roomName = engine.getCurrentLocation() == null
			? "Nieznane pomieszczenie"
			: engine.getCurrentLocation().toString();
		int newCount = searchCountByRoom.getOrDefault(roomName, 0) + 1;
		searchCountByRoom.put(roomName, newCount);

		boolean clueAlreadyFound = clueFoundByRoom.getOrDefault(roomName, false);
		if (!clueAlreadyFound) {
			String clue = generateClueForRoom(roomName);
			clueFoundByRoom.put(roomName, true);
			engine.getEventLog().addClue(clue);
			lastActionMessage = "Udalo sie! Znalazles nowy trop: " + clue;
			return;
		}

		lastActionMessage = "Po dokladnym sprawdzeniu nic nowego nie znaleziono.";
	}

	private void handleMinigame() {
		// TODO: podpiąć minigry
		String reward = "Dodatkowa poszlaka z minigry";
		lastActionMessage = "Minigra zakonczona sukcesem. Otrzymujesz: " + reward;
	}

    // TODO: placeholder, trzeba to przemyśleć
	private String generateClueForRoom(String roomName) {
		List<String> clues = List.of(
			"odcisk palca na kieliszku",
			"strzep pergaminu z zapiskami",
			"fragment tkaniny pasujacy do marynarki podejrzanego",
			"slady blota prowadzace do bocznego wyjscia",
			"zarys klucza od tajnej szuflady"
		);

		int index = Math.abs(roomName.hashCode()) % clues.size();
		return clues.get(index);
	}
}
