/* This work is licensed under a Creative Commons CCZero 1.0 Universal License.
 * See http://creativecommons.org/publicdomain/zero/1.0/ for more information. */

#include <signal.h>
#include <stdio.h>
#include "../open62541/open62541.h"
#include "../model/bachkhoa.h"
#include <modbus.h>
#include <sys/time.h>

UA_Boolean running = true;
modbus_t *mb;
uint16_t tab_reg[32];
uint16_t set_reg[32];
bool run = false;
uint16_t duty = 50;
bool start = false;
bool stop = false;

static void
beforeReadStart(UA_Server *server ,
                const UA_NodeId *sessionId, void *sessionContext,
                const UA_NodeId *nodeid, void *nodeContext,
                const UA_NumericRange *range, const UA_DataValue *data) {

	uint8_t res[1] = {0};
        modbus_read_bits(mb, 0, 1, res);

	if(res[0] == 1)
		start = true;
	else
		start = false;

	UA_Variant value;
        UA_Variant_setScalar(&value, &start, &UA_TYPES[UA_TYPES_BOOLEAN]);
        UA_Server_writeValue(server, UA_NODEID_NUMERIC(2, 11101), value);
}

static void
beforeReadStop(UA_Server *server ,
                const UA_NodeId *sessionId, void *sessionContext,
                const UA_NodeId *nodeid, void *nodeContext,
                const UA_NumericRange *range, const UA_DataValue *data) {

	uint8_t res[1] = {0};
        modbus_read_bits(mb, 1, 1, res);
	//printf("stop = %d\n", res[0]);

        if(res[0] == 1)
                stop = true;
        else
                stop = false;

	UA_Variant value;
        UA_Variant_setScalar(&value, &stop, &UA_TYPES[UA_TYPES_BOOLEAN]);
        UA_Server_writeValue(server, UA_NODEID_NUMERIC(2, 11102), value);
}


static void
beforeReadDelay(UA_Server *server ,
                const UA_NodeId *sessionId, void *sessionContext,
                const UA_NodeId *nodeid, void *nodeContext,
                const UA_NumericRange *range, const UA_DataValue *data) {

	struct timeval tv;

	gettimeofday(&tv, NULL);

	unsigned long long millisecondsSinceEpoch =
		(unsigned long long)(tv.tv_sec) * 1000 +
		(unsigned long long)(tv.tv_usec) / 1000;

	UA_UInt16 startTime = millisecondsSinceEpoch % 10000;
	//printf("start = %d\n", startTime);

        UA_Variant value;
        UA_Variant_setScalar(&value, &startTime, &UA_TYPES[UA_TYPES_UINT16]);
        UA_Server_writeValue(server, UA_NODEID_NUMERIC(2, 11103), value);
}

static UA_StatusCode
writeRun(UA_Server *server ,
                const UA_NodeId *sessionId, void *sessionContext,
                const UA_NodeId *nodeid, void *nodeContext,
                const UA_NumericRange *range, const UA_DataValue *data) {

	int temp = 0;
        run = * (UA_Boolean*) data->value.data;
	if(run == true)
        	temp = 1;
	else
		temp = 0;

	modbus_write_bit(mb, 2, temp);

        return UA_STATUSCODE_GOOD;
}

static UA_StatusCode
readRun(UA_Server *server ,
                const UA_NodeId *sessionId, void *sessionContext,
                const UA_NodeId *nodeid, void *nodeContext,
                UA_Boolean sourceTimeStamp, const UA_NumericRange *range,
                UA_DataValue *dataValue) {

        UA_Variant_setScalarCopy(&dataValue->value, &run, &UA_TYPES[UA_TYPES_BOOLEAN]);
        dataValue->hasValue = true;
        return UA_STATUSCODE_GOOD;
}

static void
addRunDataSource(UA_Server *server) {
        UA_VariableAttributes attr = UA_VariableAttributes_default;
        attr.displayName = UA_LOCALIZEDTEXT("en-US", "Run");
        attr.accessLevel = UA_ACCESSLEVELMASK_READ | UA_ACCESSLEVELMASK_WRITE;

        UA_DataSource runDataSource;
        runDataSource.read = readRun;
        runDataSource.write = writeRun;
        UA_Server_addDataSourceVariableNode(server, UA_NODEID_NUMERIC(2, 11104),
                                        UA_NODEID_NUMERIC(2, 11100),
                                        UA_NODEID_NUMERIC(0, UA_NS0ID_ORGANIZES),
                                        UA_QUALIFIEDNAME(2, "Run"),
                                        UA_NODEID_NUMERIC(0, UA_NS0ID_BASEDATAVARIABLETYPE),
                                        attr, runDataSource,
                                        NULL, NULL);
}



static UA_StatusCode
writeDuty(UA_Server *server ,
         	const UA_NodeId *sessionId, void *sessionContext,
                const UA_NodeId *nodeid, void *nodeContext,
                const UA_NumericRange *range, const UA_DataValue *data) {


	duty = * (UA_UInt16*) data->value.data;
	modbus_write_register(mb, 0, duty);

	return UA_STATUSCODE_GOOD;
}

static UA_StatusCode
readDuty(UA_Server *server ,
                const UA_NodeId *sessionId, void *sessionContext,
                const UA_NodeId *nodeid, void *nodeContext,
                UA_Boolean sourceTimeStamp, const UA_NumericRange *range,
		UA_DataValue *dataValue) {

	UA_Variant_setScalarCopy(&dataValue->value, &duty, &UA_TYPES[UA_TYPES_UINT16]);
	dataValue->hasValue = true;
	return UA_STATUSCODE_GOOD;
}

