package app;

import java.util.ArrayList;
import java.util.List;

public class BootstrapServentList {

    private List<ServentInfo> activeServents;
    private List<ServentInfo> deletedServents;
    private List<ServentInfo> idleServents;


    public BootstrapServentList() {
        this.activeServents = new ArrayList<>();
        this.deletedServents = new ArrayList<>();
        this.idleServents = new ArrayList<>();
    }


    public List<ServentInfo> getActiveServents() {
        return activeServents;
    }


    public List<ServentInfo> getDeletedServents() {
        return deletedServents;
    }


    public List<ServentInfo> getIdleServents() {
        return idleServents;
    }

}
