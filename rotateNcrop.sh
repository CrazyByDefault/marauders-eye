#!/bin/bash

for file in ~/Pictures/*.jpg; 
do
convert "$file" -rotate 180 -crop +0-165 "${file%.jpg}"_cropNrotate.jpg
done
