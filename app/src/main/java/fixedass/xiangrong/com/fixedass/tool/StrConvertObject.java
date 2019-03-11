package fixedass.xiangrong.com.fixedass.tool;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fixedass.xiangrong.com.fixedass.bean.Address;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrow;
import fixedass.xiangrong.com.fixedass.bean.AssetBorrowQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetChange;
import fixedass.xiangrong.com.fixedass.bean.AssetChangeQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetCountBill;
import fixedass.xiangrong.com.fixedass.bean.AssetCountDetail;
import fixedass.xiangrong.com.fixedass.bean.AssetFix;
import fixedass.xiangrong.com.fixedass.bean.AssetFixQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetOperate;
import fixedass.xiangrong.com.fixedass.bean.AssetScrap;
import fixedass.xiangrong.com.fixedass.bean.AssetScrapQuery;
import fixedass.xiangrong.com.fixedass.bean.AssetStorage;
import fixedass.xiangrong.com.fixedass.bean.AssetTest;
import fixedass.xiangrong.com.fixedass.bean.AssetTestQuery;
import fixedass.xiangrong.com.fixedass.bean.CountState;
import fixedass.xiangrong.com.fixedass.bean.Dept;
import fixedass.xiangrong.com.fixedass.bean.DptPeople;
import fixedass.xiangrong.com.fixedass.bean.Function;
import fixedass.xiangrong.com.fixedass.bean.User;

/**
 * Created by Administrator on 2018/5/2.
 * 获得的字符串转换为相应的对象数组
 */

public class StrConvertObject {

    /*Str转换为User*/
    public static ArrayList<User> strConvertUser(String info){
        ArrayList<User> userList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                User user = new Gson().fromJson(jsonObject.toString(),User.class);
                userList.add(user);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return userList;
    }

