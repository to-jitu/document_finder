# document_finder
Smart Document Finder

Maven Build - mvn package

Run the application

java -jar finder-0.0.1-SNAPSHOT.jar

<h1>Initial Setup</h1>

1. Open http://localhost:3119/document/swagger-ui.html

2. Click on finder-controller

3. Click on GET /finder/index

4. Click on Try it Out

5. Enter below two directory location

dataDir -  Directory for application to search files to be indexed

indexDir - Directory for application to store the index

![Set Up](/images/set_up.png)

Once you input the dataDir and indexDir, Click on Execute

App will search for all files and try to create indexes. App should return Code 200

![Response](/images/response.png)


<h1>Search files</h1> 

Use below url in browser -

http://localhost:3119/document/index.html

<h4>Simple Search</h4>

Just enter few words. App will use words as token and try to search the file containing those texts and return files containing those words. It is OR operation on words

![Simple Search](/images/search_oracle.png)

![Simple Search](/images/search_oracle_java.png)
