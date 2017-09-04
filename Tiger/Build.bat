cmd /c mvn clean compile test-compile
cmd /c mvn package -DskipTests
copy target\Tiger-1.0.0.jar .
