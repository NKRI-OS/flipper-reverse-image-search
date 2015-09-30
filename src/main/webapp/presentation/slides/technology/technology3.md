### ![Lire](resources/logo_lire.png)<!-- .element: style="border:0px; box-shadow: 0 0 0 rgba(0, 0, 0, 0); vertical-align: middle;" -->
-------------
- Extración de descriptores visuales<!-- .element: class="fragment" data-fragment-index="1" -->
    ###### Basados en color, textura y formas<!-- .element: class="fragment" data-fragment-index="1" -->
- Indexación<!-- .element: class="fragment" data-fragment-index="2" -->
    ###### Motor [Apache Lucene](http://lucene.apache.org/)<!-- .element: class="fragment" data-fragment-index="2" -->
- Búsquedas<!-- .element: class="fragment" data-fragment-index="3" -->
    ###### Similitudes puntuación<!-- .element: class="fragment" data-fragment-index="3" -->

note:
Lire es una librería de Java
Extrae descriptores y los indexa utilizando Lucene por detrás

Estádares MPEG-7: ColorLayout y EdgeHistogram
CEDD: Color and Edge Directivity Descriptor:
incorpora el color y la textura de la información en un histograma.
PHOG: Histograma de Pirámide de Gradientes Orientados:
Este método es similar al MPEG-7 Histograma de bordes
, pero tiene una mejor precisión antes pequeñas variaciones de la imagen
