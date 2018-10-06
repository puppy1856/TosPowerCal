package com.moonpa.tospowercal;

public class AccountSettingList
{
    private String title,content;

    public AccountSettingList(String title,String content)
    {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String changeTitle)
    {
        title = changeTitle;
    }

    public void setContent(String changeContext)
    {
        content = changeContext;
    }

    public String getTitle()
    {
        return title;
    }

    public String getContent()
    {
        return content;
    }
}
