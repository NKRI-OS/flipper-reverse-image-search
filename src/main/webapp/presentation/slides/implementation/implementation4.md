### *LireBuilder.search()*
-----------------
```java
ImageSearcher searcher = new GenericFastImageSearcher(maxHits,
    feature.getValueClass());

// searching with a image file
ImageSearchHits hits = searcher.search(inputStream, indexReader);

// Get results ordered by score
for (int i = 0; i < hits.length(); i++)
{
    float score = hits.score(i);

    LirePictureSortable lp = new LirePictureSortable(
    UUID.fromString(hits.doc(i).
        getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0]),
        score, feature);
}
```
