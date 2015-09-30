### Recuperación de Imágenes con Lire
-----------------
```java
// Created a generic search with Descriptor class
ImageSearcher searcher = new GenericFastImageSearcher(maxHits,
    EdgeHistogram.class);

// searching with a image file
ImageSearchHits hits = searcher.search(inputStream, indexReader);

// Get results ordered by score
for (int i = 0; i < hits.length(); i++)
{
    UUID pictureUUID = UUID.fromString(hits.doc(i).
        getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0]);

    float score = hits.score(i);
}
```
