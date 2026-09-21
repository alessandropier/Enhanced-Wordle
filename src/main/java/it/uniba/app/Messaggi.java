package it.uniba.app;

public class Messaggi {

    public static String get(String chiave, String lingua) {
        if (lingua == null) {
            lingua = "ITA";
        }
        
        switch (lingua.toUpperCase()) {
            case "ENG":
                return getEnglish(chiave);
            case "ESP":
                return getSpanish(chiave);
            case "ITA":
            default:
                return getItalian(chiave);
        }
    }

    private static String getItalian(String chiave) {
        switch (chiave) {
            // Titolo App
            case "titolo.app": return "Wordle Java";

            // Primo avvio
            case "primo.avvio.titolo": return "Primo Avvio - Selezione Lingua";
            case "primo.avvio.msg": return "Seleziona la lingua iniziale / Select initial language:";
            
            // Bottoni principali
            case "btn.nuova": return "NUOVA";
            case "btn.arrenditi": return "ARRENDITI";
            case "btn.esci": return "ESCI";
            case "btn.aggiungi": return "AGGIUNGI PAROLA";
            case "btn.notte": return "Notte";
            case "btn.giorno": return "Giorno";
            case "btn.lunghezza": return "Lunghezza: ";
            case "btn.lingua": return "Lingua: %s";
            
            // Tooltip
            case "tooltip.aiuto": return "Aiuto & Regole";
            case "tooltip.hint": return "Richiedi Indizio (Utilizzabile una sola volta)";
            
            // Nuova partita
            case "nuova.msg": return "Nuova partita avviata! Inizia a digitare.";
            case "nuova.titolo": return "Nuova Partita";
            
            // Uscita
            case "uscita.msg": return "Sei sicuro di voler uscire dal gioco?";
            case "uscita.titolo": return "Conferma uscita";
            case "uscita.segreta": return "La parola segreta era: ";
            case "uscita.gioco": return "Uscita";
            
            // Lunghezza dialog
            case "lunghezza.titolo": return "Cambia Dimensione";
            case "lunghezza.msg": return "Seleziona la lunghezza della parola:";
            
            // Tentativi / errori
            case "tentativo.lunghezza.errata.titolo": return "Attenzione";
            case "tentativo.lunghezza.errata.msg": return "La parola deve essere di %d lettere!";
            case "tentativo.parola.non.valida.titolo": return "Parola non valida";
            case "tentativo.parola.non.valida.msg": return "La parola inserita non è presente nell'elenco delle parole consentite!";
            case "vittoria.msg": return "Complimenti, hai indovinato la parola!";
            case "vittoria.titolo": return "Vittoria";
            case "gameover.msg": return "Tentativi terminati!";
            case "gameover.titolo": return "Game Over";
            
            // Aggiungi parola
            case "aggiungi.titolo": return "Aggiungi Parola Personalizzata";
            case "aggiungi.msg": return "Inserisci una nuova parola di %d lettere da aggiungere nel sistema:";
            case "aggiungi.errore.lunghezza": return "La parola deve essere di esattamente %d lettere!";
            case "aggiungi.errore.lunghezza.titolo": return "Errore";
            case "aggiungi.errore.caratteri": return "La parola deve contenere solo lettere dell'alfabeto!";
            case "aggiungi.errore.caratteri.titolo": return "Caratteri non validi";
            case "aggiungi.errore.uguali": return "Davvero!? La stessa lettera? Mi dispiace ciccio, non è possibile!";
            case "aggiungi.successo.msg": return "Parola aggiunta con successo!";
            case "aggiungi.successo.titolo": return "Successo";
            case "aggiungi.esistente.msg": return "La parola è già presente nel dizionario (interno o extra)ṭ";
            
            // Hint
            case "hint.titolo": return "💡 Indizio Strategico Casuale";
            case "hint.btn.ok": return "Ho capito, grazie!";
            case "hint.nessuno.titolo": return "Nessun Indizio Disponibile!";
            case "hint.nessuno.msg": return "<b>Nessun Indizio Disponibile!</b><br><br>Hai già scoperto o escluso tutte le informazioni possibili.<br>Non ci sono hint applicabili in questo momento!";
            case "hint.uno.msg": return "<b>Potere della Lampadina: Esclusione!</b><br><br>Il sistema ha analizzato la parola e ha oscurato <b>nuove lettere</b> che non fanno parte della parola segreta.";
            case "hint.due.msg": return "<b>Potere della Lampadina: Lettera Iniziale!</b><br><br>La parola segreta inizia con la lettera: <span style='font-size: 16pt; color: #3498DB;'><b>%s</b></span><br><i>È stata evidenziata in blu sulla tastiera!</i>";
            case "hint.tre.msg": return "<b>Potere della Lampadina: Indizio di Presenza!</b><br><br>Fai attenzione: la parola segreta contiene sicuramente la lettera: <span style='font-size: 16pt; color: #D4AC0D;'><b>%s</b></span><br><i>È stata evidenziata in giallo speciale sulla tastiera!</i>";

            // Cambio lingua dialog
            case "lingua.titolo": return "Cambia Lingua";
            case "lingua.msg": return "Seleziona la nuova lingua:";
            case "lingua.successo.msg": return "Lingua cambiata con successo in %s! Il sistema è stato ricaricato.";
            case "lingua.successo.titolo": return "Ricaricamento completato";
            
            // Aiuto dialog
            case "aiuto.titolo": return "Regole del Gioco & Guida UI";
            case "aiuto.checkbox": return "Non mostrare più questo messaggio all'avvio";
            case "aiuto.h1": return "1. Regole di Wordle";
            case "aiuto.h1.testo": return "L'obiettivo è indovinare la parola segreta nel minor numero di tentativi possibili. Dopo ogni tentativo, ciascuna casella verrà colorata per darti un indizio sulla parola segreta.";
            case "aiuto.verde": return "<b>Verde</b>: La lettera è corretta e si trova nella posizione giusta.";
            case "aiuto.giallo": return "<b>Giallo</b>: La lettera è presente nella parola ma in una posizione errata.";
            case "aiuto.grigio": return "<b>Grigio</b>: La lettera non è presente nella parola segreta.";
            case "aiuto.h2": return "2. Guida all'Interfaccia e Bottoni";
            case "aiuto.ui.lunghezza": return "Scegli la lunghezza della parola da indovinare.";
            case "aiuto.ui.nuova": return "Avvia una nuova partita.";
            case "aiuto.ui.arrenditi": return "Rivela la parola segreta.";
            case "aiuto.ui.esci": return "Chiude l'applicazione.";
            case "aiuto.ui.nottegiorno": return "Alterna il tema grafico e salva automaticamente la preferenza.";
            case "aiuto.ui.aggiungi": return "Permette l'inserimento di una nuova parola nel dizionario.";
            case "aiuto.ui.aiuto": return "Apre questa schermata con le regole e la guida.";
            case "aiuto.ui.hint": return "Se possibile, fornisce un aiuto all'utente (utilizzabile solo una volta a partita).";
            case "aiuto.ui.tastiera": return "Digita le lettere, premi <b>INVIO</b> per confermare o <b>⌫</b> per cancellare.";
            case "aiuto.popup.nome": return "AIUTO";
            case "aiuto.popup.hint.nome": return "HINT";

            // tastiera invio
            case "tastiera.invio": return "INVIO";
            case "tastiera.nome": return "Tastiera";

            default: return chiave;
        }
    }

