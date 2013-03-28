package pr2_kniha_jizd.Exception;

public class MyException extends Exception{
    private String exceprionMsg="";
    private boolean show;
    public MyException(Throwable thrwbl) {
        super(thrwbl);
    }

    public MyException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public MyException(String string) {
        super(string);
    }

    public MyException() {
    }
    
    public void addException(String exception)
    {
        exceprionMsg += exception+"\n";
    }
    
    public boolean isException()
    {
        if("".equals(exceprionMsg))
            return false;
        else
            return true;
    }
    public void setException()
    {
        exceprionMsg = "";
    }
    public String getException()
    {
        if(!"".equals(exceprionMsg))
        {
            return exceprionMsg;
        }
        return null;
    }
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    
    
    
}
