
cmd /c mvn clean compile test-compile
cmd /c mvn package -DskipTests
java -cp target/* baniya.Baniya