    private static String getEnglish(String chiave) {
        switch (chiave) {
             // Titolo App
            case "titolo.app": return "Wordle Java";

            // Primo avvio
            case "primo.avvio.titolo": return "Initial Start - Language Selection";
            case "primo.avvio.msg": return "Select initial language / Seleziona la lingua iniziale:";
            
            // Bottoni principali
            case "btn.nuova": return "NEW";
            case "btn.arrenditi": return "GIVE UP";
            case "btn.esci": return "EXIT";
            case "btn.aggiungi": return "ADD WORD";
            case "btn.notte": return "Night";
            case "btn.giorno": return "Day";
            case "btn.lunghezza": return "Length: ";
            case "btn.lingua": return "Language: %s";
            
            // Tooltip
            case "tooltip.aiuto": return "Help & Rules";
            case "tooltip.hint": return "Request Hint (Usable once per game)";
            
            // Nuova partita
            case "nuova.msg": return "New game started! Start typing.";
            case "nuova.titolo": return "New Game";
            
            // Uscita
            case "uscita.msg": return "Are you sure you want to exit the game?";
            case "uscita.titolo": return "Confirm Exit";
            case "uscita.segreta": return "The secret word was: ";
            case "uscita.gioco": return "Exit";
            
            // Lunghezza dialog
            case "lunghezza.titolo": return "Change Size";
            case "lunghezza.msg": return "Select word length:";
            
            // Tentativi / errori
            case "tentativo.lunghezza.errata.titolo": return "Attention";
            case "tentativo.lunghezza.errata.msg": return "The word must be %d letters long!";
            case "tentativo.parola.non.valida.titolo": return "Invalid Word";
            case "tentativo.parola.non.valida.msg": return "This word is not in the allowed word list!";
            case "vittoria.msg": return "Congratulations, you guessed the word!";
            case "vittoria.titolo": return "Victory";
            case "gameover.msg": return "Attempts exhausted!";
            case "gameover.titolo": return "Game Over";
            
            // Aggiungi parola
            case "aggiungi.titolo": return "Add Custom Word";
            case "aggiungi.msg": return "Enter a new %d-letter word to add to the system:";
            case "aggiungi.errore.lunghezza": return "The word must be exactly %d letters long!";
            case "aggiungi.errore.lunghezza.titolo": return "Error";
            case "aggiungi.errore.caratteri": return "The word must contain only alphabet letters!";
            case "aggiungi.errore.caratteri.titolo": return "Invalid characters";
            case "aggiungi.errore.uguali": return "Really!? The same letter? Sorry buddy, that's not possible!";
            case "aggiungi.successo.msg": return "Word added successfully!";
            case "aggiungi.successo.titolo": return "Success";
            case "aggiungi.esistente.msg": return "The word is already present in the dictionary (internal or extra)!";
            
            // Hint
            case "hint.titolo": return "💡 Random Strategic Hint";
            case "hint.btn.ok": return "Got it, thanks!";
            case "hint.nessuno.titolo": return "No Hints Available!";
            case "hint.nessuno.msg": return "<b>No Hints Available!</b><br><br>You have already discovered or excluded all possible information.<br>There are no applicable hints at this moment!";
            case "hint.uno.msg": return "<b>Lightbulb Power: Exclusion!</b><br><br>The system analyzed the word and grayed out <b>new letters</b> that are not part of the secret word.";
            case "hint.due.msg": return "<b>Lightbulb Power: Initial Letter!</b><br><br>The secret word starts with the letter: <span style='font-size: 16pt; color: #3498DB;'><b>%s</b></span><br><i>It has been highlighted in blue on the keyboard!</i>";
            case "hint.tre.msg": return "<b>Lightbulb Power: Presence Hint!</b><br><br>Pay attention: the secret word definitely contains the letter: <span style='font-size: 16pt; color: #D4AC0D;'><b>%s</b></span><br><i>It has been highlighted in a special yellow on the keyboard!</i>";
            
            // Cambio lingua dialog
            case "lingua.titolo": return "Change Language";
            case "lingua.msg": return "Select new language:";
            case "lingua.successo.msg": return "Language successfully changed to %s! System reloaded.";
            case "lingua.successo.titolo": return "Reload Completed";
            
            // Aiuto dialog
            case "aiuto.titolo": return "Game Rules & UI Guide";
            case "aiuto.checkbox": return "Don't show this message again at startup";
            case "aiuto.h1": return "1. Wordle Rules";
            case "aiuto.h1.testo": return "The goal is to guess the secret word in the fewest possible attempts. After each guess, each tile will change color to give you a clue about the secret word.";
            case "aiuto.verde": return "<b>Green</b>: The letter is correct and in the right position.";
            case "aiuto.giallo": return "<b>Yellow</b>: The letter is in the word but in the wrong position.";
            case "aiuto.grigio": return "<b>Gray</b>: The letter is not in the secret word.";
            case "aiuto.h2": return "2. Interface & Buttons Guide";
            case "aiuto.ui.lunghezza": return "Choose the length of the word to guess.";
            case "aiuto.ui.nuova": return "Starts a new game.";
            case "aiuto.ui.arrenditi": return "Reveals the secret word.";
            case "aiuto.ui.esci": return "Closes the application.";
            case "aiuto.ui.nottegiorno": return "Toggles the graphic theme and automatically saves the preference.";
            case "aiuto.ui.aggiungi": return "Allows adding a new word to the dictionary.";
            case "aiuto.ui.aiuto": return "Opens this screen with rules and guide.";
            case "aiuto.ui.hint": return "If possible, provides a hint to the user (can only be used once per game).";
            case "aiuto.ui.tastiera": return "Type letters, press <b>ENTER</b> to confirm or <b>⌫</b> to delete.";
            case "aiuto.popup.nome": return "HELP";
            case "aiuto.popup.hint.nome": return "HINT";

            // tastiera invio
            case "tastiera.invio": return "ENTER";
            case "tastiera.nome": return "Keyboard";

            default: return chiave;
        }
    }

