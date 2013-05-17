Kniha jízd semestrální práce Martin Saslík

 Program po načtení vypíše seznam jízd a dále čeká na akce uživatele je možno zobrazit seznam vozidel seznam řidičů full textově vyhledávat a seřazovat jednotlivé parametry jízdy řidiče i vozu vzestupně a sestupně. Dále je možno přidávat nové záznamy a editovat či mazat ty staré. K dispozici uživateli je I export či import databáze do textového souboru pro případ potřeby přenést danou databáze na jiný počítač samozřejmostí je možnost uložit jednotlivé záznamy jízd či kompletní záznamy řidiče/vozu do textového souboru.
 
program obsahuje nekolik tříd přičemš každá vykonává vlastní činost 

class Main //třída spouštící MainGui a obstarávající správu menu programu
 
class Browse // okno pro procházení složek v počítači pro uložení či načtení db i pro
      ukládání jednotlivých zaznamu v textove podobje

class Details // třída zobrazujídí detajly dědicí z Jdialog ppošle BdAcces nazev tabulky a
      ID zaznamu a vypíše data z databáze

class MainGui // hlavní okno programu obstarává zobrazení a správu progamových tlačítek

class Statistika // zobrazení statistiky pro danou tabulku pošle do DbAcces název tabulky
    a vipíše výsledek

class TablePanel // panel obstaráávající správný vípis tabulky

class TableModel //třída dědící z AbstractTableModel potřebná pro správné zobrazení dat v
      tabulce

class MyException// vlastní třída dědící z exception 

class MyExceptionDetector// třída kontoluje chiby v případě chyb vyhodí MyException

class CarAdd// třída děědící z Jpanel načte data od uživatele a pošle je do DbAcces

class DriverAdd// třída děědící z Jpanel načte data od uživatele a pošle je do DbAcces

class RideAdd// třída děědící z Jpanel načte data od uživatele a pošle je do DbAcces

class DbAccess// třída obstarávající veškerou databázovou komunikaci // obsahuje všechny
      databázové příkazy s vyjímkou tříd ExportDb a ImportDb tyto 2 třídy si tvoří příkazy
      lvastní

class DbAccessInterface // inerface pto DbAcces budeli potřeba zmněnit načítání mýsto z 
      databáze na načítání z textového souboru stačí aby daná třída implementovala 
      DbAccesInterface a program s vyjímkou (ExportDb ImportDb) bude fungovat tak jak má

class ExportDb //třída exportuje databázy vytáhne z ní všechna data sformátuje do 
      potřebného formátu pro následovné importování a uloží na určené mýsto

class ImportDb// načte vybraný textový soubor a přidá záznamy do databáze 
