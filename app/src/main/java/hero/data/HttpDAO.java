package hero.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hero.api.DataCallback;
import hero.api.HttpRequestSender;
import hero.util.SPSupport;
import hero.util.Util;

public class HttpDAO {

    String ipPort;
    Context context;
    static HttpDAO instance;

    public HttpDAO(Context context, String ipPort){
        this.context = context;
        this.ipPort = ipPort;
    }

    public static HttpDAO getInstance(Context context, String ipPort){
        if (instance == null) instance = new HttpDAO(context, ipPort);
        return instance;
    }

    public void putDeviceToken(String childId, String token, String androidId, String deviceName, DataCallback callback){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("childId", childId)
                    .put("pushToken", token)
                    .put("androidId", androidId)
                    .put("deviceName", deviceName);
        } catch (JSONException e){
            e.printStackTrace();
        }
        new HttpRequestSender("PUT", ipPort + "/child/verify/parent", jsonObj.toString(), callback).execute();
    }

    public void getLocList(String childId){
        new HttpRequestSender("GET", ipPort+"/location/list/"+childId+"/"+Util.getLongHour0(), null,
            new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    JSONArray jsonArr = data.getJSONArray("data");
                    List<LocationDTO> locList = new ArrayList<>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        long id = jsonObj.getLong("safezoneId");
                        String name = jsonObj.getString("name");
                        double latitude = jsonObj.getDouble("latitude");
                        double longitude = jsonObj.getDouble("longitude");
//                        int radius = jsonObj.getInt("radius");
                        Calendar fromTime = Util.timeStrToCalendar(jsonObj.getString("fromTime"));
                        Calendar toTime = Util.timeStrToCalendar(jsonObj.getString("toTime"));
                        String type = jsonObj.getString("type");
                        double latA = jsonObj.getDouble("latA");
                        double lngA = jsonObj.getDouble("lngA");
                        double latB = jsonObj.getDouble("latB");
                        double lngB = jsonObj.getDouble("lngB");
                        double latC = jsonObj.getDouble("latC");
                        double lngC = jsonObj.getDouble("lngC");
                        double latD = jsonObj.getDouble("latD");
                        double lngD = jsonObj.getDouble("lngD");
                        QuadDTO quad = new QuadDTO(latA, lngA, latB, lngB, latC, lngC, latD, lngD);
                        locList.add(new LocationDTO(id, name, latitude, longitude, 0, fromTime, toTime, type, quad));
                    }
                    LocationDAO.getInstance(context).saveList(locList);
                }
            }
        ).execute();
    }

    public void getTaskList(String childId){
        new HttpRequestSender("GET", ipPort+"/task/list/"+childId+"?date="+ Util.getLongHour0()+"&provider=sw", null,
            new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    JSONArray jsonArr = data.getJSONArray("data");
                    List<TaskDTO> taskList = new ArrayList<>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        long id = jsonObj.getLong("taskId");
                        String name = jsonObj.getString("name");
                        String status = jsonObj.getString("status");
                        Calendar fromTime = Util.timeStrToCalendar(jsonObj.getString("fromTime"));
                        Calendar toTime = Util.timeStrToCalendar(jsonObj.getString("toTime"));
                        String type = jsonObj.getString("type");
                        String description = jsonObj.getString("description");
                        taskList.add(new TaskDTO(id, name, type, description, fromTime, toTime, status));
                    }
                    TaskDAO.getInstance(context).saveList(taskList);
                }
            }
        ).execute();
    }

    public void getQuestList(String childId){
        new HttpRequestSender("GET", ipPort+"/quest/list/"+childId+"?status=ASSIGNED", null,
                new DataCallback() {
                    @Override
                    public void onDataReceiving(JSONObject data) throws Exception {
                        JSONArray jsonArr = data.getJSONArray("data");
                        List<QuestDTO> questList = new ArrayList<>();
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            long id = jsonObj.getLong("questId");
                            String name = jsonObj.getString("name");
                            int badge = jsonObj.getInt("reward");
                            String description = jsonObj.getString("description");
                            String title = jsonObj.getString("title");
                            questList.add(new QuestDTO(id, name, badge, description, title, "assigned"));
                        }
                        QuestDAO.getInstance(context).saveList(questList);
                    }
                }
        ).execute();
    }

    public void getBadgeList(String childId){
        new HttpRequestSender("GET", ipPort+"/quest/list/"+childId+"?status=DONE", null,
                new DataCallback() {
                    @Override
                    public void onDataReceiving(JSONObject data) throws Exception {
                        JSONArray jsonArr = data.getJSONArray("data");
                        QuestDAO questDao = QuestDAO.getInstance(context);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            long id = jsonObj.getLong("questId");
                            String name = jsonObj.getString("name");
                            int badge = jsonObj.getInt("reward");
                            String description = jsonObj.getString("description");
                            String title = jsonObj.has("title") ? jsonObj.getString("title") : "";
                            questDao.add(new QuestDTO(id, name, badge, description, title, "done"), null);
                            if (i >= 5) break;
                        }
                    }
                }
        ).execute();
    }

    public void getChildInfo(final String childId, final DataCallback callback){
        new HttpRequestSender("GET", ipPort+"/child/" + childId, null, new DataCallback() {
            @Override
            public void onDataReceiving(JSONObject data) throws Exception {
                JSONObject childObj = data.getJSONObject("data");
                SPSupport spSupport = new SPSupport(context);
                spSupport.set("child_name", childObj.getString("name"));
                spSupport.set("child_nickname", childObj.has("nickName") ? childObj.getString("nickName") : "");
                spSupport.set("child_photo", childObj.has("photo") ? childObj.getString("photo"): "");
                callback.onDataReceiving(null);
            }
        }).execute();
    }

    public void getAllData(String childId, DataCallback callback){
        getLocList(childId);
        getTaskList(childId);
        getQuestList(childId);
        getBadgeList(childId);
        getChildInfo(childId, callback);
    }

}
