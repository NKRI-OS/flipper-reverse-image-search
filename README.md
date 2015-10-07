Flipper Open Reverse Image Search
==================

"Scalable platform to storage, publishing and advanced search of pictures"

Final project from ME in Computing, [MUEI](http://www.fic.udc.es/muei), [Facultade de Informática](http://www.fic.udc.es), [Universidade da Coruña](http://www.udc.gal). The [thesis](https://forxa.mancomun.org/docman/view.php/379/376/tfm.pdf), other documentation and source code can be obtain from [A Forxa Mancomun](https://forxa.mancomun.org/projects/flipper/). You can view the presentation slides [here](http://dalbelap.github.io/revealjs-muei-tfm/).

Installation
-------

Apache Cassandra 2.1 installation from [Planet Cassandra](http://www.planetcassandra.org/cassandra/?dlink=http://www.datastax.com/documentation/cassandra/2.1/cassandra/install/installDeb_t.html) or DataStax documentation web site http://docs.datastax.com/en/cassandra/2.1/cassandra/install/install_cassandraTOC.html

Install version 2.1.9
```
$ sudo apt-get install dsc21=2.1.9-1 cassandra=2.1.9
$ sudo service cassandra stop
$ sudo rm -rf /var/lib/cassandra/data/system/*
$ sudo service cassandra start
```

*Keyspace* and tables
------

- Create the keyspace and tables

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

- Production mode create the keyspace using the following cql file:
```
$ cqlsh -f src/main/resources/config/cql/create-keyspace-prod.cql
```

Solr Configuration
------------------

To search text data from Cassandra (metadata from pictures) it is used a Solr node with [DataStax Enterprise Search](http://www.datastax.com/products/datastax-enterprise-search). It requires an account from DataStax

- Installing DSE Search
[Sign up](https://academy.datastax.com/downloads?destination=downloads\&dxt=DX) an account to download DSE Search packets. You can install DSE Search from different methods like GUI or Text mode, using a repository from your GNU/Linux distribution or in a Cloud service, [here](http://docs.datastax.com/en/datastax_enterprise/4.7/datastax_enterprise/install/installTOC.html) is the official documentation from DataStax. To install DSE as a service using APT repositories on Debian-based systems follow the bellow command lines:

```
# repository from DataStax
$ echo "deb http://yourusername:yourpassword@debian.datastax.com/enterprise stable main" | sudo tee -a /etc/apt/sources.list.d/datastax.sources.list

# public key
$ curl -L https://debian.datastax.com/debian/repo_key | sudo apt-key add -

$ sudo apt-get update

$ sudo apt-get install dse-full (Use for all product levels.)
$ sudo apt-get install dse-full opscenter (Also installs OpsCenter.)
```

- Configure DataStax Enterprise Search

```
# after install stop the service
$ sudo service cassandra stop
$ sudo rm -rf /var/lib/cassandra/data/system/*

# enable Cassandra DSE as Solr mode
$ sudo vim /etc/default/dse
        SOLR_ENABLED=1

# start services
$ sudo service dse start
$ sudo service datastax-agent start
$ sudo nodetool status
```

- Index Cassandra tables
After the DSE Search installation, it is required to index the tables from Cassandra that you can store in Solr node. This repository includes a pre-configuration and a shell script to index Flipper tables in Solr using the *dsetool* from DSE. When a table is indexed in Solr, DSE creates a *solr_query* column in the tables for search in Solr using CQL (Cassandra Query Language) queries:

```
$ cd solr_config
# view the content from YAML files
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

    ...

    /**
     * Queries default fetch size.
     */
    /* disabled PAGING to use Solr with DataStax Search */
    private int fetchSize = Integer.MAX_VALUE;
```

YAML Configuration
------------------

- Rememberme key: change the remember key value:
```
$ echo jhipster.security.rememberme.key: `cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1 | sha256sum | cut -d' ' -f 1`
```

- Authentication: change the XAuth secret key:
```
$ vim src/main/resources/config/application.yml
authentication:
    xauth:
        secret: myXAuthSecret
        # Token is valid 30 minutes
        tokenValidityInSeconds: 1800
```

- Email configuration: pre-configured for a [Mailgun](https://mailgun.com) service:

```
$ vim src/main/resources/config/application.yml
mail:
    host: smtp.mailgun.org
    port: 465
    username: postmaster@yourdomain-from-mailgun-servie.mailgun.org
    password: yourpassword-from-your-mailgun-service
    protocol: smtps
    tls: true
    auth: true
    from: no-reply@localhost
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
- Óscar Pedreira Fernández for their help and advice in carrying out.
- Apache Lucene and Solr developers.
- Mathias Lux and Oge Marques and other developers  for share the library [Lire](http://www.lire-project.net/).
- Cassandra developers, Planet Cassandra and DataStax.
- [Julien Dubois](https://github.com/jdubois) and all [JHipster](https://github.com/jhipster/generator-jhipster) Yeoman generator developers for issues resolved.
- Developers and software engineers for write and share knowledge.

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