static void
addDutyDataSource(UA_Server *server) {
	UA_VariableAttributes attr = UA_VariableAttributes_default;
	attr.displayName = UA_LOCALIZEDTEXT("en-US", "Duty");
	attr.accessLevel = UA_ACCESSLEVELMASK_READ | UA_ACCESSLEVELMASK_WRITE;

	UA_DataSource runDataSource;
	runDataSource.read = readDuty;
	runDataSource.write = writeDuty;
	UA_Server_addDataSourceVariableNode(server, UA_NODEID_NUMERIC(2, 11105),
					UA_NODEID_NUMERIC(2, 11100),
					UA_NODEID_NUMERIC(0, UA_NS0ID_ORGANIZES),
					UA_QUALIFIEDNAME(2, "Duty"),
					UA_NODEID_NUMERIC(0, UA_NS0ID_BASEDATAVARIABLETYPE),
					attr, runDataSource,
					NULL, NULL);
}

static UA_DataValue *externalValue;
static void
addExternalRunDataSource(UA_Server *server) {

	UA_ValueBackend valueBackend;
	valueBackend.backendType = UA_VALUEBACKENDTYPE_EXTERNAL;
	valueBackend.backend.external.value = &externalValue;

	UA_Server_setVariableNode_valueBackend(server, UA_NODEID_NUMERIC(1, 3000), valueBackend);
}


static void stopHandler(int sign)
{
    UA_LOG_INFO(UA_Log_Stdout, UA_LOGCATEGORY_SERVER, "received ctrl-c");
    running = false;
}

/* ------------------------------------------------------------*/

int main(int argc, char * argv[])
{
    signal(SIGINT, stopHandler);
    signal(SIGTERM, stopHandler);


// Khoi chay mot Client Modbus TCP
    mb = modbus_new_tcp("192.168.137.162", 5020);
    int isModbusSucc = modbus_connect(mb);
    if (isModbusSucc == -1) {
        fprintf(stderr, "Modbus connection failed: %s\n",
                modbus_strerror(errno));
    } else
    	printf("Modbus connection successful! %d\n", isModbusSucc);


// Khoi chay mot Server OPC UA empty
    UA_Server *server = UA_Server_new();

// Cai dat de co the nhap IP address va portnumber
    if(argc > 2){
	UA_Int16 port_number = atoi(argv[2]);
	UA_ServerConfig_setMinimal(UA_Server_getConfig(server), port_number, 0);
    }
    else
	UA_ServerConfig_setDefault(UA_Server_getConfig(server));
    if(argc > 1) {
	UA_String hostname;
	UA_String_init(&hostname);
	hostname.length = strlen(argv[1]);
	hostname.data = (UA_Byte *) argv[1];
	UA_ServerConfig_setCustomHostname(UA_Server_getConfig(server), hostname);
    }


// Tien hanh add Information Model vao Server
    UA_StatusCode retval = bachkhoa(server);
    /* create nodes from nodeset */
    if (retval != UA_STATUSCODE_GOOD)
    {
        UA_LOG_ERROR(UA_Log_Stdout, UA_LOGCATEGORY_SERVER, "Could not add imformation model to server!!!");
        retval = UA_STATUSCODE_BADUNEXPECTEDERROR;
    }


//Get Namespace Index
    size_t ns0;
    UA_UInt16 isFound0 = UA_Server_getNamespaceByName(server, UA_STRING("http://opcfoundation.org/UA/"), &ns0);
    if (isFound0 == UA_STATUSCODE_GOOD) UA_LOG_INFO(UA_Log_Stdout, UA_LOGCATEGORY_USERLAND, "Namespace0 have index:  %d", ns0);
    else UA_LOG_INFO(UA_Log_Stdout, UA_LOGCATEGORY_USERLAND, "Not found Namespace0 in Information Model!!!");

    size_t ns1;
    UA_UInt16 isFound1 = UA_Server_getNamespaceByName(server, UA_STRING("https://thesis.org/outstations/"), &ns1);
     if (isFound1 == UA_STATUSCODE_GOOD) UA_LOG_INFO(UA_Log_Stdout, UA_LOGCATEGORY_USERLAND, "Namespace1 have index:  %d", ns1);
    else UA_LOG_INFO(UA_Log_Stdout, UA_LOGCATEGORY_USERLAND, "Not found Namespace1 in Information Model!!!");   

/* Add DataSource to LED01 */
    addRunDataSource(server);
    addDutyDataSource(server);
    addExternalRunDataSource(server);



//Add Callback Method to Temperature Node
    UA_ValueCallback callback;
    callback.onRead = beforeReadStart;
    callback.onWrite = NULL;
    UA_Server_setVariableNode_valueCallback(server, UA_NODEID_NUMERIC(ns1, 11101), callback);

    UA_ValueCallback callback1;
    callback1.onRead = beforeReadStop;
    callback1.onWrite = NULL;
    UA_Server_setVariableNode_valueCallback(server, UA_NODEID_NUMERIC(ns1, 11102), callback1);

    UA_ValueCallback callback2;
    callback2.onRead = beforeReadDelay;
    callback2.onWrite = NULL;
    UA_Server_setVariableNode_valueCallback(server, UA_NODEID_NUMERIC(ns1, 11103), callback2);


// Run Server
    UA_LOG_INFO(UA_Log_Stdout, UA_LOGCATEGORY_USERLAND, "Server is starting....");
    retval = UA_Server_run(server, &running);
    /* Den day, CT vao vong lap vo han cho den khi user nhan Ctrl+C */
    /* Truoc do, CT chay tuan tu tu tren xuong duoi */

// Delete Client Modbus TCP
    modbus_close(mb);
    modbus_free(mb);

// Delete Server OPC UA
    UA_LOG_INFO(UA_Log_Stdout, UA_LOGCATEGORY_USERLAND, "Server is shutting down....");
    UA_Server_delete(server);

    return (int) retval;
}

