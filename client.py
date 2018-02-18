#!/usr/bin/python
import socket
import argparse
import struct
import os
import signal

###for use in central computer
###for this one I left in the basic game logic because it might be useful
###for our project. If not, just delete it
###the main is still needed

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

def client(ip):
    """Client function"""
    s = socket.socket()
    s.connect((ip, 3679))
    cont = 0
    while(cont == 0):
        #message = recv(s)
        code = recv(s)
        print(code)
    s.close

def main():
    client('140.160.139.121')

if __name__ == '__main__':
    main()
