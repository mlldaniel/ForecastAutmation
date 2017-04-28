package rss;

public class RSSHeader {
    private String title = "";
    private String link = "";
    private String description = "";
    private String language = "";
    private String wpWxrVersion="";
    private String wpBaseSiteUrl="";
    private String wpBaseBlogUrl="";
    //Under wp:author Tag
    private String wpAuthorId="";
    private String wpAuthorLogin="";
    private String wpAuthorEmail="";
    private String wpAuthorDisplayName="";
    private String wpAuthorFirstName="";
    private String wpAuthorLastName="";
    
  
  
    public String getTitle() {
      return title;
    }
    public void setTitle(String title) {
      this.title = title;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public String getLink() {
      return link;
    }
    public void setLink(String link) {
      this.link = link;
    }
    public String getLanguage() {
      return language;
    }
    public void setLanguage(String language) {
      this.language = language;
    }

    /**
     * @return the wpWxrVersion
     */
    public String getWpWxrVersion() {
        return wpWxrVersion;
    }

    /**
     * @param wpWxrVersion the wpWxrVersion to set
     */
    public void setWpWxrVersion(String wpWxrVersion) {
        this.wpWxrVersion = wpWxrVersion;
    }

    /**
     * @return the wpBaseSiteUrl
     */
    public String getWpBaseSiteUrl() {
        return wpBaseSiteUrl;
    }

    /**
     * @param wpBaseSiteUrl the wpBaseSiteUrl to set
     */
    public void setWpBaseSiteUrl(String wpBaseSiteUrl) {
        this.wpBaseSiteUrl = wpBaseSiteUrl;
    }

    /**
     * @return the wpBaseBlogUrl
     */
    public String getWpBaseBlogUrl() {
        return wpBaseBlogUrl;
    }

    /**
     * @param wpBaseBlogUrl the wpBaseBlogUrl to set
     */
    public void setWpBaseBlogUrl(String wpBaseBlogUrl) {
        this.wpBaseBlogUrl = wpBaseBlogUrl;
    }

    /**
     * @return the wpAuthorId
     */
    public String getWpAuthorId() {
        return wpAuthorId;
    }

    /**
     * @param wpAuthorId the wpAuthorId to set
     */
    public void setWpAuthorId(String wpAuthorId) {
        this.wpAuthorId = wpAuthorId;
    }

    /**
     * @return the wpAuthorLogin
     */
    public String getWpAuthorLogin() {
        return wpAuthorLogin;
    }

    /**
     * @param wpAuthorLogin the wpAuthorLogin to set
     */
    public void setWpAuthorLogin(String wpAuthorLogin) {
        this.wpAuthorLogin = wpAuthorLogin;
    }

    /**
     * @return the wpAuthorEmail
     */
    public String getWpAuthorEmail() {
        return wpAuthorEmail;
    }

    /**
     * @param wpAuthorEmail the wpAuthorEmail to set
     */
    public void setWpAuthorEmail(String wpAuthorEmail) {
        this.wpAuthorEmail = wpAuthorEmail;
    }

    /**
     * @return the wpAuthorDisplayName
     */
    public String getWpAuthorDisplayName() {
        return wpAuthorDisplayName;
    }

    /**
     * @param wpAuthorDisplayName the wpAuthorDisplayName to set
     */
    public void setWpAuthorDisplayName(String wpAuthorDisplayName) {
        this.wpAuthorDisplayName = wpAuthorDisplayName;
    }

    /**
     * @return the wpAuthorFirstName
     */
    public String getWpAuthorFirstName() {
        return wpAuthorFirstName;
    }

    /**
     * @param wpAuthorFirstName the wpAuthorFirstName to set
     */
    public void setWpAuthorFirstName(String wpAuthorFirstName) {
        this.wpAuthorFirstName = wpAuthorFirstName;
    }

    /**
     * @return the wpAuthorLastName
     */
    public String getWpAuthorLastName() {
        return wpAuthorLastName;
    }

    /**
     * @param wpAuthorLastName the wpAuthorLastName to set
     */
    public void setWpAuthorLastName(String wpAuthorLastName) {
        this.wpAuthorLastName = wpAuthorLastName;
    }



}