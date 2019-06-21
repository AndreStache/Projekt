package vorlesungsplan.dm;

import vorlesungsplan.pd.Block;
import vorlesungsplan.pd.Modul;
import vorlesungsplan.pd.Semester;
import vorlesungsplan.pd.Vorlesungsplan;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class VorlesungsplanDB {
    private Connection conn;
    private Statement stmt;
    private static final VorlesungsplanDB db = new VorlesungsplanDB();

    private VorlesungsplanDB() throws VorlesungsplanDBException {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/projekt?serverTimezone=UTC", "root", "");
            conn.setAutoCommit(false);
            // int level = Connection.TRANSACTION_SERIALIZABLE;
            int level = Connection.TRANSACTION_READ_COMMITTED;
            conn.setTransactionIsolation(level);
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            //throw new VorlesungsplanDBException("Keine Datenbankverbindung.", ex);
            throw new VorlesungsplanDBException(ex.toString());
        }
    }

    public void closeConnection() throws VorlesungsplanDBException {
        try {
            if (conn != null) {
                System.out.print("Verbindungsabbau ...");
                conn.rollback();
                conn.close();
                System.out.println(" erfolgreich");
            }
            conn = null;
        } catch (SQLException ex) {
            throw new VorlesungsplanDBException("Datenbankverbindung kann nicht geschlossen werden.");
        }
    }

    public HashMap<String, Modul> getAllBlock() {
        HashMap<String, Modul> ergebnis = new HashMap<String, Modul>();
        String query = "select modul_nr from modul";
        try {
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                Modul t = new Modul();
                ergebnis.put(t.getBlock(), t);
            }
        } catch (SQLException ex) {
            // assert false: "SQL pr�fen";
            throw new VorlesungsplanDBException("Datenbankzugriffsfehler: " + ex);
        }
        return ergebnis;
    }

    public static VorlesungsplanDB getInstance() throws VorlesungsplanDBException {
        return db;
    }

    public Boolean createVorlesungsplan(Integer semester, LocalDate erstellDatum, Integer semesterZahl) {
        String query = "select plan_nr from vorlesungsplan where semester_id = " + semester + " AND semester_zahl = " + semesterZahl;
//        System.out.println(query);
        Integer maxPlanNr = 0;

        try {
            ResultSet rset = stmt.executeQuery(query);

            if (rset.next() == false) {
                query = "select max(plan_nr) as plan_nr from vorlesungsplan";
                ResultSet maxPlanNrResult = stmt.executeQuery(query);

                while (maxPlanNrResult.next()) {
                    maxPlanNr = maxPlanNrResult.getInt("plan_nr");

                    maxPlanNr++;
                }
				/*query = "insert into vorlesungsplan(plan_nr, semester_id, erstellungsdatum, semester_zahl)"
						+ " values(" + maxPlanNr + ", " + semester + ", to_date('" + erstellDatum + "', 'yyyy-mm-dd'), " + semesterZahl + ")";*/
                query = "insert into vorlesungsplan(plan_nr, semester_id, erstellungsdatum, semester_zahl)"
                        + " values(" + maxPlanNr + ", " + semester + ", '" + erstellDatum + "', " + semesterZahl + ")";
//                System.out.println(query);
                Integer result = stmt.executeUpdate(query);
                if (result == 1) {
                    return true;
                }
                return false;
//                System.out.println(result);

            } else {
                // #TODO Das muss dem User angezeigt werden
                return false;
            }
        } catch (SQLException ex) {
            // assert false: "SQL pr�fen";
            throw new VorlesungsplanDBException("Datenbankzugriffsfehler: " + ex);
        }
    }

    public boolean deleteVorlesungplanung(Integer semesterNr, Integer semesterZahl) {
        String query = "DELETE FROM vorlesungsplan WHERE semester_id = " + semesterNr + " AND semester_zahl = " + semesterZahl;
        System.out.println(query);
        try {
            //TODO R�ckmeldungen an den User
            Integer result = stmt.executeUpdate(query);
            if (result == 1) {
                    return true;
            }
            return false;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new VorlesungsplanDBException("Vorlesungsplanung konnte nicht gel�scht werden: " + e);
        }
    }

    public Integer getSemesterBySemesterBez(String semester_bez) {
        String query = "SELECT semester_id FROM semester WHERE semester_bez LIKE '" + semester_bez + "%'";
//		System.out.println(query);
        Integer semester_nr = null;
        // System.out.println(query);
        try {
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                semester_nr = rset.getInt("semester_id");
            }
        } catch (SQLException ex) {
            // assert false: "SQL pr�fen";
            throw new VorlesungsplanDBException("Datenbankzugriffsfehler: " + ex);
        }

        System.out.println("Semester_id: " + semester_nr);
        return semester_nr;
    }

    public void saveVorlesungsplan() {
        try {
            conn.commit();
        } catch (SQLException ex) {
            throw new VorlesungsplanDBException("Vorlesungsplan konnte nicht gespeichert werden: " + ex);
        }
    }

    public Modul getModulByModulNr(String modulNr) {
        Modul modul = new Modul();
        String query = "SELECT modul_nr, nameshort, anzahlblock, name FROM modul WHERE modul_nr LIKE '" + modulNr + "%'";

        try {
            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                modul.setNameShort(rset.getString("nameshort"));
                modul.setModuleNumber(rset.getString("modul_nr"));
                modul.setAnzahlBloecke(rset.getInt("anzahlbloecke"));
                modul.setName(rset.getString("name"));
            }
        } catch (SQLException ex) {
            // assert false: "SQL pr�fen";
            throw new VorlesungsplanDBException("Datenbankzugriffsfehler: " + ex);
        }

        return modul;
    }

    public Vorlesungsplan loadVorlesungsplan(String semesterBez, int semesterZahl) {
        Vorlesungsplan plan = null;
        String query = "select plan_nr, semester_zahl, erstellungsdatum, aenderungsdatum, semester_bez from vorlesungsplan JOIN semester ON vorlesungsplan.semester_id = semester.semester_id where semester_zahl = " + semesterZahl + " AND semester_bez LIKE '" + semesterBez + "'";

        System.out.println(query);

        try {
			ResultSet rset = stmt.executeQuery(query);
			while (rset.next()) {
                Semester semester = new Semester();
                semester.setSemester_bez(rset.getString("semester_bez"));

                plan = new Vorlesungsplan(semester, semesterZahl);
                plan.setErstellungsdatum(rset.getDate("erstellungsdatum").toLocalDate());

                if (rset.getDate("aenderungsdatum") != null) {
                    plan.setAenderungsdatum(rset.getDate("aenderungsdatum").toLocalDate());
                }
			}
		} catch (SQLException ex) {
			// assert false: "SQL pr�fen";
			throw new VorlesungsplanDBException("Datenbankzugriffsfehler: " + ex);
        }

        return plan;
    }

    public Boolean insertBlockPlan(Integer plan_nr, List<Block> blockListe) {
        Integer maxBlockPlan = 0;
        String query = "select max(blockplan_id) as blockplan_id from blockplan";
        ResultSet maxBlockPlanResult = null;
        try {
            maxBlockPlanResult = stmt.executeQuery(query);
            while (maxBlockPlanResult.next()) {
                maxBlockPlan = maxBlockPlanResult.getInt("blockplan_id");

                maxBlockPlan++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Block block : blockListe) {
            String insert = "INSERT INTO blockplan value(" + maxBlockPlan + ", " + block.getBlockId() + ", " + plan_nr + ")";
            try {
                stmt.executeUpdate(insert);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public Boolean updateVorlesungsplan(Integer plan_nr, Integer semesterNr, Integer semesterId, LocalDate aenderungsdatum, List<Block> blockListe) {

        String query = "UPDATE vorlesungsplan SET semester_id = " + semesterId + ", semester_zahl = " + semesterNr + ", aenderungsdatum = '" + aenderungsdatum + "' WHERE plan_nr = " + plan_nr + ";";

        try {
            stmt.executeUpdate(query);

            Boolean insert = insertBlockPlan(plan_nr, blockListe);
            if (insert) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
