# Jurder Jystery: DevDoc

## Po co?
Ten dokument powinien zagwarantować, że każdy z nas wie jak końcowy efekt ma wyglądać i co ma zrobić aby do niego dojść.

## Opis gry
Klon Cluedo - gra kryminalna w Javie.

## Omówienie settingu
Akcja dzieje się w współczesnym Krakowie. Akcja dzieje się wewnątrz bliżej nieokreślonego dworka/willi. Styl narracyjny lekko humorystyczny. Pewnego jessieniego wieczoru przedstawiciel elity został zamordowany na wystawionym w swoim domu. Twoim zadaniem jest dowiedzieć się, kto go zabił.

## Gameplay: Główna pętla
Rozgrywka to gra typu choose-your-own-adventure jako aplikacja w terminalu. Gracz eksploruje setting poprzez podejmowanie jednej decyzji z kontekstualnego menu. Typowa pętla to coś w stylu tekst/kontekst->decyzja->rezolucja->tekst/kontekst.

## Gameplay: Sterowanie
Gracz steruje używając klawiatury aby wybrać jedną z opcji z menu oraz do kontroli miniger.

## Gameplay: Win-condition
Odnalezienie sprawcy i poprawne sformułowanie finałowego oskarżenia. Gracz nie ma ograniczenia czasowego, może wysunąć oskarżenie w dowolnym momencie, ale win jest tylko przy poprawnym. Sformułowanie przyjmuje formę czterech pytań: kto, gdzie, czym i dlaczego.

## Gameplay: Mechaniki
* Eksploracja różnych lokacji.
* Przesłuchiwanie podejrzanych (system dialogowy)
* Przeszukiwanie pomieszczenia (aktywowanie minigier)
* Dziennik zdarzeń, zapisujący co się wydarzyło
* Formułowanie finalnego oskarżenia
* Wiele grywalnych postaci
* Wybór poziomu trudności - różne postaci mają inny poziom trudności
* Zbieranie dowodów czasami zależne od wejścia do lokacji, czasami siedzące za minigrą

## Zakres projektu
### Minimum: gra musi być grywalna, czyli:
Wszystkie mechaniki muszą być zaimplementowane
Poprawny win condition
Wszystkie testy zaliczone poprawnie
Wyróżnianie kluczowych elementów kolorami
System wyboru postaci/możliwość grania innymi postaciami
### Fajnie jeśli:
GUI
Rozbudowane dialogi z NPC
Działający wybór poziomu trudności
### Na pewno nie:
Audio

## Content: Lokacje/mapa
* Sala balowa
* Biblioteka
* Kuchnia
* Łazienka
* Ogród (taki mniejszy)
* Jadalnia
* Gabinet
* Garaż
* Piwnica
* Sypialnia
* Przedsionek (lokacja startowa)

## Content: NPC
* Kamerdyner
* Hrabina
* Celebryta
* Polityk
* Kucharz
* Random z ulicy
* Kochanka/Osoba z elity

## Content: PC
* Doświadczony detektyw
* Dziennikarka śledcza
* Młody prywatny detektyw
* Technik śledczy

## Content: minigry
* Zgadywanie liczby w ograniczonej liczbie prób
* Rozszyfrowanie słowa szyfrem przestawieniowym
* Zagadki (ala gollum)
* Gry zręcznościowe (jeśli się da?)
* Modyfikowany snake (?)

## UI
Menu w terminalu z kolorowym wyróżnieniem kluczowych informacji. Ekran czyści się po każdej decyzji/grze.

## Techniczne
src/
 ├── main/
 │    ├── Main.java
 │    │
 │    ├── engine/
 │    │     ├── GameEngine.java
 │    │     ├── GameLoop.java
 │    │     ├── SceneManager.java
 │    │     └── InputHandler.java
 │    │
 │    ├── world/
 │    │     ├── Location.java
 │    │     ├── MapGraph.java
 │    │     ├── Evidence.java
 │    │     ├── MurderCase.java
 │    │     └── EventLog.java
 │    │
 │    ├── characters/
 │    │     ├── PlayerCharacter.java
 │    │     ├── NPC.java
 │    │     ├── Suspect.java
 │    │     ├── Detective.java
 │    │     ├── Journalist.java
 │    │     ├── ForensicTech.java
 │    │     └── YoungDetective.java
 │    │
 │    ├── dialogue/
 │    │     ├── Dialogue.java
 │    │     ├── DialogueNode.java
 │    │     ├── DialogueChoice.java
 │    │     └── DialogueManager.java
 │    │
 │    ├── gameplay/
 │    │     ├── InvestigationSystem.java
 │    │     ├── ExplorationSystem.java
 │    │     ├── AccusationSystem.java
 │    │     ├── SearchSystem.java
 │    │     └── DifficultySystem.java
 │    │
 │    ├── minigames/
 │    │     ├── Minigame.java
 │    │     ├── GuessNumberGame.java
 │    │     ├── CipherGame.java
 │    │     ├── RiddleGame.java
 │    │     ├── SnakeGame.java
 │    │     └── ReflexGame.java
 │    │
 │    ├── ui/
 │    │     ├── TerminalUI.java
 │    │     ├── MenuRenderer.java
 │    │     ├── ColorManager.java
 │    │     └── ScreenCleaner.java
 │    │
 │    ├── data/
 │    │     ├── GameDataLoader.java
 │    │     ├── NPCFactory.java
 │    │     ├── LocationFactory.java
 │    │     └── DialogueFactory.java
 │    │
 │    └── utils/
 │          ├── RandomUtils.java
 │          ├── TextUtils.java
 │          └── Constants.java
 │
 └── test/
      ├── gameplay/
      ├── minigames/
      ├── dialogue/
      └── world/

BARDZO WSTĘPNIE, JEŚLI COŚ NIE MA SENSU - KONSULTUJEMY!!!