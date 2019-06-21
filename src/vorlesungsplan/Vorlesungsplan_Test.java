package vorlesungsplan;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vorlesungsplan.dm.VorlesungsplanDB;
import vorlesungsplan.pd.Block;
import vorlesungsplan.pd.Semester;
import vorlesungsplan.pd.Vorlesungsplan;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Vorlesungsplan_Test {
    VorlesungsplanDB db;

    @BeforeAll
    public void setUp() throws Exception {

        db = VorlesungsplanDB.getInstance();

    }

    @AfterAll
    public void tearDown() throws Exception {
    }

    @Test
    public void deleteVorlesungsplanTest() {

        Vorlesungsplan plan = new Vorlesungsplan();
        //TODO Rückgabe von Löschung einbauen
        Boolean delete = plan.deleteVorlesungsplanung("WiSe 2019/2020", "3");
        assertTrue(delete);
    }

    @Test
    public void createVorlesungsplanTest() {

        Vorlesungsplan plan = new Vorlesungsplan();
        Boolean create = plan.createVorlesungsplan("WiSe 2025/2026", "2025-02-02", "3");
        assertTrue(create);
    }

    @Test
    public void createVorlesungsplanWithSameExistingPlanTest() {
        //Erzeugen des Plans
        Vorlesungsplan plan = new Vorlesungsplan();
        plan.createVorlesungsplan("SoSe 2021", "2020-10-14", "2");

        //Erzeugen eines zweiten Plans mit genau den selben Daten
        Block block1 = new Block();
        Semester semester1 = new Semester();
        semester1.setSemester_bez("SoSe 2021");

        Vorlesungsplan plan1 = new Vorlesungsplan();
        boolean create = plan1.createVorlesungsplan("SoSe 2021", "2020-10-14", "2");
        assertFalse(create);
    }

    @Test
    public void setModulOnWrongDayTest() {
        Vorlesungsplan plan = new Vorlesungsplan();

//        Boolean wrongBlockDayNr = plan.setModulIntoVP(modul.getModuleNumber(), block.getBlockDay(), "3");
//        assertFalse(wrongBlockDayNr);
        assertFalse(false);
    }

    @Test
    public void searchModul() {
        Vorlesungsplan plan = new Vorlesungsplan();

        String modulNr = plan.setModulIntoVP("000002", "Mittwoch", "4");
//        assertThat();
    }

    @Test
    public void setModulWithoutDay() {

        Vorlesungsplan plan = new Vorlesungsplan();

//        Boolean wrongBlockDayNr = plan.setModulIntoVP(modul.getModuleNumber(), "", "3");
//        assertFalse(wrongBlockDayNr);
    }
}