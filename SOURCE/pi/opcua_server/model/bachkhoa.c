/* WARNING: This is a generated file.
 * Any manual changes will be overwritten. */

#include "bachkhoa.h"


/* bachkhoa - ns=1;i=10000 */

static UA_StatusCode function_bachkhoa_0_begin(UA_Server *server, UA_UInt16* ns) {
UA_StatusCode retVal = UA_STATUSCODE_GOOD;
UA_ObjectAttributes attr = UA_ObjectAttributes_default;
attr.displayName = UA_LOCALIZEDTEXT("", "bachkhoa");
retVal |= UA_Server_addNode_begin(server, UA_NODECLASS_OBJECT,
UA_NODEID_NUMERIC(ns[1], 10000LU),
UA_NODEID_NUMERIC(ns[0], 85LU),
UA_NODEID_NUMERIC(ns[0], 35LU),
UA_QUALIFIEDNAME(ns[1], "bachkhoa"),
UA_NODEID_NUMERIC(ns[0], 58LU),
(const UA_NodeAttributes*)&attr, &UA_TYPES[UA_TYPES_OBJECTATTRIBUTES],NULL, NULL);
return retVal;
}

static UA_StatusCode function_bachkhoa_0_finish(UA_Server *server, UA_UInt16* ns) {
return UA_Server_addNode_finish(server, 
UA_NODEID_NUMERIC(ns[1], 10000LU)
);
}

/* Bach Khoa CS1 - ns=1;i=11000 */

static UA_StatusCode function_bachkhoa_1_begin(UA_Server *server, UA_UInt16* ns) {
UA_StatusCode retVal = UA_STATUSCODE_GOOD;
UA_ObjectAttributes attr = UA_ObjectAttributes_default;
attr.displayName = UA_LOCALIZEDTEXT("", "Bach Khoa CS1");
retVal |= UA_Server_addNode_begin(server, UA_NODECLASS_OBJECT,
UA_NODEID_NUMERIC(ns[1], 11000LU),
UA_NODEID_NUMERIC(ns[1], 10000LU),
UA_NODEID_NUMERIC(ns[0], 47LU),
UA_QUALIFIEDNAME(ns[1], "Bach Khoa CS1"),
UA_NODEID_NUMERIC(ns[0], 58LU),
(const UA_NodeAttributes*)&attr, &UA_TYPES[UA_TYPES_OBJECTATTRIBUTES],NULL, NULL);
return retVal;
}

static UA_StatusCode function_bachkhoa_1_finish(UA_Server *server, UA_UInt16* ns) {
return UA_Server_addNode_finish(server, 
UA_NODEID_NUMERIC(ns[1], 11000LU)
);
}

/* Tram 1 - ns=1;i=11100 */

static UA_StatusCode function_bachkhoa_2_begin(UA_Server *server, UA_UInt16* ns) {
UA_StatusCode retVal = UA_STATUSCODE_GOOD;
UA_ObjectAttributes attr = UA_ObjectAttributes_default;
attr.displayName = UA_LOCALIZEDTEXT("", "Tram 1");
retVal |= UA_Server_addNode_begin(server, UA_NODECLASS_OBJECT,
UA_NODEID_NUMERIC(ns[1], 11100LU),
UA_NODEID_NUMERIC(ns[1], 11000LU),
UA_NODEID_NUMERIC(ns[0], 47LU),
UA_QUALIFIEDNAME(ns[1], "Tram 1"),
UA_NODEID_NUMERIC(ns[0], 58LU),
(const UA_NodeAttributes*)&attr, &UA_TYPES[UA_TYPES_OBJECTATTRIBUTES],NULL, NULL);
return retVal;
}

static UA_StatusCode function_bachkhoa_2_finish(UA_Server *server, UA_UInt16* ns) {
return UA_Server_addNode_finish(server, 
UA_NODEID_NUMERIC(ns[1], 11100LU)
);
}

