package com.yisipu.chartmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.adapter.AllAdpter;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.dialog.CoursePointDialog;
import com.yisipu.chartmap.dialog.UpdatePointDialog;

import java.util.ArrayList;
import java.util.List;

/**
 *我的收藏
 * Created by Administrator on 2016/7/29.
 */
public class CollectActivity extends SerialPortActivity {

    private SharedPreferences sp2;

    private AllAdpter adpter;  //绑定数据的adpter
    private ExpandableListView listView; //控件
    private ArrayList<String> fatherList;   //放置父类数据
    private List<List<String>> childList;  //子类数据
    private List<String> list=new ArrayList<>();   //中间数据保存量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_collect);
        sp2 = getSharedPreferences("sp", MODE_APPEND);
//        Logger.i("aaaaaa"+sb.getMMSI());

        initTitle();

        finView();
        initData();
        listView = (ExpandableListView) findViewById(R.id.all);  //获取控件对象
        initDate(); //初始化数据
        setAdpter();  //绑定数据

    }


    /*
    dialog回调更新航线名
     */
    public  void UpdateCourseName(int index,String old_name,String course_name){
        childList.get(1).remove(old_name);
        childList.get(1).add(index,course_name);
        adpter.notifyDataSetChanged();
    }
    CoursePointDialog cpd;
    /*
    修改航线
     */
    public void UpdateCourse(int index,String name){
        DBManager dbManager=new DBManager(CollectActivity.this);
//        Logger.i("name3344:"+name);
        List<CollectPointBean> ls=dbManager.getCoursePoints(name);
        List<String> ls2=new ArrayList<>();
        for(int i=0;i<ls.size();i++){
            ls2.add(i,ls.get(i).getName());
        }
        cpd=new CoursePointDialog(CollectActivity.this,name,ls2,index);
//

       cpd.setNoOnclickListener(null, new CoursePointDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                cpd.dismiss();
            }
        });
        cpd.show();
    }

    /*
    显示航线
     */
    public  void ShowCourse(String name){
        Intent intent=new Intent(CollectActivity.this,DhActivity.class);
        intent.putExtra("course_name",name);
        startActivity(intent);
    }
    /*
   删除航线
    */
    public  void deleteCourse(String name){
        childList.get(1).remove(name);
        adpter.notifyDataSetChanged();
    }
    /*
删除收藏点

 */
    public void deleteCollect(String name) {
        childList.get(0).remove(name);
        adpter.notifyDataSetChanged();


    }
    /*
显示收藏点

*/
    public void ShowCollect(String name){
        Intent intent=new Intent(CollectActivity.this,MainActivity.class);
        intent.putExtra("collect_name",name);
        startActivity(intent);
    }
    /*
修改航点

*/
     UpdatePointDialog up;
    CollectPointBean sp;

    public void UpdateCollect(final  String name){
    DBManager dbManager=new DBManager(CollectActivity.this);
        Logger.i("name3344:"+name);
   sp=dbManager.getCollect(name);

        up=new UpdatePointDialog(CollectActivity.this,""+sp.getName(),""+sp.getLongitude(),""+sp.getLatitude(),sp.getImage());

//        up.setNameText(""+sp.getName());
//        up.setJdText(""+sp.getLongitude());
//       up.setWdText(""+sp.getLatitude());
        up.setYesOnclickListener(null, new UpdatePointDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                String name2=up.getEt_name().getText().toString();
                String wd=up.getEt_wd().getText().toString();
                String jd=up.getEt_jd().getText().toString();
                if(TextUtils.isEmpty(name2)){
                    Toast.makeText(CollectActivity.this,"航点名不能为空",Toast.LENGTH_SHORT).show();

                    return;
                }
                if(TextUtils.isEmpty(wd)){
                    Toast.makeText(CollectActivity.this,"纬度不能为空",Toast.LENGTH_SHORT).show();

                    return;
                }
                if(TextUtils.isEmpty(jd)){
                    Toast.makeText(CollectActivity.this,"经度不能为空",Toast.LENGTH_SHORT).show();

                    return;
                }

               if(Math.abs(Double.valueOf(wd))>90) {
                   Toast.makeText(CollectActivity.this, "纬度范围在90到-90,请重输", Toast.LENGTH_SHORT).show();
                   return;
               }
                if(Math.abs(Double.valueOf(jd))>180){
                    Toast.makeText(CollectActivity.this,"经度范围在180到-180,请重输",Toast.LENGTH_SHORT).show();
                    return;
                }

                DBManager dbManager=new DBManager(CollectActivity.this);


                int i=0;
                for( i=0;i<childList.get(0).size();i++){
                    if(childList.get(0).get(i).equals(name)){
                        break;
                    }
                }
                childList.get(0).remove(i);
                childList.get(0).add(i,name2);
                adpter.notifyDataSetChanged();
                sp.setName(name2);
                sp.setLongitude(Double.parseDouble(jd));
                sp.setLatitude(Double.parseDouble(wd));
                sp.setImage(up.getPosition());
                dbManager.updateCollectBean(sp);

                Toast.makeText(CollectActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                up.dismiss();
            }
        });
        up.setNoOnclickListener(null, new UpdatePointDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                up.dismiss();
            }
        });
        up.show();
    }
    private void initDate() {
        fatherList=new ArrayList<String>();
        childList=new ArrayList<List<String>>();
        /**
         * 初始化父listview
         */
        fatherList.add("航点列表");
        fatherList.add("航线列表");

        /**
         * 初始化子listview
         */
        for (int i = 0; i < fatherList.size(); i++) {
            initChild(i);  //为每一个父类增加子类数据
        }
    }

    private void initChild(int id) {
        list=new ArrayList<String>();
        switch (id) {
            case 0:
                DBManager db = new DBManager(this);
                CollectPointBean cb=new CollectPointBean();
                List<CollectPointBean> ls=db.getCollects();
                for(CollectPointBean cl:ls){
                    list.add(cl.getName());
                }
//                cb.setName("sdgk");
//                cb.setLongitude(6);
//                db.addCollectPoint(cb);
//                CollectPointBean s=db.getCollect("sdgk");
//                if(s!=null){
//                    Toast.makeText(this,"sd"+s.getLongitude(),Toast.LENGTH_SHORT).show();
//                }
//                list.add("疯狂搞笑");
//                list.add("搞笑综艺");
//                list.add("原创搞笑");
//                list.add("经典搞笑");
                childList.add(list);
                break;
            case 1:
                DBManager db2= new DBManager(this);
                CollectPointBean cb2=new CollectPointBean();
                List<String> ls2=db2.getAllCourseName();

                childList.add(ls2);
                break;

            default:
                break;
        }
    }
    private void setAdpter()  //绑定数据
    {
        if (adpter==null) {
            adpter = new AllAdpter(fatherList, childList, this);
            listView.setAdapter(adpter);
        }


    }
    @Override
    void receiver(String str) {

    }

    @Override
    void gpsreceiver(Gpsbean gpsbean) {

    }

    @Override
    void hdopreceiver(Jdbean jdbean) {

    }



    public void finView() {



    }



    public void initData() {
//      DBManager db=new DBManager(this);
//        List<CollectPointBean> ls=new ArrayList<>();
//        for(int i=0;i<7;i++){
//            CollectPointBean cb=new CollectPointBean();
//            cb.setIndex(i);
//            cb.setLatitude(23+i);
//            cb.setLongitude(115+i);
//            cb.setName("哈哈"+i);
//            cb.setCourse_name("哈哈");
//            cb.setType(1);
//            ls.add(cb);
//        }
//
//        db.addCourse(ls);
//        List<CollectPointBean> ls2=new ArrayList<>();
//        for(int i=0;i<7;i++){
//            CollectPointBean cb=new CollectPointBean();
//            cb.setIndex(i);
//            cb.setLatitude(23+i);
//            cb.setLongitude(115+i);
//            cb.setName("hehe"+i);
//            cb.setCourse_name("hehe");
//            cb.setType(1);
//            ls2.add(cb);
//        }
//
//        db.addCourse(ls2);
        }




    public void initTitle() {
        hideTitleRight();

        setTitleText("我的收藏");
        showTitle();
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }




    public void setting(View view) {


    }



    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onResume() {


        super.onResume();

    }

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

    }


}
