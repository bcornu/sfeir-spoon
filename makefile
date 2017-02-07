BadCalculator=`cat calc_location`

spoon-base=java -classpath target:lib/spoon-core-5.5.0-jar-with-dependencies.jar spoon.Launcher -i $(BadCalculator) --level WARN

clean:
	@rm -rf spooned/*
	@rm -rf target/*

mkdirs:
	@mkdir -p spooned
	@mkdir -p target

install: clean
	@javac src/main/java/com/sfeir/*/*.java -classpath lib/spoon-core-5.5.0-jar-with-dependencies.jar -d target

init: mkdirs install
	@$(spoon-base)

spoon-gui: install
	@$(spoon-base) -g

spoon0: install
	@$(spoon-base) -p com.sfeir.processors.MyFirstProcessor

spoon1: install
	@$(spoon-base) -p com.sfeir.processors.MyFirstProcessor:com.sfeir.processors.MySecondProcessor

spoon2: install
	@$(spoon-base) -p com.sfeir.processors.MyFirstProcessor:com.sfeir.processors.MySecondProcessor:com.sfeir.processors.MyThirdProcessor

test:
	@javac spooned/com/sfeir/badcalculator/*.java -d target
	@java -classpath target com.sfeir.badcalculator.CalculatorMain

spoon0-test: spoon0 test

spoon1-test: spoon1 test

spoon2-test: spoon2 test
