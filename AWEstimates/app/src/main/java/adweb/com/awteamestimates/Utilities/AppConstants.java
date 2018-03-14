package adweb.com.awteamestimates.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.GetRoles.RoleIdModel;
import adweb.com.awteamestimates.Models.MoreIssueDetailModel;

/**
 * Created by PoojaGaonkar on 2/5/2018.
 */

public class AppConstants {

   public  static   String tempPass;
    public static List<CurrentEstimatedIssue> FullProjectList;
    public static CharSequence CurrentSelectedProject;
    public static Iterator<CurrentEstimatedIssue> CurrentIssueDetails;
    //public static List<MoreIssueDetailModel> CurrentProjectDetailList;
    public static HashMap<String, String> CurrentProjectDetailMap;
    public static ArrayList ProjectTitles;
    public static CurrentEstimatedIssue CurrentEstimatedIssue;
    public static boolean isRefreshed;
    public static ArrayList<String> ProjectRoleTitles;
    public static CharSequence CurrentSelectedRole;
    public static Boolean isRoleEnabled;
    public static ArrayList<RoleIdModel> ProjectRoleList;
    public static String Protocol;
    public static RoleIdModel CurrentRoleDetails;
    public static String BaseUrl;
    public static boolean isLoggedOut;
    public static boolean isEstimated;
    public static String estimatedProjectKey = "";
    public static ArrayList<String> estimatedIssueKeys;
    public static int totalIssueCounter;
    public static LinkedHashMap<String, String> MyEstimatedIssuesMap;
    public static ArrayList<MoreIssueDetailModel> CurrentProjectDetailList;

    public static void ClearAll()
    {
        tempPass = null;
         FullProjectList = null;
        CurrentSelectedProject= null;
         CurrentIssueDetails= null;
        // List<MoreIssueDetailModel> CurrentProjectDetailList= null;
        CurrentProjectDetailMap= null;
       ProjectTitles= null;
         CurrentEstimatedIssue= null;
          isRefreshed= false;
         ProjectRoleTitles= null;
          CurrentSelectedRole= null;
         isRoleEnabled= null;
         ProjectRoleList= null;
          Protocol= null;
          CurrentRoleDetails= null;
          BaseUrl= null;
          isLoggedOut= true;
          isEstimated= false;
          estimatedProjectKey= null;
         estimatedIssueKeys= null;
    }
}
