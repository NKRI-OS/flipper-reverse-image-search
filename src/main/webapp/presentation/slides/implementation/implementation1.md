### Patrón [**Repository**](http://www.martinfowler.com/eaaCatalog/repository.html)
----------------
```java
public Page<Picture> findByOwner(String owner, Pageable pageable) {

  String query = String.format("{\"q\":\"owner:%s\", " +
                  "\"start\": %d, " +
                  "\"sort\":\"created DESC\"}",

  List<Picture> pictures = pictureAccesor.
    findSolrQueryOrdered(query, pageable.getPageSize()).all();

  long total = userCounterAccessor.
    getPictureCounter(owner).one().getLong("picture_counter");

  return new PageImpl<>(pictures, pageable, total);
}
```
