##
# Pathfinding visualization
#
# @file
# @version 0.1

compile:
	javac -classpath "/usr/lib/jvm/java-8-openjdk/jre/lib/ext/jfxrt.jar:" *.java

run:
	java -classpath "/usr/lib/jvm/java-8-openjdk/jre/lib/ext/jfxrt.jar:" Main

clean:
	rm *.class
# end
