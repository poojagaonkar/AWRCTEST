package adweb.com.awteamestimates.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.GetRoles.RoleIdModel;

/**
 * Created by PoojaGaonkar on 2/5/2018.
 */

public class AppConstants {

    public  static  String tempPass;
    public static List<CurrentEstimatedIssue> FullProjectList;
    public static CharSequence CurrentSelectedProject;
    public static Iterator<CurrentEstimatedIssue> CurrentIssueDetails;
    //public static List<MoreDetailModel> CurrentProjectDetailList;
    public static HashMap<String, String> CurrentProjectDetailMap;
    public static ArrayList ProjectTitles;
    public static CurrentEstimatedIssue CurrentEstimatedIssue;
    public static boolean isRefreshed;
    public static ArrayList<String> ProjectRoleTitles;
    public static CharSequence CurrentSelectedRole;
    public static Boolean isRoleEnabled;
    public static ArrayList<RoleIdModel> ProjectRoleList;
}
