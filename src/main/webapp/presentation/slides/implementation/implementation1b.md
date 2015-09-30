### Interfaces Accessor
----------------
```java
@Accessor
public interface PictureAccessor {

  @Query("SELECT * FROM picture " +
      "WHERE solr_query = :query LIMIT :total")
  Result<Picture> findSolrQueryOrdered(@Param("query") String query,
    @Param("total") int total);

}
```

--------------------------

```java
    @Query("SELECT picture_counter FROM " +
        "user_counter WHERE user = :user")
    ResultSet getPictureCounter(@Param("user") String user);

```
