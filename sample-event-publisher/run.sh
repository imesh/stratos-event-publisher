ARGS=$*
mvn exec:java -Dexec.mainClass="org.samples.apache.stratos.event.Main" -Dexec.args="agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 ${ARGS}"