    public static AssetStorage convertSto(String info){
        AssetStorage storage = null;
        try {
            JSONObject object = new JSONObject(info);
            storage = new Gson().fromJson(object.toString(), AssetStorage.class);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return storage;
    }

    public static ArrayList<AssetStorage> strConvertSto(String info) {
        ArrayList<AssetStorage> stoList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AssetStorage assetStorage = new Gson().fromJson(jsonObject.toString(), AssetStorage.class);
                stoList.add(assetStorage);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return stoList;
    }

    /*str转换为Function*/
    public static ArrayList<Function> strConvertFunction(String info){
        ArrayList<Function> functionList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(info);

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Function function = new Gson().fromJson(jsonObject.toString(),Function.class);
                functionList.add(function);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return functionList;
    }

    /*str转换成AssetBorrowQuery 集合*/
    public static ArrayList<AssetBorrowQuery> setConvertAssetBorrowQuery(String info){
        ArrayList<AssetBorrowQuery> borrowQueryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                AssetOperate<AssetBorrow> assetOperate = new AssetOperate<>();
                JSONObject jsonObject1 = new JSONObject(jsonObject.get("assetOperate").toString());

                assetOperate.setState(jsonObject1.getInt("state"));
                assetOperate.setOperNote(jsonObject1.getString("operNote"));

                JSONObject userObj = jsonObject1.getJSONObject("user");
                User user = new Gson().fromJson(userObj.toString(), User.class);
                assetOperate.setUser(user);

                assetOperate.setCreatedate(jsonObject1.getString("createdate"));

                AssetBorrowQuery borrowQuery = new AssetBorrowQuery();
                borrowQuery.setAssetOperate(assetOperate);
                borrowQuery .setOperbillCode(jsonObject.getString("operbillCode"));

                if (!jsonObject.get("deptPeople").toString().equals("null")){
                    JSONObject deptPeopleJson = new JSONObject(jsonObject.get("deptPeople").toString());
                    DptPeople dptPeople = new Gson().fromJson(deptPeopleJson.toString(), DptPeople.class);
                    borrowQuery.setDeptPeople(dptPeople);
                }

                borrowQuery.setBorrowDate(jsonObject.getString("borrowDate"));
                borrowQuery.setBorrowDays(jsonObject.getInt("borrowDays"));
                borrowQuery.setReturnDate(jsonObject.getString("returnDate"));
                borrowQuery.setBorrowGroup(jsonObject.getString("borrowGroup"));
                borrowQuery.setBorrowGroupName(jsonObject.getString("borrowGroupName"));
                borrowQuery.setBorrowCompany(jsonObject.getString("borrowCompany"));
                borrowQuery.setBorrowCompanyName(jsonObject.getString("borrowCompanyName"));
                borrowQuery.setBorrowDepatment(jsonObject.getString("borrowDepatment"));
                borrowQuery.setBorrowDeptName(jsonObject.getString("borrowDeptName"));

                //借用信息
                JSONArray borrowJson = new JSONArray(jsonObject1.getString("list").toString());

                List<AssetBorrow> borrowList = new ArrayList<>();
                for(int j=0;j<borrowJson.length();j++){
                    JSONObject borrowObject = borrowJson.getJSONObject(j);

                    AssetBorrow assetBorrow = new AssetBorrow();
                    assetBorrow.setBarCode(borrowObject.getString("barCode"));              //资产编码

                    if (!borrowObject.get("deptPeople").toString().equals("null")){
                        assetBorrow.setBorrowPeople(borrowObject.getJSONObject("deptPeople").getString("pName"));       //借用人
                    }

                    assetBorrow.setBorrowDate(borrowObject.getString("borrowDate"));        //借用日期
                    assetBorrow.setBorrowDays(borrowObject.getInt("borrowDays"));           //借用时长
                    assetBorrow.setBorrowInfo(borrowObject.getString("borrowInfo"));        //借用原因

                    assetBorrow.setIsReturn(borrowObject.getInt("isReturn"));               //资产状态
                    assetBorrow.setReturnDate(borrowObject.getString("returnDate"));        //归还日期
                    //借用资产
                    AssetStorage assetStorage = new AssetStorage();
                    JSONObject stoObject = borrowObject.getJSONObject("storage");                   //资产信息
                    assetStorage.setAssName(stoObject.getString("assName"));                 //资产名称

                    assetStorage.setUseGroup("");   //使用集团

                    assetStorage.setUseCompany(stoObject.getJSONObject("companyInfo").getString("deptName"));//使用公司
                    assetStorage.setUseDept(stoObject.getJSONObject("deptInfo").getString("deptName"));       //使用部门
                    assetStorage.setUsePeople(stoObject.getJSONObject("usePeopleEntity").getString("pName"));   //使用人
                    assetStorage.setCareMan(stoObject.getJSONObject("careUser").getString("pName"));            //保管员
                    assetStorage.setStoreAddress(stoObject.getJSONObject("address").getString("addrName"));     //地址

                    assetBorrow.setStorage(assetStorage);

                    borrowList.add(assetBorrow);
                }

                borrowQuery.getAssetOperate().setList(borrowList);
                borrowQuery.setSelected(false);
                borrowQueryList.add(borrowQuery);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return borrowQueryList;
    }

    /*str转换成AssetOperate<AssetBorrow>*/
    public static AssetOperate setConvertAssetOperateBorrow(JSONObject borrowOperateObject){
        AssetOperate operate = new AssetOperate();
        try {
            operate.setOperbillCode(borrowOperateObject.getString("operbillCode"));
            /**
             * borrowOperateObject.get("examUser")
             */
            if (!(borrowOperateObject.get("examUser")).toString().equals("null")){
                JSONObject examUserJson = new JSONObject(borrowOperateObject.get("examUser").toString());
                User examUser = new Gson().fromJson(examUserJson.toString(), User.class);
                operate.setExamUser(examUser);
            }

            operate.setExamdate(borrowOperateObject.getString("examdate"));
            operate.setOperNote(borrowOperateObject.getString("operNote"));

            JSONArray listJSON = new JSONArray(borrowOperateObject.getString("list").toString());
            List<AssetBorrow> borrowList = new ArrayList<>();
            for(int i=0;i<listJSON.length();i++){
                JSONObject borrowObject = listJSON.getJSONObject(i);
                AssetBorrow assetBorrow = new AssetBorrow();
                assetBorrow.setBorrowUUID(borrowObject.getString("borrowUUID"));

                if (!borrowObject.get("deptPeople").toString().equals("null")){
                    assetBorrow.setBorrowPeople(borrowObject.getJSONObject("deptPeople").getString("pName"));       //借用人
                }

                assetBorrow.setBorrowDate(borrowObject.getString("borrowDate"));        //借用日期
                assetBorrow.setBorrowDays(borrowObject.getInt("borrowDays"));           //借用时长
                assetBorrow.setBorrowInfo(borrowObject.getString("borrowInfo"));        //借用原因

                JSONObject storageJSON = borrowObject.getJSONObject("storage");
                AssetStorage storage = new Gson().fromJson(storageJSON.toString(), AssetStorage.class);
                assetBorrow.setStorage(storage);
                borrowList.add(assetBorrow);
            }
            operate.setList(borrowList);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return operate;
    }

    /*str转换成AssetChangeQuery集合*/
    public static ArrayList<AssetChangeQuery> setConvertAssetChangeQuery(String info){
        ArrayList<AssetChangeQuery> changeQueryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                AssetOperate<AssetChange> assetOperate = new AssetOperate<>();

                JSONObject jsonObject1 = new JSONObject(jsonObject.get("assetOperate").toString());
                assetOperate.setState(jsonObject1.getInt("state"));                         //状态
                assetOperate.setOperNote(jsonObject1.getString("operNote"));

                JSONObject userObj = jsonObject1.getJSONObject("user");
                User user = new Gson().fromJson(userObj.toString(), User.class);
                assetOperate.setUser(user);

                assetOperate.setCreatedate(jsonObject1.getString("createdate"));

                AssetChangeQuery changeQuery = new AssetChangeQuery();
                changeQuery.setAssetOperate(assetOperate);

                changeQuery .setOperbillCode(jsonObject.getString("operbillCode"));         //转移单号

                changeQuery.setChangeDate(jsonObject.getString("changeDate"));

                changeQuery.setOldGroup("");

                JSONObject oldCompanyObj = jsonObject.getJSONObject("oldCompanyObj");
                Dept oldCompany = new Gson().fromJson(oldCompanyObj.toString(), Dept.class);
                changeQuery.setOldCompanyObj(oldCompany);

                JSONObject oldDeptObj = jsonObject.getJSONObject("oldDeptObj");
                Dept oldDept = new Gson().fromJson(oldDeptObj.toString(), Dept.class);
                changeQuery.setOldDeptObj(oldDept);

                JSONObject oldPeopleObj = jsonObject.getJSONObject("oldPeopleObj");
                DptPeople oldPeople = new Gson().fromJson(oldPeopleObj.toString(), DptPeople.class);
                changeQuery.setOldPeopleObj(oldPeople);

                changeQuery.setOldPlace(jsonObject.getString("oldPlace"));

                changeQuery.setNewGroup("");

                if (!jsonObject.get("newCompanyObj").toString().equals("null")){
                    JSONObject newCompanyObj = jsonObject.getJSONObject("newCompanyObj");
                    Dept newCompany = new Gson().fromJson(newCompanyObj.toString(), Dept.class);
                    changeQuery.setNewCompanyObj(newCompany);
                }

                if (!jsonObject.get("newDeptObj").toString().equals("null")){
                    JSONObject newDeptObj = jsonObject.getJSONObject("newDeptObj");
                    Dept newDept = new Gson().fromJson(newDeptObj.toString(), Dept.class);
                    changeQuery.setNewDeptObj(newDept);
                }

                if (!jsonObject.get("newDeptPeople").toString().equals("null")){
                    JSONObject newDeptPeople = jsonObject.getJSONObject("newDeptPeople");
                    DptPeople newDptPeo = new Gson().fromJson(newDeptPeople.toString(), DptPeople.class);
                    changeQuery.setNewDeptPeople(newDptPeo);
                }

                changeQuery.setNewPlace(jsonObject.getString("newPlace"));

                //转移信息
                JSONArray list = new JSONArray(jsonObject1.getString("list").toString());
                //资产编码，资产名称，资产状态，转出日期，，新使用人，新使用公司，新使用部门，新保管员，新地址

                List<AssetChange> changeList = new ArrayList<>();
                for (int j=0;j<list.length();j++){
                    JSONObject changeObj = list.getJSONObject(j);
                    AssetChange assetChange = new Gson().fromJson(changeObj.toString(), AssetChange.class);
                    changeList.add(assetChange);
                }

                changeQuery.getAssetOperate().setList(changeList);
                changeQuery.setSelected(false);
                changeQueryList.add(changeQuery);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return changeQueryList;
    }

    public static AssetOperate setConvertAssetOperateTransfer(JSONObject operateObject){
        AssetOperate assetOperate = new AssetOperate();
        try {
            assetOperate .setOperbillCode(operateObject.getString("operbillCode"));         //转移单号

            if (!(operateObject.get("examUser")).toString().equals("null")){
                JSONObject examUserJson = new JSONObject(operateObject.get("examUser").toString());
                User examUser = new Gson().fromJson(examUserJson.toString(), User.class);
                assetOperate.setExamUser(examUser);
            }

            assetOperate.setExamdate(operateObject.getString("examdate"));
            assetOperate.setOperNote(operateObject.getString("operNote"));
            assetOperate.setState(operateObject.getInt("state"));

            JSONArray listJSON = new JSONArray(operateObject.getString("list").toString());
            List<AssetChange> transferList = new ArrayList<>();
            for(int i=0;i<listJSON.length();i++){
                JSONObject changeObj = listJSON.getJSONObject(i);
                AssetChange assetChange = new Gson().fromJson(changeObj.toString(), AssetChange.class);
                transferList.add(assetChange);
            }
            assetOperate.setList(transferList);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return assetOperate;
    }

    /*str转换成AssetFixQuery集合*/
    public static ArrayList<AssetFixQuery> setConvertAssetFixQuery(String info){
        ArrayList<AssetFixQuery> fixQueryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonObject1 = new JSONObject(jsonObject.get("assetOperate").toString());
                AssetOperate<AssetFix> assetOperate = new AssetOperate<>();
                assetOperate.setOperNote(jsonObject1.getString("operNote"));                  //备注
                assetOperate.setState(jsonObject1.getInt("state"));                         //状态
                JSONObject userObj = jsonObject1.getJSONObject("user");
                User user = new Gson().fromJson(userObj.toString(), User.class);
                assetOperate.setUser(user);
                assetOperate.setCreatedate(jsonObject1.getString("createdate"));

                AssetFixQuery fixQuery = new AssetFixQuery();
                fixQuery.setAssetOperate(assetOperate);
                fixQuery.setOperbillCode(jsonObject.getString("operbillCode"));
                //维修信息
                JSONArray list = new JSONArray(jsonObject1.getString("list").toString());
                //资产编码，资产名称，资产状态，使用集团，使用公司，使用部门，使用人，保管员，地址，维修单位、维修人、维修电话、维修费用、维修日期、故障描述、备注信息
                List<AssetFix> fixList = new ArrayList<>();
                for (int j=0;j<list.length();j++){
                    JSONObject fixObj = list.getJSONObject(j);
                    AssetFix assetFix = new Gson().fromJson(fixObj.toString(), AssetFix.class);
                    fixList.add(assetFix);
                }
                fixQuery.getAssetOperate().setList(fixList);
                fixQuery.setSelected(false);
                fixQueryList.add(fixQuery);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return fixQueryList;
    }

    /*str转换成AssetOperate<AssetBorrow>*/
    public static AssetOperate setConvertAssetOperateFix(JSONObject fixOperateObject){
        AssetOperate operate = new AssetOperate();
        try {
            operate.setOperbillCode(fixOperateObject.getString("operbillCode"));

            if (!(fixOperateObject.get("examUser")).toString().equals("null")){
                JSONObject examUserJson = new JSONObject(fixOperateObject.get("examUser").toString());
                User examUser = new Gson().fromJson(examUserJson.toString(), User.class);
                operate.setExamUser(examUser);
            }
            operate.setExamdate(fixOperateObject.getString("examdate"));

            JSONObject userJson = new JSONObject(fixOperateObject.get("user").toString());
            User user = new Gson().fromJson(userJson.toString(), User.class);
            operate.setUser(user);

            operate.setCreatedate(fixOperateObject.getString("createdate"));
            operate.setOperNote(fixOperateObject.getString("operNote"));

            JSONArray listJSON = new JSONArray(fixOperateObject.getString("list").toString());
            List<AssetFix> fixList = new ArrayList<>();
            for(int i=0;i<listJSON.length();i++){
                JSONObject fixObj = listJSON.getJSONObject(i);
                AssetFix assetFix = new Gson().fromJson(fixObj.toString(), AssetFix.class);
                fixList.add(assetFix);
            }
            operate.setList(fixList);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return operate;
    }

    /*str转换成AssetTsetQuery集合*/
    public static ArrayList<AssetTestQuery> setConvertAssetTestQuery(String info){
        ArrayList<AssetTestQuery> testQueryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonObject1 = new JSONObject(jsonObject.get("assetOperate").toString());

                AssetOperate<AssetTest> assetOperate = new AssetOperate<>();
                assetOperate.setState(jsonObject1.getInt("state"));                         //状态
                assetOperate.setOperNote(jsonObject1.getString("operNote"));
                JSONObject userObj = jsonObject1.getJSONObject("user");
                User user = new Gson().fromJson(userObj.toString(), User.class);
                assetOperate.setUser(user);
                assetOperate.setCreatedate(jsonObject1.getString("createdate"));

                AssetTestQuery testQuery = new AssetTestQuery();
                testQuery.setAssetOperate(assetOperate);
                testQuery.setOperbillCode(jsonObject.getString("operbillCode"));
                //维修信息
                JSONArray list = new JSONArray(jsonObject1.getString("list").toString());
                //资产编码，资产名称，资产状态，使用集团，使用公司，使用部门，使用人，保管员，地址，维修单位、维修人、维修电话、维修费用、维修日期、故障描述、备注信息
                List<AssetTest> testList = new ArrayList<>();
                for (int j=0;j<list.length();j++){
                    JSONObject testObj = list.getJSONObject(j);
                    AssetTest assetTest = new Gson().fromJson(testObj.toString(), AssetTest.class);
                    testList.add(assetTest);
                }
                testQuery.getAssetOperate().setList(testList);
                testQuery.setSelected(false);
                testQueryList.add(testQuery);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return testQueryList;
    }
    /*str转换成AssetOperate<AssetBorrow>*/
    public static AssetOperate setConvertAssetOperateTest(JSONObject testOperateObject){
        AssetOperate operate = new AssetOperate();
        try {
            operate.setOperbillCode(testOperateObject.getString("operbillCode"));

            if (!(testOperateObject.get("examUser")).toString().equals("null")){
                JSONObject examUserJson = new JSONObject(testOperateObject.get("examUser").toString());
                User examUser = new Gson().fromJson(examUserJson.toString(), User.class);
                operate.setExamUser(examUser);
            }
            operate.setExamdate(testOperateObject.getString("examdate"));

            JSONObject userJson = new JSONObject(testOperateObject.get("user").toString());
            User user = new Gson().fromJson(userJson.toString(), User.class);
            operate.setUser(user);

            operate.setCreatedate(testOperateObject.getString("createdate"));
            operate.setOperNote(testOperateObject.getString("operNote"));

            JSONArray listJSON = new JSONArray(testOperateObject.getString("list").toString());
            List<AssetTest> testList = new ArrayList<>();
            for(int i=0;i<listJSON.length();i++){
                JSONObject testObj = listJSON.getJSONObject(i);
                AssetTest assetTest = new Gson().fromJson(testObj.toString(), AssetTest.class);
                testList.add(assetTest);
            }
            operate.setList(testList);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return operate;
    }

    /*str转换成AssetTsetQuery集合*/
    public static ArrayList<AssetScrapQuery> setConvertAssetScrapQuery(String info){
        ArrayList<AssetScrapQuery> scrapQueryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonObject1 = new JSONObject(jsonObject.get("assetOperate").toString());

                AssetOperate<AssetScrap> assetOperate = new AssetOperate<>();
                assetOperate.setState(jsonObject1.getInt("state"));                         //状态
                JSONObject userObj = jsonObject1.getJSONObject("user");
                User user = new Gson().fromJson(userObj.toString(), User.class);
                assetOperate.setUser(user);
                assetOperate.setCreatedate(jsonObject1.getString("createdate"));
                assetOperate.setOperNote(jsonObject1.getString("operNote"));

                AssetScrapQuery scrapQuery = new AssetScrapQuery();
                scrapQuery.setAssetOperate(assetOperate);
                scrapQuery.setOperbillCode(jsonObject.getString("operbillCode"));
                //维修信息
                JSONArray list = new JSONArray(jsonObject1.getString("list").toString());
                //资产编码，资产名称，资产状态，使用集团，使用公司，使用部门，使用人，保管员，地址，维修单位、维修人、维修电话、维修费用、维修日期、故障描述、备注信息
                List<AssetScrap> ScrapList = new ArrayList<>();
                for (int j=0;j<list.length();j++){
                    JSONObject ScrapObj = list.getJSONObject(j);
                    AssetScrap assetScrap = new Gson().fromJson(ScrapObj.toString(), AssetScrap.class);
                    ScrapList.add(assetScrap);
                }
                scrapQuery.getAssetOperate().setList(ScrapList);
                scrapQuery.setSelected(false);
                scrapQueryList.add(scrapQuery);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return scrapQueryList;
    }
    /*str转换成AssetOperate<AssetBorrow>*/
    public static AssetOperate setConvertAssetOperateScrap(JSONObject scrapOperateObject){
        AssetOperate operate = new AssetOperate();
        try {
            operate.setOperbillCode(scrapOperateObject.getString("operbillCode"));

            if (!(scrapOperateObject.get("examUser").toString().equals("null"))){
                JSONObject examUserJson = new JSONObject(scrapOperateObject.get("examUser").toString());
                User examUser = new Gson().fromJson(examUserJson.toString(), User.class);
                operate.setExamUser(examUser);
            }
            operate.setExamdate(scrapOperateObject.getString("examdate"));

            JSONObject userJson = new JSONObject(scrapOperateObject.get("user").toString());
            User user = new Gson().fromJson(userJson.toString(), User.class);
            operate.setUser(user);

            operate.setCreatedate(scrapOperateObject.getString("createdate"));
            operate.setOperNote(scrapOperateObject.getString("operNote"));

            JSONArray listJSON = new JSONArray(scrapOperateObject.getString("list").toString());
            List<AssetScrap> scrapList = new ArrayList<>();
            for(int i=0;i<listJSON.length();i++){
                JSONObject scrapObj = listJSON.getJSONObject(i);
                AssetScrap assetScrap = new Gson().fromJson(scrapObj.toString(), AssetScrap.class);
                scrapList.add(assetScrap);
            }
            operate.setList(scrapList);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return operate;
    }

    /*Str转换为AssetCountBill*/
    public static ArrayList<AssetCountBill> strConvertCount(String info){
        ArrayList<AssetCountBill> countList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AssetCountBill assetCountBill = new Gson().fromJson(jsonObject.toString(), AssetCountBill.class);
                countList.add(assetCountBill);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return countList;
    }

    /*Str转换为AssetCountDetail*/
    public static ArrayList<AssetCountDetail> strConvertCountDetail(String info){
        ArrayList<AssetCountDetail> countList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AssetCountDetail detail = new Gson().fromJson(jsonObject.toString(), AssetCountDetail.class);
                countList.add(detail);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return countList;
    }

    public static ArrayList<CountState> strConvertCountState(String info) {
        ArrayList<CountState> stateList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CountState countState = new Gson().fromJson(jsonObject.toString(), CountState.class);
                stateList.add(countState);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return stateList;
    }

    public static List<Dept> strConvertDept(String info){
        List<Dept> deptList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Dept dept = new Gson().fromJson(jsonObject.toString(), Dept.class);
                deptList.add(dept);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return deptList;
    }

    public static List<Address> strConvertAddress(String info){
        List<Address> addList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(info);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Address address = new Gson().fromJson(jsonObject.toString(), Address.class);
                addList.add(address);
            }
        }catch (JSONException e){

        }
        return addList;
    }
}
