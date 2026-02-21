<h1>Information Retrieval System </h1>

<h2>Intro</h2>
This repository contains an Information Rettrieval System developed for a work assignment at the University of Pavia for the Information Retrieval Class by prof. Luigi Santagelo.
I will use the dataset created by github user EnricoBenedetti in his repository GarfieldRetrieve. In which he goes beyond and performs statistical studies on the dataset. 

Plans:
    - I plan for at least a boolean retrieval system
    - The main aspect will be to search comics through text but additionally through text on 
        - Time period
        - By specific vignette text
    - Currently I plan to use case folding completly and delete hyphens to focus on the retrieving part of the system. 

I do not know if it is a good idea, but i'll try adding first the postings to the dictionary and then computing ALL statistics. 

Additional Comment: This is my first time using java so I do expect this project to not be on the same level as other IRS, however I see it as a great opportunity to inmerse myself in the language. 


- TO DO
  - Search options
    - Multiword (in progress)
  - Interface

// Deleted / Modified nodes arent direclty removed from the datastructures, but when saving changes, the saved documents should not include them, when searching additional trees will be used to avoid older versions. Yeah?

Maybe a tree for deletions and one for modifications?

NOTES:
    - I am aware that I am using the word "term" in reference to tokens in multiple areas of the code, it helps me understand the code. 
    - If my current computer cannot handle having the dictionary loaded in memory, due to time constraints I will simply limit the dictionary size. 