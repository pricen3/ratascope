#!/bin/bash

sudo mount //140.160.109.22/carrolllab/Raspi /home/pi/Batman/carrolllab -o username=carrolllab,password=lab532

echo -n "Enter date|mouse for filename (ex: 170129PD5) : "
read text
echo "Is $text correct? Press Y or N"
read answer
if [[ $answer == "Y" || $answer == "y" ]]; then
    echo "Okay - Video for mouse $text will begin shortly"
else
    echo "Aww - c'mon. Try again !"
    exit
fi

(sudo python IRLED.py &)

raspivid -t -0 -w 450  -h 300 -fps 25 -b 2000000 -cfx 128:128 -o /home/pi/Batman/carrolllab/$text.h264 
