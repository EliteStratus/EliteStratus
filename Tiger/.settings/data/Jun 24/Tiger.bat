REM Installation
REM 1. Tiger-1.0.0.jar
REM 2. chromedriver.exe
REM 3. Run.bat
REM 4. File1_Schema.csv
REM 5. File_Products.csv

REM Run main program
java -cp Tiger-1.0.0.jar tiger.Tiger <passwd>

REM Run Gabbar program
java -cp Tiger-1.0.0.jar tiger.Gabbar

REM Run pasword program
REM java -cp Tiger-1.0.0.jar tiger.Passwd  | Enter Password: [<xxxx><2 Digits for Days>]
