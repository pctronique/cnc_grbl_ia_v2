package cnc.events;

import cnc.MachineState;

public interface GrblListener {

    void onLineReceived(String line);

    void onStateChanged(MachineState state);

    void onError(String error);

    void onFileProgress(int percent);

    void onJobCompleted();
}
