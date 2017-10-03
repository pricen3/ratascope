import RPi.GPIO as GPIO
import datetime
import time
import os

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(7, GPIO.LOW)

def Blink(numTimes, speed):
    for i in range(0,numTimes): ## Run loop numTimes
        GPIO.output(7, True) ## Keep GPIO off (pin 7)
    print("Current LED status: Off")
    print("I will turn them on in...")
    print(str(offlen/3600))
    print("...hours")
    print("I am set to record at 2:00PM until 11:50AM tomorrow")
    print("If you have made a mistake, ctrl-c to exit and use 'sudo reboot'")
    time.sleep(offlen) ## Wait for xx hours
    print("Current LED status: On")
    GPIO.output(7, False) ## Switch on GPIO (pin 7)
    time.sleep(43800)
    print("Current LED status: Off")
    GPIO.output(7, True)
    time.sleep(4)
    #os.system('pkill raspivid')
    print("Done")
    GPIO.cleanup()

dt = datetime.datetime
now = dt.now()
delta = datetime.datetime(year = now.year, month = now.month, day = now.day, hour = 18, minute =00, second = 0) - datetime.datetime.now()
offlen = delta.seconds

Blink(int(1),float(1)) #Run loop
