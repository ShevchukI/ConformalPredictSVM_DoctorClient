package com.tools;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Doctor;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class Constant {
    private static final String INSTANCE_NAME = "mainDoctorInstance";
    private static final String USER_MAP_NAME = "modelDeveloper";
    private static final String DATASET_MAP_NAME = "dataset";
    private static final String KEY_MAP_NAME = "key";
    private static final String MISCELLANEOUS_MAP_NAME = "misc";
    private static final String PATIENT_MAP_NAME = "patient";
    private final static String PHONEREG = "[+][3][8][0][0-9]{9}";
    private final static String EMAILREG = "[a-zA-Z0-9]+[@][a-z]+[.]{0,1}[a-z]{1,3}";
    private final static String WHEIGHT = "[0-9]{1,3}[.]{0,1}[0-9]{1,3}";

    public static void createInstanceAndMap() {
        Config config = new Config();
        config.setInstanceName(INSTANCE_NAME);
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(1918);
        config.addMapConfig(createMapWithName(USER_MAP_NAME));
        config.addMapConfig(createMapWithName(DATASET_MAP_NAME));
        config.addMapConfig(createMapWithName(KEY_MAP_NAME));
        config.addMapConfig(createMapWithName(MISCELLANEOUS_MAP_NAME));
        config.addMapConfig(createMapWithName(PATIENT_MAP_NAME));
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
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

    public static String[] getAuth() {
        String[] auth = new String[2];
        auth[0] = new Encryptor().decrypt(getMapByName(KEY_MAP_NAME).get("key").toString(),
                getMapByName(KEY_MAP_NAME).get("vector").toString(), getMapByName(USER_MAP_NAME).get("login").toString());
        auth[1] = new Encryptor().decrypt(getMapByName(KEY_MAP_NAME).get("key").toString(),
                getMapByName(KEY_MAP_NAME).get("vector").toString(), getMapByName(USER_MAP_NAME).get("password").toString());
        return auth;
    }


    public static String responseToString(HttpResponse response) throws IOException {
        return  EntityUtils.toString(response.getEntity());
    }

    public static void fillMap(Doctor doctorFromJson, String[] authorization) {
        Doctor doctor = doctorFromJson;
        String key;
        String vector;
        key = new Encryptor().genRandString();
        vector = new Encryptor().genRandString();
        getMapByName(KEY_MAP_NAME).put("key", key);
        getMapByName(KEY_MAP_NAME).put("vector", vector);
        getMapByName(USER_MAP_NAME).put("login", new Encryptor().encrypt(key, vector, authorization[0]));
        getMapByName(USER_MAP_NAME).put("password", new Encryptor().encrypt(key, vector, authorization[1]));
        getMapByName(USER_MAP_NAME).put("id", doctor.getId());
        getMapByName(USER_MAP_NAME).put("name", doctor.getName());
        getMapByName(USER_MAP_NAME).put("surname", doctor.getSurname());
        if (doctor.getSpecialization() != null) {
            getMapByName(USER_MAP_NAME).put("specId", doctor.getSpecialization().getId());
            getMapByName(USER_MAP_NAME).put("specName", doctor.getSpecialization().getName());
        } else {
            getMapByName(USER_MAP_NAME).put("specId", "-2");
            getMapByName(USER_MAP_NAME).put("specName", "Empty");
        }
        getMapByName(MISCELLANEOUS_MAP_NAME).put("pageIndex", "1");
    }

    public static void run(Runnable treatment) {
        if(treatment == null) throw new IllegalArgumentException("The treatment to perform can not be null");
        if(Platform.isFxApplicationThread()) treatment.run();
        else Platform.runLater(treatment);
    }

    public static void clearInstance(){
        getMapByName(USER_MAP_NAME).clear();
        getMapByName(DATASET_MAP_NAME).clear();
        getMapByName(KEY_MAP_NAME).clear();
        getMapByName(MISCELLANEOUS_MAP_NAME).clear();
        getMapByName(PATIENT_MAP_NAME).clear();
    }

    public static void getAlert(String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

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

    public static String getPHONEREG() {
        return PHONEREG;
    }

    public static String getEMAILREG() {
        return EMAILREG;
    }

    public static String getWHEIGHT() {
        return WHEIGHT;
    }
}
