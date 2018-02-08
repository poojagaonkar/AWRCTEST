package adweb.com.awteamestimates.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.MoreDetailModel;
import adweb.com.awteamestimates.Models.ProjectModel;

/**
 * Created by PoojaGaonkar on 2/5/2018.
 */

public class AppConstants {

    public  static  String tempPass;
    public static List<CurrentEstimatedIssue> FullProjectList;
    public static CharSequence CurrentSelectedProject;
    public static Iterator<CurrentEstimatedIssue> CurrentIssueDetails;
    public static List<MoreDetailModel> CurrentProjectDetailList;
    public static HashMap<String, String> CurrentProjectDetailMap;
    public static ArrayList ProjectTitles;
}
