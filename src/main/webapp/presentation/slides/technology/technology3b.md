### ![Lire](resources/logo_lire.png)<!-- .element: style="border:0px; box-shadow: 0 0 0 rgba(0, 0, 0, 0); vertical-align: middle;" -->
-------------
- Indexación
    - Motor [Apache Lucene](http://lucene.apache.org/)
    - Espacios métricos con índices invertidos<!-- .element: class="fragment" data-fragment-index="1" -->
    - localización de <!-- .element: class="fragment" data-fragment-index="2" --> *hash* del vector<!-- .element: class="fragment" data-fragment-index="2" -->

- Búsquedas<!-- .element: class="fragment" data-fragment-index="3" -->
    - Lista de<!-- .element: class="fragment" data-fragment-index="3" --> *documentos* iguales o similares<!-- .element: class="fragment" data-fragment-index="3" -->
    - Ordenada por puntuación<!-- .element: class="fragment" data-fragment-index="4" -->

note:

    Los índices invertidos guardan un conjunto de términos de un texto,
    y una lista de los documentos donde aparece cada término.

    Para la entrada de los descriptores de Lire,
    los términos son las distancias de espacios métricos que representa el vector de entrada.

    Según el número de ocurrencias para cada término se devuelve una lista de documentos
    del índice con una puntuación de similitud.
