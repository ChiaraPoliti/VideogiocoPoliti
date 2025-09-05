# Super Mario Bros - Politi Chiara

## Introduzione
Il progetto vuole essere un tentativo di replica di un livello base del videogame _Super Mario Bros_ del 1985: si tratta di un gioco _platform_ (cioè a piattoforme) che consiste nel superamento del livello saltando su piattaforme ed evitando nemici. Per poterlo avviare è necessario avere la JVM sul dispositivo.


## Set-up: istruzioni
Per l'avvio del gioco, è necessaria l'installazione della JVM. L'entry-point del progetto è il file _main.java_, collocato nel package _main_. Da un IDE come Eclipse si può eseguire il codice cliccando su _Run_ o in alternativa si può aprire il prompt dei comandi, andare nella cartella in cui è collocato il file in questione e digitare, in ordine: _javac main.java_ e poi _java main.java_.
A questo punto si aprirà una finestra: cliccare sul pulsante in basso al centro per l'avvio del gioco. 

Ci sono due tipi di nemici: i \textit{Goomba} e i \textit{Koopa}: i primi sono dei funghi dal colore marrone e i secondi sono delle tartarughe verdi. Per eliminare i nemici è necessario saltare sopra di loro: eventuali collisioni dai lati penalizzeranno il giocatore.
I blocchi si 'rompono' esclusivamente colpendoli da sotto (meglio sugli spigoli laterali).
C'è un tipo unico di potenziamento per il giocatore, un fungo che esce dai blocchi se colpiti che permette la crescita di Mario.
