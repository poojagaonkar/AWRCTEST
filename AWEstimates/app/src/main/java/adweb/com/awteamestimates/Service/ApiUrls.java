package adweb.com.awteamestimates.Service;

/**
 * Created by PoojaGaonkar on 2/1/2018.
 */

public  class ApiUrls {

    //<editor-fold desc="GET">
    /**
     * http://172.21.128.209:2990/jira/rest/api/2/user?username=admin
     * : Params : Basic auth username and password
     */
    public  static String USER_DETAIL_URL = "/rest/api/2/user?key=";

    /**
     * This API gives active project details.
     * eg: http://172.21.128.209:2990/jira/rest/adwebTeamEstimates/2/teamEstimation/currentEstimatedIssues
     * Params : Basic auth username and password
     */
    public  static String FETCH_PROJECTS_URL = "/rest/adwebTeamEstimates/2/teamEstimation/currentEstimatedIssues";

    /**
     *  This API gives result of all the details for each user with their min max and average estimate
     *  eg: http://172.21.128.209:2990/jira/rest/api/2/issue/RCDEM-1
     *  Params : Basic auth username and password
     */

    public  static  String FETCH_ROLE_ESTIMATES = "rest/adwebTeamEstimates/2/teamEstimation/currentTeamEstimations/";
    /*
    * Fetches more issue details

     */
    public  static  String MORE_DETAILS_URL = "/rest/api/2/issue/";

    /**
     *  Get project details based on roles
     *  eg: http://172.21.128.209:2990/jira/rest/adweb/2/teamEstimates/AD-1/admin
     *  Params : Basic auth username and password , Issue Key and username
     */
    public  static  String FETCH_ROLE_BASED_PROJECT_URL ="/rest/adweb/2/teamEstimates/" ;

    //</editor-fold>

    //<editor-fold desc="POST">

    /**
     * Login user.
     * eg: http://172.21.128.209:2990/jira/rest/auth/1/session .
     * Params : {"username":"admin","password":"admin"}
     */
    public  static String LOGIN_URL = "/rest/auth/1/session";

    /**
    * This API works to enter estimate details from user.
     * eg http://172.21.128.209:2990/jira/rest/adwebTeamEstimates/2/teamEstimation/currentTeamEstimations.
     * Params {"userName":"admin","issueKey":"RCDEM-1","teamEstimate":"2w 2d 1h"}
     */
    public  static  String SUBMIT_ESTIMATION_URL = "/rest/adwebTeamEstimates/2/teamEstimation/currentTeamEstimations";
    //</editor-fold>
}
