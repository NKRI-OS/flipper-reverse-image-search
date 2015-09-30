README for flipper
==================

"Scalable platform to storage, publishing and advanced search of pictures"

Final project from ME in Computing. MUEI, Facultade de Informática, Universidade da Coruña.

Install
-------

Cassandra DataStax 2.1: http://docs.datastax.com/en/cassandra/2.1/cassandra/install/install_cassandraTOC.html

Insall version 2.7

$ sudo apt-get install dsc21=2.1.7-1 cassandra=2.1.7
$ sudo service cassandra stop
$ sudo rm -rf /var/lib/cassandra/data/system/*
$ sudo service cassandra start


Config
------

Create data keyspace and tables

```
$ cqlsh -f src/main/resources/config/cql/create-keyspace.cql
$ cqlsh -k flipper -f src/main/resources/config/cql/create-tables.cql 
$ cqlsh -k flipper -f src/main/resources/config/cql/entity_Picture.cql
$ cqlsh -k flipper -f src/main/resources/config/cql/entity_Metadata.cql
$ cqlsh -k flipper -f src/main/resources/config/cql/entity_PictureSearch.cql
$ cqlsh -k flipper -f src/main/resources/config/cql/entity_PictureFound.cql
$ cqlsh -k flipper -f src/main/resources/config/cql/generalcounter.cql
$ cqlsh -k flipper -f src/main/resources/config/cql/picturesearch_counter.cql
$ cqlsh -k flipper -f src/main/resources/config/cql/user_counter.cql

```

For production mode create this keyspace
```
$ cqlsh -f src/main/resources/config/cql/create-keyspace-prod.cql
```

Maven
-----

Problems with datastax-core 2.1.7.1 in guava and netty dependencies.
To resolve, use the las pom.xml from master JHipster on github or update lasted version of JHipster:
```xml
       <datastax-driver.version>2.1.7.1</datastax-driver.version>

        <!--
        <dependency>
            <groupId>org.apache.cassandra</groupId>
            <artifactId>cassandra-all</artifactId>
            <version>${cassandra.version}</version>
            <exclusions>
                <exclusion>
                    <artifactId>slf4j-log4j12</artifactId>
                    <groupId>org.slf4j</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        -->

        <dependency>
            <groupId>org.cassandraunit</groupId>
            <artifactId>cassandra-unit-spring</artifactId>
            <version>2.1.3.1</version>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <artifactId>slf4j-log4j12</artifactId>
                    <groupId>org.slf4j</groupId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-cassandra</artifactId>
        </dependency>
        <!-- DataStax driver -->
        <dependency>
            <groupId>com.datastax.cassandra</groupId>
            <artifactId>cassandra-driver-core</artifactId>
            <version>${datastax-driver.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>com.codahale.metrics</groupId>
                    <artifactId>metrics-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.datastax.cassandra</groupId>
            <artifactId>cassandra-driver-mapping</artifactId>
            <version>${datastax-driver.version}</version>
        </dependency>
        <!-- Spring Cloud -->
```

Solr Configuration
------------------

For search metadatas and text data it is used a Solr node with DataStax Enterprise Search. It requires to install DataStax Enterprise Searh using an account and a repository.

- Install DSE
[Sign up](https://academy.datastax.com/downloads?destination=downloads\&dxt=DX) an account to download DSE packets. For a Debian based GNU/Linux installation follow bellow instructions:
```
# Repositorio de DataStax, acceso con usuario y password del registro en la web
$ echo "deb http://username:password@debian.datastax.com/enterprise stable main" | sudo tee -a /etc/apt/sources.list.d/datastax.sources.list

# public key
$ curl -L https://debian.datastax.com/debian/repo_key | sudo apt-key add -

$ sudo apt-get update

$ sudo apt-get install dse-full (Use for all product levels.)
$ sudo apt-get install dse-full opscenter (Also installs OpsCenter.)
```

- Configure DataStax Enterprise Search
```
$ sudo service cassandra stop
$ sudo rm -rf /var/lib/cassandra/data/system/*

# Edit Cassandra DSE as Solr mode
$ sudo vim /etc/default/dse
        SOLR_ENABLED=1

# Start services
$ sudo service dse start
$ sudo service datastax-agent start
$ sudo nodetool status
```

- Index tables 
```
$ cd solr_config
$ for i in `ls config*.yaml`; do echo "## $i"; cat $i; done

## config-metadata.yaml
default_query_field: description
auto_soft_commit_max_time: 1000
generate_docvalues_for_fields: '*'
enable_string_copy_fields: false

## config-pictureFound.yaml
default_query_field: title
auto_soft_commit_max_time: 1000
generate_docvalues_for_fields: '*'
enable_string_copy_fields: false

## config-pictureSearch.yaml
default_query_field: created
auto_soft_commit_max_time: 1000
generate_docvalues_for_fields: '*'
enable_string_copy_fields: false

## config-picture.yaml
default_query_field: title
#directory_factory_class: Similarity
auto_soft_commit_max_time: 1000
generate_docvalues_for_fields: '*'
enable_string_copy_fields: false

$ sudo dsetool create_core flipper.picture generateResources=true reindex=true coreOptions=config-picture.yaml
$ sudo dsetool create_core flipper.metadata generateResources=true reindex=true coreOptions=config-metadata.yaml
$ sudo dsetool create_core flipper.picturesearch generateResources=true reindex=true coreOptions=config-pictureSearch.yaml
$ sudo dsetool create_core flipper.picturefound generateResources=true reindex=true coreOptions=config-pictureFound.yaml
```

- Configure CassandraProperties in Spring Boot
To search and paging with Solr (*solr_query*) must be disabled default PAGING in Cassandra. The file src/main/java/gal/udc/fic/muei/tfm/dap/flipper/config/cassandra/CassandraProperties.java is configured with the following properties:

```java
    /**
     * Compression supported by the Cassandra binary protocol: can be NONE, SNAPPY, LZ4.
     */
    /* enabled LZ4 compressión */
    private String compression = ProtocolOptions.Compression.LZ4.name();

