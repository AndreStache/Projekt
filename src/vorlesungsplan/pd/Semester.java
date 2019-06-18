package vorlesungsplan.pd;

import java.util.Date;

public class Semester {
    String semester_bez;
    Date semesterStart;
    Date semesterEnde;

    public Semester() {
    }

    public Semester(String semester_bez, Date semesterStart, Date semesterEnde) {
        this.semester_bez = semester_bez;
        this.semesterStart = semesterStart;
        this.semesterEnde = semesterEnde;
    }

    public String getSemester_bez() {
        return semester_bez;
    }

    public void setSemester_bez(String semester_bez) {
        this.semester_bez = semester_bez;
    }

    public Date getSemesterStart() {
        return semesterStart;
    }

    public void setSemesterStart(Date semesterStart) {
        this.semesterStart = semesterStart;
    }

    public Date getSemesterEnde() {
        return semesterEnde;
    }

    public void setSemesterEnde(Date semesterEnde) {
        this.semesterEnde = semesterEnde;
    }


}
