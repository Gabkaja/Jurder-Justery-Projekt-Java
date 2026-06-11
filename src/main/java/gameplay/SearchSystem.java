package gameplay;

import java.util.*;

import engine.GameEngine;
import engine.SceneManager;
import world.Location;
import world.MurderCase;
import characters.Suspect;

public class SearchSystem extends SceneManager {

	private final Map<String, Integer> searchCountByRoom;
	private final Map<String, Boolean> clueFoundByRoom;
	private final Map<String, Boolean> minigameDoneByRoom;
	private String lastActionMessage;

	// Globalna pula dowodów dla całej gry
	private static List<String> globalCluePool = null;
	private final Random random = new Random();

	public SearchSystem(GameEngine engine) {
		super(engine);
		this.searchCountByRoom = new HashMap<>();
		this.clueFoundByRoom = new HashMap<>();
		this.minigameDoneByRoom = new HashMap<>();
		this.lastActionMessage = "Co chcesz zrobić w tym pomieszczeniu?";

		if (globalCluePool == null) {
			initializeGlobalPool();
		}
	}

	private void initializeGlobalPool() {
		globalCluePool = new ArrayList<>();
		MurderCase caseInfo = engine.getMurderCase();

		if (caseInfo != null) {
			// Dodajemy dowody kluczowe
			globalCluePool.add("Zapiski wskazują na motyw: " + caseInfo.getMotive().getDescription());
			globalCluePool.add("Narzędzie zbrodni pasuje do kategorii: " + getWeaponCategoryFromJSON(caseInfo.getWeapon()));
			globalCluePool.add("W szafce leży zakurzony przedmiot, który może mieć znaczenie dla sprawy.");
		}

		// Dodajemy "wypełniacze", żeby gracz zawsze coś znalazł
		globalCluePool.add("Znajdujesz stary paragon, ale nie wydaje się istotny.");
		globalCluePool.add("W kącie wala się trochę kurzu i pajęczyny.");
		globalCluePool.add("Nic ciekawego, tylko sterta starych gazet.");
		globalCluePool.add("Znajdujesz niedopaloną zapałkę.");
	}
	public void markRoomAsDone(String roomId) {
		if (roomId != null) {
			this.minigameDoneByRoom.put(roomId.toLowerCase().trim(), true);
		}
	}

	@Override
	public String getTitle() {
		return "Interakcja z otoczeniem";
	}

	@Override
	public String getNarration() {
		Location current = engine.getCurrentLocation();
		String roomName = (current == null) ? "Nieznane pomieszczenie" : current.getName();
		int attempts = searchCountByRoom.getOrDefault(roomName, 0);

		return "Aktualna lokacja: " + roomName + "\n"
				+ "Liczba przeszukań tego pokoju: " + attempts + "\n\n"
				+ lastActionMessage;
	}

	@Override
	public List<String> getOptions() {
		return List.of(
				"Przeszukaj zakamarki pomieszczenia (Zwykłe szukanie)",
				"Podejmij wyzwanie śledcze (Minigra dedykowana lokacji)",
				"Porozmawiaj z podejrzanym", // <--- NOWOŚĆ
				"Przejdź do innego pomieszczenia",
				"Otwórz Dziennik Śledztwa",
				"Sformułuj Finałowe Oskarżenie"
		);
	}

	@Override
	public SceneManager onChoice(int choice) {
		return switch (choice) {
			case 1 -> { handleRoomSearch(); yield this; }
			case 2 -> handleMinigame();
			case 3 -> new SuspectSelectionScene(engine, this);
			case 4 -> new ExplorationSystem(engine, this);
			case 5 -> new InvestigationSystem(engine, this);
			case 6 -> {
				AccusationSystem accusation = new AccusationSystem(engine);
				accusation.makeAccusation();
				yield this;
			}
			default -> this;
		};
	}

