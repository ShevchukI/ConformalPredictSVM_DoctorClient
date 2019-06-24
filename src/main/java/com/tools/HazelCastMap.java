package com.tools;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Dataset;
import com.models.Doctor;
import com.models.Patient;

public class HazelCastMap {
    private static final String INSTANCE_NAME = "mainDoctorInstance";
    private static final String USER_MAP_NAME = "authorizationDoctor";
    private static final String DOCTOR_MAP_NAME = "doctor";
    private static final String DATASET_MAP_NAME = "datasetDoctor";
    private static final String KEY_MAP_NAME = "keyDoctor";
    private static final String MISCELLANEOUS_MAP_NAME = "miscDoctor";
    private static final String PATIENT_MAP_NAME = "patient";
    private static HazelcastInstance hazelcastInstance;

    public static void createInstanceAndMap() {
        Config config = new Config();
        config.setInstanceName(INSTANCE_NAME);
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(1919);
        config.addMapConfig(createMapWithName(USER_MAP_NAME));
        config.addMapConfig(createMapWithName(DATASET_MAP_NAME));
        config.addMapConfig(createMapWithName(KEY_MAP_NAME));
        config.addMapConfig(createMapWithName(MISCELLANEOUS_MAP_NAME));
        config.addMapConfig(createMapWithName(PATIENT_MAP_NAME));
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }

    private static MapConfig createMapWithName(String mapName) {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setName(mapName);
        return mapConfig;
    }

    public static HazelcastInstance getInstance() {
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME);
    }

    public static IMap getMapByName(String mapName) {
        return Hazelcast.getHazelcastInstanceByName(INSTANCE_NAME).getMap(mapName);
    }

//    public static void fillMap(Doctor doctorFromJson, String[] authorization) {
////        Doctor doctor = doctorFromJson;
//        String key = Encryptor.genRandString();
//        String vector = Encryptor.genRandString();
//        getMapByName(KEY_MAP_NAME).put("key", key);
//        getMapByName(KEY_MAP_NAME).put("vector", vector);
//        getMapByName(USER_MAP_NAME).put("login", Encryptor.encrypt(key, vector, authorization[0]));
//        getMapByName(USER_MAP_NAME).put("password", Encryptor.encrypt(key, vector, authorization[1]));
//
//        getDoctorMap().put(1, doctorFromJson);
//
//        getMiscellaneousMap().put("pageIndex", 1);
//    }

//    public static void clearInstance() {
//        getMapByName(USER_MAP_NAME).clear();
//        getMapByName(DATASET_MAP_NAME).clear();
//        getMapByName(KEY_MAP_NAME).clear();
//        getMapByName(DOCTOR_MAP_NAME).clear();
//        getMapByName(MISCELLANEOUS_MAP_NAME).clear();
//        getMapByName(PATIENT_MAP_NAME).clear();
//    }

    public static String getUserMapName() {
        return USER_MAP_NAME;
    }

    public static String getDatasetMapName() {
        return DATASET_MAP_NAME;
    }

    public static String getKeyMapName() {
        return KEY_MAP_NAME;
    }

    public static String getMiscellaneousMapName() {
        return MISCELLANEOUS_MAP_NAME;
    }

    public static String getPatientMapName() {
        return PATIENT_MAP_NAME;
    }

    public static IMap<Integer, Doctor> getDoctorMap() {
        return hazelcastInstance.getMap(DOCTOR_MAP_NAME);
    }

    public static IMap<String, Integer> getMiscellaneousMap() {
        return hazelcastInstance.getMap(MISCELLANEOUS_MAP_NAME);
    }

    public static IMap<Integer, Patient> getPatientMap() {
        return hazelcastInstance.getMap(PATIENT_MAP_NAME);
    }

    public static IMap<Integer, Dataset> getDataSetMap(){
        return hazelcastInstance.getMap(DATASET_MAP_NAME);
    }

//    public static void changePassword(String password){
//        HazelCastMap.getMapByName(HazelCastMap.getUserMapName()).put("password",
//                Encryptor.encrypt(HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get("key").toString(),
//                HazelCastMap.getMapByName(HazelCastMap.getKeyMapName()).get("vector").toString(),
//                password));
//    }

//    public static void changeUserInformation(Doctor doctor){
//        getDoctorMap().put(1, doctor);
//    }
}
