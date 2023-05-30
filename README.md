# Plus One
A tool for learning Japanese using the " i + 1 " 
(Input Hypothesis) method coupled with SRS 
(Spaced Repetition System). This application
is heavily inspired by Anki, the flashcard SRS study
tool.

(Note: " i + 1 " focuses on comprehensible input by only
adding one new piece of information at a time. 
<br>More can be read here: 
[Wikipedia](https://en.wikipedia.org/wiki/Input_hypothesis))

# Description
### Current State
In Plus One's current state, the application acts as a
repository that can store sentences input by the user.

Sentences can include:

- Sentence's Source Name
- Type of source (e.g. Visual Novel, Manga, etc.)
- Source's URL (if applicable)
- An image associated with the source

Sentences can also be marked as "Sequential", which
will link sentences together in sequential order. This
can be important as some sentences may need context
from the previous sentence to understand the meaning 
of the current sentence.

(Users can continue to add to a sequential sequence of sentences
between sessions, or start a sequence from a previously
un-sequenced sentence)

### Future State
A fully implemented version of Plus One will allow users
to add sentences to their repository of sentences as they 
consume Japanese content. From those stored sentences, the
user will be able to study flashcards (using SRS) that are generated 
from those sentences. The focus of a flashcard will be a single word within the 
given sentence. 

The user will be able to set how many new words they want 
to learn every day, which is where " i + 1 " will be implemented.
From the user's repository of sentences, words will be chosen that
have sentences with the fewest unknown (i.e. not in the user's 
current pool of studied words) words.

At first, if the user is a genuine novice, their sentences with
target words will have many more unknown words than just the new word. Therefore
words will also be chosen based on their overall frequency (ranking within
the Japanese language) and their ability to decrease 
the unknown words within stored sentences. 

As the user's amount of known words and stored sentences 
increases, users will primarily encounter true " i + 1 "
sentences when studying new vocabulary. In addition, as
users add more sentences with already known words, those
sentences (if they're " i + 1 ") will be randomly chosen from
when studying those known words. Randomization of sentences
for target words will (hopefully) reduce a user's tendency
to recall the word's meaning by merely remembering the
sentence.


### Future Features

#### Short Term
- Ability to browse and edit stored sentences
- Ability to batch add sentences from a larger block of text

#### Long Term
- Implementation of SRS studying
  - Using JMdict and frequency list, create a usable dictionary
  for flashcard generation
  - Parsing of stored sentences into their comprised words
  - Creation of an occurrence table to keep track of 
  a words " i + 1 " status and it's associated sentences
  - Implementation of SRS algorithm (most likely based
  on SM-2 algorithm)

### Technology

Plus One is currently written completely in Java (using JDK-17), and is currently using: 
- Swing (primarily with SpringLayout) for the GUI
- H2 Database Engine for the SQL database
- JSON.simple for JSON parsing and manipulation

# Running

Requires Java Runtime 61.0 (Java JDK 17)

### Run From JAR File 

1. Locate PlusOne.jar which can be found [Here](build/libs/PlusOne.jar) (build/libs/PlusOne.jar)
2. PlusOne.jar can be kept in the "libs" folder, or it's advised to create a folder for PlusOne.jar as 
the application creates and saves additional files (database/images/settings files)
3. Using the terminal, navigate to the folder that contains PlusOne.jar and run:
```shell
java -jar PlusOne.jar
```
4. As a first time user, you will be prompted with where you want to save your database and images to 
(Default will save to the folder containing PlusOne.jar)

### Run From IDE
1. Using your preferred IDE for Java, run Main.java located in src/main/java
2. Same step as step 4 above, but default database files and user_settings.json will be saved to the project
directory

# How To Use

### Initial Startup
When first starting Plus One, the user will need to choose where the database and image files will be saved

![First initialization screen giving default and custom options for saving database and images](C:\Projects\Java\PlusOne\src\main\resources\readme\images\initialization_root.PNG "Plus One Initialization Root")

