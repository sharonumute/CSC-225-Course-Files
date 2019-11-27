#!/bin/bash
javac Nice.java
FILES=./Input/*.txt
MOUT=./MyOutput/*.txt
OUT=./Output/*.txt
i=1
for f in $FILES
do
  java Nice < $f > ./MyOutput/test$i.txt
  i=$((i+1))
done

j=1
for f in $OUT
do
  diff "./MyOutput/test$j.txt" "$f"
  j=$((j+1))
done
