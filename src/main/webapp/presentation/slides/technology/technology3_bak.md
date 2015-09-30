### ![Lire](resources/logo_lire.png)<!-- .element: style="border:0px; box-shadow: 0 0 0 rgba(0, 0, 0, 0); vertical-align: middle;" -->
-------------
- Extrae características, indexa y recupera de imágenes
- Descriptores visuales<!-- .element: class="fragment" data-fragment-index="1" -->
    - Basados en color, textura y formas<!-- .element: class="fragment" data-fragment-index="1" -->
- Indexación
    - Motor [Apache Lucene](http://lucene.apache.org/)
    - Basados en el color<!-- .element: class="fragment" data-fragment-index="2" -->
        ###### Histogramas RGB y HSV, MPEG-7 ColorLayout, AutoColor Correlogram, CEDD<!-- .element: class="fragment" data-fragment-index="2" -->
    - Basados en las texturas<!-- .element: class="fragment" data-fragment-index="3" -->
        ###### MPEG-7 Edge Histogram y PHOG<!-- .element: class="fragment" data-fragment-index="3" -->
    - Basados en formas<!-- .element: class="fragment" data-fragment-index="4" -->
        ###### SIFT y SURF<!-- .element: class="fragment" data-fragment-index="4" -->

note:
    Lire es una librería de Java
        Extrae descriptores y los indexa utilizando Lucene por detrás

    Estádares MPEG-7: ColorLayout y EdgeHistogram
    CEDD: Color and Edge Directivity Descriptor: incorpora el color y la textura de la información en un histograma.
    PHOG: Histograma de Pirámide de Gradientes Orientados: Este método es similar al MPEG-7 Histograma de bordes
        , pero tiene una mejor precisión antes pequeñas variaciones de la imagen
