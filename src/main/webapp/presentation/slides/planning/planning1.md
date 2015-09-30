### Planificación
-----------------
1. Requisitos y tecnologías
2. Aplicación base<!-- .element: class="fragment" data-fragment-index="2" -->
3. Almacenamiento<!-- .element: class="fragment" data-fragment-index="3" -->
4. Extracción de metadatos<!-- .element: class="fragment" data-fragment-index="4" -->
5. Indexación de descriptores visuales<!-- .element: class="fragment" data-fragment-index="5" -->
6. Integración con Solr<!-- .element: class="fragment" data-fragment-index="6" -->
7. Recuperación de imágenes<!-- .element. class="fragment" data-fragment-index="7" -->

note:
    1. en primer lugar se realiza un estudio de tecnologías para la extracción y recuperación de imágenes basada en contenido,
    es aquí donde se crear los primeros prototipos con Lire y con Spring Boot

    2. estructura base de JHipster, configuración para soporte con Cassandra y dependencias necesarias,
        así como corrección de bugs en la versión de JHipster

    3. Almacenamiento de Imágenes en la base de datos de Cassandra,
    creación de la entidad pictures y soporte para imágenes en binario
     Reescalado de imágenes

     4. Extracción de los metadatos en el servicio de almacenamiento de imágenes,
     guardado de todos los metadatos en una tabla de cassandra  de forma asíncrona, junto con información de las imágenes.

     5. Extracción de diferentes descriptores de imagen

     6. Integración de Solr para realizar búsquedas basadas en textos y otros campos,
     creación de los recursos del servicio REST
     adaptación de la interfaz web

     7. Recuperación basada en contenidos de imágenes, aquí se recuperan mediante Lire imágenes de búsquedas,
     estas búsquedas a su vez son almacenadas en Cassandra.
