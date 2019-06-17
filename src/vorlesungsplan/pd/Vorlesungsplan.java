package vorlesungsplan.pd;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import vorlesungsplan.dm.VorlesungsplanDB;
import vorlesungsplan.dm.VorlesungsplanDBException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static java.lang.Integer.parseInt;

public class Vorlesungsplan {
    private Semester semester;
    private Block block;
    private Integer semesterZahl;
    private static VorlesungsplanDB vorlesungsplanDB = VorlesungsplanDB.getInstance();

    //	private static HashMap<String, Modul> availableModule =	vorlesungsplanDB.getAllModule();


    public Vorlesungsplan() {
    }

    public Vorlesungsplan(Semester semester, Block block, Integer semesterZahl) {
        this.semester = semester;
        //block muss hier glaube ich nicht drin sein
        this.block = block;
        this.semesterZahl = semesterZahl;
    }


//	public static Modul searchModul(int id) {
//		Modul modul = vorlesungsplanDB.searchModul(id);
//		return modul;
//	}

    public void stop() throws VorlesungsplanDBException {
        vorlesungsplanDB.closeConnection();
    }

    public String setModulIntoVP(String modulNr, String blockDay, String blockNr) {
        //TODO Fertig machen, �bergabe mit freigegebenen TAgen, wie viele Bl�cke sind noch frei
        //Bl�cke, Counter
        //TODO nur Ziffern NumberException wie bei Felix?
//		modul.setModuleNumber(000001);

        if (modulNr.isEmpty()) {
            showErrorMessage("Modul Nummer darf nicht leer sein.");
        } else if (blockNr.isEmpty()) {
            showErrorMessage("Modul muss einem Block zugeordnet sein.");
        } else {
            Modul modul = vorlesungsplanDB.getModulByModulNr(modulNr);
        }

        return modulNr;
    }

    //TODO Semester muss als Kombination aus Bezeichnung und SemesterZahl gespeichert werden
    public boolean createVorlesungsplan(String semesterBez, String erstellungsDatum, String semesterZahl) {
        Integer semesterNr = 0;
        Integer zahl = 0;
        LocalDate erstellDatum = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

		/*System.out.println(semesterBez);
		System.out.println(erstellungsDatum);
		System.out.println(semesterZahl);*/

        if (!(erstellungsDatum.isEmpty())) {
            erstellDatum = LocalDate.parse(erstellungsDatum, df);
        } else {//TODO Fehlermeldung falls Datenfelder leer sind
            //			showErrorMessage(".");
            //			showErrorMessage("Der Plan konnte nicht erstellt werden, bitte Semesternummer eingeben.");
        }

        if (!(semesterBez.isEmpty()) && !(semesterZahl.isEmpty())) {
            semesterNr = vorlesungsplanDB.getSemesterBySemesterBez(semesterBez);
            zahl = parseInt(semesterZahl);
        }
        System.out.println("Semester ID: " + semesterNr);
        //TODO Einstellen, dass nur gespeichert werden kann, wenn die Vorbedingungen fehlerfrei sind.
        vorlesungsplanDB.createVorlesungsplan(semesterNr, erstellDatum, zahl);
        showSuccessMessage("Plan wurde erstellt");

        return true;
    }

    //TODO muss noch auf SemesterJahr angepasst werden
    public Boolean deleteVorlesungsplanung(String semester, String semesterZahl) {
        Integer semesterNr = 0;
        Boolean sucessfulDeletion = false;
        Integer semeter = parseInt(semesterZahl);
        if (semester.isEmpty()) {
            showErrorMessage("Semester darf nicht leer sein.");
        } else {
            semesterNr = vorlesungsplanDB.getSemesterBySemesterBez(semester);
            sucessfulDeletion = vorlesungsplanDB.deleteVorlesungplanung(semesterNr, semeter);
            showSuccessMessage("Der Plan wurde erfolgreich gel�scht");
        }
        //TODO Einstellen, dass nur gelöscht werden kann, wenn die Vorbedingungen fehlerfrei sind.
        return sucessfulDeletion;
    }

    public void saveVorlesungsplan() {
        vorlesungsplanDB.saveVorlesungsplan();
        //TODO Fehlermeldungen hierher ziehen, aus DM Schicht
    }

    public Vorlesungsplan loadVorlesungsplan(String semesterJaht, Integer semesterZahl) {
        Semester semester = new Semester();
        semester.setSemester_bez(semesterJaht);
        Vorlesungsplan plan = vorlesungsplanDB.loadVorlesungsplan(semester, semesterZahl);

        return plan;
    }

    public void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Achtung!");
        alert.setHeaderText("Bitte �berpr�fen Sie die Eingaben:");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSuccessMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Abgeschlossen");
        alert.setHeaderText("Vorgang erfolgreich:");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
