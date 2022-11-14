Boolean Expression:
Representa una expressio booleana i conte una String on es guarda la expressio original i el node arrel de la
representacio en arbre d’aquesta. Nomes permet crear instancies amb expressions correctes.

Per comprovar el seu correcte funcionament hem fet un test per cada funcio publica, aquesta classe utilitza la classe de
ExpressionTreeNode, pero no te cap sentit testejar-la sense aquesta, per tant no hem fet unn stub ni res semblant.

-Agafar la expressio original d'una expressio, hem creat una expressio booleana (amb valor correcte per que sino no es
crea) i despres recuperat correctament la funcio original a partir d'aquesta.

-Agafar la arrel del arbre que representa la expressio, hem creat una expressio booleana (amb valor correcte per que
sino no es crea) i despres agafat la arrel i comprovat que era la arrel del arbre esperat.

-Per comprovar la funció de creacio hem hagut de comprovar dues coses, primer que comprova correctament si una expressio
es correcte i despres que, de les expressions correctes, que el seu arbre es el que volem.
Per comprovar que nomes accepta les expressions correctes he definit un conjunt de prova que inclou tots els casos que
poden ocorrer: expressions correctes amb tots els operadors possibles, casos de parentitzacio incorrecte, corxetes "{"
o cometes " " mal tancades, expressions en que falta un o mes operand o operador, casos en que les cometes o corxetes
estaven buits i, per ultim la expressio buida. En tots aquests casos comprova correctament si la expressio es valida o
no.
Per comprovar que l'arbre es el desitjat hem creat expressions booleanes correctes i hem transformat l'arbre en una
string i comprovat que ens tornava el valor esperat.
