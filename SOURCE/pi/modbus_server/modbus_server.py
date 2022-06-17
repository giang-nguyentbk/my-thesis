#!/usr/bin/env python3
"""
Pymodbus Server With Updating Thread
--------------------------------------------------------------------------

This is an example of having a background thread updating the
context while the server is operating. This can also be done with
a python thread::

    from threading import Thread

    thread = Thread(target=updating_writer, args=(context,))
    thread.start()
"""
# --------------------------------------------------------------------------- #
# import the modbus libraries we need
# --------------------------------------------------------------------------- #
from pymodbus.server.asynchronous import StartTcpServer
from pymodbus.device import ModbusDeviceIdentification
from pymodbus.datastore import ModbusSequentialDataBlock
from pymodbus.datastore import ModbusSlaveContext, ModbusServerContext
from pymodbus.transaction import ModbusRtuFramer, ModbusAsciiFramer
import RPi.GPIO as GPIO

# --------------------------------------------------------------------------- #
# import the twisted libraries we need
# --------------------------------------------------------------------------- #
from twisted.internet.task import LoopingCall

# --------------------------------------------------------------------------- #
# configure the service logging
# --------------------------------------------------------------------------- #
import logging
logging.basicConfig()
log = logging.getLogger()
log.setLevel(logging.DEBUG)

# --------------------------------------------------------------------------- #
# define your callback process
# --------------------------------------------------------------------------- #

led_pin = 18
btnON_pin = 20
btnOFF_pin = 21
GPIO.setmode(GPIO.BCM)

GPIO.setwarnings(False)
GPIO.setup(led_pin, GPIO.OUT)
GPIO.setup(btnON_pin, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(btnOFF_pin, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

pwm = GPIO.PWM(led_pin, 100)
pwm.start(50)
duty = 50

def updating_writer(a):
    """ A worker process that runs every so often and
    updates live values of the context. It should be noted
    that there is a race condition for the update.

    :param arguments: The input arguments to the call
    """



#    log.debug("updating the context")
    context = a[0]
    register = 3
    slave_id = 0x00
    address = 0x00
    
    fx = 1
    coils = context[slave_id].getValues(fx, address, count=3)

    value = context[slave_id].getValues(register, address, count=1)
#    print(value)
#    print("----------------------------------------------------")
    if coils[2] == True:
        if value[0] >= 0 and value[0] <= 100:
            if value[0] != duty:
                pwm.ChangeDutyCycle(value[0])
    else:
        pwm.ChangeDutyCycle(0)

    on = GPIO.input(btnON_pin)
    off = GPIO.input(btnOFF_pin)

    fx_wr = 5
    context[slave_id].setValues(fx_wr, address, [int(on), int(off)])


def run_updating_server():
    # ----------------------------------------------------------------------- # 
    # initialize your data store
    # ----------------------------------------------------------------------- # 
    
    store = ModbusSlaveContext(
        di=ModbusSequentialDataBlock(0, [0]*2),
        co=ModbusSequentialDataBlock(0, [0]*6),
        hr=ModbusSequentialDataBlock(0, [0]*2),
        ir=ModbusSequentialDataBlock(0, [0]*2))
    context = ModbusServerContext(slaves=store, single=True)
    
    # ----------------------------------------------------------------------- # 
    # initialize the server information
    # ----------------------------------------------------------------------- # 
    identity = ModbusDeviceIdentification()
    identity.VendorName = 'pymodbus'
    identity.ProductCode = 'PM'
    identity.VendorUrl = 'http://github.com/riptideio/pymodbus/'
    identity.ProductName = 'pymodbus Server'
    identity.ModelName = 'pymodbus Server'
    
    # ----------------------------------------------------------------------- # 
    # run the server you want
    # ----------------------------------------------------------------------- # 
    time = 0.1  # 5 seconds delay
    loop = LoopingCall(f=updating_writer, a=(context,))
    loop.start(time, now=False) # initially delay by time
    StartTcpServer(context, identity=identity, address=("192.168.137.162", 5020))

def current_milliseconds():
    return round(time.time() * 1000)


if __name__ == "__main__":
	try:
		run_updating_server()
	except KeyboardInterrupt:
		pass
	pwm.stop()
	GPIO.cleanup()