/* Delay - ns=1;i=11103 */

static UA_StatusCode function_bachkhoa_3_begin(UA_Server *server, UA_UInt16* ns) {
UA_StatusCode retVal = UA_STATUSCODE_GOOD;
UA_VariableAttributes attr = UA_VariableAttributes_default;
attr.minimumSamplingInterval = 0.000000;
attr.userAccessLevel = 1;
attr.accessLevel = 1;
/* Value rank inherited */
attr.valueRank = -2;
attr.dataType = UA_NODEID_NUMERIC(ns[0], 5LU);
UA_UInt16 *variablenode_ns_1_i_11103_variant_DataContents =  UA_UInt16_new();
if (!variablenode_ns_1_i_11103_variant_DataContents) return UA_STATUSCODE_BADOUTOFMEMORY;
UA_UInt16_init(variablenode_ns_1_i_11103_variant_DataContents);
*variablenode_ns_1_i_11103_variant_DataContents = (UA_UInt16) 0;
UA_Variant_setScalar(&attr.value, variablenode_ns_1_i_11103_variant_DataContents, &UA_TYPES[UA_TYPES_UINT16]);
attr.displayName = UA_LOCALIZEDTEXT("", "Delay");
#ifdef UA_ENABLE_NODESET_COMPILER_DESCRIPTIONS
attr.description = UA_LOCALIZEDTEXT("", "Delay");
#endif
retVal |= UA_Server_addNode_begin(server, UA_NODECLASS_VARIABLE,
UA_NODEID_NUMERIC(ns[1], 11103LU),
UA_NODEID_NUMERIC(ns[1], 11100LU),
UA_NODEID_NUMERIC(ns[0], 47LU),
UA_QUALIFIEDNAME(ns[1], "Delay"),
UA_NODEID_NUMERIC(ns[0], 63LU),
(const UA_NodeAttributes*)&attr, &UA_TYPES[UA_TYPES_VARIABLEATTRIBUTES],NULL, NULL);
UA_UInt16_delete(variablenode_ns_1_i_11103_variant_DataContents);
return retVal;
}

static UA_StatusCode function_bachkhoa_3_finish(UA_Server *server, UA_UInt16* ns) {
return UA_Server_addNode_finish(server, 
UA_NODEID_NUMERIC(ns[1], 11103LU)
);
}

/* OFF - ns=1;i=11102 */

static UA_StatusCode function_bachkhoa_4_begin(UA_Server *server, UA_UInt16* ns) {
UA_StatusCode retVal = UA_STATUSCODE_GOOD;
UA_VariableAttributes attr = UA_VariableAttributes_default;
attr.minimumSamplingInterval = 0.000000;
attr.userAccessLevel = 1;
attr.accessLevel = 1;
/* Value rank inherited */
attr.valueRank = -2;
attr.dataType = UA_NODEID_NUMERIC(ns[0], 1LU);
UA_Boolean *variablenode_ns_1_i_11102_variant_DataContents =  UA_Boolean_new();
if (!variablenode_ns_1_i_11102_variant_DataContents) return UA_STATUSCODE_BADOUTOFMEMORY;
UA_Boolean_init(variablenode_ns_1_i_11102_variant_DataContents);
*variablenode_ns_1_i_11102_variant_DataContents = (UA_Boolean) false;
UA_Variant_setScalar(&attr.value, variablenode_ns_1_i_11102_variant_DataContents, &UA_TYPES[UA_TYPES_BOOLEAN]);
attr.displayName = UA_LOCALIZEDTEXT("", "OFF");
#ifdef UA_ENABLE_NODESET_COMPILER_DESCRIPTIONS
attr.description = UA_LOCALIZEDTEXT("", "OFF");
#endif
retVal |= UA_Server_addNode_begin(server, UA_NODECLASS_VARIABLE,
UA_NODEID_NUMERIC(ns[1], 11102LU),
UA_NODEID_NUMERIC(ns[1], 11100LU),
UA_NODEID_NUMERIC(ns[0], 47LU),
UA_QUALIFIEDNAME(ns[1], "OFF"),
UA_NODEID_NUMERIC(ns[0], 63LU),
(const UA_NodeAttributes*)&attr, &UA_TYPES[UA_TYPES_VARIABLEATTRIBUTES],NULL, NULL);
UA_Boolean_delete(variablenode_ns_1_i_11102_variant_DataContents);
return retVal;
}

