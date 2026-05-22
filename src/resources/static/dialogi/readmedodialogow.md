"# System Dialogowy — Gra Detektywistyczna

## Architektura

System oparty na szablonach z placeholderami, fragmentach zdan i unikalnych wariantach per NPC.

```
dialogue_system/
├── system/                         # Definicje systemowe
│   ├── dialogue_types.json         # 15 kategorii dialogów
│   ├── speech_styles.json          # 10 stylów mówienia + modyfikatory
│   ├── emotions.json               # 10 emocji + przejścia między nimi
│   ├── certainty_levels.json       # 5 poziomów pewności + modyfikatory PL
│   └── fragments.json              # Fragmenty zdań (opener + core + closer)
├── templates/                      # Wspólne szablony (z placeholderami)
│   ├── powitania.json              # 15 szablonów
│   ├── alibi.json                  # 18 szablonów
│   ├── obserwacje_wzrokowe.json    # 18 szablonów
│   ├── obserwacje_sluchowe.json    # 15 szablonów
│   ├── opinie.json                 # 15 szablonów
│   ├── podejrzenia.json            # 12 szablonów
│   ├── relacje.json                # 12 szablonów
│   ├── motywy.json                 # 12 szablonów
│   ├── reakcja_na_dowod.json       # 15 szablonów
│   ├── klamstwa.json               # 12 szablonów
│   ├── nerwowosc.json              # 12 szablonów
│   ├── odkrycie_sprzecznosci.json  # 12 szablonów
│   ├── sekrety.json                # 12 szablonów
│   ├── reakcja_na_oskarzenie.json  # 10 szablonów
│   └── reakcja_na_presje.json      # 10 szablonów
├── npc/                            # Unikalne dialogi per NPC
│   ├── kamil_krakowski.json        # Kamerdyner — ELEGANCKI/ZIMNY
│   ├── honorata_hanska.json        # Hrabina — ELEGANCKI/SPOKOJNY
│   ├── cyprian_czerwinski.json     # Celebryta — SWOBODNY/NERWOWY
│   ├── piotr_pawlowski.json        # Polityk — DYPLOMATYCZNY/WYMIJAJACY
│   ├── konstanty_kulczycki.json    # Kucharz — BEZPOSREDNI/AGRESYWNY
│   ├── wieslaw_wydra.json          # Wspólnik — GORZKI/AGRESYWNY
│   └── patrycja_pawlowska.json     # Pisarka — SPOSTRZEGAWCZY/SPOKOJNY
└── README.md
```

## Placeholdery

W szablonach uzyto placeholderów, które sa zamieniane dynamicznie:

| Placeholder | Opis | Przyklad |
|---|---|---|
| `{suspect}` | Imie i nazwisko podejrzanego | Kamil Krakowski |
| `{room}` | Nazwa lokacji | Biblioteka |
| `{weapon}` | Narzedzie zbrodni | Posazek |
| `{time}` | Godzina zdarzenia | 22:30 |
| `{victim}` | Imie i nazwisko ofiary | Andrzej Arciszewski |

## Jak generator sklada wypowiedz

### 1. Wybor szablonu
Generator wybiera szablon na podstawie:
- **Typu dialogu** (ALIBI, OBSERWACJE, itp.)
- **Emocji NPC** (filtruje po `emotion`)
- **Poziomu zaufania** (filtruje po `minTrust`)
- **Wymaganego dowodu** (filtruje po `requiresClue`)
- **Poziomu pewnosci** (filtruje po `certaintyRange`)

### 2. Priorytet: NPC > Szablon
1. Sprawdz unikalne dialogi NPC (`npc/*.json`)
2. Jesli brak — uzyj wspolnego szablonu (`templates/*.json`)
3. Zastosuj modyfikatory stylu mowienia

### 3. System fragmentow
Alternatywnie, generator moze zlozyc wypowiedz z fragmentow:
```
opener (losowy) + core (wg typu) + closer (losowy)
= \"Szczerze mówiąc, widziałem {suspect} w {room} tuż przed północą.\"
```

### 4. Modyfikatory pewnosci
Poziom `certainty` NPC modyfikuje wypowiedz:
- **90+**: \"Na pewno widziałem...\"
- **60-89**: \"O ile dobrze pamietam...\"
- **35-59**: \"Chyba widzialem...\"
- **15-34**: \"Moglo mi sie wydawac...\"
- **0-14**: \"Ktos mi mowil, ze...\"

### 5. Modyfikatory stylu
Styl NPC dodaje prefiksy/sufiksy/fillery:
- **ELEGANCKI**: \"Jesli pan pozwoli...\" + \"...rzecz jasna...\"
- **AGRESYWNY**: \"Sluchaj —\" + \"...do cholery...\"
- **NERWOWY**: \"Ja... znaczy...\" + \"...chyba.\"

## Statystyki

| Element | Ilosc |
|---|---|
| Wspolne szablony | ~190 |
| Unikalne dialogi NPC (x7) | ~350 |
| Fragmenty zdan | ~120 |
| Style mowienia | 10 |
| Emocje | 10 |
| Poziomy pewnosci | 5 |
| **Szacowane kombinacje** | **Tysiace** |

## Klamstwa i sprzecznosci

Kazde klamstwo ma pole `contradictedBy` wskazujace, jaki typ dowodu je obala:
```json
{
  \"lieType\": \"FALSZYWE_ALIBI\",
  \"contradictedBy\": [\"SWIADEK_OBSERWACJI\", \"NAGRANIE\", \"SLADY_FIZYCZNE\"]
}
```

## Profile NPC

| NPC | Styl 1 | Styl 2 | Domyslna emocja |
|---|---|---|---|
| Kamil Krakowski | ELEGANCKI | ZIMNY | NEUTRALNY |
| Honorata Hanska | ELEGANCKI | SPOKOJNY | NEUTRALNY |
| Cyprian Czerwinski | SWOBODNY | NERWOWY | NEUTRALNY |
| Piotr Pawlowski | DYPLOMATYCZNY | WYMIJAJACY | NEUTRALNY |
| Konstanty Kulczycki | BEZPOSREDNI | AGRESYWNY | NEUTRALNY |
| Wieslaw Wydra | GORZKI | AGRESYWNY | GNIEW |
| Patrycja Pawlowska | SPOSTRZEGAWCZY | SPOKOJNY | NEUTRALNY |
"