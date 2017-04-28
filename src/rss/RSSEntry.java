package rss;

import java.util.ArrayList;
import java.util.List;

public class RSSEntry {
    private String title = "";
    private String link = "";
    private String dcCreator="";
    private String guid="";//HAs Attribute
    private String guidAttributeIsPermaLink="";
    private String description = "";
    private String contentEncoded="";//CDATA ??
    private String excerptEncoded="";//Not CDATA  ??
    //wp:
    private String wpPostId="";
    private String wpPostDate="";
    private String wpPostDateGmt="";
    private String wpCommentStatus="";
    private String wpPingStatus="";
    private String wpPostName="";
    private String wpStatus="";
    private String wpPostParent="";
    private String wpMenuOrder="";
    private String wpPostType="";
    private String wpPostPassword="";
    private String wpIsSticky="";
    //Categories
    private List<String> category= new ArrayList();
    private List<String> categoryAttributeDomain= new ArrayList();
    private List<String> categoryAttributeNiceName= new ArrayList();
    //wp:postmeta
    private List<String> metaKey = new ArrayList();
    private List<String> metaValue = new ArrayList();

    public String getTitle() {
      return title;
    }
    public void setTitle(String title) {
      this.title = title;
    }
    public String getLink() {
      return link;
    }
    public void setLink(String link) {
      this.link = link;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public String getGuid() {
      return guid;
    }
    public void setGuid(String guid) {
      this.guid = guid;
    }

    /**
     * @return the dcCreator
     */
    public String getDcCreator() {
        return dcCreator;
    }

    /**
     * @param dcCreator the dcCreator to set
     */
    public void setDcCreator(String dcCreator) {
        this.dcCreator = dcCreator;
    }

    /**
     * @return the guidAttributeIsPermaLink
     */
    public String getGuidAttributeIsPermaLink() {
        return guidAttributeIsPermaLink;
    }

    /**
     * @param guidAttributeIsPermaLink the guidAttributeIsPermaLink to set
     */
    public void setGuidAttributeIsPermaLink(String guidAttributeIsPermaLink) {
        this.guidAttributeIsPermaLink = guidAttributeIsPermaLink;
    }

    /**
     * @return the contentEncoded
     */
    public String getContentEncoded() {
        return contentEncoded;
    }

    /**
     * @param contentEncoded the contentEncoded to set
     */
    public void setContentEncoded(String contentEncoded) {
        this.contentEncoded = contentEncoded;
    }

    /**
     * @return the excerptEncoded
     */
    public String getExcerptEncoded() {
        return excerptEncoded;
    }

    /**
     * @param excerptEncoded the excerptEncoded to set
     */
    public void setExcerptEncoded(String excerptEncoded) {
        this.excerptEncoded = excerptEncoded;
    }

    /**
     * @return the wpPostId
     */
    public String getWpPostId() {
        return wpPostId;
    }

    /**
     * @param wpPostId the wpPostId to set
     */
    public void setWpPostId(String wpPostId) {
        this.wpPostId = wpPostId;
    }

    /**
     * @return the wpPostDate
     */
    public String getWpPostDate() {
        return wpPostDate;
    }

    /**
     * @param wpPostDate the wpPostDate to set
     */
    public void setWpPostDate(String wpPostDate) {
        this.wpPostDate = wpPostDate;
    }

    /**
     * @return the wpPostDateGmt
     */
    public String getWpPostDateGmt() {
        return wpPostDateGmt;
    }

    /**
     * @param wpPostDateGmt the wpPostDateGmt to set
     */
    public void setWpPostDateGmt(String wpPostDateGmt) {
        this.wpPostDateGmt = wpPostDateGmt;
    }

    /**
     * @return the wpCommentStatus
     */
    public String getWpCommentStatus() {
        return wpCommentStatus;
    }

    /**
     * @param wpCommentStatus the wpCommentStatus to set
     */
    public void setWpCommentStatus(String wpCommentStatus) {
        this.wpCommentStatus = wpCommentStatus;
    }

    /**
     * @return the wpPingStatus
     */
    public String getWpPingStatus() {
        return wpPingStatus;
    }

    /**
     * @param wpPingStatus the wpPingStatus to set
     */
    public void setWpPingStatus(String wpPingStatus) {
        this.wpPingStatus = wpPingStatus;
    }

    /**
     * @return the wpPostName
     */
    public String getWpPostName() {
        return wpPostName;
    }

    /**
     * @param wpPostName the wpPostName to set
     */
    public void setWpPostName(String wpPostName) {
        this.wpPostName = wpPostName;
    }

    /**
     * @return the wpStatus
     */
    public String getWpStatus() {
        return wpStatus;
    }

    /**
     * @param wpStatus the wpStatus to set
     */
    public void setWpStatus(String wpStatus) {
        this.wpStatus = wpStatus;
    }

    /**
     * @return the wpPostParent
     */
    public String getWpPostParent() {
        return wpPostParent;
    }

    /**
     * @param wpPostParent the wpPostParent to set
     */
    public void setWpPostParent(String wpPostParent) {
        this.wpPostParent = wpPostParent;
    }

    /**
     * @return the wpMenuOrder
     */
    public String getWpMenuOrder() {
        return wpMenuOrder;
    }

    /**
     * @param wpMenuOrder the wpMenuOrder to set
     */
    public void setWpMenuOrder(String wpMenuOrder) {
        this.wpMenuOrder = wpMenuOrder;
    }

    /**
     * @return the wpPostType
     */
    public String getWpPostType() {
        return wpPostType;
    }

    /**
     * @param wpPostType the wpPostType to set
     */
    public void setWpPostType(String wpPostType) {
        this.wpPostType = wpPostType;
    }

    /**
     * @return the wpPostPassword
     */
    public String getWpPostPassword() {
        return wpPostPassword;
    }

    /**
     * @param wpPostPassword the wpPostPassword to set
     */
    public void setWpPostPassword(String wpPostPassword) {
        this.wpPostPassword = wpPostPassword;
    }

    /**
     * @return the wpIsSticky
     */
    public String getWpIsSticky() {
        return wpIsSticky;
    }

    /**
     * @param wpIsSticky the wpIsSticky to set
     */
    public void setWpIsSticky(String wpIsSticky) {
        this.wpIsSticky = wpIsSticky;
    }

    /**
     * @return the category
     */
    public List<String> getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(List<String> category) {
        this.category = category;
    }

    /**
     * @return the categoryAttributeDomain
     */
    public List<String> getCategoryAttributeDomain() {
        return categoryAttributeDomain;
    }

    /**
     * @param categoryAttributeDomain the categoryAttributeDomain to set
     */
    public void setCategoryAttributeDomain(List<String> categoryAttributeDomain) {
        this.categoryAttributeDomain = categoryAttributeDomain;
    }

    /**
     * @return the categoryAttributeNiceName
     */
    public List<String> getCategoryAttributeNiceName() {
        return categoryAttributeNiceName;
    }

    /**
     * @param categoryAttributeNiceName the categoryAttributeNiceName to set
     */
    public void setCategoryAttributeNiceName(List<String> categoryAttributeNiceName) {
        this.categoryAttributeNiceName = categoryAttributeNiceName;
    }

    /**
     * @return the metaKey
     */
    public List<String> getMetaKey() {
        return metaKey;
    }

    /**
     * @param metaKey the metaKey to set
     */
    public void setMetaKey(List<String> metaKey) {
        this.metaKey = metaKey;
    }

    /**
     * @return the metaValue
     */
    public List<String> getMetaValue() {
        return metaValue;
    }

    /**
     * @param metaValue the metaValue to set
     */
    public void setMetaValue(List<String> metaValue) {
        this.metaValue = metaValue;
    }
}