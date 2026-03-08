@echo off
java -Xms2G -Xmx2G -XX:+UseG1GC -jar paper-1.21-130.jar nogui
pause