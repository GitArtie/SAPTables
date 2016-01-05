package biz.clavis.saptables;

/**
 * Created by Mueller on 10.12.2015.
 */
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONSingleton {
    public List<SAPTable> sapTables;
    public Context context;
    public Map<String, SAPTable> ITEM_MAP = new HashMap<String, SAPTable>();

    private static JSONSingleton ourInstance = new JSONSingleton();

    private static String loadJSONFromAsset(Context context) {
        String json = null;
        try{
            InputStream is = context.getAssets().open("tables.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONSingleton getInstance() {
        return ourInstance;
    }

    public static JSONSingleton getInstance(Context context) {
        if (ourInstance.context != context) {
            ourInstance = new JSONSingleton(context);
        }
        return ourInstance;
    }

    private JSONSingleton() {}

    private JSONSingleton(Context context) {
        if(context != null) {
            String json = loadJSONFromAsset(context);
            sapTables = new ArrayList<SAPTable>();
            try {
                JSONArray jsonarr = new JSONArray(json);
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject jsonobj = jsonarr.getJSONObject(i);
                    SAPTable saptab = new SAPTable(Integer.toString(i), jsonobj.getString("table"), jsonobj.getString("description"), jsonobj.getString("group"));
                    ITEM_MAP.put(Integer.toString(i), saptab);
                    sapTables.add(saptab);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}

class SAPTable {
    public String id;
    public String table;
    public String description;
    public String group;

    public SAPTable(String id, String table, String description, String group) {
        this.id = id;
        this.table = table;
        this.description = description;
        this.group = group;
    }
}
