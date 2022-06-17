# my-thesis
The thesis name is "*BUILDING A REMOTE MONITORING AND CONTROL SOLUTION INTEGRATING WITH OPC UA*".

The project's tasks are:
  1. Acquiring data from Raspberry Pi gateways which will exchange data with Modbus/DNP3 server at monitoring outstations.
  2. Sending total data using OPCUA protocol and Wireguard VPN from distributed gateways to a Azure VPS.
  3. Exchange data with an Android and Web application using Socket.IO.

The project's components are:
  1. Raspberry Pi gateway: 
     - Modbus Client: play a role as Modbus driver to connect and exchange data with Modbus server at monitoring oustations.
     - OPC UA Server: a terminal for a bridge between a gateway and a VPS.
     - Wireguard Client: turn gateways and VPS into a local network which is private and secure.
     - Modbus Server: simulate Modbus servers at monitoring outstations.
  2. Azure VPS:
     - OPC UA Client: play a role as OPC UA driver to connect and exchange data with OPC UA server at gateways.
     - Backend Server NodeJS: used to create Socket.IO topics to exchange data between Android/Web apps and VPS. Also, logging data to MongoDB database.
       - (Because of time constraint, building a Web app is tend to server-side-rendering instead of an independent frontend).
     - Wireguard Server.
     - OPC UA Server: simulate OPC UA servers at gateways.
  3. Android App features:
     - Login.
     - Outstation list.
     - User profile.
     - Realtime data at numeric form and linechart form.
     - Alarm.
  4. Web app features: 
     - Features similar Android app.
     - Oustation list on Map.
     - Download Excel reports.
     - Register.
