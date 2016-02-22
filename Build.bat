@echo off

:c
cls
"C:\Program Files\Java\jdk1.8.0_45\bin\javac.exe" -d ./bin/ -cp .; ./src/*.java
pause
goto c