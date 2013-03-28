package pr2_kniha_jizd.database;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import pr2_kniha_jizd.Browse;

public class ExportDb {
    public ExportDb() {
    String cesta = new Browse().saveDialog();
    String select = "DRIVER";
    String data = getString(select);
    select = "CAR";
        data += getString(select);
    select = "RIDE";
            data += getString(select);
    saveToTxt(cesta, data);
    }
    private void saveToTxt(String cesta, String data)        
    {
        if(cesta == null || cesta.equals(""))
        {
        }else{
                try {
                FileWriter outFile = new FileWriter(cesta, true);
                PrintWriter out = new PrintWriter(outFile);
                out.print(data);
                out.close();
            } catch (IOException e) {
            }
        }
    }
    private String getString(String select)
    {
        DbRead db = new DbRead("select * from APP."+select);
        String[] colum = db.getColum();
        String[][] data = db.getData();
        String add ="INSERT INTO \"APP\".\""+select+"\"(";
        for(int x = 1;x<colum.length;x++)
        {
            add += colum[x];
            if(x<colum.length-1)
            {
             add+=",";
            }
        }
        add += ")VALUES(";
            String save = "";
        for(String[] data_ : data)
        {
            save +=add;
            for(int x = 1;x<data_.length;x++)
            {
                boolean number = true;
                if("RIDE".equals(select) && (x == 7 || x==8))
                {
                    String kod;
                    String idString = null;
                    if(x==7)
                    {
                        kod = "select * from APP.DRIVER WHERE DRIVERID =";
                        idString =  "(select DRIVERID from APP.DRIVER WHERE ";
                    }else{
                        kod = "select * from APP.CAR WHERE CARID =";
                        idString =  "(select CARID from APP.CAR WHERE ";

                    }
                    int id = Integer.parseInt(data_[x]);
                    DbRead db_ = new DbRead(kod+id);
                    String[] columX = db_.getColum();
                    String[] dataX = db_.getData()[0];
                    for(int c = 1;c<columX.length;c++)
                    {
                        if(dataX[c] != null)
                        {
                            idString +=columX[c]+" = '"+dataX[c]+"'";
                            if(c<columX.length-1)
                            {
                                idString+=" and ";
                            }
                        }
                    }
                    save+= idString +")";
                }else{ 
                    try{
                        Integer k = Integer.parseInt(data_[x]);
                    }catch(NumberFormatException ex)
                    {
                        number = false;
                    }
                    if(data_[x] == null || number)
                    {
                        save +=data_[x];          
                    }else{
                        save +="'"+data_[x]+"'";
                    }
                }
                
                if(x<data_.length-1)
                {
                 save+=",";
                }
            }
        save +=")\n";
        }
        return save;
    }
}
