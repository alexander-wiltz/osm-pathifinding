# Wegfinder
### Ein kürzester Weg Algorithmus unter Verwendung von OpenStreetMap

##### Bachelorthesis - Alexander Wiltz

Eine JAVA-Implementierung des A*-Algorithmus

### Ablauf
Eine Integration von OSM-Daten, die in einem bestimmten XML-Format vorliegen, werden diese, sofern kein Eintrag vorliegt,
von einem entsprechendem Datei-Handler in ein definiertes Objekt migriert.
Mit diesem Objekt wird mittels Mapping der Dateiinhalt an eine Datenbank in einem Docker-Container abgelegt.

Diese Datenbank bildet die Basis des gesamten Webservices.
Kommt eine Anfrage über einen REST-Endpunkt, so wird im Bereich der Geschäftslogik, über die Nodes ein kürzester Weg mit Hilfe des implementierten A*-Algorithmus berechnet.
Wenn die Berechnung erfolgreich war, wird ein GeoJSON Objekt erstellt und über den REST-Punkt zurückgegeben.

Eine einfache FrontEnd-Integration des Frameworks: Leaflet, bekommt dieses GeoJSON-Objekt und rendert diesen berechneten Weg.

### Start und Ziel
Es muss unterschieden werden, ob es sich um eine genaue Adresse handelt oder lediglich eine Straße.
Der Weg, außer der Start und Zielstraße, darf nur die darauf liegenden Punkte berücksichtigen.
Start und Ziel müssen die dort vorhandenen Gebäude berücksichtigen aufgrund der Ankunftsgenauigkeit.

Nimmt man immer die Gebäude an, kann es passieren, dass aneinanderliegende Gebäude als lose Punkte betrachtet werden und durchschritten werden,
obwohl dies physisch nicht möglich ist.
Gebäude bestehen in der Regel aus vier Datenpunkten, bei denen die Grundstücke als Vierecke dargestellt werden.

Bei den Daten gibt es zur Unterscheidung ein Attribut: Gebäude

### Kosten (Straßentypen)
Der A+-Algorithmus berücksichtigt beim Ermitteln des günstigsten Weges die Kosten einer Option.
Die Kosten setzen sich aus einer möglichen Geschwindigkeit zusammen und der Wegstrecke.
Also die Zeit die benötigt wird, um diese Option zu nutzen.

- Fußwege sind langsamer als Feldwege
- Feldwege sind langsamer als Innerorts
- Innerorts ist langsamer als Landstraßen
- Landstraßen sind langsamer als Autobahnen
