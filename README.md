# mc1 Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw clean compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:7070/mc/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw clean package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

# Descrizione progetto e motivazione scelte effettuate

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
Se una risorsa richiama direttamente un metodo transactional, deve essere transactional anch'essa. Questa rallentava di molto la chiamata in quanto la generate fa largo uso di entity in memoria.
Chiamando un'altra risorsa solo per la persist risolve il problema in poco tempo.

Ci sono un paio di assunzioni riguardanti la business logic (ci sono anche dei commenti a codice). 
1: Non e' detto che ci siano dei listini di acquisto per tutti prodotti contenuti nella tabella degli stock, questo incidi sulla scelta del magazzino da utilizzare.
2: Un listino promozionale e' attivo solo se e' attivo il listino base da cui e' stato generato.





