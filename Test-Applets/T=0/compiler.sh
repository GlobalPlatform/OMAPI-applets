#!/bin/sh

javac -g -source 1.3 -target 1.1 -classpath $JC_HOME/lib/api.jar src/org/globalplatform/javacard/omapitest1/*.java
javac -g -source 1.3 -target 1.1 -classpath $JC_HOME/lib/api.jar src/org/globalplatform/javacard/omapitest2/*.java
javac -g -source 1.3 -target 1.1 -classpath $JC_HOME/lib/api.jar src/org/globalplatform/javacard/omapitest3/*.java
javac -g -source 1.3 -target 1.1 -classpath $JC_HOME/lib/api.jar src/org/globalplatform/javacard/omapitest4/*.java

