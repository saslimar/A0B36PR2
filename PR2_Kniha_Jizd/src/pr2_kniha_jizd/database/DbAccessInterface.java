package pr2_kniha_jizd.database;
public  interface DbAccessInterface {
    
    public static final int CAR = 1;
    public static final int DRIVER = 2;
    public static final int RIDE = 3;

    // <editor-fold defaultstate="collapsed" desc="DB READ">
    public String getDetails();// vrací string z detaily načtený v DbReadDetails
    public void DbReadException(String jmeno, String prijmeni, String datum);// nastavý promnenné colum a data na vyhledání ve smenech pro zadane parametry pokud nejde o človjeka nastavý je na všechny SPZ a CarID z tabulky CAR
    public void DbRead(int i, int id);// nastavý promnenné na vyhledané záznamy z tabulky i pokud jejich ID = id
    public void getToCar();// nastavý je na statistiku aut
    public void getToDriver();//nastavuje na statistiku řidičů
    public void DbReadRideEdit(int i, boolean cislo);// nastavý je pro potřebně zobrazení v Ride edit i = tabulka cislo true vytahne poze id
    public void DbReadForDelete(int i, int id);// nastavý data na záznam z ride který mí i ID (i tabulka) stejný s id 
    public void DbReadDetails(int i, int id);// vytáhne data a seřazené jakodetaily uloží do stringu
    public void DbRead(int i) ;// uloží do promnených kopletní tabulku i
    public void DbRead(int i, String search);// nactení hodnost s vyhledanym stringem v tabulcee i
    public void DbRead(String prikaz);// vrátí hodnoty dle zadaného db příkazu
    public String[][] getData();// vrátí pole dat
    public String[] getColum();// vrátí pole nadpisu slopců dat

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DB WRITE">

    public void DbWriteDriverEdit(String jmeno, String prijmeni, String datum, int select);// zmnení data v záznamu s ID v tabulce řidiče
    public void DbWriteDriverAdd(String jmeno, String prijmeni, String datum);// přidá záznam do tabulky řidiče
    public void DbWriteRideEdit(String datum, String odkud, String kam, String duvod,
            Integer vzdalenost, Integer spotreba, Integer driverId, Integer carId, int select);// zmnení data v záznamu s ID v tabulce jízdy
    public void DbWriteRideAdd(String datum, String odkud, String kam, String duvod,
            Integer vzdalenost, Integer spotreba, Integer driverId, Integer carId);// přidá záznam do tabulky jízd
    public void DbWriteCarAdd(String znacka, String spz, String druh, String firemni);// přidá záznam do tabulky aut
    public void DbWriteCarEdit(String znacka, String spz, String druh, String firemni, int select);// zmnení data v záznamu s ID v tabulce aut
    public void DbWriteDelete(int i, int id);// vymaže ze zadané tabulky zaznam s ID
    public void DbWriteDeleteRide(int i, int id); // vymaže jídu jejíš zaznam I ID = id
    public void DbWrite(String prikaz);// metoda pro zapis do databáze pouze otevře spojení , odešle příkaz a uzavře spojení
    
    
    // </editor-fold>
}