static UA_StatusCode function_bachkhoa_4_finish(UA_Server *server, UA_UInt16* ns) {
return UA_Server_addNode_finish(server, 
UA_NODEID_NUMERIC(ns[1], 11102LU)
);
}

/* ON - ns=1;i=11101 */

static UA_StatusCode function_bachkhoa_5_begin(UA_Server *server, UA_UInt16* ns) {
UA_StatusCode retVal = UA_STATUSCODE_GOOD;
UA_VariableAttributes attr = UA_VariableAttributes_default;
attr.minimumSamplingInterval = 0.000000;
attr.userAccessLevel = 1;
attr.accessLevel = 1;
/* Value rank inherited */
attr.valueRank = -2;
attr.dataType = UA_NODEID_NUMERIC(ns[0], 1LU);
UA_Boolean *variablenode_ns_1_i_11101_variant_DataContents =  UA_Boolean_new();
if (!variablenode_ns_1_i_11101_variant_DataContents) return UA_STATUSCODE_BADOUTOFMEMORY;
UA_Boolean_init(variablenode_ns_1_i_11101_variant_DataContents);
*variablenode_ns_1_i_11101_variant_DataContents = (UA_Boolean) false;
UA_Variant_setScalar(&attr.value, variablenode_ns_1_i_11101_variant_DataContents, &UA_TYPES[UA_TYPES_BOOLEAN]);
attr.displayName = UA_LOCALIZEDTEXT("", "ON");
#ifdef UA_ENABLE_NODESET_COMPILER_DESCRIPTIONS
attr.description = UA_LOCALIZEDTEXT("", "ON");
#endif
retVal |= UA_Server_addNode_begin(server, UA_NODECLASS_VARIABLE,
UA_NODEID_NUMERIC(ns[1], 11101LU),
UA_NODEID_NUMERIC(ns[1], 11100LU),
UA_NODEID_NUMERIC(ns[0], 47LU),
UA_QUALIFIEDNAME(ns[1], "ON"),
UA_NODEID_NUMERIC(ns[0], 63LU),
(const UA_NodeAttributes*)&attr, &UA_TYPES[UA_TYPES_VARIABLEATTRIBUTES],NULL, NULL);
UA_Boolean_delete(variablenode_ns_1_i_11101_variant_DataContents);
return retVal;
}

static UA_StatusCode function_bachkhoa_5_finish(UA_Server *server, UA_UInt16* ns) {
return UA_Server_addNode_finish(server, 
UA_NODEID_NUMERIC(ns[1], 11101LU)
);
}

UA_StatusCode bachkhoa(UA_Server *server) {
UA_StatusCode retVal = UA_STATUSCODE_GOOD;
/* Use namespace ids generated by the server */
UA_UInt16 ns[2];
ns[0] = UA_Server_addNamespace(server, "http://opcfoundation.org/UA/");
ns[1] = UA_Server_addNamespace(server, "https://thesis.org/outstations/");

/* Load custom datatype definitions into the server */
if((retVal = function_bachkhoa_0_begin(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_1_begin(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_2_begin(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_3_begin(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_4_begin(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_5_begin(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_5_finish(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_4_finish(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_3_finish(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_2_finish(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_1_finish(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
if((retVal = function_bachkhoa_0_finish(server, ns)) != UA_STATUSCODE_GOOD) return retVal;
return retVal;
}
