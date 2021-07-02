# ing-sw-2021-Scalise-Scaini-DallAglio

## FUNZIONALITÀ IMPLEMENTATE

The project includes the MVC implementation of Masters of Reinassance rulebook. It features singleplayer and multiplayer games,
both a Command Line Interface and a Graphic User Interface and includes simultaneous games and server persistence as
added features.

## TESTING

Model and controller components have been tested thoroughly with JUnit. Network and graphic interface
components have only been tested manually.

**Overall testing report**
![JUnit overall testing](/Deliveries/jacoco_test_coverage/total_coverage.png)

**Controller testing report**
![JUnit controller testing](/Deliveries/jacoco_test_coverage/controller_coverage.png)

GameController class has been partly tested manually for more complex and fringe cases

**Model testing report**

**model.player**
![JUnit model.player testing](/Deliveries/jacoco_test_coverage/player_coverage.png)

**model.game**
![JUnit model.game testing](/Deliveries/jacoco_test_coverage/game_coverage.png)

model.card and model.resources mainly consist in CLI related methods and setters, hence lesser coverage. 


## AVVIO

The project contains a client-side .jar and a server-side .jar.

Server args include server port number. 
In case of empty args, the server runs with default value 1234.
`java -jar GC02Server.jar [--port portnumber]`

Client args include server port number and address, desired graphic interface. In case of empty args,
the client runs with default values 127.0.0.1, 1234, gui.
`java -jar GC02.jar [--address serveraddress] [--port portnumber] [--view gui/cli]`


## NOTES ON OS COMPATIBILITY

The software has been produced entirely on Linux operative systems. Manual testing has been done on Linux operative
systems via bash and Windows OS via cmd and wsl. No manual testing has been done on MacOS.

Server persistence implements an OS-specific function, Code Line Interface may vary according 
to how the running shell processes Unicode characters. Compatibility may not be fully granted.
