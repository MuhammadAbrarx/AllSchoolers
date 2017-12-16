// package com.abdullahsoft.allschoolers.AsyncTask;
//
// /**
//  * Created by Md.A-Desktop-10 on 12-Nov-16.
//  */
//
// import android.app.Activity;
// import android.os.AsyncTask;
// import android.util.Log;
//
// import com.mdabrarapps.kernalbd.com.idie.Handlers.OkHttpRequestHandler;
// import com.mdabrarapps.kernalbd.com.idie.Interfaces.OnTaskCancelledListener;
// import com.mdabrarapps.kernalbd.com.idie.Interfaces.OnTaskCompletedListener;
// import com.mdabrarapps.kernalbd.com.idie.Models.ApiCalls;
// import com.mdabrarapps.kernalbd.com.idie.Models.LoginValues;
// import com.mdabrarapps.kernalbd.com.idie.Parsers.JsonParser;
// import com.mdabrarapps.kernalbd.com.idie.Utils.MiscUtils;
// import com.mdabrarapps.kernalbd.com.idie.Utils.Msg;
// import com.mdabrarapps.kernalbd.com.idie.Utils.NetUtils;
//
// import org.json.JSONException;
// import org.json.JSONObject;
//
// import java.util.ArrayList;
//
// import okhttp3.Response;
//
// /**
//  * Represents an asynchronous login/registration task used to authenticate
//  * the user.
//  */
// public class UserLoginTask extends AsyncTask<Void, Void, LoginValues> {
//
//     static boolean dataLoadFromServerError;
//     static boolean dataInDb;
//     static boolean applicationRestarted = true;
//     static boolean responseLoadError = false;
//
//     // boolean activated = false;
//
//     //Server Calls
//     private UserLoginTask loginTask = null;
//     private OnTaskCompletedListener onTaskCompletedListenerListener;
//     private OnTaskCancelledListener onTaskCancelledListenerListener;
//
//     OkHttpRequestHandler okHandler;
//     Response response;
//
//     //Json
//     JsonParser jParser;
//     ArrayList<JSONObject> arrList_JObjs;
//     //    ArrayList<QuestionariesObject> arrList_qObjs;
//
//     //Activities
//     Activity callerActivity;
// //    Activity nextActivity;
// //    SigninSignupActivity callerActivity;
//
//     private final String mEmail;
//     private final String mPassword;
//
//     //Network tasks
//     //    PlusOneButton plusOneButton;
//     final String keyEmail = "email";
//     final String keyPassword = "password";
//     final String keyProfileId = "profile_id";
//     final String key_x_api_key = "key";
//     final String key_x_api_forServer = "x-api-key";
//
//     String valueEmail;
//     String valuePassword;
//     String valueProfileId;
//     String value_x_api_key;
//
//     //Table_names
//     String tableName_Questionnaries = "questionnaires";
//     String tableName_AnswerField = "answer_field";
//
//     //Database tasks
// //    DbAdapter dbAdapter;
//
//     //    Tags
//     String TAG = "UserLoginTask";
//
//     public UserLoginTask (Activity activity,String email, String password, UserLoginTask loginTask, OnTaskCompletedListener taskCompletedListener)
//     {
//         Log.e(TAG,"User Login Task Constructor Called");
//         this.callerActivity = activity;
//         this.loginTask = loginTask;
//         this.onTaskCompletedListenerListener = taskCompletedListener;
//         mEmail = email;
//         mPassword = password;
//     }
//
//     @Override
//     protected LoginValues doInBackground(Void... params) {
//         Log.e(TAG,"UserLogin TASK BACKGROUND STARTED");
//
//         try {
//             // Adding Parameters
//
//             valueEmail = mEmail;
//             valuePassword = mPassword;
//             okHandler = new OkHttpRequestHandler(callerActivity);
//             okHandler.BuildCache();
//             okHandler.SetTimeouts(15, 30, 15);
//             okHandler.AddingKeyValuesToRequestBody(keyEmail, valueEmail);
//             Log.e(TAG, "Email Key: " + keyEmail + " Email Value : " + valueEmail);
//             okHandler.AddingKeyValuesToRequestBody(keyPassword, valuePassword);
//             Log.e(TAG, "Password Key: " + keyPassword + " Pass Value : " + valuePassword);
//             okHandler.BuildingRequestBody();
//             okHandler.AddUrlToRequestBuilder(ApiCalls.LOGIN);
//             okHandler.BuildingPostTypeRequestWithCustomBody();
//             okHandler.FinalizeBuildingRequest();
// //                Thread.sleep(2000);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
// //            catch (InterruptedException e) {
// //                return false;
// //            }
//
//         //Checking INTERNET & RETURNED RESPONSE STATUS
//
//         if (NetUtils.isNetworkAvailable(callerActivity)) {
//             dataLoadFromServerError = false;
//
//             if (okHandler.isRequestProblem()) {
//                 Msg.showErr(TAG, "Error Downloading Data");
//                 responseLoadError = true;
//             } else {
//                 Msg.showErr(TAG, "Downloading Success");
//                 if (okHandler.isResponseProblem()) {
//                     Msg.showErr(TAG, "Downloading Success but Data error");
//                     responseLoadError = true;
//                 } else {
//                     Msg.showErr(TAG, "Date collection complete,And Started Parsing Data");
//                 }
//             }
//             response = okHandler.NewRequestCallSync();
//             //If new request call fails then use the url list from database to load cached images from image loader
//
//             Msg.showErr(TAG, "Ok Response Cached");
//
//         } else {
//             Log.e(TAG, "NO INTERNET CONNECTION");
//         }
//         if (arrList_JObjs!=null)
//         {
//             if (!arrList_JObjs.isEmpty())
//             {
//                 arrList_JObjs.clear();
//             }
//         }
//
//         JsonCommands();
//
//         //Usually just one array item as the login reply success gives
//         //Each json object holds only only one key value pair
//         // Boolean status = false;
//         LoginValues loginValues = new LoginValues();
//
//         if (arrList_JObjs!=null)
//         {
//
//             try {
//                 for (int i = 0; i < arrList_JObjs.size(); i++) {
//                     JSONObject jsonObject = arrList_JObjs.get(i);
//
//                     if (jsonObject.has("status"))
//                     {
//                         loginValues.status = jsonObject.getBoolean("status");
//                     }
//
//                     if (jsonObject.has("activated"))
//                     {
//                         loginValues.activated = jsonObject.getBoolean("activated");
//                     }
//                     else
//                     {
//                         loginValues.activated = null;
//                     }
//
//                     if (jsonObject.has("message"))
//                     {
//                         if (MiscUtils.CheckValueStatus("message"))
//                         {
//                             loginValues.message = jsonObject.getString("message");
//                         }
//                     }
//
//                     if (jsonObject.has(key_x_api_key))
//                     {
//                         if (MiscUtils.CheckValueStatus(key_x_api_key))
//                         {
//                             value_x_api_key = jsonObject.getString(key_x_api_key);
//                         }
//                     }
//
//                     if (jsonObject.has(keyProfileId))
//                     {
//                         if (MiscUtils.CheckValueStatus(keyProfileId)) {
//                             valueProfileId = jsonObject.getString(keyProfileId);
//                         }
//                     }
//
//                 }
//             } catch (JSONException e) {
//                 e.printStackTrace();
//             }
//         }
//         else
//         {
//             Log.e(TAG,"arrList_JObjs found null");
//         }
//
//
// //            }
//         return loginValues;
//
//     }
//
//
//     public void JsonCommands() {
//         if (response != null) {
//             Msg.showErr("Response", response.toString());
//
//             jParser = new JsonParser(callerActivity);
//             jParser.requestParse(response);
//
//             jParser.initializeRootObj();
//             if (JsonParser.jsonRootObject != null) {
//                 //Initializes an arraylist with all the keyvalue pairs in a Json Object
//                 arrList_JObjs = new ArrayList<>();
// //                arrList_JObjs = jParser.getKeyValuePairFromJObj(JsonParser.jsonRootObject);
//                 arrList_JObjs.add(JsonParser.jsonRootObject);
//
//             } else {
//                 Log.e(TAG, "JsonParser.jsonRootObject found null");
//             }
//
//
//             Msg.showMsg(callerActivity, TAG, "Json parse done Successfully");
//
//         } else // Here response is NULL
//         {
//
//             Msg.showMsg(callerActivity, TAG, "Response found null,adjust the OKHTTP Conditions here");
//         }
//
//         //Ending Json Commands
//     }
//
//     @Override
//     protected void onPostExecute(final LoginValues loginValues)
//     {
//         onTaskCompletedListenerListener.onProfileIdandXApiKeyCollectionComplete(valueProfileId,value_x_api_key);
//         onTaskCompletedListenerListener.onLoginTaskCompleted(loginValues.status,loginValues.activated,loginValues.message);
//         loginTask = null;
//
//     }
//
//     @Override
//     protected void onCancelled() {
//         loginTask = null;
//
//         onTaskCancelledListenerListener.onLoginTaskCancelled();
//     }
//
//
// }
//
