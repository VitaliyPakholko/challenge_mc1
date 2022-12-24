# Monclick challenge

## Pre requisiti

A scelta tra:  
JDK/SDF Java 11+.  
Maven.  
Oppure:  
Docker.

# Descrizione progetto e motivazione scelte effettuate

Per startare il progetto in locale (Intellij) : CTRL+CTRL mvn clean quarkus:dev.  
Per il packaging: mvn clean package.

Una volta startato per visualizzare la dev console di quarkus: http://localhost:7070/mc/q/dev/.    
Il server in locale starta su: http://localhost:7070/mc.  
Per visualizzare lo swagger delle varie chiamate: http://localhost:7070/mc/q/swagger-ui.  

## Modello

Il DB originale non ha subito cambiamenti.  
Entity, DTO e mapper autogenerati con JPA buddy.   
Entity in seguito aggiustate con Json references per evitare ricorsioni nella serializzazione.  
Aggiunto mapping di colonne FK in formato stringa in modo da velocizzare le query togliendo la necessita delle join per recuperare PK di tabelle referenziate.

I DTO sono tutti rich, contengono gli oggetti completi. E' stato creato un solo DTO costituito da stringhe per evitare eager fetching e mostrare meno informazioni.  
Scelta motivata dalla comodita' di avere DTO e mappers autogenerati, l'opzione migliore ma piu' costosa sarebbe stata quella di crearli manualmente e scrivere i relativi mapper con il pattern builder.

## Chiamate REST

E' stata creata un'unica risorsa data la semplicita' della coppia dei requisiti+business logic.  
Per visualizzare PRODUCT BY ID e' consigliato fare la chiamata direttamente nel browser invece che su swagger-ui per usufruire del pretty JSON ed avere controllo sugli ogetti collegati al product.

La chiamata FILTERED PRODUCTS ha gli input in AND come da requisito, in piu' ignora qualsiasi attributo null in input in modo da consentire qualsiasi combinazione di filtri.

La chiamata GENERATE SALE PRICINGS prima pulisce la tabella corrispettiva del DB e poi calcola e persiste i listini richiesti dalla business logic.  
Esiste un'altra chiamata PERSIST PRICINGS, la chiamata non e' da utilizzare direttamente ma serve per un workaround riguardo @Transactional di quarkus.   
Se una risorsa richiama direttamente un metodo transactional, deve essere transactional anch'essa. Questo rallentava di molto la chiamata in quanto la generate fa largo uso di entity in memoria.  
Chiamando un'altra risorsa solo per la persist risolve il problema in poco tempo.

Ci sono un paio di assunzioni riguardanti la business logic (ci sono anche dei commenti a codice).  
1: Non e' detto che ci siano dei listini di acquisto per tutti prodotti contenuti nella tabella degli stock, questo incide sulla scelta del magazzino da utilizzare.  
2: Un listino promozionale e' attivo solo se e' attivo il listino base da cui e' stato generato.

## Scelte legate alla performance

Si e' scelto di minimizzare il numero di accessi al DB a favore di mantenere in cache tutti i dati utili per la generazione dei listini di vendita.
Dato che la quantita' di tuple del listino di acquisti e' limitata e del fatto che sono facilmente gruppabili prima per codice prodotto e poi per magazzino,
le strutture dati utilizzate sono delle HashMap innestate. Questo garantisce un tempo di accesso costante senza dover ricercare dentro liste o set.  
Le performance dell'applicazione ne traggono beneficio, purche' i dati acquisiti durante una listAll() della repository trovino spazio in memoria.  
Nel caso di tabelle piu' popolose si potrebbe fare del batching prendendo N prodotti alla volta dal listino di acquisti per poi generare i corrispettivi di vendita.

## Testing

Il testing automatico non e' stato implementato in quanto esaurito il tempo a disposizione per la challenge.