	private void handleRoomSearch() {
		Location current = engine.getCurrentLocation();
		if (current == null) return;

		String roomName = current.getName();
		searchCountByRoom.put(roomName, searchCountByRoom.getOrDefault(roomName, 0) + 1);

		// 1. Sprawdź, czy to miejsce zbrodni
		MurderCase caseInfo = engine.getMurderCase();
		if (caseInfo != null && current.getId().equalsIgnoreCase(caseInfo.getCrimeScene().getId())) {
			if (!clueFoundByRoom.getOrDefault(roomName, false)) {
				String[] hints = {
						"Czujesz tu dziwny, duszący zapach środków czystości, jakby ktoś próbował coś zmyć z podłogi.",
						"Zauważasz nienaturalne ślady przesuwania mebli – jakby rozegrała się tu szamotanina.",
						"W rogu pomieszczenia leży dziwnie porzucony przedmiot osobisty ofiary.",
						"Coś w tym pokoju wydaje się... niespokojne. Ślady na dywanie wskazują na gwałtowny ruch."
				};

				String crimeSceneClue = hints[random.nextInt(hints.length)];
				clueFoundByRoom.put(roomName, true);
				engine.getEventLog().addClue("Poszlaka w pokoju: " + crimeSceneClue);
				lastActionMessage = "Udało się! Znajdujesz coś ciekawego: " + crimeSceneClue;
				return;
			}
		}

		// 2. Jeśli nie miejsce zbrodni (lub już przeszukane), sprawdź standardową blokadę
		if (clueFoundByRoom.getOrDefault(roomName, false)) {
			lastActionMessage = "Przetrząsnąłeś już każdy kąt w tym pokoju. Nic więcej tu nie ma.";
			return;
		}

		// 3. Pobranie dowodu z globalnej puli (reszta logiki bez zmian)
		if (globalCluePool.isEmpty()) {
			lastActionMessage = "Przeszukałeś już wszystkie możliwe miejsca w domu. Nie ma tu już nic nowego.";
		} else {
			String clue = globalCluePool.remove(random.nextInt(globalCluePool.size()));
			clueFoundByRoom.put(roomName, true);
			engine.getEventLog().addClue(clue);
			lastActionMessage = "Udało się! Znajdujesz coś ciekawego: " + clue;
		}
	}

	private SceneManager handleMinigame() {
		Location current = engine.getCurrentLocation();
		if (current == null) return this;

		String roomId = current.getId().toLowerCase().trim();

		if (minigameDoneByRoom.getOrDefault(roomId, false)) {
			lastActionMessage = "Wyczerpałeś już limit wyzwań w tym pokoju.";
			return this;
		}

		return switch (roomId) {
			case "lazienka" -> new gameplay.minigames.CipherGame(engine, this);
			case "biblioteka" -> new gameplay.minigames.RiddleGame(engine, this);
			case "gabinet" -> new gameplay.minigames.VigenereGame(engine, this);
			case "piwnica" -> new gameplay.minigames.LockpickingGame(engine, this);
			case "sypialnia" -> new gameplay.minigames.AlibiCheckGame(engine, this);
			case "przedsionek", "ogrod" -> new gameplay.minigames.BoobyTrapGame(engine, this);
			case "salabalowa" -> new gameplay.minigames.RhythmGame(engine, this);
			case "garaz" -> new gameplay.minigames.PigpenGame(engine, this);
			default -> new gameplay.minigames.GuessNumberGame(engine, this);
		};
	}

	public void setMinigameResult(boolean success, String message) {
		Location current = engine.getCurrentLocation();
		if (current != null) {
			markRoomAsDone(current.getId());
		}
		this.lastActionMessage = message;
	}

