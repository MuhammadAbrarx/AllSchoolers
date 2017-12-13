package abdullahsoft.com.thenewspaperapp.Parser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.util.Log;

import com.mdabrarapps.kernalbd.com.idie.Adapters.DbAdapter;
import com.mdabrarapps.kernalbd.com.idie.Utils.Msg;
import com.mdabrarapps.kernalbd.com.idie.Models.QuestionariesObject;
import com.mdabrarapps.kernalbd.com.idie.Models.Questionaries_AnswerField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import okhttp3.Response;

/**
 * Created by Md. Abrar on 5/4/2016.
 * the parser was developed to parse data and store it inside database
 */
public class JsonParser
{

    public static JSONObject jsonRootObject, jsonObject, jsonObjExtension = null;
    public static JSONArray jsonRootArray, jsonArray;
    public static String jsonData;

    public ArrayList<JSONArray> jArrays = new ArrayList<>();
    public ArrayList<String> jObjExtNameList = new ArrayList<>();
    public static ArrayList<String> jObjNameList = new ArrayList<>();//For later accessing
    public ArrayList<String> jObjExtensionList = new ArrayList<>();


    public Context context;

    //All Profile Keys
    final String keyFirstName = "first_name";
    final String keyLastName = "last_name";
    final String keyEmail = "email";
    final String keyMembershipId = "membership_id";
    final String keyMobile = "mobile";
    final String keyDob = "dob";
    final String keyAddress = "address";
    final String keyCity = "city";
    final String keyState = "state";
    final String keyZipcode = "zipcode";
    final String keyCountryId = "country_id";
    final String keyFacebook = "facebook";
    final String keyTwitter = "twitter";
    final String keyYoutube = "youtube";
    final String keyToken = "token";
    final String keySignupDate = "signup_date";
    final String keySignupIP = "signup_ip";
    final String keySignupDevice = "signup_device";
    final String keySignupBrowser = "signup_browser";
    final String keyActivationDate = "activation_date";
    final String keyLastLogin = "last_login";
    final String keyLastIP = "last_ip";
    final String keyLastDevice = "last_device";
    final String keyLastBrowser = "last_browser";
    final String keyEnabled = "enabled";
    final String keyMembership = "membership";
    final String keyPrice = "price";
    final String keyPhoto = "photo";
    final String keySong = "song";
    final String keyProfileId = "profile_id";

    //All Membership Offer Keys
    final String keyId = "id";
    final String keyDescription = "description";

    //All Membership Offer Values
    String valueId;
    String valueDescription;

    //All Profile Values
    String valueFirstName;
    String valueLastName;
    String valueEmail;
    String valueMembershipId;
    String valueMobile;
    String valueDob;
    String valueAddress;
    String valueCity;
    String valueState;
    String valueZipCode;
    String valueCountryId;
    String valueFacebook;
    String valueTwitter;
    String valueYoutube;
    String valueToken;
    String valueSignupDate;
    String valueSignupIP;
    String valueSignupDevice;
    String valueSignupBrowser;
    String valueActivationDate;
    String valueLastLogin;
    String valueLastIP;
    String valueLastDevice;
    String valueLastBrowser;
    String valueEnabled;
    String valueMembership;
    String valuePrice;
    String valuePhoto;
    String valueSong;
    String valueProfileId;

    String jsonArrayName, jsonObjName;

    String TAG = "Json Parser";
    DbAdapter dbAdapter;

//    public static String[] urls;

    public JsonParser(Context context)
    {
        this.context = context;
        dbAdapter = DbAdapter.getInstance(context);
        Msg.showErr(TAG, "Context Loaded");
    }

