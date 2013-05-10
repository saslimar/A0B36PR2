package pr2_kniha_jizd.Exception;

public class MyException extends Exception {// vlastní třída dědíci z exception

    private String exceprionMsg = "";// vlastní chybová hláška
    private boolean show;// informace o tom máli se vyhodit nebo nemá

    public MyException(Throwable thrwbl) {// konstruktor
        super(thrwbl);
    }

    public MyException(String string, Throwable thrwbl) {// kontruktor
        super(string, thrwbl);
    }

    public MyException(String string) {//kontruktor
        super(string);
    }

    public MyException() {//kontruktor
    }

    public void addException(String exception)// přidání textu k puvodnímu textu chyby
    {
        exceprionMsg += exception + "\n";
    }

    public boolean isException()// má se chyba vyhodit
    {
        if ("".equals(exceprionMsg)) {
            return false;
        } else {
            return true;
        }
    }

    public void setException()// vynulování textu chyby
    {
        exceprionMsg = "";
    }

    public String getException()// vrátí text chyby
    {
        if (!"".equals(exceprionMsg)) {
            return exceprionMsg;
        }
        return null;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {// set show
        this.show = show;
    }
}
