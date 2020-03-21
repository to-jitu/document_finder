# document_finder
Smart Document Finder - Search for files (txt, docs, presentation ppt, pdf, excel, etc)

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

<h4>Search based on date and time</h4>

User can enter natural language when it was stored and some text

![Simple Search](/images/search_date.png)

<h4>Search based on Tag</h4>

App will automatically tag the documents/files for below category -

1. Person

2. Date 

3. Organization

4. Location

<h3>Use #Tag</h3>

NOTE - The App can be trained on many other domain specific category for automatically tag 
  
![Simple Search](/images/search_person.png)

<h4>Search based on filename</h4>

![Simple Search](/images/search_filename.png)

<h4>More refined query</h4>

![Simple Search](/images/search_stored_2_years.png)

<h1>Future Development</h1>

The Indexing and Searching can be improved a lot like -

1. Indexing more searchable fields 

2. More NLP processing for Named Entity Recognition, Part of Speech, context analysis for more improved automatic tagging.

3. Improved content extraction for domain specific need. Extract information from shapes (ppt), shapes, images, videos, etc.

4. Improved search result based on analytics on user search text.


