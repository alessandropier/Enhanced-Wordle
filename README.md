# Personalized Wordle Offline
[![Build and Publish Release](https://github.com/alessandropier/Wordle/actions/workflows/ingsw2122.yml/badge.svg)](https://github.com/alessandropier/Wordle/actions/workflows/ingsw2122.yml) [![Coverage Status](https://coveralls.io/repos/github/alessandropier/Wordle/badge.svg?branch=v1.0)](https://coveralls.io/github/alessandropier/Wordle?branch=v1.0)

Initially, this started as a repository for the implementation of the _Wordle_ game _(in Italian)_ via Command Line Interface _(CLI)_ created during the _Software Engineering_ course using the _Agile SCRUM_ framework and using _GitHub Actions_ for _CI/CD_.

Subsequently, I continued the project to integrate additional features such as:
- **Automatic** and **_random generation_** of the _word to guess_ using a _built in dictionary_.
- **Complete _User Interface_** built with _Java Swing_ and _FlatLaf_.
- A **persistent UI theme preference** system that allows users to toggle between _day and night modes_, automatically saving their selection across sessions.
- A **custom dictionary** feature that enables users to add new words and expand the game's vocabulary. _(note that if invalid words are manually added to the extra words file, they will be skipped by the system, as well as duplicates.)_

## 🕹️ Playing the Game

<div align="center">
  <figure>
    <img src="resources/video.gif" width="500" alt="Demo Wordle">
  </figure>
</div>

1. **Main Game Window**
   * **Grid Area**: Displays your guesses attempt by attempt, coloring each letter to show your progress.
   * **Input Field & Submit Button**: Type your 5-letter guess in the text box and click the submit button _(or press Enter)_ to play your turn.

2. **Control Buttons**
   * **"Nuova" (New Game) Button**: Click this button at any time to start a brand new game with a fresh target word.
   * **"Abbandona" (Give Up) Button**: Click this if you want to surrender the current match and reveal the hidden word.
   * **"Esci" (Exit) Button**: Closes and exits the application.

3. **Customization & Themes**
   * **"Aggiungi parola" (Add Word)**: Click this button to open a dedicated input prompt where you can type and add a new custom word directly to your game dictionary saved locally (`parole_extra.txt`).
   * **"Notte/Giorno" (Dark/Light Mode)**: Click this toggle button to switch between the dark theme and the light theme of the user interface for a comfortable playing experience.

## 🎮 Starting the Game
The game is distributed as an executable `.jar` file. To launch Wordle, make sure you have **Java (JRE/JDK 11 or higher)** installed.

### 💻 Windows, Linux and Mac
1. Download the `Wordle.jar` file from the [Releases](https://github.com/alessandropier/Wordle/releases) section.
2. Double-click on the file.

## ⚠️ Resolution issues or window too large?
If the window appears scaled incorrectly on your operating system, you can force the correct display by using the dedicated startup script:
- **On Windows:** download the `avvia_wordle.bat`, save it in the game folder and double-click on it.
- **On Linux / macOS:** download the `avvia_wordle.sh`, open the terminal in the game folder and launch the `.sh` script with:

  ```bash
  chmod +x avvia.sh
  ./avvia_wordle.sh
  ```

## Future Developments Ideas
1. Check if the guess is an Italian word by using a dictionary of all 5-letter Italian words.
2. Add a light bulb button that provides a hint to the user (only once per game).

## Repository Structure

The repository structure is as follows:
```
|-- .github 
|    |-- workflows
|    |      |-- ingsw2122.yml
|    |-- reports
|    |      |-- checkstyle
|    |      |-- spotbugs
|    |      |-- jacoco/tests
|    |      |-- tests/test
|–– config
|    |–– checkstyle
|–– docs _(documentation related to version v1.0 for the Soft. Eng. exam)_
|    |–– Assegnazione progetto.md
|    |–– Guida per lo studente.md
|    |–– img
|    |–– Report.md
|–– gradle
|–– src
|    |–– main
|    |–– test
|–– .gitignore
|–– build.gradle
|–– README.md
|–– gradlew
|–– gradle.bat
|–– settings.gradle
```

The roles of the various components are detailed below:

- `.github/workflows/ingsw2122.yml`: details the directives to ensure *continuous integration* through the use of GitHub Actions;
- `config/`: hosts configuration files. The only basic configuration required is the one for the checkstyle tool;
- `docs/`: hosts the project documentation, including figures (in the `img/` subfolder).
<br>The `Report.md` file will be used to draft the final project report.
<br>The folder also collects:
    - `Assegnazione progetto.md`: containing the detailed description of the assigned project;
    - `Guida per lo studente.md`: containing the description of all configuration steps necessary to activate the workflow supporting project development;
- `gradle/`: hosts the `.jar` related to the *Gradle* dependency management system.
- `src`: main project folder, where all application code is written. `main/` will contain the source files and `test/` will contain the expected unit tests.
- `.gitignore`: specifies all files that must be excluded from the version control system.
- `build.gradle`: outlines the directives and configuration for *Gradle*.
- `gradlew` and `gradlew.bat`: *Gradle* executables, dedicated to Unix and Windows respectively.
- `settings.gradle`: *Gradle* configuration file.