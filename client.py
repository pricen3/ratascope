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

def parse_command_line_args():
    """ Use argparse to return command line arguments
    as a namespace object.
    """
    parser = argparse.ArgumentParser()
    parser.add_argument(
        '-ip',
        help='IP address of cage',
        metavar='ip',
        type=str,
        required=True
    )
    parser.add_argument(
        '-s',
        help='Comand string to be sent',
        metavar='s',
        type=str,
        required=True
    )
    return parser.parse_args()

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

def client(ip, send_string):
    """Client function"""
    s = socket.socket()
    s.connect((ip, 3684)) #3679
    cont = 0
    while(cont == 0):
        #message = recv(s)
        #code = recv(s)
        #print(code)
        send(s, send_string)
        code = recv(s)
        if code == "Y":
            cont += 1
    s.close

def main():
    args = parse_command_line_args()
    client(args.ip, args.s)

if __name__ == '__main__':
    main()
