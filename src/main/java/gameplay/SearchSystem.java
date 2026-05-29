package gameplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import engine.GameEngine;
import engine.SceneManager;
import world.Location;
import world.MurderCase;
import gameplay.minigames.*;

public class SearchSystem extends SceneManager {

	private final Map<String, Integer> searchCountByRoom;
	private final Map<String, Boolean> clueFoundByRoom;
	private final Map<String, Boolean> minigameDoneByRoom;
	private String lastActionMessage;
	private final Random random = new Random();

	public SearchSystem(GameEngine engine) {
		super(engine);
		this.searchCountByRoom = new HashMap<>();
		this.clueFoundByRoom = new HashMap<>();
		this.minigameDoneByRoom = new HashMap<>();
		this.lastActionMessage = "Co chcesz zrobić w tym pomieszczeniu?";
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
			case 3 -> new ExplorationSystem(engine, this);
			case 4 -> new InvestigationSystem(engine, this);
			case 5 -> {
				this.lastActionMessage = "[System Oskarżeń nie jest jeszcze zaimplementowany]";
				yield this;
			}
			default -> this;
		};
	}

	private String getWeaponCategoryFromJSON(String weaponName) {
		if (weaponName == null) return "NIEZNANA";

		String weapon = weaponName.trim();

		if (List.of("Nóż kuchenny", "Tasak", "Żyletka", "Sekator", "Nożyk do listów").contains(weapon)) {
			return "OSTRE";
		}

		if (List.of("Świecznik", "Ciężka książka", "Posążek", "Łopata", "Popielniczka", "Młotek").contains(weapon)) {
			return "TĘPE";
		}

		if (List.of("Lina od kotary", "Kabel od suszarki", "Łańcuch", "Lina", "Poduszka", "Pasek").contains(weapon)) {
			return "DUSZĄCE";
		}

		if (List.of("Trucizna", "Środki chemiczne", "Zatrute wino", "Tabletki nasenne", "Kanister").contains(weapon)) {
			return "TRUJĄCE_CHEMICZNE";
		}

		if (List.of("Pistolet", "Klucz francuski", "Kilof").contains(weapon)) {
			return "MECHANICZNE";
		}

		if (List.of("Laska", "Wieszak", "Rozbita butelka", "Grabie", "Widelec do mięsa").contains(weapon)) {
			return "IMPROWIZOWANE";
		}

		return "NIEZNANA";
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
			case "lazienka" -> new CipherGame(engine, this);
			case "biblioteka" -> new RiddleGame(engine, this);
			case "gabinet" -> new VigenereGame(engine, this);
			case "garaz", "strych" -> new LockpickingGame(engine, this);
			case "jadalnia", "pokoj_goscinny" -> new AlibiCheckGame(engine, this);
			case "przedsionek", "ogrod" -> new BoobyTrapGame(engine, this);

			default -> new GuessNumberGame(engine, this);
		};
	}

	public void setMinigameResult(boolean success, String message) {
		Location current = engine.getCurrentLocation();
		if (current != null && success) {
			minigameDoneByRoom.put(current.getId(), true);
		}
		this.lastActionMessage = message;
	}

	private void handleRoomSearch() {
		Location current = engine.getCurrentLocation();
		if (current == null) return;

		String roomName = current.getName();
		searchCountByRoom.put(roomName, searchCountByRoom.getOrDefault(roomName, 0) + 1);

		if (clueFoundByRoom.getOrDefault(roomName, false)) {
			lastActionMessage = "Przetrząsnąłeś już każdy kąt w tym pokoju. Nic więcej tu nie ma.";
			return;
		}

		String clue = generateSpecificClue(current, "RANDOM");
		clueFoundByRoom.put(roomName, true);
		engine.getEventLog().addClue(clue);
		lastActionMessage = "Udało się! Znajdujesz coś ciekawego: " + clue;
	}

	public String generateSpecificClue(Location currentRoom, String clueCategory) {
		MurderCase caseInfo = engine.getMurderCase();
		if (caseInfo == null) return "Niewyraźny ślad buta.";

		if (currentRoom.getId().equalsIgnoreCase(caseInfo.getCrimeScene().getId())) {
			return "Silne ślady walki oraz krew na podłodze. To " + currentRoom.getName() + " jest miejscem zbrodni!";
		}

		String targetCategory = clueCategory.toUpperCase();

		if ("RANDOM".equals(targetCategory)) {
			String[] pools = {"MOTIVE", "WEAPON", "KILLER"};
			targetCategory = pools[random.nextInt(pools.length)];
		}

		return switch (targetCategory) {
			case "MOTIVE" -> "Zapiski księgowe wskazują na motyw: " + caseInfo.getMotive().getDescription();
			case "WEAPON" -> {
				String weaponCat = getWeaponCategoryFromJSON(caseInfo.getWeapon());

				yield switch (weaponCat) {
					case "OSTRE" ->
							"POSZLAKA MEDYCZNA: Ofiara posiada precyzyjne rany kłute o równych krawędziach. " +
									"Lekarz twierdzi, że sprawca użył fabrycznego, bardzo ostrego ostrza.";

					case "TĘPE" ->
							"POSZLAKA MEDYCZNA: Sekcja wykazuje rozległe pęknięcie kości czaszki od uderzenia. " +
									"Na miejscu zbrodni nie ma krwi na ścianach, co sugeruje ciężki przedmiot tępy.";

					case "DUSZĄCE" ->
							"POSZLAKA MEDYCZNA: Charakterystyczna sina bruzda wokół szyi oraz wybroczyny wywołane niedotlenieniem. " +
									"Morderca musiał udusić ofiarę.";

					case "TRUJĄCE_CHEMICZNE" ->
							"POSZLAKA TOKSYKOLOGICZNA: Brak jakichkolwiek śladów zewnętrznych. Toksykologia wykryła " +
									"gwałtowne zatrzymanie akcji serca spowodowane substancją chemiczną lub trucizną.";

					case "MECHANICZNE" ->
							"POSZLAKA BALISTYCZNA: Ślady zniszczeń, metalowe opiłki lub rany wskazują na użycie " +
									"ciężkiego narzędzia mechanicznego bądź broni miotającej.";

					case "IMPROWIZOWANE" ->
							"POSZLAKA KRYMINALISTYCZNA: Rany są nieregularne i poszarpane. Wygląda na to, że sprawca panikował " +
									"i złapał pierwszy lepszy przedmiot improwizowany, który nawinął mu się pod rękę.";

					default ->
							"POSZLAKA: Ślady na ciele ofiary są niejednoznaczne. Narzędzie zbrodni pozostaje zagadką.";
				};
			}
			case "KILLER" -> "Świadek zeznał, że w sprawę zamieszany jest bezpośrednio: " + caseInfo.getKiller();
			default -> "W koszu leży podarty papier ze strzępami motywu: '" + caseInfo.getMotive().getLabel() + "'";
		};
	}
}