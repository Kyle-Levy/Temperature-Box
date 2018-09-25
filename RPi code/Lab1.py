#################################################################################################
#
#   Lab1.py: this python script will read temperature data and output the temperature t0 the 7
#            LEDs.  the code runs off a "mock" interupt and will trigger when GPIO 23 fits the
#            conditional.
#
##################################################################################################


import RPi.GPIO as GPIO
import os
import glob
import time
import socket

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)


# Code from http://www.circuitbasics.com/raspberry-pi-ds18b20-temperature-sensor-tutorial/
os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')

base_dir = '/sys/bus/w1/devices/'

################################ I/O PIN DECLARATIONS #########################################
# setting GPIO pins as outputs LSB-> MSB
GPIO.setup(17, GPIO.OUT)    #6
GPIO.setup(27, GPIO.OUT)    #5
GPIO.setup(22, GPIO.OUT)    #4
GPIO.setup(5, GPIO.OUT)     #3
GPIO.setup(6, GPIO.OUT)     #2
GPIO.setup(13, GPIO.OUT)    #1
GPIO.setup(26, GPIO.OUT)    #0
GPIO.setup(19, GPIO.OUT)    #0
#GPIO pin 8 is the input.
GPIO.setup(23, GPIO.IN)     #Button
GPIO.setup(21, GPIO.IN)     #Temp_sensor_detector
GPIO.setup(4, GPIO.IN)


#Initialise GPIO13 to low (False) so that the LED is off.
# Code taken from DS18B20 tutorial
def read_temp_raw():
    device_folder = glob.glob(base_dir + '28*')[0]
    device_file = device_folder + '/w1_slave'
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

def read_temp():
    lines = read_temp_raw()
    loop_error = 0
    while lines[0].strip()[-3:] != 'YES':
        if loop_error > 3:
            return False, False
        time.sleep(0.2)
        print('')
        lines = read_temp_raw()
        loop_error += 1
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        temp_c = float(temp_string) / 1000.0
        temp_f = temp_c * 9.0 / 5.0 + 32.0
        return temp_c, temp_f

def decimal2binary(num):
    binary = []
    for i in [6,5,4,3,2,1,0]:
        binary.append(num // 2**i)
        num = num % 2**i
    return(binary)

def binary2decimal(binary_array):
    binary_array= list(reversed(binary_array))
    num=0
    for i in [6,5,4,3,2,1,0]:
        num = num + binary_array[i] * (2**i)
    return(num)

def twosCompliment(num):
    bin_array = decimal2binary(abs(num))
    invert_bin = [0, 0, 0, 0, 0, 0, 0]
    for i in [6,5,4,3,2,1,0]:
        if bin_array[i] == 0:
            invert_bin[i]= 1
        elif bin_array[i] ==1:
            invert_bin[i]=0
    twoscomp= decimal2binary(binary2decimal(invert_bin) +1)
    return(twoscomp)


################################ START SELF WRITTEN CODE #####################################
def clear_pins():
    pin_array = [17, 27, 22, 5, 6, 13, 26]
    for j in range(0, 7):
        GPIO.output(pin_array[j], False)

def disconnected_lights():
    pin_array = [17, 27, 5, 13, 26]
    for pin in pin_array:
        GPIO.output(pin, True)




def connect_to_server(disconnected_list):
    try:
        host = '192.168.43.21'
        port = 4444
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host, port))
        time.sleep(3)
        to_send = 'E'
        sock.send(to_send + '\n')
        print('E sent')

        big_string = ''
        for element in disconnected_list:
            if element != False and element != 'False':
                big_string+= str(element) + '-'
        big_string = big_string[:-1]
        print(big_string)
        sock.send(big_string + '\n')
        to_send = 'S'
        sock.send(to_send + '\n')
        print('S sent')

        return True, sock, list(())
    except socket.error:
        return False, False, disconnected_list

def bin_add(*args): return bin(sum(int(x, 2) for x in args))[2:]


def main_function(connected, sock, disconnected_list):
    while 1:
        while True:
            #call temp method and
            if not connected:
                connected, sock, disconnected_list = connect_to_server(disconnected_list)
                if not connected:
                    computer_toggle_on = 0


            temp_sensor_connected = GPIO.input(21)
            print('Temp_sensor_connected: ' + str(temp_sensor_connected))

            if temp_sensor_connected:
                try:
                    temp_c, temp_f = read_temp()
                    if(temp_c == False and temp_f == False):
                        if(connected):
                            return connected, sock, disconnected_list

                except IndexError:
                    if(connected):
                        return connected, sock, disconnected_list


                celc = str(temp_c)

                if not connected:
                    disconnected_list.insert(0, celc)
                    if (len(disconnected_list) > 300):
                        print('QUEUE FULL')
                        disconnected_list.pop()

                #If connected, send the data
                if connected:
                    try:
                        to_send = celc
                        sock.send(to_send + '\n')
                    except socket.error:
                        connected = False
                        sock.close()
                        computer_toggle_on = 0

                if temp_c >=0:
                        bin_array = decimal2binary(int(temp_c))
                        GPIO.output(19,False)
                elif temp_c < 0:
                        bin_array = twosCompliment(temp_c)
                        GPIO.output(19,True)

                pin_array = [17, 27, 22, 5, 6, 13, 26]

                button_pressed = GPIO.input(23)

                #Receive attempted button press from GUI, if connect

                if connected:
                    try:
                        print('help Im stuck')
                        computer_toggle_on = int(sock.recv(100).decode()[2])
                        print('Computer toggled on: ' + str(computer_toggle_on))
                    except socket.error:
                        connected = False
                        sock.close()
                        computer_toggle_on = 0

                #If hardware button is pressed, or GUI button is toggled on
                if (not button_pressed or computer_toggle_on):
                    for i in range(0,7):
                        if bin_array[i] == 1:
                            GPIO.output(pin_array[i], True)
                        elif bin_array[i] == 0:
                            GPIO.output(pin_array[i], False)
                else:
                    clear_pins()

                print('End of loop')
            #Temperature not connected,
            else:
                #but connected to socket, send server data
                disconnected_lights()
                print('Not connected')
                if connected:
                    try:
                        to_send = 'D'
                        sock.send(to_send + '\n')
                        print('D sent')
                        time.sleep(.13)
                    except socket.error:
                        connected = False
                        sock.close()
                        time.sleep(.13)
                else:
                    disconnected_list.insert(0,'D')
                    time.sleep(.8)
                    if(len(disconnected_list) > 300):
                        print('QUEUE FULL')
                        disconnected_list.pop()

connection = False
disconnected_list = list(())
while True:
    clear_pins()
    if not connection:
        connection, sock, disconnected_list = main_function(False, False, disconnected_list)
    connection, sock, disconnected_list = main_function(connection, sock, disconnected_list)