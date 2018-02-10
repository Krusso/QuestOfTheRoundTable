# QuestOfTheRoundTable
COMP3004B 2018 Team 2 Project

**Authors**
- Krystian Wojcicki (kwojcicki)
- Neha Rao (Neha-Rao)
- Michael Kuang (Krusso)

**Requirements**
- Java version 8 (v important otherwise none of our amazing lambdas will work)
- Eclipse Oxygen
- JavaFX (version??)
- ..

**Setup**
- Install Eclipse Oxygen from eclipse.org
- Install JavaFX Using eclipse (help->install new software->work with: choose your eclipse version (Oxygen)->->General Purpose Tools->install e(fx)lipse)
- For UI, download Scene Builder by Gluon. Set path by going on eclipse -> Windows -> Preferences -> JavaFX -> choose the scene build.exe 

**Running**
- To run the game: Run JavaFXQTRT/src/main/java/src/client/Main.java
- To run tests:
  - Create a run configuration to run JUnit tests (Run -> Run Configuration -> JUnit(right click) -> New)
  - Select "Run all tests in the selected project, package or source folder" and set src/test/java as the source folder
  - Set TestRunner as JUnit 4
  - Apply and Run

**FAQ**
- I don't understand this terrible code ???
  - ***To be fair, you have to have a very high IQ to understand Krystian's code. The algorithms are extremely complex, and without a solid grasp of data structures most of it will go over a typcal programmer's head. There's also Krystian's lambda functions, which are deftly woven into his controllers - his coding style draws heavily from the MVC design pattern, for instance. The other contributors understand this stuff; they have the intellectual capacity to truly appreciate the quality of this code, to realize that it's not just extensible - it says something deep about COMPUTER SCIENCE. As a consequence people who dislike Krystian's code truly ARE idiots - of course they wouldn't appreciate, for instance, the meaning of Krystian's half finished PR "if(card.)", which itself is a cryptic reference to the blizzard card game Hearthstone I'm smirking right now just imagining one of those addlepated simpletons scratching their heads in confusion as Krystian's genius unfolds itself in their IDEs. What fools... how I pity them. And yes by the way, I DO have a Krystian tattoo. And no, you cannot see it. It's for the ladies' eyes only- And even they have to demonstrate that they're within 5 IQ points of my own (preferably lower) beforehand.***
  
 **For real tho FAQ**
 - If eclipse complains about src.client package error do the following:
  - eclipse->right-click project -> Build Path -> Configure Build Path...
    - Then go to Java Build Path and under the Source tab delete src.client folder and then hit apply/close.
