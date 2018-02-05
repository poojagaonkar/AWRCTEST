package adweb.com.awteamestimates.Service;

/**
 * Created by PoojaGaonkar on 2/1/2018.
 */

public  class ApiUrls {

    //<editor-fold desc="GET">
    public  static String LOGIN_URL = "/rest/auth/1/session";
    public  static String USER_DETAIL_URL = "/rest/api/2/user?key=";
    public  static String FETCH_PROJECTS_URL = "/rest/adwebTeamEstimates/2/teamEstimation/currentEstimatedIssues"; //This API gives active project details.
    public  static  String MORE_DETAILS_URL = "/rest/adwebTeamEstimates/2/teamEstimation/currentTeamEstimations/"; //This API gives result of all the details for each user with their min max and average estimate

    //</editor-fold>

    //<editor-fold desc="POST">
    public  static  String SUBMIT_ESTIMATION_URL = "/rest/adwebTeamEstimates/2/teamEstimation/currentTeamEstimations"; //This API works to enter estimate details from user.
    //</editor-fold>
}
