package org.thailandsbc.cloneplanting;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Icanzenith on 8/24/15 AD.
 */
public class BaseApplication extends Application{

    public boolean CheckCallBackStatus(int statuscode) {
        switch (statuscode) {
            case AjaxStatus.AUTH_ERROR:
                Toast.makeText(this, "Auth_ERROR", Toast.LENGTH_LONG).show();
                return false;

            case AjaxStatus.DATASTORE:
                Toast.makeText(this, "Datastore", Toast.LENGTH_LONG).show();
                return false;

            case AjaxStatus.DEVICE:
                Toast.makeText(this, "Device", Toast.LENGTH_LONG).show();
                return false;

            case AjaxStatus.FILE:
                Toast.makeText(this, "File", Toast.LENGTH_LONG).show();
                return false;

            case AjaxStatus.MEMORY:
                Toast.makeText(this, "Memory", Toast.LENGTH_LONG).show();
                return false;

            case AjaxStatus.NETWORK:
                Toast.makeText(this, "Network", Toast.LENGTH_LONG).show();
                return false;

            case AjaxStatus.NETWORK_ERROR:

                Toast.makeText(this, "Network Error : การเชิ่มต่ออินเตอร์เน็ตมีปัญหา กรุณาตรวจสอบการเชื่อมต่ออีกครั้ง", Toast.LENGTH_LONG).show();

                return false;

            case AjaxStatus.TRANSFORM_ERROR:

                Toast.makeText(this, "Transform Error", Toast.LENGTH_LONG).show();
                return false;

            default:
                // Normal Situation

                return true;
        }
    }

    public MyCloneItemData getClonePositionView() {
        return ClonePositionView;
    }

    public void setClonePositionView(MyCloneItemData clonePositionView) {
        ClonePositionView = clonePositionView;
    }

    private MyCloneItemData ClonePositionView;

    private Map<String, String> Sector;
    private Map<String, Integer> Picture_Sector;
    private int[] pic_sec;
    String[] sector_name;
    private String[] sectorcode = {"A","B","C","D","E","G","H","I","J","K","L"};

    @Override
    public void onCreate() {
        super.onCreate();
        sector_name = getResources().getStringArray(R.array.Sector);
        pic_sec = new int[] { R.drawable.a, R.drawable.b,
                R.drawable.c,R.drawable.d,R.drawable.e, R.drawable.g, R.drawable.h,
                R.drawable.i, R.drawable.j, R.drawable.k, R.drawable.l };
        Sector = new HashMap<String, String>();
        Sector.put("A", sector_name[0]);
        Sector.put("B", sector_name[1]);
        Sector.put("C", sector_name[2]);
        Sector.put("D", sector_name[3]);
        Sector.put("E", sector_name[4]);
        Sector.put("G", sector_name[5]);
        Sector.put("H", sector_name[6]);
        Sector.put("I", sector_name[7]);
        Sector.put("J", sector_name[8]);
        Sector.put("K", sector_name[9]);
        Sector.put("L", sector_name[10]);

        Picture_Sector = new HashMap<String, Integer>();
        Picture_Sector.put("A", pic_sec[0]);
        Picture_Sector.put("B", pic_sec[1]);
        Picture_Sector.put("C", pic_sec[2]);
        Picture_Sector.put("D", pic_sec[3]);
        Picture_Sector.put("E", pic_sec[4]);
        Picture_Sector.put("G", pic_sec[5]);
        Picture_Sector.put("H", pic_sec[6]);
        Picture_Sector.put("I", pic_sec[7]);
        Picture_Sector.put("J", pic_sec[8]);
        Picture_Sector.put("K", pic_sec[9]);
        Picture_Sector.put("L", pic_sec[10]);
    }



    public String[] getSectorcode() {
        return sectorcode;
    }

    public void setSectorcode(String[] sectorcode) {
        this.sectorcode = sectorcode;
    }

    public int[] getPic_sec() {
        return pic_sec;
    }

    public void setPic_sec(int[] pic_sec) {
        this.pic_sec = pic_sec;
    }



    public String[] getSector_name() {
        return sector_name;
    }

    public void setSector_name(String[] sector_name) {
        this.sector_name = sector_name;
    }

    public Map<String, Integer> getPicture_Sector() {
        return Picture_Sector;
    }

    public void setPicture_Sector(Map<String, Integer> picture_Sector) {
        Picture_Sector = picture_Sector;
    }

    public UserData getUserData() {
        UserData u = new UserData();
        SharedPreferences s = this.getApplicationContext()
                .getSharedPreferences("CLONEPLANTING", MODE_PRIVATE);
        u.setName(s.getString("Name", "Name Problem"));
        u.setSector(s.getString("Sector", "Sector"));
        u.setPic_sector(s.getInt("Pic_Sector", 0));
        u.setSectorCode(s.getString("Sector_Code", "S"));
        u.setUserID(s.getInt("userid", 0));

        return u;
    }

    public Map<String, String> getSector() {
        return Sector;
    }

    public void setSector(Map<String, String> sector) {
        Sector = sector;
    }

    class UserData {
        private int UserID;
        private String Name;
        private String Sector;
        private int Pic_sector;
        private String SectorCode;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getSector() {
            return Sector;
        }

        public void setSector(String sector) {
            Sector = sector;
        }

        public int getPic_sector() {
            return Pic_sector;
        }

        public void setPic_sector(int pic_sector) {
            Pic_sector = pic_sector;
        }

        public String getSectorCode() {
            return SectorCode;
        }

        public void setSectorCode(String sectorCode) {
            SectorCode = sectorCode;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }

    }

}

