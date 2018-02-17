#!/usr/bin/python          
import socket             
import argparse
import struct
import os
import signal

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

def server(ip, word):
    """server function"""
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)       
    s.bind((ip, 3679))
    s.listen(5)                
    while True:
        c, addr = s.accept()
        ###message is what is being sent to the client. Change as needed###
        message = 'Board : ' + board + '(' + str(guesses) + ' guesses left)'
        send(c, message)
        send(c, 'C')
        guess = recv(c)
        ###need to put in what to do with the recieved message###
        c.close()
        exit()
    s.close()
