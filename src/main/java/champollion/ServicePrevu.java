package champollion;

public class ServicePrevu {
    private final UE ue;
    private int volumeCM;
    private int volumeTD;
    private int volumeTP;

    public ServicePrevu(UE ue) {
        this.ue = ue;
    }

    public void ajouterVolume(int cm, int td, int tp) {
        this.volumeCM += cm;
        this.volumeTD += td;
        this.volumeTP += tp;
    }

    public int getVolumeCM() {
        return volumeCM;
    }

    public int getVolumeTD() {
        return volumeTD;
    }

    public int getVolumeTP() {
        return volumeTP;
    }
}
