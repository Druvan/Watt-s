compileAll: 
	javac *.java

Server.class:
	javac Server.java

DBadmin.class:
	javac DBadmin.java

runServer: Server.class DBadmin.class
	java -cp .:mysql-connector-java-5.1.40-bin.jar Server

clean: 
	@rm -f *.class
