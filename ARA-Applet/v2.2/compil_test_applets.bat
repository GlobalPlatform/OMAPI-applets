REM Location of Java executables
@SET JAVADIR=..\..\..\..\Tools\j2sdk1.5.0_22\bin
REM Location of Java Card classes
@SET JCKIT=..\..\..\..\Tools\DEVENV\java_card_kit-2_2_2\java_card_kit-2_2_2\java_card_kit-2_2_2-rr-bin-windows-do
@SET VERSION=1.0


@ECHO **************************************************************************
@ECHO ** Compilation OMAPI ARA applet                                         **
@ECHO **************************************************************************

%JAVADIR%\javac.exe -g -classpath %JCKIT%\lib\api.jar ^
   -d bin ^
   src\org\globalplatform\test\ara\*.java


%JAVADIR%\java.exe -classpath %JCKIT%\lib\converter.jar;%JCKIT%\lib\offcardverifier.jar ^
   com.sun.javacard.converter.Converter ^
   -nobanner -out CAP ^
   -exportpath %JCKIT%\api_export_files ^
   -d out ^
   -debug ^
   -applet 0xA0:0x00:0x00:0x01:0x51:0x41:0x43:0x4C:0x00 org.globalplatform.test.ara.ARAapplet ^
   -classdir bin ^
   org.globalplatform.test.ara 0xA0:0x00:0x00:0x01:0x51:0x41:0x43:0x4C %VERSION%

pause
