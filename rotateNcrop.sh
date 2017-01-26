#!/bin/bash

for file in /media/gitanjali/Parrot_Mini/Airborne_Night/media/*.jpg; 
do
convert "$file" -rotate 180 -crop +0-165 "${file%.jpg}"_cropNrotate.jpg
done
