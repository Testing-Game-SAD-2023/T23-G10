<h1> Student DB - Gruppo 10 </h1>
La documentazione dell'API è consultabile al link: https://app.swaggerhub.com/apis-docs/LUCIILARDI/StudentDB/1.0 . <br/>
Se il link non funzionasse è comunque possibile visualizzare la documentazione attraverso il file APIdoc.html nella cartella doc. <br/> <br/>

<h2> Installazione </h2>
L'applicazione è fornita attraverso il file studentDB-0.0.1-SNAPSHOT.jar nella cartella bin. Nella stessa cartella è anche fornito un file di configurazione <code>application.properties</code>; in particolare, le ultime due righe consentono di scegliere il porto su cui avviare il server e se utilizzare https o meno. Ci si assicuri che tale file si trovi nella stessa cartella del file jar. <br/>
<br/> Per avviare l'applicazione da terminale: <code>java -jar studentDB-0.0.1-SNAPSHOT.jar</code> <br/> <br/>
E' necessario avere una versione di <code>Java 17</code> o superiore, reperibile su https://www.oracle.com/it/java/technologies/downloads/. 
<br/> <br/>
Nel momento in cui si apportasse qualche modifica al sistema, il file jar deve essere rigenerato. Per farlo seguire i seguenti passaggi: <br/> 

<h3> 1) Installazione Maven </h3>
<p> Per prima cosa scaricare Maven dal link https://maven.apache.org/download.cgi. L'installazione di Apache Maven è un semplice processo di estrazione dell'archivio e aggiunta della cartella <code>bin</code> con il comando <code>mvn</code> alla variabile <code>PATH</code>.</p>
<p>I passi dettagliati sono:</p>
<ul>

<li>
<p>Avere un'installazione JDK sul proprio sistema. Settare la variabile d'ambiente <code>JAVA_HOME</code> per puntare all'installazione JDK oppure impostare l'eseguibile <code>java</code> sul <code>PATH</code>.</p></li>
<li>
<p>Estrarre l'archivio della distribuzione in una cartella qualsiasi</p></li>
</ul>

<div class="verbatim">
<pre><code class="language-cmd">unzip apache-maven-3.9.2-bin.zip
</code></pre></div>
<p>oppure</p>

<div class="verbatim">
<pre><code class="language-sh">tar xzvf apache-maven-3.9.2-bin.tar.gz
</code></pre></div>
<p>Alternativamente usa il tuo strumento di estrazione archivio preferito.</p>
<ul>

<li>
<p>Aggiungere la cartella <code>bin</code> della cartella creata <code>apache-maven-3.9.2</code> alla variabile d'ambiente <code>PATH</code></p></li>
<li>
<p>Confermare l'installazione con <code>mvn -v</code> in una nuova shell. Il risultato dovrebbe essere simile a:</p></li>
</ul>

<div class="verbatim">
<pre><code>Apache Maven 3.9.2 (c9616018c7a021c1c39be70fb2843d6f5f9b8a1c)
Maven home: /opt/apache-maven-3.9.2
Java version: 1.8.0_45, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: &quot;mac os x&quot;, version: &quot;10.8.5&quot;, arch: &quot;x86_64&quot;, family: &quot;mac&quot;
</code></pre></div></section>

<h3> 2) Generazione file jar tramite Maven </h3>
Aprire il terminale e portarsi nella directory <code>studentDB</code>. Eseguire il seguente comando: 
<code>mvn clean install</code>. Alla fine del processo verrà generato il file <code>studentDB-0.0.1-SNAPSHOT.jar</code> nella cartella <code>studentDB/target</code>

<h3> 3) Avvio dell'applicazione </h3>
Per avviare l'applicazione da terminale si segua la stessa procedura descritta sopra, ossia si sposti in una stessa directory il file jar precedentemente generato e il file di configurazione fornito ed eseguire il comando <br/> <code>java -jar studentDB-0.0.1-SNAPSHOT.jar</code><br/>

<h2> Se si vuole provare l'applicazione da Eclipse </h2>
Per importare il progetto su Eclipse: Import->Maven->Existing Maven Projects e selezionare la cartella studentDB come root o tramite clone. <br/>
Se le dipendenze non dovessero essere risolte automaticamente si può provare, nel file pom.xml, la seguente procedura: tasto destro-> Maven -> Update Project. <br/>
Per avviare il server eseguire il file StudentDBApplication.java . <br/>

<br/> <br/> 
NOTA: Siamo a conoscenza di un bug di Spring Boot per il quale, quando si lancia l'applicazione, a volte non vengono istanziati tutti i Bean necessari (si è osservato che tale comportamento si presenta soprattutto quando vengono modificate le dipendenze nel pom.xml). Nel nostro caso si tratta del Bean JavaMailSender che dovrebbe essere creato automaticamente a partire dal file di configurazione application.properties nel path studentDB/src/main/resources. Se tale problema dovesse presentarsi la soluzione più semplice è quella di spostare il file application.properties in un'altra cartella e riportarlo al path precedente, in modo da forzare un refresh.

