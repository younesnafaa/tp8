package champollion;

import java.util.Date;

public class Intervention {
    private final Date date;
    private final int duree;
    private final UE ue;
    private final TypeIntervention type;

    public Intervention(Date date, int duree, UE ue, TypeIntervention type) {
        this.date = date;
        this.duree = duree;
        this.ue = ue;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public int getDuree() {
        return duree;
    }

    public UE getUe() {
        return ue;
    }

    public TypeIntervention getType() {
        return type;
    }
}
