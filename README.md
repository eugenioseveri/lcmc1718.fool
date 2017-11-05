# FOOL
FOOL sta per "**F**unctional **O**bject-**O**riented **L**anguage". SVM sta per "**S**tack **V**irtual **M**achine". Si tratta di un progetto realizzato nell'ambito del corso di "Linguaggi, Compilatori e Modelli Computazionali" che prevede di creare un compilatore per questo linguaggio in Java con  [ANTLR](http://www.antlr3.org/).

I file sorgenti in FOOL hanno estensione ".fool" e vengono compilati dall'assembler della SVM per produrre un codice oggetto (in formato testuale) eseguibile dalla SVM.

##  Installare il plugin ANTLR 4 in Eclipse
* Installare dal marketplace di Eclipse il plugin "ANTLR 4 IDE 0.3.6".
* Creare sul computer una cartella in cui salvare i file di ANTLR: [antlr-runtime-4.7.jar](http://www.antlr.org/download/antlr-runtime-4.7.jar) e [antlr-4.7-complete.jar](http://www.antlr.org/download/antlr-4.7-complete.jar).
* Creare in Eclipse un nuovo progetto Java o aprirne uno esistente.
* Copiare (o creare) nella directory principale del progetto un file ".g4" (se non è già presente), aprirlo e salvarlo (dopo aver fatto una qualche modifica) in modo che si attivi la funzionalità ANTLR 4 per il progetto.
* Configurazione workspace (da fare un'unica volta sul primo progetto creato): clic destro sul progetto -> Properties -> ANTLR 4 -> Tool -> Configure Workspace Settings e poi:
    * cliccare su ->Add e selezionare "antlr-4.7-complete.jar" dalla cartella in cui è stato precedentemente salvato;
    * mettere la spunta alla versione 4.7 appena aggiunta;
    * nelle opzioni: indicare "./src" come directory, rimuovere la spunta (se presente) da "Generate a parse tree listener" e "Generate parse tree visitors".
* Configurare Eclipse (da fare un'unica volta, creazione user library ANTLR runtime):
    * Window->Preferences->Java->Build Path->User Libraries;
    * cliccare su ->new (e scrivere "ANTLR runtime");
    * cliccare su ->Add External Jars (e selezionare "antlr-runtime-4.7.jar" dalla cartella in cui è stato precedentemente salvato);
* Configurare il progetto aggiungendo la user library "ANTLR runtime": clic destro sul progetto->Build Path->Add Libraries->User Library e selezionare "ANTLR runtime".
