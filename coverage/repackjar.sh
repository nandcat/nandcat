#!/bin/sh
mkdir tmp_exp-jar
cd tmp_exp-jar
jar -xf ../../target/nandcat-0.1-SNAPSHOT-jar-with-dependencies.jar
rm META-INF/LICENSE
jar cfm ../stripped.jar META-INF/MANIFEST.MF .
cd ..
#rm -r tmp_exp-jar

java -cp emma.jar emma instr -m overwrite -ix "+nandcat.*" -cp stripped.jar
mv stripped.jar nandcat-with-emma.jar
#java -cp stripped.jar:emma.jar nandcat.Nandcat

