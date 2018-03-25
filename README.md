# QuestOfTheRoundTable
COMP3004B 2018 Team 2 Project

**Authors**
- Krystian Wojcicki (kwojcicki)
- Neha Rao (Neha-Rao)
- Michael Kuang (Krusso)

**Requirements**
- Java version 8 (v important otherwise none of our amazing lambdas will work)
- Eclipse Oxygen
- JavaFX version 9.0.1 (JavaFX version 8 should work as well)

**Setup**
- Install Eclipse Oxygen from eclipse.org
- Install JavaFX Using eclipse (help->install new software->work with: choose your eclipse version (Oxygen)->->General Purpose Tools->install e(fx)lipse)
- To work with UI, download Scene Builder by Gluon. Set path by going on eclipse -> Windows -> Preferences -> JavaFX -> choose the scene build.exe 

**Running**
- To run the game: Run JavaFXQTRT/src/main/java/src/client/Main.java
- To run tests:
  - Create a run configuration to run JUnit tests (Run -> Run Configuration -> JUnit(right click) -> New)
  - Select "Run all tests in the selected project, package or source folder" and set src/test/java as the source folder
  - Set TestRunner as JUnit 4
  - Apply and Run
  
**Important Notes**
- When running the JUnit tests inside the src.client package, it is recommended to use a larger screen (e.g on a 24" monitor and 15.6" laptop) and a resolution of either 1920x1080 or 1600x900. 
- Also, it is recommended to run the JUnit tests inside the src.client package separately (one by one) as log files may get confusing.
- When reading the logs, the log will show player numbers from 0 to 3 because these are the indexes used in the code. However, for UI/UX purposes, the game application will display player numbers from 1 to 4. This holds true for any logs.
- When playing the game, in order to keep it "hot-seat" styled, a player must click on "start turn" button before they may begin their turn. This is true for AI turn as well so the player can see what the AI will do for their turn.

 **FAQ**
 - If eclipse complains about src.client package error do the following:
  - eclipse->right-click project -> Build Path -> Configure Build Path...
    - Then go to Java Build Path and under the Source tab delete src.client folder and then hit apply/close.
    
    
 **Bower Components**
 - The web client uses other AngularJS modules from github so, like Maven, download Bower. Instructions are on this page [https://bower.io/]
 - Once you have bower installed, navigate to QuestOfTheRoundTable/server/src/main/resources/static on your terminal and do `bower install angular-dragdrop`
 - We might consider to bower install sockjs and stomp (most likely not since they have a CDN)
