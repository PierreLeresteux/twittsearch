TWITTSEARCH

V.1.0.0
@author : Pierre Leresteux
@date : Oct. 2012

But : Lancer des recherches sur Twitter par mot-clef (hashtag), afficher les 15 premiers résultats et sauvegarder les résultats de recherche pour les afficher directement ensuite.

Utilisation :

mvn jetty:run

Toutes les requêtes sont en Content-type : APPLICATION/JSON
Lancer une recherche : GET http://localhost:8080/twittsearch?q=devoxx
=> Affiche les 15 premiers résultats de la recherche portant sur les derniers tweets comportant #devoxx

Afficher l'historique de la recherche : GET http://localhost:8080/twittsearch
=> Affiche toutes les recherches effectuées

Afficher une recherche : GET http://localhost:8080/twittsearch/2
=> Affiche la recherche portant l'ID 2

Supprime une recherche de l'historique : DELETE http://localhost:8080/twittsearch/2
 => Supprime la recheche portant l'ID 2