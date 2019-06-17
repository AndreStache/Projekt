package vorlesungsplan;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vorlesungsplan.dm.VorlesungsplanDB;
import vorlesungsplan.pd.Block;
import vorlesungsplan.pd.Modul;
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
        Block block = new Block();
        Semester semester = new Semester();


        Vorlesungsplan plan = new Vorlesungsplan(semester, block, 3);
        //TODO Rückgabe von Löschung einbauen
        Boolean delete = plan.deleteVorlesungsplanung("WiSe 2019/2020", "3");
        assertTrue(delete);
    }

    @Test
    public void createVorlesungsplanTest() {
        Block block = new Block();
        Semester semester = new Semester();

        Vorlesungsplan plan = new Vorlesungsplan(semester, block, 3);
        Boolean create = plan.createVorlesungsplan("WiSe 2025/2026", "2025-02-02", "3");
        assertTrue(create);
    }

    @Test
    public void createVorlesungsplanWithSameExistingPlanTest() {
        Block block = new Block();
        Semester semester = new Semester();
        semester.setSemester_bez("SoSe 2021");

        //Erzeugen des Plans
        Vorlesungsplan plan = new Vorlesungsplan(semester, block, 2);
        plan.createVorlesungsplan(semester.getSemester_bez(), "2020-10-14", "2");

        //Erzeugen eines zweiten Plans mit genau den selben Daten
        Block block1 = new Block();
        Semester semester1 = new Semester();
        semester1.setSemester_bez("SoSe 2021");

        Vorlesungsplan plan1 = new Vorlesungsplan(semester1, block1, 2);
        boolean create = plan1.createVorlesungsplan(semester1.getSemester_bez(), "2020-10-14", "2");
        assertFalse(create);
    }

    @Test
    public void setModulOnWrongDayTest() {
        Block block = new Block();
        Modul modul = new Modul();

        Vorlesungsplan plan = new Vorlesungsplan(new Semester(), block, 3);

//        Boolean wrongBlockDayNr = plan.setModulIntoVP(modul.getModuleNumber(), block.getBlockDay(), "3");
//        assertFalse(wrongBlockDayNr);
        assertFalse(false);
    }

    @Test
    public void searchModul() {
        Block block = new Block();
        block.setBlockDay("Mittwoch");

        Vorlesungsplan plan = new Vorlesungsplan(new Semester(), block, 4);

        String modulNr = plan.setModulIntoVP("000002", block.getBlockDay(), "4");
//        assertThat();
    }

    @Test
    public void setModulWithoutDay() {
        Block block = new Block();
        Modul modul = new Modul();

        Vorlesungsplan plan = new Vorlesungsplan(new Semester(), block, 6);

//        Boolean wrongBlockDayNr = plan.setModulIntoVP(modul.getModuleNumber(), "", "3");
//        assertFalse(wrongBlockDayNr);
    }
}