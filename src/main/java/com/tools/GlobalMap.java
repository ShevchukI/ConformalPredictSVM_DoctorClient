package com.tools;

import com.models.Dataset;
import com.models.Doctor;
import com.models.Patient;

import java.util.HashMap;
import java.util.Map;

public class GlobalMap {
    private static GlobalMap ourInstance = new GlobalMap();

    private static Map<String, String> userMap;
    private static Map<Integer, Doctor> doctorMap;
    private static Map<Integer, Dataset> dataSetMap;
    private static Map<String, String> keyMap;
    private static Map<String, String> miscMap;
    private static Map<Integer, Patient> patientMap;

    public static GlobalMap getInstance() {
        return ourInstance;
    }

    public static void fillMap(String[] authorization){
        String key = Encryptor.genRandString();
        String vector = Encryptor.genRandString();
        getKeyMap().put(Constant.getKEY(), key);
        getKeyMap().put(Constant.getVECTOR(), vector);
        getUserMap().put(Constant.getLOGIN(), Encryptor.encrypt(key, vector, authorization[0]));
        getUserMap().put(Constant.getPASSWORD(), Encryptor.encrypt(key, vector, authorization[1]));
        getMiscMap().put(Constant.getPageIndex(), "1");
    }

    public static void clearMap(){
        userMap.clear();
        dataSetMap.clear();
        dataSetMap.clear();
        keyMap.clear();
        miscMap.clear();
        patientMap.clear();
    }

    public static Map<String, String> getUserMap() {
        return userMap;
    }

    public static Map<Integer, Doctor> getDoctorMap() {
        return doctorMap;
    }

    public static Map<Integer, Dataset> getDataSetMap() {
        return dataSetMap;
    }

    public static Map<String, String> getKeyMap() {
        return keyMap;
    }

    public static Map<String, String> getMiscMap() {
        return miscMap;
    }

    public static Map<Integer, Patient> getPatientMap() {
        return patientMap;
    }

    private GlobalMap() {
        userMap = new HashMap<>();
        doctorMap = new HashMap<>();
        dataSetMap = new HashMap<>();
        keyMap = new HashMap<>();
        miscMap = new HashMap<>();
        patientMap = new HashMap<>();
    }
}