    private static String getSpanish(String chiave) {
        switch (chiave) {
             // Titolo App
            case "titolo.app": return "Wordle Java";

            // Primo avvio
            case "primo.avvio.titolo": return "Inicio inicial - Selección de idioma";
            case "primo.avvio.msg": return "Seleccionar idioma inicial / Seleziona la lingua iniziale:";
            
            // Bottoni principali
            case "btn.nuova": return "NUEVO";
            case "btn.arrenditi": return "RENDIRSE";
            case "btn.esci": return "SALIDA";
            case "btn.aggiungi": return "AÑADIR PALABRA";
            case "btn.notte": return "NOCHE";
            case "btn.giorno": return "DÍA";
            case "btn.lunghezza": return "LONGITUD: ";
            case "btn.lingua": return "IDIOMA: %s";
            
            // Tooltip
            case "tooltip.aiuto": return "Ayuda y Normas";
            case "tooltip.hint": return "Solicitar pista (se puede usar una vez por partida)";
            
            // Nuova partita
            case "nuova.msg": return "¡Nueva partida iniciada! Empieza a escribir.";
            case "nuova.titolo": return "Nueva partida";
            
            // Uscita
            case "uscita.msg": return "¿Estás seguro de que quieres salir del juego?";
            case "uscita.titolo": return "Confirmar salida";
            case "uscita.segreta": return "La palabra secreta era: ";
            case "uscita.gioco": return "Salida";
            
            // Lunghezza dialog
            case "lunghezza.titolo": return "Cambiar tamaño";
            case "lunghezza.msg": return "Selecciona la longitud de la palabra:";
            
            // Tentativi / errori
            case "tentativo.lunghezza.errata.titolo": return "Atención";
            case "tentativo.lunghezza.errata.msg": return "¡La palabra debe tener %d letras!";
            case "tentativo.parola.non.valida.titolo": return "Palabra no válida";
            case "tentativo.parola.non.valida.msg": return "¡Esta palabra no está en la lista de palabras permitidas!";
            case "vittoria.msg": return "¡Felicidades, has adivinado la palabra!";
            case "vittoria.titolo": return "Victoria";
            case "gameover.msg": return "¡Se han agotado los intentos!";
            case "gameover.titolo": return "Juego terminado";
            
            // Aggiungi parola
            case "aggiungi.titolo": return "Agregar palabra personalizada";
            case "aggiungi.msg": return "Ingrese una nueva palabra de %d letras para añadir al sistema:";
            case "aggiungi.errore.lunghezza": return "¡La palabra debe tener exactamente %d letras!";
            case "aggiungi.errore.lunghezza.titolo": return "Error";
            case "aggiungi.errore.caratteri": return "¡La palabra debe contener únicamente letras del alfabeto!";
            case "aggiungi.errore.caratteri.titolo": return "Caracteres no válidos";
            case "aggiungi.errore.uguali": return "¿En serio? ¿La misma letra? Lo siento, amigo, ¡eso no es posible!";
            case "aggiungi.successo.msg": return "¡Palabra añadida con éxito!";
            case "aggiungi.successo.titolo": return "Éxito";
            case "aggiungi.esistente.msg": return "¡La palabra ya está en el diccionario (interno o externo)!";
            
            // Hint
            case "hint.titolo": return "💡 Consejo estratégico aleatorio";
            case "hint.btn.ok": return "Entendido, ¡gracias!";
            case "hint.nessuno.titolo": return "¡No hay pistas disponibles!";
            case "hint.nessuno.msg": return "<b>¡No hay pistas disponibles!</b><br><br>Ya has descubierto o descartado toda la información posible.<br>¡No hay pistas aplicables en este momento!";
            case "hint.uno.msg": return "<b>Lightbulb ¡Poder: exclusión!</b><br><br>El sistema analizó la palabra y puso en gris las <b>nuevas letras</b> que no forman parte de la palabra secreta.";
            case "hint.due.msg": return "<b>Lightbulb Power: ¡Inicial!</b><br><br>La palabra secreta empieza con la letra: <span style='font-size: 16pt; color: #3498DB;'><b>%s</b></span><br><i>¡Se ha resaltado en azul en el teclado!</i>";
            case "hint.tre.msg": return "<b>Lightbulb Poder: ¡Pista de presencia!</b><br><br>Presta atención: la palabra secreta definitivamente contiene la letra: <span style='font-size: 16pt; color: #D4AC0D;'><b>%s</b></span><br><i>¡Se ha resaltado en un amarillo especial en el teclado!</i>";
            
            // Cambio lingua dialog
            case "lingua.titolo": return "Cambiar idioma";
            case "lingua.msg": return "Seleccione un nuevo idioma:";
            case "lingua.successo.msg": return "¡Idioma cambiado correctamente a %s! Sistema recargado.";
            case "lingua.successo.titolo": return "Recarga completada";
            
            // Aiuto dialog
            case "aiuto.titolo": return "Reglas del juego y guía de la interfaz";
            case "aiuto.checkbox": return "No volver a mostrar este mensaje al iniciar";
            case "aiuto.h1": return "1. Reglas de Wordle";
            case "aiuto.h1.testo": return "El objetivo es adivinar la palabra secreta en el menor número de intentos posible. Tras cada intento, cada casilla cambiará de color para darte una pista sobre la palabra secreta.";
            case "aiuto.verde": return "<b>Verde</b>: La letra es correcta y está en la posición adecuada.";
            case "aiuto.giallo": return "<b>Amarillo</b>: La letra está en la palabra, pero en la posición incorrecta.";
            case "aiuto.grigio": return "<b>Gris</b>: La letra no está en la palabra secreta.";
            case "aiuto.h2": return "2. Guía de la interfaz y los botones";
            case "aiuto.ui.lunghezza": return "Elige la longitud de la palabra que hay que adivinar.";
            case "aiuto.ui.nuova": return "Inicia una partida nueva.";
            case "aiuto.ui.arrenditi": return "Revela la palabra secreta.";
            case "aiuto.ui.esci": return "Cierra la aplicación.";
            case "aiuto.ui.nottegiorno": return "Alterna el tema gráfico y guarda automáticamente la preferencia.";
            case "aiuto.ui.aggiungi": return "Permite añadir una nueva palabra al diccionario.";
            case "aiuto.ui.aiuto": return "Abre esta pantalla con las reglas y la guía.";
            case "aiuto.ui.hint": return "Si es posible, ofrece una pista al usuario (solo se puede usar una vez por partida).";
            case "aiuto.ui.tastiera": return "Escribe letras, pulsa <b>ENVIO</b> para confirmar o <b>⌫</b> para borrar.";
            case "aiuto.popup.nome": return "AYUDA";
            case "aiuto.popup.hint.nome": return "PISTA";

            // tastiera invio
            case "tastiera.invio": return "ENVIO";
            case "tastiera.nome": return "Teclado";

            default: return chiave;
        }
    }
}