    public void requestParse(Response response)
    {
        try
        {
            jsonData = response.body().string();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        Msg.showErr(TAG, "Parse request done");

    }

    ///-----------Get all Stuff zone---------------\\\

    public void initializeRootObj()
    {
        try
        {
            this.jsonRootObject = new JSONObject(jsonData);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void initializeRootArray()
    {
        try
        {
            this.jsonRootArray = new JSONArray(jsonData);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    public ArrayList<JSONObject> getKeyValuePairFromJObj(JSONObject jObj)
    {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
//        kvPair.key = jObj.
        Iterator keys = jObj.keys();

        try
        {
            while (keys.hasNext())
            {
//                KeyValuePair kvPair=new KeyValuePair();
                JSONObject tempJobj = new JSONObject();
                String key = (String) keys.next();
                String value = jObj.getString(key);
                Log.e(TAG, "Value : " + value + " for key " + key);
                tempJobj.put(key, value);

                jsonObjects.add(tempJobj);
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObjects;
    }

    public void getAllJsonRootKeys(ArrayList<String> jArrayNames)
    {
//        jArrayNames = new ArrayList<String>(); // must not say it's new or data don't pass to the AnotherList sent
        try
        {
            jsonRootObject = new JSONObject(jsonData);
            Iterator keys = jsonRootObject.keys();

            while (keys.hasNext())
            {
                String singleKey = (String) keys.next();
                jArrayNames.add(singleKey);
                Msg.showMsg(context, TAG, singleKey);//everything is good here ..
            }

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllRootKeysFromAGivenJsonObject(JSONObject jsonObject)
    {
//        jArrayNames = new ArrayList<String>(); // must not say it's new or data don't pass to the AnotherList sent
        Log.e(TAG,"Getting All Root Keys");
        ArrayList<String> list = new ArrayList<>();
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                Iterator keys = jsonObject.keys();
                while (keys.hasNext())
                {
                    String singleKey = (String) keys.next();
                    list.add(singleKey);
                    // Msg.showMsg(context, TAG, singleKey);
                    Log.e(TAG,"singleKey : "+singleKey);
                    //everything is good here ..
                }
            }
            else
            {
                JSONArray jArrayNames = jsonObject.names();
                ArrayList<Integer> intList = new ArrayList<>();
                for (int i=0;i<jArrayNames.length();i++)
                {
                    intList.add(Integer.parseInt(jArrayNames.get(i).toString()));
                    Log.e(TAG,"jArrayNames.get(i).toString() : "+jArrayNames.get(i).toString());
                }
                Collections.sort(intList);


                for (int i=0;i<intList.size();i++)
                {
                    list.add(String.valueOf(intList.get(i)));
                    Log.e(TAG,"String.valueOf(intList.get(i) : "+String.valueOf(intList.get(i)));
                }
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public JSONArray getJSONArray(JSONArray motherArray, JSONObject motherObject, String jsonArrayName, int index)
    {

        this.jsonArrayName = jsonArrayName;
        try
        {
            if (motherArray != null)
            {
                jsonArray = motherArray.getJSONArray(index);
            }
            else if (motherObject != null)
            {
                jsonArray = motherObject.getJSONArray(jsonArrayName);
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        Msg.showErr(TAG, "Got Json Array : " + this.jsonArrayName);

        return jsonArray;

    }

    public JSONObject getJSONObject(JSONArray motherArray, JSONObject motherObject, String jsonObjName, int index)
    {

        this.jsonObjName = jsonObjName;
        try
        {
            if (motherArray != null)
            {
                jsonObject = motherArray.getJSONObject(index);
            }
            else if (motherObject != null)
            {
                jsonObject = motherObject.getJSONObject(jsonObjName);
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        Msg.showErr(TAG, "Got Json Array : " + jsonObjName);

        return jsonObject;


    }

    //    public void parseQuestionaries(Context context,ArrayList<QuestionariesObject> arrList_qObjs)
    public void parseQuestionaries(Context context, JSONObject jsonObject)
    {

        JSONObject jObj;
//        JSONObject tmpJObj;
        dbAdapter = DbAdapter.getInstance(context);

        String jarrString;
        JSONArray jarray;
        jObjNameList.clear();
        jObjNameList = null;
        jObjNameList = new ArrayList<>();
        Long id = Long.valueOf(0), id1 = Long.valueOf(0), id2 = Long.valueOf(0);
        Boolean extension = false;
        try
        {

//            dbAdapter.OpenDbConnectionWritable();
            if (jsonObject != null)
            {
                //Need to use get key method or different values including extension won't be counted

                //For testing purpose only
                // jObjNameList.clear();
                jObjNameList = new ArrayList<>(getAllRootKeysFromAGivenJsonObject(jsonObject));

                // for testing purpose only
                for (int i = 0; i < jObjNameList.size(); i++)
                {
                    Log.d(TAG,"jObjNameList : "+jObjNameList.get(i));
                }

                for (int i = 0; i < jObjNameList.size(); i++)
                {
                    //Can be put in a seperate function to form a recursion but for now this will do for better understanding of code/bigger picture
                    QuestionariesObject qObj = new QuestionariesObject();
                    Questionaries_AnswerField qObj_ansField = new Questionaries_AnswerField();

//                    jObj = jsonObject.optJSONObject(Integer.toString(i)); // Opt returns null instread of throwing an exception
                    if (jsonObject.getJSONObject(jObjNameList.get(i))==null)
                    {
                        Log.e(TAG, "JSON OBJ FOUND NULL INSIDE LOOP");
                        break;

                    }
                    else
                    {
                        jObj = jsonObject.getJSONObject(jObjNameList.get(i)); // getting questions one by one
                    }

                    if (jObj != null)
                    {

                        qObj.question_id = jObj.getInt(QuestionariesObject.FIELD_NAMES.QUESTION_ID);
                        qObj.question = jObj.getString(QuestionariesObject.FIELD_NAMES.QUESTION);
                        qObj.answer_type = jObj.getString(QuestionariesObject.FIELD_NAMES.ANSWER_TYPE);
                        qObj.question_type = jObj.getString(QuestionariesObject.FIELD_NAMES.QUESTION_TYPE);
                        qObj.parent_id = jObj.getInt(QuestionariesObject.FIELD_NAMES.PARENT_ID);
                        qObj.parent_question = jObj.getString(QuestionariesObject.FIELD_NAMES.PARENT_QUESTION);

                        //Insert to Answer Field Table
//                        need to retrive the array from root json data
                        jarrString = jObj.optString(QuestionariesObject.FIELD_NAMES.ANSWER_FIELD);// Opt returns null instread of throwing an exception

                        //enter if statement only if it is an jsonArray means it start with charectar [
                        if (!jarrString.equals("null") && !jarrString.equals("") && jarrString.charAt(0)=='[')
                        {
                            Log.e(TAG, "jarrString found : " + jarrString + " for question id : " + qObj.question_id);
                            jarray = new JSONArray(jarrString);

                            qObj_ansField.question_id = qObj.question_id;
                            qObj_ansField.answers = jarray.toString();
                            // for (int k=0;k<jarray.length();k++)
                            // {
                            //     qObj_ansField.answers.add(jarray.getString(k));
                            // }
                            // qObj_ansField.answer1 = jarray.getString(0);
                            // qObj_ansField.answer2 = jarray.getString(1);
//                    qObj.answer_field = (Questionaries_AnswerField) jObj.get(QuestionariesObject.FIELD_NAMES.ANSWER_FIELD);
                            dbAdapter.insertData(qObj_ansField, null);
                        }
                        else
                        {
                            Log.e(TAG, "jarrString is null for question id : " + qObj.question_id);
                        }
//                        jarr = new JSONArray(jObj.getJSONArray(QuestionariesObject.FIELD_NAMES.ANSWER_FIELD));

                        qObj.answer_field_count = jObj.getInt(QuestionariesObject.FIELD_NAMES.ANSWER_FIELD_COUNT);
                        qObj.answer_id = jObj.optInt(QuestionariesObject.FIELD_NAMES.ANSWER_ID);
                        qObj.answer = jObj.getString(QuestionariesObject.FIELD_NAMES.ANSWER);

                        //Mimicking True/False values as SQLite don't have any built in boolean func.
//                        jObj = jObj.optJSONObject(QuestionariesObject.FIELD_NAMES.EXTENSION);
                        String extensNullCheck;
                        if (jObj.has(QuestionariesObject.FIELD_NAMES.EXTENSION))
                        {
                            extensNullCheck = jObj.getString(QuestionariesObject.FIELD_NAMES.EXTENSION);
                            if (!extensNullCheck.equals("null") && !extensNullCheck.equals("") && !extensNullCheck.equals(" "))
                            {
                                extension = true;
                                qObj.extension = 1;
                                extensNullCheck = "data exists";
                            }
                            Log.e(TAG,"Found extension null check status : "+extensNullCheck);
                        }
                        else
                        {
                            extension = false;
                            qObj.extension = 0;
                            extensNullCheck = "null";
                            Log.e(TAG,"Found extension null check status : "+extensNullCheck);
                        }
//                        if (!jObj.isNull(QuestionariesObject.FIELD_NAMES.EXTENSION))

                        //                arrList_qObjs.add(qObj);
                        id = dbAdapter.insertData(qObj, null);


                        if (id <= 0)
                        {
                            //Toast a msg that error inserting to database
                            Msg.showErr(TAG, "ERROR INSERTING TO DATABASE");

                        }
                        else
                        {
                            Msg.showErr(TAG, "INSERTED Question No. " + qObj.question_id);
                        }

                        Log.e(TAG, "Parsed Question No. " + qObj.question_id);

                        if (extension)
                        {

                            //Insert extension as new object only if
                            jObj = jObj.optJSONObject(QuestionariesObject.FIELD_NAMES.EXTENSION);
                            if (jObj != null)
                            {
                                jObjExtNameList.clear();
                                jObjExtNameList = new ArrayList<>(getAllRootKeysFromAGivenJsonObject(jObj));
                                jObj = jObj.optJSONObject(jObjExtNameList.get(0));//Getting the first item by default as the json is structured for having only one extension for now
                                //using recursion

//                            Store jsonObjects in a arraylist.. then run a seperate loop to store them to db for easier insertion
                                parseExtensions(context, jObj);
                                Log.e(TAG, "Starting parse of Extension");
                            }
                            else
                            {
                                Log.e(TAG, "jObj extension found null");
                            }


                        }
                    }
                    else
                    {
                        Log.e(TAG, "Question No : " + (i) + " is found null for now");
                    }

                }

            }
            else
            {
                Log.e(TAG, "JSON OBJ FOUND NULL");
            }

//            dbAdapter.CloseConnectionDB();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }catch (SQLiteConstraintException e)
        {
            e.printStackTrace();
        }catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }

    public void parseExtensions(Context context, JSONObject jObj)
    {
        DbAdapter dbAdapter = DbAdapter.getInstance(context);
        Long id = Long.valueOf(0), id1 = Long.valueOf(0), id2 = Long.valueOf(0);
        Boolean extension = false;
        try
        {

            //Can be put in a seperate function to form a recursion but for now this will do for better understanding of code/bigger picture
            QuestionariesObject qObj = new QuestionariesObject();
            Questionaries_AnswerField qObj_ansField = new Questionaries_AnswerField();

//                    jObj = jsonObject.optJSONObject(Integer.toString(i)); // Opt returns null instread of throwing an exception
            if (jObj != null)
            {

                qObj.question_id = jObj.getInt(QuestionariesObject.FIELD_NAMES.QUESTION_ID);
                qObj.question = jObj.getString(QuestionariesObject.FIELD_NAMES.QUESTION);
                qObj.answer_type = jObj.getString(QuestionariesObject.FIELD_NAMES.ANSWER_TYPE);
                qObj.question_type = jObj.getString(QuestionariesObject.FIELD_NAMES.QUESTION_TYPE);
                qObj.parent_id = jObj.getInt(QuestionariesObject.FIELD_NAMES.PARENT_ID);
                qObj.parent_question = jObj.getString(QuestionariesObject.FIELD_NAMES.PARENT_QUESTION);

                //Insert to Answer Field Table
                JSONArray jarr = jObj.optJSONArray(QuestionariesObject.FIELD_NAMES.ANSWER_FIELD);// Opt returns null instread of throwing an exception
                if (jarr != null)
                {
                    qObj_ansField.question_id = qObj.question_id;
//                    qObj_ansField.answer1 = jarr.getString(0);
//                    qObj_ansField.answer2 = jarr.getString(1);
//                    qObj.answer_field = (Questionaries_AnswerField) jObj.get(QuestionariesObject.FIELD_NAMES.ANSWER_FIELD);
                    qObj_ansField.answers = jarr.toString();
                    Log.e(TAG,"FOUND jarr : "+jarr.toString()+" while inserting qObj_ansField ");
                    dbAdapter.insertData(qObj_ansField, null);
                    Log.e(TAG, "answer field jarray inserted successfully ..");
                }
                else
                {
                    Log.e(TAG, "answer field jarray found null ..");
                }

                qObj.answer_field_count = jObj.getInt(QuestionariesObject.FIELD_NAMES.ANSWER_FIELD_COUNT);
                qObj.answer_id = jObj.optInt(QuestionariesObject.FIELD_NAMES.ANSWER_ID);
                qObj.answer = jObj.getString(QuestionariesObject.FIELD_NAMES.ANSWER);

                //Mimicking True/False values as SQLite don't have any built in boolean func.
//                        jObj = jObj.optJSONObject(QuestionariesObject.FIELD_NAMES.EXTENSION);
                if (!jObj.isNull(QuestionariesObject.FIELD_NAMES.EXTENSION))
                {
                    extension = true;
                    qObj.extension = 1;
                }
                else
                {
                    extension = false;
                    qObj.extension = 0;

                }
//                arrList_qObjs.add(qObj);
                id = dbAdapter.insertData(qObj, null);


                if (id <= 0)
                {
                    //Toast a msg that error inserting to database
                    Msg.showErr(TAG, "ERROR INSERTING TO DATABASE");

                }
                else
                {
                    Msg.showErr(TAG, "INSERTED Question No. " + qObj.question_id);
                }

                Log.e(TAG, "Parsed Question No. " + qObj.question_id);

                if (extension)
                {

                    //Insert extension as new object only if

                    jObj = jObj.getJSONObject(QuestionariesObject.FIELD_NAMES.EXTENSION);
                    jObjExtensionList.clear();
                    jObjExtNameList = new ArrayList<>(getAllRootKeysFromAGivenJsonObject(jObj));
                    jObj = jObj.optJSONObject(jObjNameList.get(0));//Getting the first item by default as the json is structured for having only one extension for now
                    //using recursion
//                            Store jsonObjects in a arraylist.. then run a seperate loop to store them to db for easier insertion
                    parseExtensions(context, jObj);


                }
            }
            else
            {
                Log.e(TAG, "Extension is found null for now,plz recheck null checking");
            }


        }catch (JSONException e)
        {
            e.printStackTrace();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }catch (SQLiteConstraintException e)
        {
            e.printStackTrace();
        }catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }


    public void parseUserProfile(Activity activity, JSONObject jsonObject)
    {
        JSONObject jObj;
//        JSONObject tmpJObj;
        String jarrString;
        JSONArray jarray;
        Long id = Long.valueOf(0), id1 = Long.valueOf(0), id2 = Long.valueOf(0);
        Boolean extension = false;
        try
        {

//            dbAdapter.OpenDbConnectionWritable();
            if (jsonObject != null)
            {
                //Need to use get key method or different values including extension won't be counted
                jObj = jsonObject;
                valueFirstName = jObj.getString(keyFirstName);
                valueLastName = jObj.getString(keyLastName);
                valueEmail = jObj.getString(keyEmail);
                valueMembershipId = jObj.getString(keyMembershipId);
                valueMobile = jObj.getString(keyMobile);
                valueDob = jObj.getString(keyDob);
                valueAddress = jObj.getString(keyAddress);
                valueCity = jObj.getString(keyCity);
                valueState = jObj.getString(keyState);
                valueZipCode = jObj.getString(keyZipcode);
                valueCountryId = jObj.getString(keyCountryId);
                valueFacebook = jObj.getString(keyFacebook);
                valueTwitter = jObj.getString(keyTwitter);
                valueYoutube = jObj.getString(keyYoutube);
                valueToken = jObj.getString(keyToken);
                valueSignupDate = jObj.getString(keySignupDate);
                valueSignupIP = jObj.getString(keySignupIP);
                valueSignupDevice = jObj.getString(keySignupDevice);
                valueSignupBrowser = jObj.getString(keySignupBrowser);
                valueActivationDate = jObj.getString(keyActivationDate);
                valueLastLogin = jObj.getString(keyLastLogin);
                valueLastIP = jObj.getString(keyLastIP);
                valueLastDevice = jObj.getString(keyLastDevice);
                valueLastBrowser = jObj.getString(keyLastBrowser);
                valueEnabled = jObj.getString(keyEnabled);
                valueMembership = jObj.getString(keyMembership);
                valuePrice = jObj.getString(keyPrice);
                valuePhoto = jObj.getString(keyPhoto);
                valueSong = jObj.getString(keySong);
                valueProfileId = jObj.getString(keyProfileId);


                SharedPreferences sharedPreferences = activity.getSharedPreferences("iDieProf", Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                sharedPrefEditor.putString(keyFirstName, valueFirstName);
                sharedPrefEditor.putString(keyLastName, valueLastName);
                sharedPrefEditor.putString(keyEmail, valueEmail);
                sharedPrefEditor.putString(keyMembershipId, valueMembershipId);
                sharedPrefEditor.putString(keyMobile, valueMobile);
                sharedPrefEditor.putString(keyDob, valueDob);
                sharedPrefEditor.putString(keyAddress, valueAddress);
                sharedPrefEditor.putString(keyCity, valueCity);
                sharedPrefEditor.putString(keyState, valueState);
                sharedPrefEditor.putString(keyZipcode, valueZipCode);
                sharedPrefEditor.putString(keyCountryId, valueCountryId);
                sharedPrefEditor.putString(keyFacebook, valueFacebook);
                sharedPrefEditor.putString(keyTwitter, valueTwitter);
                sharedPrefEditor.putString(keyYoutube, valueYoutube);
                sharedPrefEditor.putString(keyToken, valueToken);
                sharedPrefEditor.putString(keySignupDate, valueSignupDate);
                sharedPrefEditor.putString(keySignupIP, valueSignupIP);
                sharedPrefEditor.putString(keySignupDevice, valueSignupDevice);
                sharedPrefEditor.putString(keySignupBrowser, valueSignupBrowser);
                sharedPrefEditor.putString(keyActivationDate, valueActivationDate);
                sharedPrefEditor.putString(keyLastLogin, valueLastLogin);
                sharedPrefEditor.putString(keyLastIP, valueLastIP);
                sharedPrefEditor.putString(keyLastDevice, valueLastDevice);
                sharedPrefEditor.putString(keyLastBrowser, valueLastBrowser);
                sharedPrefEditor.putString(keyEnabled, valueEnabled);
                sharedPrefEditor.putString(keyMembership, valueMembership);
                sharedPrefEditor.putString(keyPrice, valuePrice);
                sharedPrefEditor.putString(keyPhoto, valuePhoto);
                sharedPrefEditor.putString(keySong, valueSong);
                sharedPrefEditor.putString(keyProfileId, valueProfileId);
                sharedPrefEditor.commit();

//                jObj.optInt(QuestionariesObject.FIELD_NAMES.ANSWER_ID);

            }
            else
            {
                Log.e(TAG, "JSON OBJ FOUND NULL");
            }

//            dbAdapter.CloseConnectionDB();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }catch (SQLiteConstraintException e)
        {
            e.printStackTrace();
        }catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }

    public void parseMembershipOffers(Activity activity, JSONObject jsonObject)
    {
        JSONObject jObj;
        String jObjString;
        JSONArray jarray;
        Long id = Long.valueOf(0), id1 = Long.valueOf(0), id2 = Long.valueOf(0);
        Boolean extension = false;
        try
        {

            if (jsonObject != null)
            {
                // keep the entire json string

                jObj = jsonObject;
                valueId = jObj.getString(keyId);
                // valueMembership = jObj.getString(keyMembership);
                // valuePrice = jObj.getString(keyPrice);
                // valuePhoto = jObj.getString(keyPhoto);
                // valueSong = jObj.getString(keySong);
                // valueDescription = jObj.getString(keyDescription);
                // valueEnabled = jObj.getString(keyEnabled);


                SharedPreferences sharedPreferences = activity.getSharedPreferences("iDieMembershipOffers", Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                /*-- here valueId is the key --*/
                Log.e(TAG,"Offer No : "+valueId+" and offer json ; "+jObj.toString());
                sharedPrefEditor.putString(valueId,jObj.toString());
                // sharedPrefEditor.putString(keyMembership, valueMembership);
                // sharedPrefEditor.putString(keyPrice, valuePrice);
                // sharedPrefEditor.putString(keyPhoto, valuePhoto);
                // sharedPrefEditor.putString(keySong, valueSong);
                // sharedPrefEditor.putString(keyEnabled, valueEnabled);

                sharedPrefEditor.commit();

//                jObj.optInt(QuestionariesObject.FIELD_NAMES.ANSWER_ID);

            }
            else
            {
                Log.e(TAG, "JSON OBJ FOUND NULL");
            }

//            dbAdapter.CloseConnectionDB();
        }catch (JSONException e)
        {
            e.printStackTrace();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }catch (SQLiteConstraintException e)
        {
            e.printStackTrace();
        }catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }

    public void parseCountries(Activity activity, JSONArray jsonArray)
    {
        String keyCountryId;
        String valueCountry;
        if (jsonArray!=null)
        {
            try
            {
                SharedPreferences sharedPreferences = activity.getSharedPreferences("iDieCountries", Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                for (int i = 0; i < jsonArray.length(); i++)
                {

                    JSONObject jObj = jsonArray.getJSONObject(i);
                    keyCountryId = jObj.getString("id");
                    valueCountry = jObj.getString("name");
                    sharedPrefEditor.putString(keyCountryId, valueCountry);

                    Log.e(TAG,"INSERTED IN SHARD PREF "+keyCountryId+" : "+valueCountry);
                }

                sharedPrefEditor.commit();

            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Log.e(TAG,"Json array found null while parsing countries");
        }

    }


}