	private String getWeaponCategoryFromJSON(String weaponName) {
		if (weaponName == null) return "NIEZNANA";
		String weapon = weaponName.trim();
		if (List.of("Nóż kuchenny", "Tasak", "Żyletka", "Sekator", "Nożyk do listów").contains(weapon)) return "OSTRE";
		if (List.of("Świecznik", "Ciężka książka", "Posążek", "Łopata", "Popielniczka", "Młotek").contains(weapon)) return "TĘPE";
		if (List.of("Lina od kotary", "Kabel od suszarki", "Łańcuch", "Lina", "Poduszka", "Pasek").contains(weapon)) return "DUSZĄCE";
		if (List.of("Trucizna", "Środki chemiczne", "Zatrute wino", "Tabletki nasenne", "Kanister").contains(weapon)) return "TRUJĄCE_CHEMICZNE";
		if (List.of("Pistolet", "Klucz francuski", "Kilof").contains(weapon)) return "MECHANICZNE";
		if (List.of("Laska", "Wieszak", "Rozbita butelka", "Grabie", "Widelec do mięsa").contains(weapon)) return "IMPROWIZOWANE";
		return "NIEZNANA";
	}

	public String generateSpecificClue(Location currentRoom, String clueCategory) {
		MurderCase caseInfo = engine.getMurderCase();
		if (caseInfo == null) return "Niewyraźny ślad buta.";

		if (currentRoom.getId().equalsIgnoreCase(caseInfo.getCrimeScene().getId())) {
			return "Ślady walki na podłodze. Ten pokój jest miejscem zbrodni!";
		}

		String targetCategory = (clueCategory == null || clueCategory.isBlank())
				? "RANDOM"
				: clueCategory.trim().toUpperCase(Locale.ROOT);

		if ("RANDOM".equals(targetCategory)) {
			String[] pools = {"KILLER", "MOTIVE", "WEAPON"};
			targetCategory = pools[random.nextInt(pools.length)];
		}

		return switch (targetCategory) {
			case "KILLER" -> "Na zakurzonym fragmencie lustra ktoś zapisał jedno imię: " + caseInfo.getKiller();
			case "MOTIVE" -> "Znaleziono skrawek papieru z notatką: " + caseInfo.getMotive().getDescription();
			case "WEAPON" -> "Znaleziono przedmiot, który może mieć związek z narzędziem typu: " + getWeaponCategoryFromJSON(caseInfo.getWeapon());
			default -> "Dziwny ślad na podłodze, którego nie potrafisz zidentyfikować.";
		};
	}

	private static class SuspectSelectionScene extends SceneManager {
		private final SceneManager returnScene;
		private List<Suspect> suspectsInThisRoom;

		public SuspectSelectionScene(GameEngine engine, SceneManager returnScene) {
			super(engine);
			this.returnScene = returnScene;

			Location currentRoom = engine.getCurrentLocation();
			if (currentRoom != null && engine.getSuspects() != null) {
				this.suspectsInThisRoom = engine.getSuspects().stream()
						.filter(s -> s.getLocationId() != null && s.getLocationId().equalsIgnoreCase(currentRoom.getId()))
						.toList();
			} else {
				this.suspectsInThisRoom = java.util.List.of();
			}
		}

		@Override
		public String getTitle() { return "Obecni w pomieszczeniu"; }

		@Override
		public String getNarration() {
			if (suspectsInThisRoom.isEmpty()) {
				return "Rozejrzałeś się dookoła. W tym pomieszczeniu nie ma żywej duszy.";
			}
			return "W tym pokoju przebywają następujące osoby. Z kim chcesz porozmawiać?";
		}

		@Override
		public List<String> getOptions() {
			List<String> options = new java.util.ArrayList<>();
			for (Suspect s : suspectsInThisRoom) {
				options.add(s.getName() + " [" + s.getTitle() + "]");
			}
			options.add("Powrót");
			return options;
		}

		@Override
		public SceneManager onChoice(int choice) {
			if (choice >= 1 && choice <= suspectsInThisRoom.size()) {
				return new InterrogationSystem(engine, this, suspectsInThisRoom.get(choice - 1));
			}
			return returnScene;
		}
	}
}