```

and 
```java
    /**
     * Queries default fetch size.
     */
    /* disabled PAGING to use Solr with DataStax Search */
    private int fetchSize = Integer.MAX_VALUE;
```


Run
---

- Developer mode
```
$ mvn compile
$ mvn -Dmaven.test.skip=true spring-boot:run
```

Execute in other terminal grunt task serve:
```
$ grunt serve
```

- Production mode
```
$ mvn compile
$ grunt build --force --no-color
$ mvn -Pprod -Dmaven.test.skip=true package
$ java -jar ./target/flipper-0.0.1-SNAPSHOT.war --spring.profiles.active=prod
```

It can run in "production mode" if you trigger the "prod" profile (there are several ways to trigger a Spring profile, for example you can add -Dspring.profiles.active=prod to your JAVA_OPTS when running your server).

*./target/flipper-0.0.1-SNAPSHOT.war* file is an executable WAR file (see next section to run it). It can also be deployed on an application server, but as it includes the Tomcat runtime libs, you will probably get some warnings. Use *target/flipper-0.0.1-SNAPSHOT.war.original* for your application server. 

Read more info about production package in JHipster page, in [production section](http://jhipster.github.io/production.html)

Thanks
------

- to Óscar Pedreira Fernández for their help and advice in carrying out this work.
- to Mathias Lux and Oge Marques for the library [Lire](http://www.lire-project.net/).
- to all Apache Lucene and Solr developers.
- to all Cassandra developers.
- to [Julien Dubois](https://github.com/jdubois) and all [JHipster](https://github.com/jhipster/generator-jhipster) Yeoman generator developers and for their issues resolved.
- to DataStax
- to all developers and software engineers who write and share knowledge.

License
-------
Flipper Open Reverse Image Search - a scalable platform to storage, publishing and advanced search of pictures
Copyright (C) 2015  David Albela Pérez

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

More info in "COPYING" file
