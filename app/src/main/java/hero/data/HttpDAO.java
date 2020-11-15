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

    public void putDeviceToken(String childId, String token, DataCallback callback){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("childId", childId)
                    .put("pushToken", token);
        } catch (JSONException e){
            e.printStackTrace();
        }
        new HttpRequestSender("PUT", ipPort + "/child/verify/parent", jsonObj.toString(), callback).execute();
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
                            String name = jsonObj.getString("name");
                            int badge = jsonObj.getInt("reward");
                            String description = jsonObj.getString("description");
                            questList.add(new QuestDTO(name, badge, description));
                        }
                        QuestDAO.getInstance(context).saveList(questList);
                    }
                }
        ).execute();
    }

    public void getBadgeList(String childId){
        new HttpRequestSender("GET", ipPort+"/quest/list/"+childId+"/badges?max-badges=6&provider=sw", null,
                new DataCallback() {
                    @Override
                    public void onDataReceiving(JSONObject data) throws Exception {
                        JSONArray jsonArr = data.getJSONArray("data");
                        List<Integer> badgeList = new ArrayList<>();
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            int badge = jsonObj.getInt("reward");
                            badgeList.add(badge);
                        }
                        QuestDAO.getInstance(context).saveListBadge(badgeList);
                    }
                }
        ).execute();
    }

    public void getChildInfo(String childId, final DataCallback callback){
        new HttpRequestSender("GET", ipPort+"/child/" + childId, null, new DataCallback() {
            @Override
            public void onDataReceiving(JSONObject data) throws Exception {
                JSONObject childObj = data.getJSONObject("data");
                SPSupport spSupport = new SPSupport(context);
                spSupport.set("child_name", childObj.getString("lastName") + " " + childObj.getString("firstName"));
                spSupport.set("child_nickname", childObj.getString("nickName"));
                spSupport.set("child_photo", childObj.getString("photo"));
                callback.onDataReceiving(null);
            }
        }).execute();
    }

    public void getAllData(String childId, DataCallback callback){

        // load task data
        getTaskList(childId);
        getBadgeList(childId);

        // load quest data
        getQuestList(childId);

        // load profile data
        getChildInfo(childId, callback);

    }

}
