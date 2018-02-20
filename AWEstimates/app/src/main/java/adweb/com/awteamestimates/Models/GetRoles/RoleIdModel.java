package adweb.com.awteamestimates.Models.GetRoles;

/**
 * Created by PoojaGaonkar on 2/15/2018.
 */

public class RoleIdModel {

    public String getRoleID() {
        return RoleID;
    }

    public void setRoleID(String roleID) {
        RoleID = roleID;
    }

    private String RoleID;

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    private  String RoleName;

    private String RoleEstimate;

    public String getRoleEstimate() {
        return RoleEstimate;
    }

    public void setRoleEstimatee(String roleEstimate) {
        RoleEstimate = roleEstimate;
    }

}
