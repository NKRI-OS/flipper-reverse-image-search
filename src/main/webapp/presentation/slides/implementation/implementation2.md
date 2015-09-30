### Indexación de Descriptores con Lire
-----------------
```java
IndexWriter indexWriter = new IndexWriter(dir, conf);

try {
    // Creates builder from Lire descriptor
    DocumentBuilder builder = DocumentBuilderFactory.
      getEdgeHistogramBuilder();

    // Creates a Lucene document
    Document document = builder.createDocument(bufferedImage,
      pictureUUID);

    // Store document in Lucene
    indexWriter.addDocument(document);

} catch (IOException e) {
    throw new LireIndexerException("Error reading image.");
}
```
