opcua_ip=10.66.66.5
opcua_port=4840

server: ../model/bachkhoa.o ../open62541/open62541.o server.o
	gcc -Wall ../model/bachkhoa.o ../open62541/open62541.o server.o -o server `pkg-config --cflags --libs libmodbus`

../model/bachkhoa.o: ../model/bachkhoa.c
	gcc -Wall -c ../model/bachkhoa.c -DUA_ENABLE_AMALGAMATION=true -o ../model/bachkhoa.o

../open62541/open62541.o: ../open62541/open62541.c
	gcc -Wall -c -std=c99 ../open62541/open62541.c -o ../open62541/open62541.o

server.o: server.c
	gcc -Wall -c server.c -o server.o `pkg-config --cflags --libs libmodbus`

clean:
	rm *.o server ../model/*.o

cleanall:
	rm *.o server ../model/*.o ../open62541/*.o

run:
	./server $(opcua_ip) $(opcua_port)

