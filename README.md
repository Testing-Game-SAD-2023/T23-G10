<h1> Student DB - Gruppo 10 </h1>
La documentazione dell'API è consultabile al link: https://app.swaggerhub.com/apis-docs/LUCIILARDI/StudentDB/1.0 . <br/>
Se il link non funzionasse è comunque possibile visualizzare la documentazione attraverso il file APIdoc.html nella cartella doc. <br/> <br/>

Per importare il progetto su Eclipse: Import->Maven->Existing Maven Projects e selezionare la cartella studentDB come root o tramite clone. <br/>
Per avviare il server eseguire il file StudentDBApplication.java .

<br/> <br/> 
NOTA: Siamo a conoscenza di un bug di Spring Boot per il quale, quando si lancia l'applicazione, a volte non vengono istanziati tutti i Bean necessari (si è osservato che tale comportamento si presenta soprattutto quando vengono modificate le dipendenze nel pom.xml). Nel nostro caso si tratta del Bean JavaMailSender che dovrebbe essere creato automaticamente a partire dal file di configurazione application.properties nel path studentDB/src/main/resources. Se tale problema dovesse presentarsi la soluzione più semplice è quella di spostare il file application.properties in un'altra cartella e riportarlo al path precedente, in modo da forzare un refresh.

