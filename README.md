# RDFDataAnalyser

## Synopsis

The main method of this application is calculation of domain representation for Linked Open Data dataset. In the application are available this basic functions:

 * defining domains and entities for calculation
 * importing dataset in HDT format or through the SPARQL endpoint
 * macro visualization - dataset coverage for each defined domain shown in the chart
 * micro visualization - completeness of information for a given entity type shown in the table
 * dataset comparison

 This application should be useful mainly for these, who need to find out domain representation of their Linked Open data dataset or they need to compare twodifferent datasets based on the domain specification.

## Code Example
Import of domains and entities in N-Triple format:
```java
ImportDomains importDomains = new ImportDomains();
importDomains.loadFileToModel(contentOfFile);
```

Import dataset Dbpedia through SPARQL endpoint:
```java
 Dataset dataset = new Dataset();
 dataset.setName("Dbpedia");
 dataset.setDescription("Dbpedia represents structural data of Wikipedia");
 dataset.setOntologyPredicate("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
 dataset.setFileName("https://dbpedia.org/sparql")
 Dataset datasetToReturn = DB.getDbAccess().createDataset(dataset, uploadedInputStream, shortCalculation);
```

## Motivation
 This application was created as the result of my master thesis work with name "Sumarizing Linked Open Data Datasets".

## Installation from source

Latest GitHub source contains Javascript front-end and Java back-end. So, the prerequisites are:

 * Java version 1.8
 * Tomcat 8.0.32
 * Maven 3

```bash
$ git clone git@github.com:jcabaiova/RDFDataAnalyser.git
$ cd RDFDataAnalyser
$ MAVEN_OPTS="-Xmx4096m -XX:MaxMetaspaceSize=4096m" mvn install
```

## Examples of usage

Directory Examples contains example files for importinf domains and entities in N-Triples format :

 * domainsInitialization.nt - initialization of 30 main domains
 * dbpediaOntology.nt - importing entities according to Dbpedia ontology
 * geonamesOntology.nt - importing entities according to GeoNames ontology
 * geoNamesDbpediaMappings.nt - importing mapping links between Dbpedia and GeoNames entities

Examples databases are in directory Resources. Path to database is set in class DbAccess in constructor:
```java
this.URL = "jdbc:sqlite:"+this.getClass().getResource("/db/rdfDataFinal.db").getFile().toString();
```

## Used libraries

Main Java libraries which were used fo the project:

 * Jersey 2.0
 * SQLite
 * ORMlite
 * RDF HDT
 * Apache Jena
## License
This code is copyrighted by Czech Technical University in Prague.