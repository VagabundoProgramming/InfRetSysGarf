<h1>Information Retrieval System </h1>

<h2>Introduction</h2>
This repository contains an Information Retrieval System developed for a work assignment at the University of Pavia for the Information Retrieval Class by prof. Luigi Santagelo.

For this project I take as base the dataset created by the github user EnricoBenedetti in his repository GarfieldRetrieve. In which he goes beyond and performs statistical studies on the dataset. 

The objective of this project was to fully develop the basic elements of an Information Retrieval System whilst creating and managining the relevant datastructures.


<h2>Functionality</h2>
The system provided in this repository has multiple functions it can perform accompanied by a simple user interface to help a user retrieve information. 

The functions of the system are divided into 3 main categories:
 - Load/Save Dataset
 - Modify Dataset in Disk
 - Query


<h3>Load/Save Dataset</h3>
This section helps with data processing, data loading and storing. 
The following methods are the most relevant here:
 - Preprocess Raw Data: Processes the dataset from EnricoBenedetti to make it easier to read for my system.
 - Load from Preprocessed Dataset: Loads both term and document dictionaries from the processed dataset. 
 - Load from Indexes: Loads both term and document dictionaries from already existing indexes. 
 - Save: Turns memory loaded dictionaries into index files to more easily load. 

 <h3>Modify Dataset in Disk</h3>
 This section manages additional dictionaries that are in disk to perform changes within the interface to the main dictionaries. 
 It includes the following methods:
 - Add Document
 - Modify Document
 - Delete Document
 - Save Changes (to new indexes)


<h3>Query</h3>
This section allows users to search through the collection of documents using a combination of rules that form a query. Currently the available operations are:
 - Search Token (t:token) : Searches for all documents containing the specified token.
 - Search String (s:"string of text") : Searches for all documents contaning the exact specified string of text.
 - Search Date (d:DD/MM/YYYY) : Searches for all documents from the specified date, in this casem you may fill a parameter such as DD with ** to enable any match. 
 - Search Weekday (w:n) : Searches for documents published on the specific weekday (1: monday - 7: sunday).

Additionally, there are currently implemented the following operators:
 - Not (-) : used before an operations, detailed above to exclude matching results. 
 - AND (&) : used betweeen operations to signal conjunction, it is the default if none are specified.
 - OR (|) : used between operations to signal disjunction. 

A brief explanation of the query rules is included within the software. 


<h2>Future Work</h2>
Since this is my first project in java there are many things to improve. 
Due to time constraints I will not solve them for the time being but I will list some of the bigest considerations for the readers:

1. Better File Management of the Project
2. Parenthesis Support for Queries
3. More & better Datastructures to support complex searches (N-vignettes, proximity, star queries)
4. Dataset partitions 
5. Improved file storing and retrieval
6. Use of cleaner code



NOTES:
    - I am aware that I am using the word "term" in reference to tokens in multiple areas of the code, it helps me understand the code but I recognize it as a bad practice. 
    - If my current computer cannot handle having the dictionary loaded in memory, due to time constraints I will simply limit the dictionary size(This did not happen).
    - This is my first time using java so I do expect this project to not be on the same level as other IRS, however I see it as a great opportunity to inmerse myself in the language. 
