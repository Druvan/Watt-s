compileAll: 
	javac -cp .:json-simple-1.1.jar *.java

Server.class:
	javac Server.java

DBadmin.class:
	javac DBadmin.java

HDAPI.class:
	javac -cp .:json-simple-1.1.jar HDAPI.java



runServer: Server.class DBadmin.class HDAPI.class
	java -cp .:mysql-connector-java-5.1.40-bin.jar:json-simple-1.1.jar Server

clean: 
	@rm -f *.class
