package vorlesungsplan.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import vorlesungsplan.pd.TableItem;
import vorlesungsplan.pd.Vorlesungsplan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ControllerVV {
    public Vorlesungsplan vorlesungsplan = new Vorlesungsplan();
    @FXML
    public TableView<TableItem> tvVorlesungsplan;
    @FXML
    public TextField tfErstellungsdatum, tfModulNr, tfBlockTag, tfBlockNr, tfAenderungsdatum;
    @FXML
    public TableColumn<TableItem, String> tcBlock;
    @FXML
    public TableColumn<TableItem, String> tcDienstag;
    @FXML
    public Button btNeu, btAbbruch, btLoeschen, btUebernehmen, btSpeichern, btAnzeigen;
    @FXML
    public DatePicker dpErstellungsdatum;
    @FXML
    public DatePicker dpDateaenderung;
    @FXML
    public ComboBox cbSemesterJahr;
    @FXML
    public ComboBox cbSemesterZahl;
    TableItem e;
    ObservableList<TableItem> allItems = FXCollections.observableArrayList();
    ObservableValue<ComboBox> semesterauswahl;

    //	Erwartet einen R�ckgabewert vom ActionEvent, damit ein Button ausgef�hrt wird
    private void actionPerformed(ActionEvent e) {
        if ((btNeu == e.getSource())) {
            vorlesungsplan.createVorlesungsplan(cbSemesterJahr.getValue().toString(), tfErstellungsdatum.getText(), cbSemesterZahl.getValue().toString());
        }

        if (btLoeschen == e.getSource()) {
            Boolean result = vorlesungsplan.deleteVorlesungsplanung(cbSemesterJahr.getValue().toString(), cbSemesterZahl.getValue().toString());

            tfErstellungsdatum.setDisable(false);
            tfErstellungsdatum.setText(LocalDate.now().toString());

            tfAenderungsdatum.setDisable(true);
            tfAenderungsdatum.setText("");

            cbSemesterJahr.setDisable(false);
            cbSemesterZahl.setDisable(false);
        }

        if (btSpeichern == e.getSource()) {
            vorlesungsplan.saveVorlesungsplan();
        }

        if (btUebernehmen == e.getSource()) {
            vorlesungsplan.setModulIntoVP(tfModulNr.getText(), tfBlockTag.getText(), tfBlockNr.getText());
        }

        if (btAnzeigen == e.getSource()) {
            LocalDate aenderungsdatum;
            Vorlesungsplan plan = vorlesungsplan.loadVorlesungsplan(cbSemesterJahr.getValue().toString(), cbSemesterZahl.getValue().toString());
            //Aenderungsdatum belegen mit bereitsausgefülltem Datum aus DB oder mit aktuellem Tagedatum
            if (plan.getAenderungsdatum() == null) {
                aenderungsdatum = LocalDate.now();
            } else {
                aenderungsdatum = plan.getAenderungsdatum();
            }
            DateTimeFormatter df;
            df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);    // 31.01.2016
            tfAenderungsdatum.setText(aenderungsdatum.format(df));
            tfErstellungsdatum.setText(plan.getErstellungsdatum().format(df));

            tfErstellungsdatum.setDisable(true);
            cbSemesterJahr.setDisable(true);
            cbSemesterZahl.setDisable(true);
        }
    }

    public void initialize() {
        //Erstelldatum Textfeld vorbelegen
        LocalDate date = LocalDate.now();
        DateTimeFormatter df;
        df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);    // 31.01.2016
        tfErstellungsdatum.setText(date.format(df));

        //SemesterJahr ComboBox vorbelegen
        cbSemesterJahr.getItems().setAll("WiSe 2019/2020", "SoSe 2019");
        cbSemesterJahr.setPromptText("Bitte waehlen");
        cbSemesterJahr.setEditable(true);
        //ChangeListend
        cbSemesterJahr.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                //Auswahl anhand von cbSemester bestimmen mit ChangeListener
                if (t1.contains("WiSe")) {
                    cbSemesterZahl.getItems().setAll("1", "3", "5");
                } else {
                    cbSemesterZahl.getItems().setAll("2", "4", "6");
                }
            }
        });

        for (int i = 0; i <= 5; i++) {
            e = new TableItem();
            e.block = "" + (i + 1);
            allItems.add(e);
        }
        ;

//		System.out.println("Block: " + allItems.get(2).getBlock());
        tcBlock.setCellValueFactory(new PropertyValueFactory<TableItem, String>("block"));
        tcDienstag.setCellValueFactory(new PropertyValueFactory<TableItem, String>("die"));
        tvVorlesungsplan.setItems(allItems);


        btNeu.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent evt) {
                actionPerformed(evt);
            }
        });

        btLoeschen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent evt) {
                actionPerformed(evt);
            }
        });
        btSpeichern.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent evt) {
                actionPerformed(evt);
            }
        });

        btUebernehmen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent evt) {
                actionPerformed(evt);
            }
        });

        btAnzeigen.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent evt) {
                actionPerformed(evt);
            }
        });

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Stage stage = (Stage) tvVorlesungsplan.getScene().getWindow();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent arg0) {
                        vorlesungsplan.stop();
                    }
                });
            }
        });
    }

    public void setTableViewCell(String modulnr, String day, Integer block) {
        switch (day) {
            case "Montag":
                allItems.get(1).setMon(modulnr);
                break;
            case "Dienstag":
                allItems.get(1).setDie(modulnr);
                break;
            case "Mittwoch":
                allItems.get(1).setMit(modulnr);
                break;
            case "Donnerstag":
                allItems.get(1).setDon(modulnr);
                break;
            case "Freitag":
                allItems.get(1).setFre(modulnr);
                break;
            default:
                break;
        }
    }


}
