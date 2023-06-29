//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


public final class StringUtils
{
    static public boolean hasValue(String s)
    {
        return s != null && s.length() > 0;
    }


    static public String safe(String s)
    {
        return safe(s, "");
    }


    static public String safe(String s, String defaultStr)
    {
        return ((s != null) ? s : defaultStr);
    }


    static public String safeTrimmed(String s)
    {
        return StringUtils.safe(s).trim();
    }
}
