package adweb.com.awteamestimates.Models;

/**
 * Created by PoojaGaonkar on 3/14/2018.
 */

public class MoreIssueDetailModel {

    public MoreIssueDetailModel(String fieldTitle, String fieldValue) {
        FieldTitle = fieldTitle;
        FieldValue = fieldValue;
    }

    public String getFieldTitle() {
        return FieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        FieldTitle = fieldTitle;
    }

    public  String FieldTitle;

    public String getFieldValue() {
        return FieldValue;
    }

    public void setFieldValue(String fieldValue) {
        FieldValue = fieldValue;
    }

    public String FieldValue;
}
