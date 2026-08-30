# Wordle [![Build and Publish Release](https://github.com/alessandropier/Wordle/actions/workflows/ingsw2122.yml/badge.svg)](https://github.com/alessandropier/Wordle/actions/workflows/ingsw2122.yml) [![Coverage Status](https://coveralls.io/repos/github/alessandropier/Wordle/badge.svg?branch=v1.0)](https://coveralls.io/github/alessandropier/Wordle?branch=v1.0)

Inizialmente, questa è nata come una repository per l'implementazione del gioco _Wordle_ tramite Command Line Interface _(CLI)_ realizzata durante il corso di _Ingegneria del Software_ utilizzando il framework _Agile SCRUM_ e utilizzando le _GitHub Actions_ per la _CI/CD_.

Successivamente, ho continuato il progetto per integrare funzionalità aggiuntive come:
- _Generazione_ automatica e _casuale_ della _parola da indovinare_
- _User Interface_ tramite _Java Swing_ e _FlatLaf_
- Permettere all'utente di _aggiungere nuove parole_ (futura)

## Esecuzione del Gioco

Il gioco è distribuito come file `.jar` eseguibile. Per avviare il Wordle, assicurati di avere installato **Java (JRE/JDK 11 o superiore)**.

### Windows
1. Scarica il file `wordle-all.jar` e `avvia_wordle.bat` dalla sezione [Releases](https://github.com/alessandropier/Wordle/releases).
2. Posiziona i due file nella stessa cartella.
3. Fai doppio click su `avvia_gioco.bat`.

### Linux / Mac
1. Scarica il file `wordle-all.jar` e `avvia_wordle.sh` dalla sezione [Releases](https://github.com/alessandropier/Wordle/releases).
2. Posiziona i due file nella stessa cartella.
3. Apri il terminale, posizionati nella cartella e rendi eseguibile lo script con: 
   `chmod +x avvia_wordle.sh`
4. Avvia il gioco con: `./avvia_wordle.sh`

## Struttura della Repository

La struttura della repository si presenta nel seguente modo:
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
|–– docs _(doc relativa alla versione v1.0 per l'esame di Ing. Softw.)_
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

Nel seguito si dettagliano i ruoli dei diversi componenti:

- `.github/workflows/ingsw2122.yml`: dettaglia le direttive per assicurare la *continuous integration* attraverso l’uso di GitHub Actions;
- `config/`: ospita i file di configurazione. L’unica configurazione di base richiesta è quella per il tool checkstyle;
- `docs/`: ospita la documentazione di progetto, incluse le figure (nella sottocartella `img/`).
<br>Il file `Report.md` verrà usato per redigere la relazione finale del progetto.
<br>La cartella raccoglie inoltre:
    - `Assegnazione progetto.md`: contenente la descrizione dettagliata del progetto assegnato;
    - `Guida per lo studente.md`: contenente la descrizione di tutti i passi di configurazione necessari per l'attivazione del flusso di lavoro a supporto dello sviluppo del progetto;
- `gradle/`: ospita il `.jar` relativo al sistema di gestione delle dipendenze *Gradle*.
- `src`: cartella principale del progetto, in cui scrivere tutto il codice dell’applicazione. In `main/` ci saranno i file sorgente e `test/` conterrà i test di unità previsti.
- `.gitignore`: specifica tutti i file che devono essere esclusi dal sistema di controllo versione.
- `build.gradle`: esplicita le direttive e la configurazione di *Gradle*.
- `gradlew` e `gradlew.bat`: eseguibili di *Gradle*, rispettivamente dedicati a Unix e Windows.
- `settings.gradle`: file di configurazione di *Gradle*.