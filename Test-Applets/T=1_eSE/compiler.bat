mkdir bin

rem USING Java version > 1.3
rem ------------------------
javac -g -source 1.3 -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest1\*.java -d bin\
javac -g -source 1.3 -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest2\*.java -d bin\
javac -g -source 1.3 -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest3\*.java -d bin\
javac -g -source 1.3 -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest4\*.java -d bin\

rem USING Java version 1.3
rem ----------------------
rem javac -g -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest1\*.java -d bin\
rem javac -g -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest2\*.java -d bin\
rem javac -g -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest3\*.java -d bin\
rem javac -g -target 1.1 -classpath %JC_HOME%\lib\api.jar src\org\globalplatform\javacard\omapitest4\*.java -d bin\
