# Murder Mystery Investigation Game

Text-based detective game written in Java. Each playthrough generates a unique case with a random killer, weapon, location, and motive. The solution is generated first and all evidence is built around it, so the case is always solvable.

## How it works

The player explores locations, interrogates suspects, and collects evidence. An investigation board tracks every action automatically. Pinning evidence to suspects and building a working hypothesis is done manually by the player. The game ends when the player submits a final accusation or exhausts all locations.

## Features

- Procedurally generated cases with consistent evidence logic
- Four playable characters, each with unique investigation skills
- Four optional minigames that unlock additional clues
- Non-linear investigation loop with no fixed order of actions

## Project structure

```
src/
  engine/       CaseGenerator, EvidenceEngine, LocationManager
  model/        Case, Suspect, Location, Evidence, Motive
  actions/      SearchAction, InterrogateAction, ConfrontAction
  board/        ActionLog, PinBoard, WorkingHypothesis
  characters/   Character, AlexKowalski, InspectorLis, Tomek, Wrona
  minigames/    CipherBreak, FingerprintMatch, PhonePin, EventReconstruction
```

## Requirements

Java 17 or higher. No external dependencies.

## Authors

University lab project. Three-person team.
