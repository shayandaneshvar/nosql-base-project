@echo off
echo You can change server port via --server.port=portNumber arg
echo Defaults: size=2500, port=64000
echo if you have less than 8GB of RAM, lower the size of generated people via size arg
echo Stop the application via Ctrl+C

java -jar nosql-base-project-0.0.1-SNAPSHOT.jar size=2500 --server.port=64001
