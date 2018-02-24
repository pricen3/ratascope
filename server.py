#!/usr/bin/python
import socket
import argparse
import struct
import os
import signal
import subprocess
import datetime
import time
import RPi.GPIO as GPIO

###for the cages###
###this just has the basic functions, it still needs to have the main
###stuff to do what we need it to do

def send(connection, message):
    """send(connction, message) function"""
    mes = message.encode('ascii')
    connection.send(struct.pack('!l',len(mes)))
    connection.send(mes)

def recv(connection):
    """recv(connection) function"""
    try:
        length = struct.unpack('!l', connection.recv(4, socket.MSG_WAITALL))
        message = connection.recv(length[0], socket.MSG_WAITALL).decode('ascii')
        return message
    except TimeoutError:
        print('timeoutError ocurred')

def server(ip):
    """server function"""
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((ip, 3684))#3679
    s.listen(5)
    while True:
        print("at top of while loop ")
        c, addr = s.accept()
        guess = str(recv(c))
	guess = guess.split(" ")
	if len(guess)==1: #and guess[0] == "hello"
	    #we're trying to establish a connection
            print("establishing connection")
	    """else:
		#client closed on us
		print("client closed")
		#c.close() ?? or s.close()?"""
	    send(c, 'Y')
            c.close()
        else:
            pid = os.fork()
            if pid==0:
                #child runs CAMPR
                send(c, 'Y')
                c.close()
                print("startHH startMM YY MM DD mouse_name duration(hours) lightoff(hours) lighton(hours)")
                print("recieved: " + str(guess))
                #guess[0]=startHH guess[1]=startMM guess[2]=yy guess[3]=mm guess[4]=dd guess[5]=mouse guess[6]=duration(hours) guess[7]=lightoff guess[8]=lighton
                duration = float(guess[6])*(3.6*(10.0**6.0))
                dt = datetime.datetime
                now = dt.now()
                delta = datetime.datetime(year = int(guess[2]), month = int(guess[3]), day = int(guess[4]), hour = int(guess[0]), minute = int(guess[1]), second = 0) - datetime.datetime.now()
                starttime = delta.seconds
                time.sleep(starttime)
                subprocess.call("sudo python IRLED2.py -on "+guess[8]+" -off "+guess[7]+" &", shell=True)
                print("after IRLED")
                subprocess.call("raspivid -t " + duration + " -w 450 -h 300 -fps 25 -b 2000000 -cfx 128:128 -o /home/pi/Batman/carrolllab/"+guess[2]+guess[3]+guess[4]+guess[5]+".h264 &", shell=True)
                print("after RASPVID")
                subprocess.call("MP4Box -add /home/pi/Batman/carrolllab/"+guess[2]+guess[3]+guess[4]+guess[5]+".h264 /home/pi/Batman/carrolllab/"+guess[2]+guess[3]+guess[4]+guess[5]+".mp4 &", shell=True)
                print("after MP4BOX")
                time.sleep(duration)
                 subprocess.call("sudo pkill -f IRLED2.py &", shell=True)
                print("killed the thing")
                GPIO.cleanup()
                exit()
            else:
                while True:
                    c_1, addr_1 = s.accept()
                    cancel = str(recv(c_1))
                    cancel = cancel.split(" ")
                    send(c_1, 'Y')
                    c_1.close()
                    if cancel[0] == "cancel":
                        #kill running experiment processes
                        print("killing experiment processes")
                        subprocess.call("sudo pkill -f IRLED2.py &", shell=True)
                        subprocess.call("sudo pkill -f raspivid &", shell=True)
                        break
                
    s.close()


def main():
    hostname = socket.gethostbyname(socket.gethostname())
    #print(hostname)
    server('')

if __name__ == '__main__':
    main()

